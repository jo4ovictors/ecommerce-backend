package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoryDTO", description = "Data transfer object representing a product category")
public class CategoryDTO extends RepresentationModel<CategoryDTO> {

    @Schema(description = "Unique identifier of the category", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the category", example = "Electronics")
    private String name;

    @Schema(description = "URL of the image representing the category", example = "https://example.com/images/electronics.png")
    private String imageUrl;

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.imageUrl = entity.getImageUrl();
    }

}
