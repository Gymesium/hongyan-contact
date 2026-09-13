package cn.gymesium.directory.controller;

import cn.gymesium.directory.dto.ProfileRequest;
import cn.gymesium.directory.dto.ProfileResponse;
import cn.gymesium.directory.security.AuthUser;
import cn.gymesium.directory.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ProfileResponse myProfile(@AuthenticationPrincipal AuthUser user) {
        return profileService.myProfile(user.id());
    }

    @PutMapping
    public ProfileResponse update(@AuthenticationPrincipal AuthUser user,
                                  @Valid @RequestBody ProfileRequest req) {
        return profileService.updateMyProfile(user.id(), req);
    }
}
