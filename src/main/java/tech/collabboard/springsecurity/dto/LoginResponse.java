package tech.collabboard.springsecurity.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
