package com.laspalmas.api.constant;

public class Authorization {
    public static final String ADMIN = "hasAuthority('ROLE_ADMIN')";
    public static final String USER = "hasAuthority('ROLE_USER')";
    public static final String OWNER = "#credencial == authentication.name";
    public static final String OWNER_OR_ADMIN = OWNER + " or " + ADMIN;
    public static final String USER_OR_ADMIN = USER + " or " + ADMIN;
}