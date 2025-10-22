package com.korea_markers_backend.user.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SignUpRequestDTO {
    private String email;
    private String password;
    private String passwordCheck;
}
