package cn.gymesium.directory.dto;

import cn.gymesium.directory.entity.Account;
import java.time.OffsetDateTime;

public record MeResponse(
        Long id,
        String username,
        String realName,
        String role,
        String status,
        OffsetDateTime lastLoginAt,
        int loginCount
) {
    public static MeResponse from(Account a) {
        return new MeResponse(a.getId(), a.getUsername(), a.getRealName(),
                a.getRole().name(), a.getStatus().name(), a.getLastLoginAt(), a.getLoginCount());
    }
}
