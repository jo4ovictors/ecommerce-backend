package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.*;
import br.edu.ifmg.produto.entities.Cart;
import br.edu.ifmg.produto.entities.PasswordRecover;
import br.edu.ifmg.produto.entities.Store;
import br.edu.ifmg.produto.entities.User;
import br.edu.ifmg.produto.repository.CartRepository;
import br.edu.ifmg.produto.repository.PasswordRecoverRepository;
import br.edu.ifmg.produto.repository.StoreRepository;
import br.edu.ifmg.produto.repository.UserRepository;
import br.edu.ifmg.produto.services.exceptions.AccessDeniedException;
import br.edu.ifmg.produto.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private int tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String uri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createRecoverToken(RequestTokenDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        String token = UUID.randomUUID().toString();

        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setToken(token);
        passwordRecover.setEmail(user.getEmail());
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        passwordRecoverRepository.save(passwordRecover);

        String body = "Acesse o link para definir uma nova senha (válido por "
                + tokenMinutes + " minutos):\n\n" + uri + token;
        emailService.sendEmail(new EmailDTO(user.getEmail(), "Recuperação de Senha", body));
    }

    public void savePassword(@Valid NewPasswordDTO dto) {
        List<PasswordRecover> list = passwordRecoverRepository.searchValidToken(dto.getToken(), Instant.now());

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Token not found");
        }

        String email = list.getFirst().getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for token email"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AccessDeniedException("[!] -> Unauthenticated User!");
        }

        String email = jwt.getClaimAsString("username");

        jwt.getClaims().forEach((k, v) -> System.out.println(k + " => " + v));


        if (email == null || email.isBlank()) {
            throw new AccessDeniedException("[!] -> JWT token does not contain the 'username' claim!");
        }

        User entity = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new ResourceNotFoundException("[!] -> User not found: " + email + "!"));

        return new UserDTO(entity);
    }

    @Transactional(readOnly = true)
    public Store _getAuthenticatedStore() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AccessDeniedException("[!] -> Unauthenticated User!");
        }

        String email = jwt.getClaimAsString("username");

        if (email == null || email.isBlank()) {
            throw new AccessDeniedException("[!] -> JWT token does not contain the 'username' claim!");
        }

        return storeRepository.findByOwnerEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("[!] -> User not found: " + email + "!"));
    }

    @Transactional(readOnly = true)
    public StoreDTO getAuthenticatedStore() {
        return new StoreDTO(_getAuthenticatedStore());
    }

    public Cart getAuthenticatedCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AccessDeniedException("[!] -> Unauthenticated User!");
        }

        String email = jwt.getClaimAsString("username");

        if (email == null || email.isBlank()) {
            throw new AccessDeniedException("[!] -> JWT token does not contain the 'username' claim!");
        }

        return cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("[!] -> User not found: " + email + "!"));

    }
}
