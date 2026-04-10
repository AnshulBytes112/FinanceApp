package com.zorvyn.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Login credentials request")
public class LoginRequest {
    @NotBlank
    @Schema(description = "User's registered email address", example = "admin@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User's password", example = "admin123")
    private String password;
}
