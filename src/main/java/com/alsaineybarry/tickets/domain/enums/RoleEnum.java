package com.alsaineybarry.tickets.domain.enums;

public enum RoleEnum {
    USER("USER"),
    ORGANIZER("ORGANIZER"),
    ADMIN("ADMIN");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static RoleEnum fromString(String roleName) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.roleName.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role with name " + roleName + " found");
    }
}
