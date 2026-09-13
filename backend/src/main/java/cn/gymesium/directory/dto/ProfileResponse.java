package cn.gymesium.directory.dto;

import cn.gymesium.directory.entity.StudentProfile;

public record ProfileResponse(
        String username,
        String realName,
        Long majorId,
        String majorName,
        String className,
        Integer enrollYear,
        Integer graduateYear,
        String employer,
        String city,
        String phone,
        String email
) {
    public static ProfileResponse from(StudentProfile p) {
        return new ProfileResponse(
                p.getAccount().getUsername(),
                p.getAccount().getRealName(),
                p.getMajor() == null ? null : p.getMajor().getId(),
                p.getMajor() == null ? null : p.getMajor().getName(),
                p.getClassName(), p.getEnrollYear(), p.getGraduateYear(),
                p.getEmployer(), p.getCity(), p.getPhone(), p.getEmail());
    }
}
