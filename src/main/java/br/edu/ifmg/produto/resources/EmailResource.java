package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.EmailDTO;
import br.edu.ifmg.produto.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/email")
@Tag(name = "Email", description = "Resource for sending system emails.")
public class EmailResource {

    @Autowired
    private EmailService emailService;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Email data transfer object containing the recipient address, subject, and message content.",
            required = true,
            content = @Content(schema = @Schema(implementation = EmailDTO.class))
    )
    @Operation(
            summary = "Send an email",
            description = "Sends a plain-text email using the provided data such as recipient, subject, and body."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email sent successfully (no content returned)."),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailDTO dto) {
        emailService.sendEmail(dto);
        return ResponseEntity.noContent().build();
    }

}
