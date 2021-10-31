import antifraud.AntifraudApplication;
import antifraud.model.Transaction;
import antifraud.model.TransactionType;
import antifraud.model.User;
import data.TestDataProvider;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.testcase.CheckResult;
import util.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static antifraud.model.ResultEnum.ALLOWED;
import static antifraud.model.ResultEnum.MANUAL_PROCESSING;
import static hassured.HMatchers.*;

public class AntifraudTest extends AntifraudBaseTest {

    private final TestDataProvider data = new TestDataProvider();
    private final TransactionTestUtil transactionUtil = new TransactionTestUtil(this);
    private final CardTestUtil cardUtil = new CardTestUtil(this);
    private final IPTestUtil ipUtil = new IPTestUtil(this);
    private final UserTestUtil userUtil = new UserTestUtil(this);
    private final TransactionTypeTestUtil transactionTypeUtil = new TransactionTypeTestUtil(this);
    private final AuthorizationTestUtil authorizationUtil = new AuthorizationTestUtil(this, cardUtil, ipUtil, transactionUtil, userUtil, transactionTypeUtil, data);
    private final TransactionFeedbackUtil feedbackUtil = new TransactionFeedbackUtil(this, Long.class);
    private final TransactionHistoryUtils historyUtils = new TransactionHistoryUtils(this, Transaction.class);


    public AntifraudTest() {
        super(AntifraudApplication.class);
    }

    @DynamicTest
    CheckResult test1() {
        return runtTestScenario(this::checkIfDefaultAdminAddedByDefault);
    }

    @DynamicTest
    CheckResult test2() {
        return runtTestScenario(this::hashTest);
    }

    @DynamicTest
    CheckResult test3() {
        transactionTypeUtil.addTransactionTypesAndCheckSuccessResult(Stream.of(data.trxType.testTransactionType));
        return runtTestScenario(this::checkAuthentication);
    }


    @DynamicTest
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

        transactionTypeUtil.addTransactionTypesAndCheckSuccessResult(data.trxType.getAllValidTransactionTypeStream());

        // Add a transaction-type having duplicate name cause exception
        transactionTypeUtil.addDuplicateAndExpectException(data.trxType.getRandomValidTransactionType());


        transactionTypeUtil.deleteTransactionTypesAndExpectSuccess(data.trxType.getAllValidTransactionTypeStream());

        transactionTypeUtil.addInvalidTransactionTypesAndExpectException(data.trxType.getAllInvalidTransactionTypeStream());

        //Remove from empty transactionType
        transactionTypeUtil.deleteNonExistingTransactionTypeAndExpectException(data.trxType.realWareTransactionType);

    }

    //    private void Checking Transaction Rules
    private void checkCountryCountRules() {
        /*// given a transaction with 3 or more country in history
        TransactionType type = data.trxType.getRandomValidTransactionType();
        TransactionQueryRequest transactionNotAllowedCountryCount = getTransactionQueryRequest(type, 3, 3, ALLOWED);

        // When ask if it is allowed
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, transactionNotAllowedCountryCount, PROHIBITED, "TODO fill it");
        // it is forbidden

        // given a transaction with 2

        TransactionQueryRequest transactionManualCountryCount = getTransactionQueryRequest(type, 2, 2, ALLOWED);
        // When ask if it is allowed
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, transactionManualCountryCount, MANUAL_PROCESSING, "TODO fill it");
        // it is manuall*/
    }

    private void checkIpRules() {
        // Having many valid trx
        // When ask they if they are valid
        // they are allowed
        /*data.trxType.getAllValidTransactionTypeStream()
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
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED,"IPs exceeded", "countries exceeded", "transaction is above allowed value");*/
    }


    //TODO merge it
    private void checkTransactionWithSuspiciousIp() {
        /*final Transaction transaction = data.transaction.builder()
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
        transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, request, PROHIBITED, "TODO fill it");*/
    }

    private void checkTransactionWithoutType() {
    }

    private void checkTransactionByAmount() {
    }

    private void checkFeedbackUpdate() {
        // Update amount test
        final TransactionType transactionType = data.trxType.testTransactionType;
        final Transaction transaction = Transaction.builder()
                .cardSerial(data.ipCard.okCard1)
                .ipAddress(data.ipCard.okIp)
                .amount(55)
                .type(transactionType.getName())
                .build();

        //TODO add expect message word
        final long trxId = transactionUtil.queryTrxAndExpectResultEnum(data.user.adminUser0, transaction, MANUAL_PROCESSING);

        //Update .....
        feedbackUtil.feedBack(data.user.adminUser0, trxId, ALLOWED);

        final TransactionType receivedType = transactionTypeUtil.getByIdentifier(transactionType.getName());

        assertThat(receivedType.getMaxAllowed(), is(transactionType.getMaxAllowed()));
        assertThat(receivedType.getMaxManual(), is(transactionType.getMaxManual()));

        final Double expectedMaxAllowed = transactionType.getMaxAllowed() * 0.8 + 0.2 * transaction.getAmount();
        assertThat(receivedType.getCurrentMaxAllowed(), is(expectedMaxAllowed));
    }

    private void checkTransactionHistory() {
        // Given a card Id 1

        // Add trx-1
        final Transaction trx1 = Transaction.builder()
                .cardSerial(data.ipCard.okCard1)
                .ipAddress(data.ipCard.okIp)
                .amount(55)
                .type(data.trxType.testTransactionType.getName())
                .build();
        final long id1 = transactionUtil.queryTrxAndExpectUnauthorizedHttpStatus(data.user.adminUser0, trx1);

        // Given a card Id 2
        // Add trx-2
        final Transaction trx2 = Transaction.builder()
                .cardSerial(data.ipCard.okCard2)
                .ipAddress(data.ipCard.okIp)
                .amount(56)
                .type(data.trxType.foodTransactionType.getName())
                .build();
        final long id2 = transactionUtil.queryTrxAndExpectUnauthorizedHttpStatus(data.user.adminUser0, trx2);

        // Given a card Id 1
        // Add trx-3
        final Transaction trx3 = Transaction.builder()
                .cardSerial(data.ipCard.okCard1)
                .ipAddress(data.ipCard.okIp)
                .amount(65)
                .type(data.trxType.insuranceTransactionType.getName())
                .build();
        final long id3 = transactionUtil.queryTrxAndExpectUnauthorizedHttpStatus(data.user.adminUser0, trx3);

        // Given a card Id 4
        // Add trx-3
        final Transaction trx4 = Transaction.builder()
                .cardSerial(data.ipCard.okCard1)
                .ipAddress(data.ipCard.okIp)
                .amount(75)
                .type(data.trxType.onlineWareTransactionType.getName())
                .build();
        final long id4 = transactionUtil.queryTrxAndExpectUnauthorizedHttpStatus(data.user.adminUser0, trx4);

        // Get history of card 1
        final List<Transaction> transactionHistory = historyUtils.getTransactionHistory(data.user.adminUser0, data.ipCard.okCard1);
        // There are 3 transactions
        assertThat(transactionHistory, hasSize(3));
        // 1 & 3 & 4
        final List<Long> trxIds = transactionHistory.stream()
                .map(Transaction::getId)
                .collect(Collectors.toList());
        assertThat(trxIds, contains(id1));
        assertThat(trxIds, contains(id2));
        assertThat(trxIds, contains(id3));
    }
}
