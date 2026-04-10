package com.zorvyn.finance.dto;

import com.zorvyn.finance.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User registration request")
public class SignupRequest {
    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "newuser@example.com")
    private String email;

    @NotBlank
    @Size(min = 6)
    @Schema(description = "Password (minimum 6 characters)", example = "password123")
    private String password;

    @NotNull
    @Schema(description = "Role assigned to the user")
    private Role role;
}
