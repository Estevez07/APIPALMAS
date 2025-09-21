package com.laspalmas.api.model.enums;

public enum Rol {
 USER,ADMIN;

   @Override
    public String toString() {
        return "ROLE_" + name();  // -> "ROLE_USER", "ROLE_ADMIN"
    }
}
