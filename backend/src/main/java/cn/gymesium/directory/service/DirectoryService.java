package cn.gymesium.directory.service;

import cn.gymesium.directory.dto.PageResponse;
import cn.gymesium.directory.dto.ProfileResponse;
import cn.gymesium.directory.repository.StudentProfileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirectoryService {

    private final StudentProfileRepository profiles;

    public DirectoryService(StudentProfileRepository profiles) {
        this.profiles = profiles;
    }

    @Transactional(readOnly = true)
    public PageResponse<ProfileResponse> search(Long majorId, Integer enrollYear, String keyword,
                                                int page, int size) {
        String normalized = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        int safeSize = Math.min(Math.max(size, 1), 50);
        var result = profiles.search(majorId, enrollYear, normalized,
                PageRequest.of(Math.max(page, 0), safeSize));
        return PageResponse.of(result, ProfileResponse::from);
    }
}
