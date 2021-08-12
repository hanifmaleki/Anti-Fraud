import antifraud.AntifraudApplication;
import antifraud.model.TransactionType;
import antifraud.model.User;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.testcase.CheckResult;

public class AntifraudCurrentStageTest extends AntifraudBaseTest {

    private final TestDataProvider data = new TestDataProvider();
    private final TransactionTestUtil transactionUtil = new TransactionTestUtil(this);
    private final CardAndIPTestUtil cardIpUtil = new CardAndIPTestUtil(this);
    private final UserTestUtil userUtil = new UserTestUtil(this);
    private final TransactionTypeTestUtil transactionTypeTestUtil = new TransactionTypeTestUtil(this);
    private final AuthorizationTestUtil authorizationUtil = new AuthorizationTestUtil(this, cardIpUtil, transactionUtil, userUtil, data);


    public AntifraudCurrentStageTest() {
        super(AntifraudApplication.class);
    }

    /*
    TODO: Scenarios
    Checking Transaction types : Access add remove get
    Checking Trqansaction Rules
        TrxCheck1_ By Country manual - prohibited
        TrxCheck2_ By IP manual - prohibited
        TrxCheck3_ By amount
        TrxCheck4_ Unknown Type

     */

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


    private void checkTransactionTypesCrud() {

        transactionTypeTestUtil.addTransactionTypesAndCheckSuccessResult(data.getAllValidTransactionTypeStream());

        // Add a transaction-type having duplicate name cause exception
        transactionTypeTestUtil.addDuplicateAndExpectException(data.getRandomTransactionType());


        transactionTypeTestUtil.deleteTransactionTypesAndExpectSuccess(data.getAllValidTransactionTypeStream());

        transactionTypeTestUtil.addInvalidTransactionTypesAndExpectException(data.getAllInvalidTransactionTypeStream());

        //Remove from empty transactionType
        transactionTypeTestUtil.deleteNonExistingTransactionTypeAndExpectException(data.realWareTransactionType);

    }

    //    private void Checking Transaction Rules
    private void checkCountryCountRules() {
        // given a transaction with 3 or more country in history
        // When ask if it is allowed
        // it is forbidden

        // given a transaction with 2
        // When ask if it is allowed
        // it is manuall
    }

    private void checkIpRules() {
        // Having many valid trx
        // When ask they if they are valid
        // they are allowed

        // Having many valid trx
        // And country history 2
        // They are manual

        // Having many valid trx
        // And country history 3
        // They are forbidden

        // Check similarly for the ip

        // Having trx invalid only for amount
        //....

        // Having trx invalid for amount as well as ip

        // Having trx invalid for amount as well as country

        // Having trx invalid for amount as well as country and ip
    }

    private void checkTransactionWithoutType() {
    }

    private void checkTransactionByAmount() {
    }
}
