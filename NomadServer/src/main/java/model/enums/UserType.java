package model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    GUEST, HOST, ADMIN, SUPER_ADMIN;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}