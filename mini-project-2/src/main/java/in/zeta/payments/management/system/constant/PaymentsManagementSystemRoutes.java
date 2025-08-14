package in.zeta.payments.management.system.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentsManagementSystemRoutes {

    public static final String BASE_URL = "/pms-api";
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserRoutes{
        public static final String CREATE_USER_URL = "/users";
        public static final String GET_ALL_USERS_URL = "/users";
        public static final String GET_USER_BY_ID_URL = "/users/{userID}";
        public static final String UPDATE_USER_URL = "/users/{userID}";
        public static final String DELETE_USER_URL = "/users/{userID}";
        public static final String LOGIN_URL = "/users/login";
        public static final String MY_PROFILE_URL = "/users/my-profile";
        public static final String CHANGE_PASSWORD_URL = "/users/change-password";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PaymentRoutes {
        public static final String CREATE_PAYMENT_URL = "/payments";
        public static final String GET_ALL_PAYMENTS_URL = "/payments";
        public static final String GET_PAYMENT_BY_ID_URL = "/payments/{id}";
        public static final String UPDATE_PAYMENT_URL = "/payments/{id}";
        public static final String DELETE_PAYMENT_URL = "/payments/{id}";
    }

    @NoArgsConstructor (access = AccessLevel.PRIVATE)
    public static final class ReportGenerationRoutes {
        public static final String GENERATE_MONTHLY_REPORT_URL = "/reports/monthly";
        public static final String GENERATE_QUARTERLY_REPORT_URL = "/reports/quarterly";

    }

}
