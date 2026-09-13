package cn.gymesium.directory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "学号不能为空")
        // 学号规则：固定 10 位数字（3 位学院 + 2 位专业 + 2 位入学年份 + 3 位个人编号），仅校验位数
        @Pattern(regexp = "\\d{10}", message = "学号为 10 位数字")
        String username,

        @NotBlank(message = "姓名不能为空")
        @Size(max = 32)
        String realName,

        @NotBlank(message = "密码不能为空")
        @Size(min = 8, max = 64, message = "密码长度 8-64 位")
        String password
) {
}
