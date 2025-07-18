package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.AddressDTO;
import br.edu.ifmg.produto.dtos.RoleDTO;
import br.edu.ifmg.produto.dtos.UserDTO;
import br.edu.ifmg.produto.dtos.UserInsertDTO;
import br.edu.ifmg.produto.entities.Address;
import br.edu.ifmg.produto.entities.Cart;
import br.edu.ifmg.produto.entities.Role;
import br.edu.ifmg.produto.entities.User;
import br.edu.ifmg.produto.projections.UserDetailsProjection;
import br.edu.ifmg.produto.repository.CartRepository;
import br.edu.ifmg.produto.repository.RoleRepository;
import br.edu.ifmg.produto.repository.UserRepository;
import br.edu.ifmg.produto.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartRepository cartRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> list = repository.findAll(pageable);
        return list.map(u -> new UserDTO(u));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> opt = repository.findById(id);
        User user = opt.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        User novo = repository.save(entity);
        return new UserDTO(novo);

    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());

        entity.getRoles().clear();
        for (RoleDTO role : dto.getRoles()) {
            Role r = roleRepository.getReferenceById(role.getId());
            entity.getRoles().add(r);
        }

        entity.getAddresses().clear();
        if (dto.getAddresses() != null) {
            for (AddressDTO addressDTO : dto.getAddresses()) {
                Address address = new Address();
                address.setStreet(addressDTO.getStreet());
                address.setCity(addressDTO.getCity());
                address.setState(addressDTO.getState());
                address.setZipCode(addressDTO.getZipCode());
                address.setNumber(addressDTO.getNumber());
                address.setComplement(addressDTO.getComplement());
                address.setMain(addressDTO.isMain());

                address.setUser(entity);
                entity.getAddresses().add(address);
            }
        }
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User not found " + id);
        }

    }

    @Transactional
    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new ResourceNotFoundException("Integrity violation");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRoleByEmail(username);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection p : result) {
            user.addRole(new Role(p.getRoleId(), p.getAuthority()));
        }

        return user;
    }

    @Transactional
    public UserDTO signup(@Valid UserInsertDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto, entity);

        Role role = roleRepository.findByAuthority("ROLE_CLIENT");
        entity.getRoles().clear();
        entity.getRoles().add(role);

        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        User novo = repository.save(entity);

        Cart cart = new Cart();
        cart.setUser(novo);

        cartRepository.save(cart);

        return new UserDTO(novo);
    }
}
