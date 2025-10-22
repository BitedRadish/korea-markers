package com.korea_markers_backend.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponseDTO {
    private String accessToken;
}
