package br.edu.ifmg.produto.repository;

import br.edu.ifmg.produto.entities.User;
import br.edu.ifmg.produto.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("""
    SELECT DISTINCT u FROM User u
    LEFT JOIN FETCH u.addresses
    WHERE u.email = :email
    """)
    Optional<User> findByEmailWithRoles(@Param("email") String email);



    User findByEmailAndPassword(String email, String password);

    @Query(nativeQuery = true,
            value = """
                    SELECT u.email as username,
                           u.password,
                           r.id as roleId,
                           r.authority
    
                    FROM tb_user u
                    INNER JOIN tb_user_role ur ON u.id = ur.user_id
                    INNER JOIN tb_role r ON r.id = ur.role_id
                    WHERE u.email = :username
                    """

    )
    List<UserDetailsProjection> searchUserAndRoleByEmail(String username);
}
