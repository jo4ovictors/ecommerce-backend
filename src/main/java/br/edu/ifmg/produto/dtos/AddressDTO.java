package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AddressDTO", description = "Data transfer object representing a user's address")
public class AddressDTO {

    @Schema(description = "Unique identifier of the address", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Street name of the address", example = "123 Main St")
    private String street;

    @Schema(description = "Street number of the address", example = "456")
    private String number;

    @Schema(description = "Additional address information (e.g., apartment or suite)", example = "Apt 12B")
    private String complement;

    @Schema(description = "City where the address is located", example = "New York")
    private String city;

    @Schema(description = "State or region where the address is located", example = "NY")
    private String state;

    @Schema(description = "ZIP or postal code of the address", example = "10001")
    private String zipCode;

    @Schema(description = "Indicates if this is the user's primary address", example = "true")
    private boolean isMain;

    @Schema(description = "Identifier of the user associated with this address", example = "2002")
    private Long userId;

    public AddressDTO(Address entity) {
        this.id = entity.getId();
        this.street = entity.getStreet();
        this.number = entity.getNumber();
        this.complement = entity.getComplement();
        this.city = entity.getCity();
        this.state = entity.getState();
        this.zipCode = entity.getZipCode();
        this.isMain = entity.isMain();

        if (entity.getUser() != null) {
            this.userId = entity.getUser().getId();
        }
    }

}
