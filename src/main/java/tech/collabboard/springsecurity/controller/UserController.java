package tech.collabboard.springsecurity.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tech.collabboard.springsecurity.dto.CreateUserDto;
import tech.collabboard.springsecurity.entities.Role;
import tech.collabboard.springsecurity.entities.User;
import tech.collabboard.springsecurity.repository.RoleRepository;
import tech.collabboard.springsecurity.repository.UserRepository;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {

        var basicRole = roleRepository.findByName(Role.Values.basic.name());
        var userFromDb = userRepository.findByUsername(dto.username());

        if (userFromDb.isPresent()) {
            System.out.println("Usuário já existe!");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));
        user.setPrimaryName(dto.primaryName());
        user.setSecondName(dto.secondName());
        user.setAvatar(dto.avatar());
        user.setCreatedAt(dto.createdAt());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);

    }
}
