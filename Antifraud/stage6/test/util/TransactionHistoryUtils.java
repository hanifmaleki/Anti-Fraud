package util;

import antifraud.model.Transaction;
import antifraud.model.User;


import java.util.List;
import java.util.function.Function;

public class TransactionHistoryUtils extends BaseRestTestUtil<Transaction>{
    public TransactionHistoryUtils(AntifraudBaseTest testClass, Class<Transaction> clazz) {
        super(testClass, clazz);
    }

    @Override
    public String getBaseAddress() {
        return "transactions/transaction-history";
    }

    @Override
    protected String getEntityName() {
        return "Transaction History";
    }

    @Override
    public Function<Transaction, String> getIdentifier() {
        return null;
    }

    public List<Transaction> getTransactionHistory(User user, String cardNumber){
        final HttpRequest httpRequest = HttpRequestBuilder.builder(this)
                .withUser(user)
                .httpRequest();
        //TODO correct it
        httpRequest.send();
    }
}
