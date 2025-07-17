package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductDTO", description = "Data transfer object representing a product")
public class ProductDTO extends RepresentationModel<ProductDTO> {

    @Schema(description = "Database generated product ID", example = "5001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters")
    @Schema(description = "Name of the product", example = "Wireless Mouse")
    private String name;

    @Schema(description = "Detailed description of the product", example = "A wireless optical mouse with ergonomic design")
    private String description;

    @Positive(message = "Price must be positive")
    @Schema(description = "Price of the product", example = "29.99")
    private BigDecimal price;

    @Schema(description = "URL of the product image", example = "https://example.com/images/wireless-mouse.png")
    private String imageUrl;

    @NotEmpty(message = "Product must have at least one category")
    @Schema(description = "Set of categories this product belongs to")
    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
        entity.getCategories().forEach(c -> this.categories.add(new CategoryDTO(c)));
    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(c -> this.categories.add(new CategoryDTO(c)));
    }

}
