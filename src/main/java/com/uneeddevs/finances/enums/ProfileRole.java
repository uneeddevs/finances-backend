package com.uneeddevs.finances.enums;

import lombok.Getter;

@Getter
public enum ProfileRole {
    ADMIN(1L),
    USER(2L);

    private final Long roleId;

    ProfileRole(Long roleId) {
        this.roleId = roleId;
    }

}
