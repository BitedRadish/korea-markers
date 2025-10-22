package com.korea_markers_backend.user.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SignInReqeustDTO {
    @Email
    private String email;
    @Valid
    private String password;
}
