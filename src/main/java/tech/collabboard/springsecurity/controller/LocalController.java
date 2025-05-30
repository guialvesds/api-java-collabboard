package tech.collabboard.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class LocalController {

    @GetMapping("/")
    public ResponseEntity<String> local() {
        return ResponseEntity.ok("Api Online!");
    }
}
