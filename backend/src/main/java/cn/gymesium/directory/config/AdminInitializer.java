package cn.gymesium.directory.config;

import cn.gymesium.directory.entity.Account;
import cn.gymesium.directory.entity.AccountStatus;
import cn.gymesium.directory.entity.Role;
import cn.gymesium.directory.repository.AccountRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** 首次启动自动创建管理员账号，避免在 SQL 里写死密码哈希。 */
@Configuration
public class AdminInitializer {

    @Bean
    public ApplicationRunner initAdmin(AccountRepository accounts, PasswordEncoder encoder, AppProperties props) {
        return args -> {
            String username = props.admin().username();
            if (accounts.existsByUsername(username)) {
                return;
            }
            Account admin = new Account();
            admin.setUsername(username);
            admin.setRealName(props.admin().realName());
            admin.setPasswordHash(encoder.encode(props.admin().password()));
            admin.setRole(Role.ADMIN);
            admin.setStatus(AccountStatus.APPROVED);
            accounts.save(admin);
        };
    }
}
