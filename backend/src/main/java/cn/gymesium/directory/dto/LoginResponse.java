package cn.gymesium.directory.dto;

public record LoginResponse(String token, MeResponse user) {
}
