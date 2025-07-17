package br.edu.ifmg.produto.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;


@Getter
public enum RoleLevel {

    ROLE_CLIENT(1),
    ROLE_SELLER(2),
    ROLE_ADMIN(3);

    private final int level;

    RoleLevel(int level) {
        this.level = level;
    }

    public static Optional<RoleLevel> fromString(String role) {
        return Arrays.stream(values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst();
    }

}
