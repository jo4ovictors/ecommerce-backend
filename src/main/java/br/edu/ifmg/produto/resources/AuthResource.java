package br.edu.ifmg.produto.resources;


import br.edu.ifmg.produto.dtos.NewPasswordDTO;
import br.edu.ifmg.produto.dtos.RequestTokenDTO;
import br.edu.ifmg.produto.dtos.UserDTO;
import br.edu.ifmg.produto.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and password recovery")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Request a password recovery token",
            description = "Sends a password recovery token to the user's registered email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Token successfully sent"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "User not found with provided email")
    })
    @PostMapping(value = "recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody RequestTokenDTO dto) {
        authService.createRecoverToken(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set a new password",
            description = "Sets a new password using the token received via email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid token or password format"),
            @ApiResponse(responseCode = "404", description = "Token not found or expired")
    })
    @PostMapping(value = "new-password")
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO dto) {
        authService.savePassword(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get authenticated user info",
            description = "Returns the details of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token")
    })
    @GetMapping(value = "/info")
    public ResponseEntity<UserDTO> getInfoUser() {
        UserDTO dto = authService.getAuthenticatedUser();
        return ResponseEntity.ok(dto);
    }

}
