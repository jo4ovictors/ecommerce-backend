package br.edu.ifmg.produto.repository;

import java.util.Optional;
import br.edu.ifmg.produto.entities.Store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByOwnerEmail(String email);
}
