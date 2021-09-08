import antifraud.AntifraudApplication;
import antifraud.model.*;
import data.TestDataProvider;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.stream.Stream;

import static antifraud.model.ResultEnum.*;

public class AntifraudTest extends AntifraudBaseTest {

    private final TestDataProvider data = new TestDataProvider();
    private final TransactionTestUtil transactionUtil = new TransactionTestUtil(this);
    private final CardAndIPTestUtil cardIpUtil = new CardAndIPTestUtil(this);
    private final UserTestUtil userUtil = new UserTestUtil(this);
    private final TransactionTypeTestUtil transactionTypeTestUtil = new TransactionTypeTestUtil(this);
    private final AuthorizationTestUtil authorizationUtil = new AuthorizationTestUtil(this, cardIpUtil, transactionUtil, userUtil, transactionTypeTestUtil, data);


    public AntifraudTest() {
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
        transactionTypeTestUtil.addTransactionTypesAndCheckSuccessResult(Stream.of(data.trxType.testTransactionType));
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


    private void checkTransactionTypesCrud() {

        transactionTypeTestUtil.addTransactionTypesAndCheckSuccessResult(data.trxType.getAllValidTransactionTypeStream());

        // Add a transaction-type having duplicate name cause exception
        transactionTypeTestUtil.addDuplicateAndExpectException(data.trxType.getRandomValidTransactionType());


        transactionTypeTestUtil.deleteTransactionTypesAndExpectSuccess(data.trxType.getAllValidTransactionTypeStream());

        transactionTypeTestUtil.addInvalidTransactionTypesAndExpectException(data.trxType.getAllInvalidTransactionTypeStream());

        //Remove from empty transactionType
        transactionTypeTestUtil.deleteNonExistingTransactionTypeAndExpectException(data.trxType.realWareTransactionType);

    }

    //    private void Checking Transaction Rules
    private void checkCountryCountRules() {
        // given a transaction with 3 or more country in history
        TransactionType type = data.trxType.getRandomValidTransactionType();
        TransactionQueryRequest transactionNotAllowedCountryCount = getTransactionQueryRequest(type, 3, 3, ALLOWED);

        // When ask if it is allowed
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, transactionNotAllowedCountryCount, PROHIBITED, "TODO fill it");
        // it is forbidden

        // given a transaction with 2

        TransactionQueryRequest transactionManualCountryCount = getTransactionQueryRequest(type, 2, 2, ALLOWED);
        // When ask if it is allowed
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, transactionManualCountryCount, MANUAL_PROCESSING, "TODO fill it");
        // it is manuall
    }

    private TransactionQueryRequest getTransactionQueryRequest(TransactionType type, int countryCount, int ipCount, ResultEnum result) {
        final Transaction transaction = data.transaction.builder()
                .type(type)
                .result(result)
                .build();


        TransactionQueryRequest transactionNotAllowedCountryCount = TransactionQueryRequest.builder()
                .transaction(transaction)
                .countryCount(countryCount)
                .ipCount(ipCount)
                .build();
        return transactionNotAllowedCountryCount;
    }

    private void checkIpRules() {
        // Having many valid trx
        // When ask they if they are valid
        // they are allowed
        data.trxType.getAllValidTransactionTypeStream()
                .map(type -> getTransactionQueryRequest(type, 1, 1, ALLOWED))
                .forEach(trxRequest -> transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, trxRequest, ALLOWED, "TODO fill it"));


        // Having many valid trx
        // And country history 2
        // They are manual
        data.trxType.getAllValidTransactionTypeStream()
                .map(type -> getTransactionQueryRequest(type, 2, 2, ALLOWED))
                .forEach(trxRequest -> transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, trxRequest, MANUAL_PROCESSING, "TODO fill it"));

        // Having many valid trx
        // And country history 3
        // They are forbidden
        data.trxType.getAllValidTransactionTypeStream()
                .map(type -> getTransactionQueryRequest(type, 3, 3, ALLOWED))
                .forEach(trxRequest -> transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, trxRequest, PROHIBITED, "TODO fill it"));

        // Check similarly for the ip
        // Check similarly for the ip
        final TransactionType type = data.trxType.getRandomValidTransactionType();
        final Transaction transaction = data.transaction.builder()
                .ipSuspicious(true)
                .type(type)
                .result(ALLOWED)
                .build();
        TransactionQueryRequest request = TransactionQueryRequest.builder()
                .ipCount(1)
                .countryCount(1)
                .transaction(transaction)
                .build();
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED, "allowed");

        request.setIpCount(4);
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, MANUAL_PROCESSING, "IPs exceeded");

        request.setIpCount(3);
        request.setCountryCount(3);
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, MANUAL_PROCESSING, "countries exceeded");

        request.setIpCount(5);
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED, "countries exceeded");

        request.setCountryCount(4);
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED, "IPs exceeded", "countries exceeded");

        transaction.setAmount(type.getMaxManuall()+1);
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED,"IPs exceeded", "countries exceeded", "transaction is above allowed value");
    }





    //TODO merge it
    private void checkTransactionWithSuspiciousIp() {
        final Transaction transaction = data.transaction.builder()
                .ipSuspicious(true)
                .type(data.trxType.getRandomValidTransactionType())
                .result(ALLOWED)
                .build();
        TransactionQueryRequest request = TransactionQueryRequest.builder()
                .ipCount(1)
                .countryCount(1)
                .transaction(transaction)
                .build();
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED, "TODO fill it");
    }

    private void checkTransactionWithStolenCard() {
        final Transaction transaction = data.transaction.builder()
                .cardStolen(true)
                .type(data.trxType.getRandomValidTransactionType())
                .result(ALLOWED)
                .build();
        TransactionQueryRequest request = TransactionQueryRequest.builder()
                .ipCount(1)
                .countryCount(1)
                .transaction(transaction)
                .build();
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED, "TODO fill it");
    }

    private void checkTransactionWithoutType() {
    }

    private void checkTransactionByAmount() {
    }
}
