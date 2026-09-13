package cn.gymesium.directory.controller;

import cn.gymesium.directory.dto.*;
import cn.gymesium.directory.security.AuthUser;
import cn.gymesium.directory.service.AuthService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return Map.of("message", "注册成功，请等待管理员审核");
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal AuthUser user) {
        return authService.me(user.id());
    }
}
