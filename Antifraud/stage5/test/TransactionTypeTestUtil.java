import antifraud.model.TransactionType;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionTypeTestUtil extends BaseTestUtil {
    public TransactionTypeTestUtil(AntifraudBaseTest testClass) {
        super(testClass);
    }


    public void addTransactionTypesAndCheckSuccessResult(Stream<TransactionType> transactionTypes) {
        clearTransactionTypes();

        AtomicReference<Integer> counter = new AtomicReference<>(0);
        transactionTypes.forEach(transactionType -> {

            // given a trxType
            // Add it
            this.addTransactionTypeAndExpectCreated(transactionType);
            counter.getAndSet(counter.get() + 1);

            // you should give it
            final List<TransactionType> receivedTypes = this.getTransactionTypesAndExpectSize(counter.get());

            checkTransactionTypeExistInList(receivedTypes, transactionType);
        });
    }

    public void addDuplicateAndExpectException(TransactionType transactionType){
        this.clearTransactionTypes();
        //First time
        this.addTransactionTypeAndExpectCreated(transactionType);

        //Second time
        TransactionType duplicateTrxType = TransactionType
                .builder()
                .name(transactionType.getName())
                .maxManuall(transactionType.getMaxManuall()+1)
                .maxAllowed(transactionType.getMaxAllowed()+1)
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

    public void deleteNonExistingTransactionTypeAndExpectException(TransactionType transactionType){
        this.clearTransactionTypes();

        this.addTransactionTypeAndExpectCreated(transactionType);

        //First time
        this.deleteTransactionAndExpectDeleted(transactionType);

        //Second time
        this.deleteTransactionTypeAndExpectException(transactionType);
    }

    public void addInvalidTransactionTypesAndExpectException(Stream<TransactionType> invalidTransactionType){
        this.clearTransactionTypes();

        invalidTransactionType.forEach(this::addInvalidTransactionTypeAndExpectException);
    }

    private void addInvalidTransactionTypeAndExpectException(TransactionType invalidTransactionType){
        this.clearTransactionTypes();

        this.addTransactionTypeAndExpectException(invalidTransactionType);
    }

    private List<TransactionType> getTransactionTypesAndExpectSize(Integer size) {
        return null;
    }

    private List<TransactionType> getTransactionTypes() {
        return this.getTransactionTypesAndExpectSize(null);
    }

    private void addTransactionTypeAndExpectCreated(TransactionType transactionType) {

    }

    private void deleteTransactionAndExpectDeleted(TransactionType transactionType) {

    }

    private void deleteTransactionTypeAndExpectException(TransactionType transactionType) {

    }

    private void clearTransactionTypes() {
        getTransactionTypes().forEach(this::deleteTransactionAndExpectDeleted);
        getTransactionTypesAndExpectSize(0);
    }

    private void addTransactionTypeAndExpectException(TransactionType invalidTransactionType) {

    }

    private void checkTransactionTypeExistInList(List<TransactionType> transactionTypeList, TransactionType transactionType) {
        final String receivedNames = transactionTypeList.stream().map(TransactionType::getName).collect(Collectors.joining(","));
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
                });
    }


}
