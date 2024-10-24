package tech.collabboard.springsecurity.dto;

import java.time.LocalDateTime;

public record CreateUserDto(String username, String password, String primaryName, String secondName, String avatar, LocalDateTime createdAt) {
}
