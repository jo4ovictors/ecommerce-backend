package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserInsertDTO", description = "DTO used to insert a new user including password")
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Password is required")
    @Schema(description = "Password for the new user account", example = "StrongPassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    public UserInsertDTO(User entity) {
        super(entity);
        this.password = entity.getPassword();
    }

}
