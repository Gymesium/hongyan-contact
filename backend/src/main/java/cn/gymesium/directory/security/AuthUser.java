package cn.gymesium.directory.security;

import cn.gymesium.directory.entity.Role;

public record AuthUser(Long id, String username, Role role) {
}
