package br.edu.ifmg.produto.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RequestTokenDTO", description = "Data Transfer Object used to request a password reset or authentication token using an email address")
public class RequestTokenDTO {

    @Schema(description = "User's email address used to request the token", example = "user@example.com")
    @NotBlank(message = "Field is required")
    @Email(message = "Invalid email address")
    private String email;

}
