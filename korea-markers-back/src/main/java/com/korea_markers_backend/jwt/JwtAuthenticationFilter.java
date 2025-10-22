package com.korea_markers_backend.jwt;

import com.korea_markers_backend.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            //  요청 헤더에서 토큰 추출
            String token = resolveToken(request);

            // 토큰이 존재하고 유효하면
            if (token != null && jwtTokenProvider.validateToken(token)) {

                // JWT에서 username(email) 추출
                String username = jwtTokenProvider.getUsername(token);

                // DB에서 사용자 정보 조회 (Spring Security UserDetails 형태로)
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // 인증 토큰 생성 (비밀번호 null, 권한은 userDetails에서 가져옴)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 6SecurityContext에 인증정보 저장 → 이후 컨트롤러에서 @AuthenticationPrincipal 등 사용 가능
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // JWT 파싱 실패나 만료, DB 조회 실패 등 모든 예외는 인증 실패로 간주
            SecurityContextHolder.clearContext();
        }

        // 다음 필터로 전달 (JWT 없거나 실패해도 체인은 계속 진행)
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 이후 부분만 잘라냄
        }
        return null;
    }
}
