import antifraud.AntifraudApplication;
import antifraud.model.User;
import data.TestDataProvider;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.testcase.CheckResult;

public class AntifraudPreviousStagesTest /*extends util.AntifraudBaseTest*/ {
/*

    private final TestDataProvider data = new TestDataProvider();
    private final TransactionTestUtil transactionUtil = new TransactionTestUtil(this);
    private final CardAndIPTestUtil cardIpUtil = new CardAndIPTestUtil(this);
    private final UserTestUtil userUtil = new UserTestUtil(this);
    private final TransactionTypeTestUtil typeUtil = new TransactionTypeTestUtil(this);
    private final AuthorizationTestUtil authorizationUtil = new AuthorizationTestUtil(this, cardIpUtil, transactionUtil, userUtil, typeUtil, data);


    public AntifraudPreviousStagesTest() {
        super(AntifraudApplication.class);
    }

    @DynamicTest
        // Check if defaultUser exist
    CheckResult test1() {
        return runtTestScenario(this::checkIfDefaultAdminAddedByDefault);
    }

    @DynamicTest
        // Test IP rest controller
    CheckResult test2() {
        return runtTestScenario(this::hashTest);
    }

    @DynamicTest
        // Adding incomplete users and expect 209
    CheckResult test3() {
        return runtTestScenario(this::checkAuthentication);
    }


    @DynamicTest
        // Delete non-existing user and except 404 NOT_FOUND
    CheckResult test4() {
        return runtTestScenario(this::checkAuthorisation);
    }


    private void checkIfDefaultAdminAddedByDefault() {
        log("Check if default admin user has already added");
        userUtil.getUsersAndExpectSize(0);
    }


    private void hashTest() {
        userUtil.checkHashingPassword(data.user.adminUser1);

        userUtil.checkHashingPassword(data.user.basicUser2);
    }


    // Just try trx query with all types of users
    private void checkAuthentication() {
        authorizationUtil.checkAuthentication(data.user.adminUser1);
        authorizationUtil.checkAuthentication(data.user.supportUser1);
        authorizationUtil.checkAuthentication(data.user.basicUser2);

    }

    private void checkAuthorisation() {
        authorizationUtil.checkAuthorizations(data.user.adminUser1);
        authorizationUtil.checkAuthorizations(data.user.supportUser1);
        authorizationUtil.checkAuthorizations(data.user.basicUser2);
    }


    @Override
    public User getDefaultAdmin() {
        return data.user.adminUser0;
    }
*/
}
