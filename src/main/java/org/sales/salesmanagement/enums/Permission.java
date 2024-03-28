package org.sales.salesmanagement.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum Permission {

        CLIENT_READ("client:read"),

        CLIENT_UPDATE("admin:update"),
        CLIENT_CREATE("admin:create"),
        ADMIN_DELETE("admin:delete"),
    ;
        private final String permission;
    public String getPermission() {
        return permission;
    }
}
