import antifraud.AntifraudApplication;
import antifraud.model.User;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.testcase.CheckResult;

public class AntifraudPreviousStagesTest extends AntifraudBaseTest {

    private final TestDataProvider data = new TestDataProvider();
    private final TransactionTestUtil transactionUtil = new TransactionTestUtil(this);
    private final CardAndIPTestUtil cardIpUtil = new CardAndIPTestUtil(this);
    private final UserTestUtil userUtil = new UserTestUtil(this);
    private final AuthorizationTestUtil authorizationUtil = new AuthorizationTestUtil(this, cardIpUtil, transactionUtil, userUtil, data);


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
        userUtil.checkHashingPassword(data.adminUser1);

        userUtil.checkHashingPassword(data.basicUser2);
    }


    // Just try trx query with all types of users
    private void checkAuthentication() {
        authorizationUtil.checkAuthentication(data.adminUser1);
        authorizationUtil.checkAuthentication(data.supportUser1);
        authorizationUtil.checkAuthentication(data.basicUser2);

    }

    private void checkAuthorisation() {
        authorizationUtil.checkAuthorizations(data.adminUser1);
        authorizationUtil.checkAuthorizations(data.supportUser1);
        authorizationUtil.checkAuthorizations(data.basicUser2);
    }


    @Override
    public User getDefaultAdmin() {
        return data.adminUser0;
    }
}
