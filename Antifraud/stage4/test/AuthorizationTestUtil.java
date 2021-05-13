import antifraud.model.Role;
import antifraud.model.User;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.OK;

public class AuthorizationTestUtil extends BaseTestUtil {

    private final CardAndIPTestUtil cardUtil;
    private final TransactionTestUtil transactionUtil;
    private final UserTestUtil userUtil;
    private final Map<Privilege, Function<Map<String, String>, Boolean>> privilegeMethodsMap = new HashMap<>();

    public AuthorizationTestUtil(AntifraudBaseTest testClass, User admin0, CardAndIPTestUtil cardUtil, TransactionTestUtil transactionUtil, UserTestUtil userUtil) {
        super(testClass);
        this.cardUtil = cardUtil;
        this.transactionUtil = transactionUtil;
        this.userUtil = userUtil;
        userUtil.addUserAndExceptStatus(admin0, OK);
    }

    enum Privilege {
        OWN_MODIFY,
        TRX_QUERY,
        CARD_MANAGEMENT,
        IP_MANAGEMENT,
        USER_MANAGEMENT;

        public static List<Privilege> ALL_PRIVILEGES = Arrays.asList(OWN_MODIFY, TRX_QUERY, CARD_MANAGEMENT, IP_MANAGEMENT, USER_MANAGEMENT);
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
        testClass.log("Checking authorizanios of the user {}", user);
        final List<Privilege> privileges = Privilege.getPrivileges(user.getRole());
        checkAuthorizations(user, privileges);
    }

    private void checkAuthorizations(User user, List<Privilege> privileges) {
        // Pre assumption: It is assumed that the only existing user in the DB is admin0
        // Add User
        testClass.log("Adding user {}", user);

        // There is no need to check it
        testClass.log("Check user exist in the list of users");

        Privilege.ALL_PRIVILEGES.stream()
                .forEach(pr -> this.checkPrivilegeForUserAndExpect(user, pr, privileges.contains(pr)));

        // delete users with adminUser
        userUtil.deleteExistingUser(user.getUsername());

    }

    private void checkPrivilegeForUserAndExpect(User user, Privilege privilege, boolean expected) {
        testClass.log("Check privilege {} for user {}=> expected: {} ", expected);
        final Function<Map<String, String>, Boolean> function = privilegeMethodsMap.get(privilege);

        final Boolean received = function.apply(getAuthorizationParameter(user));

        if (received != expected) {
            String feedback = String.format("User with role %s access to privilege %s. expected: %b___ received %b",
                    user, privilege, expected, received);
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private Map<String, String> getAuthorizationParameter(User user) {
        Map<String, String> authParameter = new HashMap<>();
        authParameter.put("user", user.getUsername());
        authParameter.put("password", user.getPassword());
        return authParameter;
    }


}
