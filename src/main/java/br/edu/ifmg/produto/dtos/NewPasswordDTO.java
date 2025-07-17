package br.edu.ifmg.produto.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NewPasswordDTO", description = "Data Transfer Object for submitting a new password along with a reset token")
public class NewPasswordDTO {

    @Schema(description = "The new password to be set for the user account", example = "SecureP@ss123")
    @NotBlank(message = "Password is required")
    private String newPassword;

    @Schema(description = "The token received via email used to authorize the password reset", example = "8f9a2b6c-f0e3-4c2d-ae1b-123456789abc")
    @NotBlank(message = "Token is required")
    private String token;

}
