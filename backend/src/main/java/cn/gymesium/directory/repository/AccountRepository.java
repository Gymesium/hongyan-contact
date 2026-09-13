package cn.gymesium.directory.repository;

import cn.gymesium.directory.entity.Account;
import cn.gymesium.directory.entity.AccountStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
            select a from Account a
            where a.role = cn.gymesium.directory.entity.Role.STUDENT
              and (:status is null or a.status = :status)
              and (:keyword is null
                   or lower(a.username) like lower(concat('%', :keyword, '%'))
                   or lower(a.realName) like lower(concat('%', :keyword, '%')))
            order by a.createdAt desc
            """)
    Page<Account> search(@Param("status") AccountStatus status,
                         @Param("keyword") String keyword,
                         Pageable pageable);
}
