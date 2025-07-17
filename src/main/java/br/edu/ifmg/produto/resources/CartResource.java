package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.*;
import br.edu.ifmg.produto.entities.Cart;
import br.edu.ifmg.produto.services.AuthService;
import br.edu.ifmg.produto.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/cart")
@Tag(name = "Cart", description = "Operations related to shopping cart")
public class CartResource {

    @Autowired
    CartService cartService;

    @Autowired
    AuthService authService;

    @Operation(
            summary = "Add items to cart",
            description = "Adds items to the authenticated user's cart. Requires the user to have the CLIENT role.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cart updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartInsertDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT')")
    public ResponseEntity<CartInsertDTO> addToCart(
            @Parameter(description = "DTO containing cart items to be added", required = true)
            @Valid @RequestBody CartInsertDTO dto) {

        Cart cart = authService.getAuthenticatedCart();

        CartInsertDTO cartDTO = cartService.addToCart(cart, dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(cartDTO);
    }

    @Operation(
            summary = "Get cart by user ID",
            description = "Retrieves the shopping cart of a user by ID. Requires CLIENT role. Usually used internally or for admin views.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Cart not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT')")
    public ResponseEntity<CartDTO> getCartByUserId(
            @Parameter(description = "ID of the user whose cart is being retrieved", required = true)
            @PathVariable Long id) {

        CartDTO cartDTO = cartService.findByUserId(id);
        return ResponseEntity.ok(cartDTO);
    }

}
