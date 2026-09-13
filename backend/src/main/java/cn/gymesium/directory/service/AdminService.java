package cn.gymesium.directory.service;

import cn.gymesium.directory.common.ApiException;
import cn.gymesium.directory.dto.AdminAccountDto;
import cn.gymesium.directory.dto.PageResponse;
import cn.gymesium.directory.entity.Account;
import cn.gymesium.directory.entity.AccountStatus;
import cn.gymesium.directory.entity.AuditLog;
import cn.gymesium.directory.repository.AccountRepository;
import cn.gymesium.directory.repository.AuditLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AccountRepository accounts;
    private final AuditLogRepository auditLogs;

    public AdminService(AccountRepository accounts, AuditLogRepository auditLogs) {
        this.accounts = accounts;
        this.auditLogs = auditLogs;
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminAccountDto> list(String status, String keyword, int page, int size) {
        AccountStatus parsed = (status == null || status.isBlank()) ? null : AccountStatus.valueOf(status);
        String normalized = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        var result = accounts.search(parsed, normalized,
                PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50)));
        return PageResponse.of(result, AdminAccountDto::from);
    }

    @Transactional
    public AdminAccountDto changeStatus(String operator, Long id, AccountStatus target) {
        Account account = load(id);
        if (target == AccountStatus.DISABLED && account.getStatus() != AccountStatus.APPROVED) {
            throw ApiException.badRequest("只能禁用已审核通过的账户");
        }
        account.setStatus(target);
        auditLogs.save(new AuditLog(operator, "STATUS_" + target.name(), account.getUsername()));
        return AdminAccountDto.from(account);
    }

    /** 仅允许删除未通过审核（PENDING / REJECTED）的账户。 */
    @Transactional
    public void delete(String operator, Long id) {
        Account account = load(id);
        if (account.getStatus() == AccountStatus.APPROVED || account.getStatus() == AccountStatus.DISABLED) {
            throw ApiException.badRequest("已审核通过的账户不允许删除，只能禁用");
        }
        auditLogs.save(new AuditLog(operator, "DELETE", account.getUsername()));
        accounts.delete(account);
    }

    private Account load(Long id) {
        Account account = accounts.findById(id)
                .orElseThrow(() -> ApiException.notFound("账户不存在"));
        if (account.getRole() != cn.gymesium.directory.entity.Role.STUDENT) {
            throw ApiException.forbidden("不允许操作管理员账户");
        }
        return account;
    }
}
