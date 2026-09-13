package cn.gymesium.directory.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Admin admin, Cors cors) {

    public record Jwt(String secret, long expireMinutes) {
    }

    public record Admin(String username, String password, String realName) {
    }

    public record Cors(List<String> allowedOrigins) {
    }
}
