package cn.gymesium.directory.dto;

import cn.gymesium.directory.entity.Account;
import java.time.OffsetDateTime;

public record AdminAccountDto(
        Long id,
        String username,
        String realName,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime lastLoginAt,
        int loginCount
) {
    public static AdminAccountDto from(Account a) {
        return new AdminAccountDto(a.getId(), a.getUsername(), a.getRealName(), a.getStatus().name(),
                a.getCreatedAt(), a.getLastLoginAt(), a.getLoginCount());
    }
}
