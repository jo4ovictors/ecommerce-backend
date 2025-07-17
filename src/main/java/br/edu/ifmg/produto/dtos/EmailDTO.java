package br.edu.ifmg.produto.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EmailDTO", description = "Data Transfer Object representing the contents of an email message")
public class EmailDTO {

    @Schema(description = "The recipient's email address", example = "user@example.com")
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String to;

    @Schema(description = "Subject line of the email", example = "Password Reset Request")
    @NotBlank(message = "Email subject is required")
    private String subject;

    @Schema(description = "Body content of the email message", example = "Click the link below to reset your password...")
    @NotBlank(message = "Email body is required")
    private String body;

}
