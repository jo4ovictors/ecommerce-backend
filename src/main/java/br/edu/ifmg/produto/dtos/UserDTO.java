package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserDTO", description = "Data transfer object representing a user")
public class UserDTO {

    @Schema(description = "Unique identifier of the user", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Field is required")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Set of roles assigned to the user")
    private Set<RoleDTO> roles = new HashSet<>();

    @Schema
    private String phone;

    @Schema(description = "Set of addresses associated with the user")
    private Set<AddressDTO> addresses = new HashSet<>();


    public UserDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        phone = user.getPhone();

        user.getRoles().forEach(role -> roles.add(new RoleDTO(role)));

        if (user.getAddresses() != null) {
            addresses = user.getAddresses()
                    .stream()
                    .map(AddressDTO::new)
                    .collect(Collectors.toSet());
        }
    }

}
