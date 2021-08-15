import antifraud.model.ResultEnum;
import antifraud.model.Role;
import antifraud.model.User;
import data.TestDataProvider;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AuthorizationTestUtil extends BaseTestUtil {

    private final CardAndIPTestUtil cardUtil;
    private final TransactionTestUtil transactionUtil;
    private final UserTestUtil userUtil;
    private final TransactionTypeTestUtil typeUtil;
    private final TestDataProvider data;
    private final Map<Privilege, Function<User, Boolean>> privilegeMethodsMap = new HashMap<>();

    public AuthorizationTestUtil(AntifraudBaseTest testClass, CardAndIPTestUtil cardUtil, TransactionTestUtil transactionUtil, UserTestUtil userUtil, TransactionTypeTestUtil typeUtil, TestDataProvider data) {
        super(testClass);
        this.cardUtil = cardUtil;
        this.transactionUtil = transactionUtil;
        this.userUtil = userUtil;
        this.typeUtil = typeUtil;
        this.data = data;

        privilegeMethodsMap.put(Privilege.OWN_MODIFY, userUtil::isAuthorizedChangeOwnPassword);
        privilegeMethodsMap.put(Privilege.TRX_QUERY, this::isAuthorizedTrx);
        privilegeMethodsMap.put(Privilege.CARD_MANAGEMENT, cardUtil::isAuthorizedCardGet);
        privilegeMethodsMap.put(Privilege.IP_MANAGEMENT, cardUtil::isAuthorizedIpGet);
        privilegeMethodsMap.put(Privilege.USER_MANAGEMENT, this::isAuthorizedUserMgt);
    }

    enum Privilege {
        OWN_MODIFY,
        TRX_QUERY,
        CARD_MANAGEMENT,
        IP_MANAGEMENT,
        USER_MANAGEMENT,
        TRANSACTION_TYPE_MANAGEMENT;

        public static List<Privilege> ALL_PRIVILEGES = Arrays.asList(
                OWN_MODIFY,
                TRX_QUERY,
                CARD_MANAGEMENT,
                IP_MANAGEMENT,
                USER_MANAGEMENT,
                TRANSACTION_TYPE_MANAGEMENT);
        public static List<Privilege> ADMIN_PRIVILEGES = ALL_PRIVILEGES;
        public static List<Privilege> SUPPORT_PRIVILEGES = Arrays.asList(OWN_MODIFY, TRX_QUERY, CARD_MANAGEMENT, IP_MANAGEMENT);
        public static List<Privilege> BASIC_PRIVILEGES = Arrays.asList(OWN_MODIFY, TRX_QUERY);

        public static List<Privilege> getPrivileges(Role role) {
            switch (role) {
                case ADMIN:
                    return ADMIN_PRIVILEGES;
                case SUPPORT:
                    return SUPPORT_PRIVILEGES;
                case USER:
                    return BASIC_PRIVILEGES;
            }
            throw new RuntimeException("No matched privileges for role " + role.toString());
        }

    }


    public void checkAuthorizations(User user) {
        testClass.log("Checking authorizations of the user {}", user);
        final List<Privilege> privileges = Privilege.getPrivileges(user.getRole());
        checkAuthorizations(user, privileges);
    }

    public void checkAuthentication(User user) {
        testClass.log("Check authentication for user {}", user.getUsername());
        // Add user
        userUtil.addUserAndExceptStatus(testClass.getDefaultAdmin(), user, HttpStatus.CREATED);
        // get trx with correct user

        transactionUtil.queryTrxAndExpectResultEnum(user, data.transaction.getAllowedTrxRequest(), ResultEnum.ALLOWED);
        // get trx with wrong password and except 401
        transactionUtil.queryTrxAndExpectUnauthorizedHttpStatus(data.user.adminUser1WithWrongPassword, data.transaction.getAllowedTrxRequest());

        userUtil.deleteExistingUser(testClass.getDefaultAdmin(), user.getUsername());

        testClass.log("Authentication test has been done succesfully for user {}", user);
    }

    private void checkAuthorizations(User user, List<Privilege> privileges) {
        // Pre assumption: It is assumed that the only existing user in the DB is admin0
        // Add User
        testClass.log("Adding user {}", user);
        userUtil.addUserAndExceptStatus(testClass.getDefaultAdmin(), user, HttpStatus.CREATED);

        // There is no need to check it
        testClass.log("Check user exist in the list of users");

        Privilege.ALL_PRIVILEGES
                .forEach(pr -> this.checkPrivilegeForUserAndExpect(user, pr, privileges.contains(pr)));

        // delete users with adminUser
        userUtil.deleteExistingUser(testClass.getDefaultAdmin(), user.getUsername());

    }

    private void checkPrivilegeForUserAndExpect(User user, Privilege privilege, boolean expected) {
        testClass.log("Check privilege {} for user {}=> expected: {} ", privilege, user, expected);
        final Function<User, Boolean> function = privilegeMethodsMap.get(privilege);

        final Boolean received = function.apply(user);

        if (received != expected) {
            String feedback = String.format("User with role %s access to privilege %s. expected: %b___ received %b",
                    user, privilege, expected, received);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private Boolean isAuthorizedUserMgt(User user) {
        return userUtil.isAuthorizedPasswordChange(user, testClass.getDefaultAdmin());
    }


    private Boolean isAuthorizedTrx(User user) {
        return transactionUtil.isUserAuthorizedForTrxQuery(user, data.transaction.getAllowedTrxRequest());
    }

    private Boolean isAuthorizedTypeManagement(User user) {
        return typeUtil.isAuthorizedForTypeManagement(user);
    }


}
