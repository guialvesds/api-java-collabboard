package tech.collabboard.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PremiumController {

    @GetMapping("/premium")
    @PreAuthorize("hasAuthority('SCOPE_premium')")
    public ResponseEntity<String> premium() {
        return ResponseEntity.ok("Conteúdo para assinantes!");
    }
}
