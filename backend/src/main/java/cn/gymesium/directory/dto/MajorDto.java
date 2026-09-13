package cn.gymesium.directory.dto;

import cn.gymesium.directory.entity.Major;

public record MajorDto(Long id, String code, String name, boolean enabled) {
    public static MajorDto from(Major m) {
        return new MajorDto(m.getId(), m.getCode(), m.getName(), m.isEnabled());
    }
}
