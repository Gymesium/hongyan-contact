package cn.gymesium.directory.controller;

import cn.gymesium.directory.dto.*;
import cn.gymesium.directory.entity.AccountStatus;
import cn.gymesium.directory.security.AuthUser;
import cn.gymesium.directory.service.AdminService;
import cn.gymesium.directory.service.MajorService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final MajorService majorService;

    public AdminController(AdminService adminService, MajorService majorService) {
        this.adminService = adminService;
        this.majorService = majorService;
    }

    @GetMapping("/accounts")
    public PageResponse<AdminAccountDto> accounts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.list(status, keyword, page, size);
    }

    @PostMapping("/accounts/{id}/approve")
    public AdminAccountDto approve(@AuthenticationPrincipal AuthUser admin, @PathVariable Long id) {
        return adminService.changeStatus(admin.username(), id, AccountStatus.APPROVED);
    }

    @PostMapping("/accounts/{id}/reject")
    public AdminAccountDto reject(@AuthenticationPrincipal AuthUser admin, @PathVariable Long id) {
        return adminService.changeStatus(admin.username(), id, AccountStatus.REJECTED);
    }

    @PostMapping("/accounts/{id}/disable")
    public AdminAccountDto disable(@AuthenticationPrincipal AuthUser admin, @PathVariable Long id) {
        return adminService.changeStatus(admin.username(), id, AccountStatus.DISABLED);
    }

    @PostMapping("/accounts/{id}/enable")
    public AdminAccountDto enable(@AuthenticationPrincipal AuthUser admin, @PathVariable Long id) {
        return adminService.changeStatus(admin.username(), id, AccountStatus.APPROVED);
    }

    @DeleteMapping("/accounts/{id}")
    public Map<String, String> delete(@AuthenticationPrincipal AuthUser admin, @PathVariable Long id) {
        adminService.delete(admin.username(), id);
        return Map.of("message", "已删除该未审核账户");
    }

    @GetMapping("/majors")
    public List<MajorDto> majors() {
        return majorService.listAll();
    }

    @PostMapping("/majors")
    public MajorDto createMajor(@Valid @RequestBody MajorRequest req) {
        return majorService.create(req);
    }

    @PutMapping("/majors/{id}")
    public MajorDto updateMajor(@PathVariable Long id, @Valid @RequestBody MajorRequest req) {
        return majorService.update(id, req);
    }

    @DeleteMapping("/majors/{id}")
    public Map<String, String> disableMajor(@PathVariable Long id) {
        majorService.disable(id);
        return Map.of("message", "专业已停用");
    }
}
