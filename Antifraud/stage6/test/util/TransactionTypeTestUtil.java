package util;

import antifraud.model.TransactionType;
import antifraud.model.User;
import exception.UnexpectedResultException;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

import static data.TransactionTypeDataProvider.TRANSACTION_TYPE_ADDRESS;
import static hassured.HMatchers.*;

public class TransactionTypeTestUtil extends BaseRestTestUtil<TransactionType> {
    public TransactionTypeTestUtil(AntifraudBaseTest testClass) {
        super(testClass, TransactionType.class
        );
    }

    private User defaultUser = this.testClass.getDefaultAdmin();


    public void addTransactionTypesAndCheckSuccessResult(Stream<TransactionType> transactionTypes) {
        this.addTransactionTypesAndCheckSuccessResult(transactionTypes, defaultUser);
    }

    public void addTransactionTypesAndCheckSuccessResult(Stream<TransactionType> transactionTypes, User user) {
        this.clearAll();

        AtomicReference<Integer> counter = new AtomicReference<>(0);
        transactionTypes.forEach(transactionType -> {

            // given a trxType
            // Add it
            this.add(transactionType, user);
            counter.getAndSet(counter.get() + 1);

            // you should give it
            final List<TransactionType> receivedTypes = this.getTransactionTypesAndExpectSize(counter.get());

            checkTransactionTypeExistInList(receivedTypes, transactionType);
        });
    }

    public void addDuplicateAndExpectException(TransactionType transactionType) {
        this.clearTransactionTypes();
        //First time
        this.addTransactionTypeAndExpectCreated(transactionType, defaultUser);

        //Second time
        TransactionType duplicateTrxType = TransactionType
                .builder()
                .name(transactionType.getName())
                .maxManual(transactionType.getMaxManual() + 1)
                .maxAllowed(transactionType.getMaxAllowed() + 1)
                .build();
        this.addTransactionTypeAndExpectException(duplicateTrxType);

    }

    public void deleteTransactionTypesAndExpectSuccess(Stream<TransactionType> transactionTypes) {
        this.clearTransactionTypes();
        this.addTransactionTypesAndCheckSuccessResult(transactionTypes);


        // Delete it
        transactionTypes.forEach(this::deleteTransactionAndExpectDeleted);

        this.getTransactionTypesAndExpectSize(0);
    }

    public void deleteNonExistingTransactionTypeAndExpectException(TransactionType transactionType) {
        this.clearTransactionTypes();

        this.addTransactionTypeAndExpectCreated(transactionType, defaultUser);

        //First time
        this.deleteTransactionAndExpectDeleted(transactionType);

        //Second time
        this.deleteTransactionTypeAndExpectException(transactionType);
    }

    public void addInvalidTransactionTypesAndExpectException(Stream<TransactionType> invalidTransactionType) {
        this.clearTransactionTypes();

        invalidTransactionType.forEach(this::addInvalidTransactionTypeAndExpectException);
    }

    public Boolean isAuthorizedForTypeManagement(User user) {
        HttpRequest httpRequest = testClass.get(TRANSACTION_TYPE_ADDRESS)
                .addHeaders(testClass.getAuthorizationHeader(user));
        final HttpResponse httpResponse = httpRequest.send();
        final int responseCode = httpResponse.getStatusCode();
        if (responseCode == HttpStatus.FORBIDDEN.value()) {
            return false;
        }
        if (responseCode == HttpStatus.OK.value()) {
            return true;
        }
        final String feedback = String.format("Unexpected status %s received from Transaction query call", httpResponse);
        throw new UnexpectedResultException(CheckResult.wrong(feedback));
    }

    private void addInvalidTransactionTypeAndExpectException(TransactionType invalidTransactionType) {
        this.clearTransactionTypes();

        this.addTransactionTypeAndExpectException(invalidTransactionType);
    }

    private List<TransactionType> getTransactionTypesAndExpectSize(Integer expectedSize) {
        final List<TransactionType> transactionTypes = this.get();

        assertThat(transactionTypes, hasSize(expectedSize));

        return transactionTypes;
    }

    private List<TransactionType> getTransactionTypes() {
        return this.getTransactionTypesAndExpectSize(null);
    }

    private void addTransactionTypeAndExpectCreated(TransactionType transactionType, User user) {
        addTransactionTypeAndExpect(transactionType, 201, user);
    }


    private void addTransactionTypeAndExpect(TransactionType transactionType, int expectedCode, User user) {
        this.sendPostRequestAndExpect(transactionType, user, expectedCode, "");
       /* if (receivedCode != expectedCode) {
            String feedback = String.format("Expected status %d after adding item %s but received status %d for request:\n%s",
                    expectedCode, transactionType.getName(), receivedCode, httpRequest.getUri());
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }*/
    }

    private void deleteTransactionAndExpectDeleted(TransactionType transactionType) {
        this.deleteTransactionTypeAndExpect(transactionType, 200);
    }

    private void deleteTransactionTypeAndExpectException(TransactionType transactionType) {
        this.deleteTransactionTypeAndExpect(transactionType, 404);
    }

    private void deleteTransactionTypeAndExpect(TransactionType transactionType, int expectedCode) {
        this.deleteTransactionTypeAndExpect(transactionType, expectedCode, defaultUser);
    }

    private void deleteTransactionTypeAndExpect(TransactionType transactionType, int expectedCode, User user) {
        final HttpRequest deleteRequest = testClass.delete(TRANSACTION_TYPE_ADDRESS + "/" + transactionType.getName())
                .addHeaders(testClass.getAuthorizationHeader(user));

        final HttpResponse httpResponse = deleteRequest.send();

        int receivedCode = httpResponse.getStatusCode();
        if (receivedCode != expectedCode) {
            String feedback = String.format("Expected status %d after deleting item %s but received status %d for request:\n%s",
                    expectedCode, transactionType.getName(), receivedCode, deleteRequest.getUri());
            throw new UnexpectedResultException(CheckResult.wrong(feedback));
        }
    }

    private void clearTransactionTypes() {
        getTransactionTypes().forEach(this::deleteTransactionAndExpectDeleted);
        getTransactionTypesAndExpectSize(0);
    }

    private void addTransactionTypeAndExpectException(TransactionType invalidTransactionType) {

    }

    private void checkTransactionTypeExistInList(List<TransactionType> transactionTypeList, TransactionType transactionType) {
 /*       final String receivedNames = transactionTypeList.stream().map(TransactionType::getName).collect(Collectors.joining(","));
        transactionTypeList
                .stream()
                .filter(type -> type.getName().equalsIgnoreCase(transactionType.getName()))
                .findFirst()
                .orElseThrow(() -> {
                    String feedback = String.format("Could not find added transaction type with name %s in the received " +
                                    "transaction types. The received names are %s",
                            transactionType.getName(),
                            receivedNames);
                    return new UnexpectedResultException(CheckResult.wrong(feedback));
                });*/

        assertThat(transactionTypeList, contains(transactionType, getEqualPredicate()));
    }


    @Override
    public String getBaseAddress() {
        return "/transaction-type";
    }

    @Override
    protected String getEntityName() {
        return "Transaction Type";
    }

    @Override
    public Function<TransactionType, String> getIdentifier() {
        return TransactionType::getName;
    }
}
