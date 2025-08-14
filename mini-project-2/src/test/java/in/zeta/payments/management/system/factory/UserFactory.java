package in.zeta.payments.management.system.factory;

import in.zeta.payments.management.system.entity.User;
import in.zeta.payments.management.system.enums.Role;

public class UserFactory {

    public static final Long USER_ID = 1001L;
    public static final String USERNAME = "testUser";
    public static final String EMAIL = "user@example.com";
    public static final String PASSWORD = "password";
    public static final Role ROLE_ADMIN = Role.ADMIN;
    public static final Role ROLE_FINANCE_MANAGER = Role.FINANCE_MANAGER;
    public static final Role ROLE_VIEWER = Role.VIEWER;

    public static User getUser() {
        User user = new User();
        user.setUserID(USER_ID);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRole(ROLE_ADMIN);
        return user;
    }
}