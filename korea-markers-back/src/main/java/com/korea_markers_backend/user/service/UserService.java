package com.korea_markers_backend.user.service;

import com.korea_markers_backend.exception.BusinessException;
import com.korea_markers_backend.exception.ErrorCode;
import com.korea_markers_backend.jwt.JwtTokenProvider;
import com.korea_markers_backend.user.dto.request.SignInReqeustDTO;
import com.korea_markers_backend.user.dto.request.SignUpRequestDTO;
import com.korea_markers_backend.user.dto.response.SignInResponseDTO;
import com.korea_markers_backend.user.entity.CustomUserDetails;
import com.korea_markers_backend.user.entity.Session;
import com.korea_markers_backend.user.entity.User;
import com.korea_markers_backend.user.repository.SessionRepository;
import com.korea_markers_backend.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;


    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    public void signUp(SignUpRequestDTO req){
        if(userRepository.findByEmail(req.getEmail()).isPresent())
            throw new BusinessException(ErrorCode.USER_EMAIL_DUPLICATE);

        if(!req.getPassword().equals(req.getPasswordCheck()))
            throw new BusinessException(ErrorCode.USER_CONFLICT_PASSWORD);

        String encodedPassword= passwordEncoder.encode(req.getPassword());

        User user=User.builder()
                .email(req.getEmail())
                .password(encodedPassword)
                .build();
        userRepository.save(user);
    }

    public SignInResponseDTO signIn(SignInReqeustDTO req, HttpServletRequest httpReq, HttpServletResponse httpRes){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        User user=userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        Session session=Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .ipAddr(extractClientIp(httpReq))
                .build();
        sessionRepository.save(session);

        addHttpOnlyCookie(httpRes,"refresh_token",refreshToken,Duration.ofDays(14));

        return SignInResponseDTO.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public SignInResponseDTO refresh(HttpServletRequest httpReq, HttpServletResponse httpRes) {
        String refreshFromCookie = getCookie(httpReq, "refresh_token");
        if (refreshFromCookie == null || refreshFromCookie.isBlank()) {
            throw new BusinessException(ErrorCode.USER_INVALID_REFRESH_TOKEN);
        }

        if (!jwtTokenProvider.validateToken(refreshFromCookie)) {
            throw new BusinessException(ErrorCode.USER_INVALID_REFRESH_TOKEN);
        }

        String email = jwtTokenProvider.getUsername(refreshFromCookie);

        Session session = sessionRepository.findByRefreshToken(refreshFromCookie)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_INVALID_REFRESH_TOKEN));

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        CustomUserDetails principal = new CustomUserDetails(user);
        String newAccess  = jwtTokenProvider.generateAccessToken(principal);
        String newRefresh = jwtTokenProvider.generateRefreshToken(principal);

        session.rotateRefreshToken(newRefresh); // 엔티티 필드 값 변경

        addHttpOnlyCookie(httpRes, "refresh_token", newRefresh, Duration.ofDays(14));

        return SignInResponseDTO.builder()
                .accessToken(newAccess)
                .build();
    }

    // ========================= 로그아웃 =========================
    @Transactional
    public void signout(HttpServletRequest httpReq, HttpServletResponse httpRes,CustomUserDetails userDetails) {
        if(userDetails==null) throw new BusinessException(ErrorCode.USER_INVALID_TOKEN);

        String refreshFromCookie = getCookie(httpReq, "refresh_token");
        if (refreshFromCookie == null || refreshFromCookie.isBlank()) {
            clearCookie(httpRes, "refresh_token");
            return;
        }

        sessionRepository.findByUser_IdAndRefreshToken(userDetails.getId(), refreshFromCookie)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_INVALID_REFRESH_TOKEN));

        sessionRepository.deleteByUser_IdAndRefreshToken(userDetails.getId(), refreshFromCookie);

        clearCookie(httpRes, "refresh_token");
    }

    private String getCookie(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return null;                    // 쿠키가 없으면 null
        for (var c : req.getCookies()) {                              // 모든 쿠키 순회
            if (name.equals(c.getName())) return c.getValue();        // 이름 일치 시 값 반환
        }
        return null;                                                  // 못 찾으면 null
    }


    private void addHttpOnlyCookie(HttpServletResponse res, String name, String value, Duration maxAge) {
        String cookie = name + "=" + value
                + "; Path=/"
                + "; HttpOnly"
                + "; Max-Age=" + maxAge.toSeconds();
        res.addHeader("Set-Cookie", cookie);
    }

    private void clearCookie(HttpServletResponse res, String name) {
        String cookie = name + "=;"
                + " Path=/"
                + "; HttpOnly"
                + "; Max-Age=0";
        res.addHeader("Set-Cookie", cookie);
    }

    private String extractClientIp(HttpServletRequest req) {
        String fwd = req.getHeader("X-Forwarded-For");
        if (fwd != null && !fwd.isBlank()) return fwd.split(",")[0].trim();
        return req.getRemoteAddr();
    }
}
