package br.edu.ifmg.produto.config.customgrant;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record CustomUserAuthorities(String username, Collection<? extends GrantedAuthority> authorities) {


}
