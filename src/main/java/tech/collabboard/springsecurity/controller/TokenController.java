package tech.collabboard.springsecurity.controller;


import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.collabboard.springsecurity.dto.LoginRequest;
import tech.collabboard.springsecurity.dto.LoginResponse;
import tech.collabboard.springsecurity.entities.Role;
import tech.collabboard.springsecurity.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;

    private final UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

       var user =  userRepository.findByUsername(loginRequest.username());

       if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)){
           throw new BadCredentialsException("Usuário ou senha inválida!");
       }

       var now = Instant.now();
       var expiresIn = 300L; /*Este campo é em segundo*/

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

       var claims = JwtClaimsSet.builder()
               .issuer("Back-End-CollabBoard") /*Nome de quem está fazendo a requisição*/
               .subject((user.get().getUserId()).toString()) /*Exibe o ID do user requisitante*/
               .issuedAt(now)  /*Hora que foi gerado o token*/
               .expiresAt(now.plusSeconds(expiresIn)) /*Tempo que expira o token*/
               .claim("scope", scopes)
               .build();

       var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

       return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
