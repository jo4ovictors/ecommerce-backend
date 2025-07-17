package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CartDTO", description = "Data transfer object representing a shopping cart")
public class CartInsertDTO {

    @Schema(description = "Unique identifier of the cart", example = "7001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User Id who owns this cart")
    private Long userId;

    @Schema(description = "List of items contained in the cart")
    private List<CartItemInsertDTO> items = new ArrayList<>();

    @Schema(description = "Total price of all items in the cart", example = "199.90")
    private BigDecimal total;

    public CartInsertDTO(Cart entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.items = entity.getItems().stream()
                .map(CartItemInsertDTO::new)
                .collect(Collectors.toList());
        this.total = entity.getTotal();
    }
}
