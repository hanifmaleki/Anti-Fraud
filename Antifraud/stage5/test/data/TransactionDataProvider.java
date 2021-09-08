package data;

import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionQueryRequest;
import antifraud.model.TransactionType;

import java.util.Random;

import static antifraud.model.ResultEnum.ALLOWED;
import static data.TestDataProvider.BASE_ADDRESS;

public class TransactionDataProvider {

    public static final String TRX_ADDRESS = BASE_ADDRESS + "/transaction";

    private final TestDataProvider data;

    public TransactionDataProvider(TestDataProvider data) {
        this.data = data;
    }

    public TestTransaction builder() {
        return new TestTransaction();
    }

    public class TestTransaction {
        private ResultEnum result;
        private TransactionType transactionType;
        private boolean ipSuspicious = false;
        private boolean cardStolen = false;

        private TestTransaction() {
        }

        public TestTransaction result(ResultEnum result) {
            this.result = result;
            return this;
        }

        public TestTransaction type(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public TestTransaction ipSuspicious(boolean ipSuspicious) {
            this.ipSuspicious = ipSuspicious;
            return this;
        }

        public TestTransaction cardStolen(boolean cardStolen) {
            this.cardStolen = cardStolen;
            return this;
        }

        public Transaction build() {
            final Transaction transaction = new Transaction();
            String cardSerial = cardStolen ? data.ipCard.stolenCard1 : data.ipCard.okCard;
            String ipAddress = ipSuspicious ? data.ipCard.suspiciousIp1 : data.ipCard.okIp;
            transaction.setCardSerial(cardSerial);
            transaction.setIpAddress(ipAddress);
            transaction.setAmount(getAmount(result, transactionType));
            transaction.setType(transactionType.getName());

            return transaction;
        }

        private final Integer getAmount(ResultEnum result, TransactionType type) {
            switch (result) {
                case ALLOWED:
                    return getAllowedTransaction(type);
                case PROHIBITED:
                    return getProhibitedTransaction(type);
                case MANUAL_PROCESSING:
                    return getManualTransaction(type);
            }
            throw new RuntimeException("Not implemented for result of " + result);
        }

        private Integer getManualTransaction(TransactionType type) {
            int bound = type.getMaxManuall() - type.getMaxAllowed();
            return new Random().nextInt(bound) + type.getMaxManuall();
        }

        private Integer getProhibitedTransaction(TransactionType type) {
            return new Random().nextInt() + type.getMaxAllowed();
        }

        private Integer getAllowedTransaction(TransactionType type) {
            return new Random().nextInt(type.getMaxAllowed());
        }

    }

    public TransactionQueryRequest getAllowedTrxRequest(){
        return getAllowedTransaction(data.trxType.getRandomValidTransactionType());
    }

    public TransactionQueryRequest getAllowedTestTrxRequest(){
        return getAllowedTransaction(data.trxType.testTransactionType);
    }

    public TransactionQueryRequest getAllowedTransaction(TransactionType randomValidTransactionType) {
        final Transaction transaction = builder()
                .type(randomValidTransactionType)
                .result(ALLOWED)
                .build();
        return TransactionQueryRequest
                .builder()
                .transaction(transaction)
                .ipCount(1)
                .countryCount(1)
                .build();
    }


}
