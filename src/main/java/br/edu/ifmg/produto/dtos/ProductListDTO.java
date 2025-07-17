package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductListDTO", description = "Data transfer object representing a product for listing purposes")
public class ProductListDTO extends RepresentationModel<ProductListDTO> {

    @Schema(description = "Database generated product ID", example = "6001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters")
    @Schema(description = "Name of the product", example = "Bluetooth Headphones")
    private String name;

    @Schema(description = "Detailed description of the product", example = "A wireless optical mouse with ergonomic design")
    private String description;

    @Positive(message = "Price must be positive")
    @Schema(description = "Price of the product", example = "89.90")
    private BigDecimal price;

    @Schema(description = "URL of the product image", example = "https://example.com/images/headphones.png")
    private String imageUrl;

    public ProductListDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
    }

}
