package com.sinanduman.library.model;

public enum Role {
    LIB_BM_C("lib.bm.c", "Grant user a permission to create a book"),
    LIB_BM_R("lib.bm.r", "Grant user a permission to query books"),
    LIB_BM_D("lib.bm.d", "Grant user a permission to delete a book"),

    LIB_M_C("lib.m.c", "Grant user a permission to create a member"),
    LIB_M_R("lib.m.r", "Grant user a permission to query a member"),
    LIB_M_D("lib.m.d", "Grant user a permission to delete a member"),
    LIB_M_R_ALL("lib.m.r.all", "Grant user a permission to query all members"),

    LIB_BO_C("lib.bo.c", "Grant user a permission to borrow a book"),
    LIB_BO_R("lib.bo.r", "Grant user a permission to query borrowings"),
    LIB_BO_R_ALL("lib.bo.r.all", "Grant user a permission to query borrowings for all users"),
    LIB_BO_RETURN("lib.bo.return", "Grant user a permission to return a book");

    private final String roleName;
    private final String roleDesc;

    Role(String roleName, String roleDesc) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    @Override
    public String toString() {
        return roleName;
    }

    public static Role findByRoleName(String name) {
        Role result = null;
        for (Role role : values()) {
            if (role.getRoleName().equalsIgnoreCase(name)) {
                result = role;
                break;
            }
        }
        return result;
    }
}
