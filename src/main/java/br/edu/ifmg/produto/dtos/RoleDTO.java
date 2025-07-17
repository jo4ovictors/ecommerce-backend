package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RoleDTO", description = "Data transfer object that represents a user role or authority (e.g., ROLE_ADMIN, ROLE_CLIENT")
public class RoleDTO {

    @Schema(description = "Unique identifier of the role", example = "1")
    private Long id;

    @Schema(description = "Name of the authority or role assigned to the user", example = "ROLE_ADMIN")
    private String authority;

    public RoleDTO(Role entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }

}
