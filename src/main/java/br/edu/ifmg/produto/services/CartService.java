package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.CartDTO;
import br.edu.ifmg.produto.dtos.CartInsertDTO;
import br.edu.ifmg.produto.dtos.CartItemInsertDTO;
import br.edu.ifmg.produto.entities.Cart;
import br.edu.ifmg.produto.entities.CartItem;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.repository.CartRepository;
import br.edu.ifmg.produto.repository.ProductRepository;
import br.edu.ifmg.produto.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public CartInsertDTO addToCart(Cart cart, CartInsertDTO dto) {
        List<CartItem> cartItems = new ArrayList<>();

        for (CartItemInsertDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDTO.getProductId()));

            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setCart(cart);

            cartItems.add(item);
        }

        cart.setItems(cartItems);
        cart.setTotal(dto.getTotal());

        cartRepository.save(cart);

        return new CartInsertDTO(cart);
    }


    @Transactional
    public CartDTO removeFromCart(Cart cart, Long productId) {
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();

            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                cart.getItems().remove(item);
            }

            BigDecimal newTotal = cart.getItems().stream()
                    .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cart.setTotal(newTotal);
        } else {
            throw new IllegalArgumentException("Produto com ID " + productId + " não encontrado no carrinho.");
        }

        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO findByUserId(Long id) {
        Cart cart = cartRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return new CartDTO(cart);
    }
}
