package cn.gymesium.directory.repository;

import cn.gymesium.directory.entity.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    @Query("""
            select p from StudentProfile p
              join p.account a
              left join p.major m
            where a.status = cn.gymesium.directory.entity.AccountStatus.APPROVED
              and a.role = cn.gymesium.directory.entity.Role.STUDENT
              and (:majorId is null or m.id = :majorId)
              and (:enrollYear is null or p.enrollYear = :enrollYear)
              and (:keyword is null
                   or lower(a.realName) like lower(concat('%', :keyword, '%'))
                   or lower(a.username) like lower(concat('%', :keyword, '%'))
                   or lower(coalesce(p.className, '')) like lower(concat('%', :keyword, '%'))
                   or lower(coalesce(p.employer, '')) like lower(concat('%', :keyword, '%'))
                   or lower(coalesce(p.city, '')) like lower(concat('%', :keyword, '%')))
            order by p.enrollYear desc nulls last, a.realName asc
            """)
    Page<StudentProfile> search(@Param("majorId") Long majorId,
                                @Param("enrollYear") Integer enrollYear,
                                @Param("keyword") String keyword,
                                Pageable pageable);
}
