package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CartItemDTO", description = "Data transfer object representing an item in the shopping cart")
public class CartItemDTO {

    @Schema(description = "Unique identifier of the cart item", example = "8001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Product associated with this cart item")
    private ProductDTO product;

    @Schema(description = "Quantity of the product in the cart", example = "2")
    private int quantity;

    @Schema(description = "Price per unit of the product at the time of adding to cart", example = "49.95")
    private BigDecimal price;

    public CartItemDTO(CartItem entity) {
        this.id = entity.getId();
        this.product = new ProductDTO(entity.getProduct());
        this.quantity = entity.getQuantity();
        this.price = entity.getPrice();
    }

}