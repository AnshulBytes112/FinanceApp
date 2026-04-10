package com.zorvyn.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "JWT authentication response containing the access token and user metadata")
public class JwtResponse {
    @Schema(description = "Bearer access token")
    private String token;

    @Schema(description = "Type of token", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Database ID of the authenticated user")
    private Long id;

    @Schema(description = "Email of the user")
    private String email;

    @Schema(description = "List of assigned roles")
    private List<String> roles;

    public JwtResponse(String token, Long id, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
