package cn.gymesium.directory.service;

import cn.gymesium.directory.common.ApiException;
import cn.gymesium.directory.common.SanitizeUtil;
import cn.gymesium.directory.dto.ProfileRequest;
import cn.gymesium.directory.dto.ProfileResponse;
import cn.gymesium.directory.entity.Major;
import cn.gymesium.directory.entity.StudentProfile;
import cn.gymesium.directory.repository.MajorRepository;
import cn.gymesium.directory.repository.StudentProfileRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final StudentProfileRepository profiles;
    private final MajorRepository majors;

    public ProfileService(StudentProfileRepository profiles, MajorRepository majors) {
        this.profiles = profiles;
        this.majors = majors;
    }

    @Transactional(readOnly = true)
    public ProfileResponse myProfile(Long accountId) {
        return ProfileResponse.from(load(accountId));
    }

    /** 学生只能修改自己的资料：accountId 来自 JWT，不接受客户端传入。 */
    @Transactional
    public ProfileResponse updateMyProfile(Long accountId, ProfileRequest req) {
        StudentProfile profile = load(accountId);
        if (req.enrollYear() != null && req.graduateYear() != null
                && req.graduateYear() < req.enrollYear()) {
            throw ApiException.badRequest("毕业年份不能早于入校年份");
        }
        Major major = null;
        if (req.majorId() != null) {
            major = majors.findById(req.majorId())
                    .orElseThrow(() -> ApiException.badRequest("所选专业不存在"));
            if (!major.isEnabled()) {
                throw ApiException.badRequest("所选专业已停用，请选择其他专业");
            }
        }
        profile.setMajor(major);
        profile.setClassName(SanitizeUtil.clean(req.className()));
        profile.setEnrollYear(req.enrollYear());
        profile.setGraduateYear(req.graduateYear());
        profile.setEmployer(SanitizeUtil.clean(req.employer()));
        profile.setCity(SanitizeUtil.clean(req.city()));
        profile.setPhone(SanitizeUtil.clean(req.phone()));
        profile.setEmail(SanitizeUtil.clean(req.email()));
        profile.setUpdatedAt(OffsetDateTime.now());
        return ProfileResponse.from(profile);
    }

    private StudentProfile load(Long accountId) {
        return profiles.findById(accountId)
                .orElseThrow(() -> ApiException.notFound("通讯录记录不存在"));
    }
}
