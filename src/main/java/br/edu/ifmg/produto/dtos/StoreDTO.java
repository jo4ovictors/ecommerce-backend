package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "StoreDTO", description = "Data transfer object representing a store")
public class StoreDTO {

    @Schema(description = "Unique identifier of the store", example = "3001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Average rating of the store (from 0.0 to 5.0)", example = "4.5")
    private Double rating;

    @Schema(description = "Estimated delivery time in minutes", example = "30")
    private Integer deliveryTime;

    @Schema(description = "Category to which the store belongs")
    private CategoryDTO category;

    @Schema(description = "User who owns the store")
    private UserDTO owner;

    @Schema(description = "List of products offered by the store")
    private List<ProductDTO> products;

    public StoreDTO(Store entity) {
        this.id = entity.getId();
        this.rating = entity.getRating();
        this.deliveryTime = entity.getDeliveryTime();

        if (entity.getOwner() != null) {
            this.owner = new UserDTO(entity.getOwner());
        }

        if (entity.getProducts() != null) {
            this.products = entity.getProducts()
                    .stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
        }

        if (entity.getMainCategory() != null) {
            this.category = new CategoryDTO(entity.getMainCategory());
        }
    }

}
