package cn.gymesium.directory.service;

import cn.gymesium.directory.common.ApiException;
import cn.gymesium.directory.common.SanitizeUtil;
import cn.gymesium.directory.dto.*;
import cn.gymesium.directory.entity.*;
import cn.gymesium.directory.repository.AccountRepository;
import cn.gymesium.directory.repository.StudentProfileRepository;
import cn.gymesium.directory.security.JwtService;
import java.time.OffsetDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AccountRepository accounts;
    private final StudentProfileRepository profiles;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(AccountRepository accounts, StudentProfileRepository profiles,
                       PasswordEncoder encoder, JwtService jwtService) {
        this.accounts = accounts;
        this.profiles = profiles;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest req) {
        if (accounts.existsByUsername(req.username())) {
            throw ApiException.badRequest("该学号已注册，请直接登录或联系管理员");
        }
        Account account = new Account();
        account.setUsername(req.username());
        account.setRealName(SanitizeUtil.clean(req.realName()));
        account.setPasswordHash(encoder.encode(req.password()));
        account.setRole(Role.STUDENT);
        account.setStatus(AccountStatus.PENDING);
        accounts.save(account);

        StudentProfile profile = new StudentProfile();
        profile.setAccount(account);
        profiles.save(profile);
    }

    @Transactional
    public LoginResponse login(LoginRequest req) {
        Account account = accounts.findByUsername(req.username())
                .orElseThrow(() -> ApiException.unauthorized("学号或密码错误"));
        if (!encoder.matches(req.password(), account.getPasswordHash())) {
            throw ApiException.unauthorized("学号或密码错误");
        }
        switch (account.getStatus()) {
            case PENDING -> throw ApiException.forbidden("账号待管理员审核，审核通过后才可登录");
            case REJECTED -> throw ApiException.forbidden("注册申请已被驳回，请联系管理员");
            case DISABLED -> throw ApiException.forbidden("账号已被禁用");
            case APPROVED -> {
            }
        }
        account.setLastLoginAt(OffsetDateTime.now());
        account.setLoginCount(account.getLoginCount() + 1);
        return new LoginResponse(jwtService.issue(account), MeResponse.from(account));
    }

    @Transactional(readOnly = true)
    public MeResponse me(Long accountId) {
        return accounts.findById(accountId)
                .map(MeResponse::from)
                .orElseThrow(() -> ApiException.unauthorized("登录已失效，请重新登录"));
    }
}
