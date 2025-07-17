package br.edu.ifmg.produto.repository;

import br.edu.ifmg.produto.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserEmail(String userEmail);

    @EntityGraph(attributePaths = {"items"})
    Optional <Cart> findByUserId(Long id);

}
