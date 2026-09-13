package cn.gymesium.directory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MajorRequest(
        @NotBlank @Size(max = 32) String code,
        @NotBlank @Size(max = 64) String name,
        Boolean enabled
) {
}
