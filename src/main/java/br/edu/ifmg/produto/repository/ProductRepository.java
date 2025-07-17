package br.edu.ifmg.produto.repository;

import br.edu.ifmg.produto.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

        @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN p.categories c
        WHERE (:categoryId IS NULL OR c.id = :categoryId)
        AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        """)
        Page<Product> findAllByCategoryAndName(
        @Param("categoryId") Long categoryId,
        @Param("name") String name,
        Pageable pageable);


        @Query("""
        SELECT p FROM Product p
        LEFT JOIN p.categories c
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR
        LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
        """)
        Page<Product> searchByName(@Param("name") String name, Pageable pageable);

        @EntityGraph(attributePaths = {"categories"})
        Page<Product> findByStoreId(Long storeId, Pageable pageable);

}
