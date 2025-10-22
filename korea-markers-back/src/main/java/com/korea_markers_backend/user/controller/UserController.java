package com.korea_markers_backend.user.controller;

import com.korea_markers_backend.user.dto.request.SignInReqeustDTO;
import com.korea_markers_backend.user.dto.request.SignUpRequestDTO;
import com.korea_markers_backend.user.dto.response.SignInResponseDTO;
import com.korea_markers_backend.user.entity.CustomUserDetails;
import com.korea_markers_backend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;



    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDTO req) {
        userService.signUp(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> signIn(
            @Valid @RequestBody SignInReqeustDTO req,
            HttpServletRequest httpReq,
            HttpServletResponse httpRes
    ) {
        SignInResponseDTO resp = userService.signIn(req, httpReq, httpRes);
        return ResponseEntity.ok(resp);
    }


    @PostMapping("/refresh")
    public ResponseEntity<SignInResponseDTO> refresh(
            HttpServletRequest httpReq,
            HttpServletResponse httpRes

    ) {
        SignInResponseDTO resp = userService.refresh(httpReq, httpRes);
        return ResponseEntity.ok(resp);
    }


    @PostMapping("/signout")
    public ResponseEntity<Void> signout(
            HttpServletRequest httpReq,
            HttpServletResponse httpRes,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        userService.signout(httpReq, httpRes,user);
        return ResponseEntity.noContent().build();
    }

}
