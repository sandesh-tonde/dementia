package com.db.ppt.dementia.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String CAREGIVER = "ROLE_CAREGIVER";

    public static final String NURSE = "ROLE_NURSE";

    public static final String MODERATOR = "ROLE_MODERATOR";

    private AuthoritiesConstants() {}
}
