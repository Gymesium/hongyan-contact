package cn.gymesium.directory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileRequest(
        Long majorId,
        @Size(max = 32) String className,
        @Min(1950) @Max(2100) Integer enrollYear,
        @Min(1950) @Max(2100) Integer graduateYear,
        @Size(max = 64) String employer,
        @Size(max = 32) String city,
        @Pattern(regexp = "|[0-9+\\-() ]{5,32}", message = "联系方式格式不正确") String phone,
        @Email(message = "邮箱格式不正确") @Size(max = 64) String email
) {
}
