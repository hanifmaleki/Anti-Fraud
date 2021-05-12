import antifraud.AntifraudApplication;
import antifraud.model.User;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.Arrays;
import java.util.List;

public class AntifraudTest extends AntifraudBaseTest {

    private final TestDataProvider data = new TestDataProvider();
    private final UserTestUtil userUtil = new UserTestUtil(this);
    private final TransactionTestUtil transactionUtil = new TransactionTestUtil(this);
    private final CardAndIPTestUtil carIpUtil = new CardAndIPTestUtil(this);

    private List<String> priviledges = Arrays.asList("own_modify", "trx_query",
            "card_management", "ip_management",
            "user_management");

    enum Privilege {
        OWN_MODIFY,
        TRX_QUERY,
        CARD_MANAGEMENT,
        IP_MANAGEMENT,
        USER_MANAGEMENT;

        public static List<Privilege> ADMIN_PRIVILEGES = Arrays.asList(OWN_MODIFY, TRX_QUERY, CARD_MANAGEMENT, IP_MANAGEMENT, USER_MANAGEMENT);
        public static List<Privilege> SUPPORT_PRIVILEGES = Arrays.asList(OWN_MODIFY, TRX_QUERY, CARD_MANAGEMENT, IP_MANAGEMENT);
        public static List<Privilege> BASIC_PRIVILEGES = Arrays.asList(OWN_MODIFY, TRX_QUERY);
    }


    public AntifraudTest() {
        super(AntifraudApplication.class);

    }

    @DynamicTest
        // Test IP rest controller
    CheckResult test1() {
        return runtTestScenario(null);
    }

    @DynamicTest
        // Adding incomplete users and expect 209
    CheckResult test2() {
        return runtTestScenario(null);
    }


    @DynamicTest
        // Delete non-existing user and except 404 NOT_FOUND
    CheckResult test3() {
        return runtTestScenario(null);
    }


    private void hashTest() {
        //Add user-1
        //Get users
        //Check Hashed Password
        //Remove user-1
        //get empty user list

        //Add user-2
        //Get Users
        //Check hashed password
        // Remove user-2
        // Check empty user list
    }


    // Just try trx query with all types of users
    private void authentication() {
        // Add adminUser1
        // get trx with correct user
        // get trx with wrong password and except 401

        // Add supportUser1
        // get trx with correctUser1
        // get trx with wrong password and except 401

        // Add basicUser1
        // get trx with correctUser1
        // get trx with wrong password and except 401

        // delete users with adminUser???
    }

    private void aTestAuthorisationBasicUser() {
        // Assume a basic user
        // He can query trx
        // He can change his password
        // He can not change other users password
        // He can not add stolen card
        // He can not remove stolen card
        // He can not add a user
    }

    private void supportUserAuthorizationTest() {
        // Assume a support user
        // He can query-trx
        // He can change his password
        // He can not change other users password
        // He can add stolen card
        // He can remove stolen card
        // He can not add a user
    }

    private void adminAuthorizationTest() {
        // Assume a support user
        // He can query-trx
        // He can change his password
        // He can not change other users password
        // He can add stolen card
        // He can remove stolen card
        // He can not add a user
    }

    private void addUserCheckPriviledgesRemove(User user, List<Privilege> privileges) {

    }


}
