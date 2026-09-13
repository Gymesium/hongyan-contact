package cn.gymesium.directory.repository;

import cn.gymesium.directory.entity.Major;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long> {
    List<Major> findByEnabledTrueOrderByNameAsc();
    List<Major> findAllByOrderByNameAsc();
    boolean existsByCode(String code);
}
