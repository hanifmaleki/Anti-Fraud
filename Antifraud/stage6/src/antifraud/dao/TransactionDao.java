package antifraud.dao;

import antifraud.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionDao {
    private HashMap<String, List<Transaction>> transactionsMap = new HashMap<>();

    private long id = 1;

    public Transaction save(Transaction transaction) {
        final List<Transaction> transactions = findByCardNumber(transaction.getCardSerial());
        transaction.setId(id++);
        transactions.add(transaction);
        return transaction;
    }

    public Optional<Transaction> fundById(long id) {
        return transactionsMap
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public List<Transaction> findByCardNumber(String cardSerial) {
        return transactionsMap.computeIfAbsent(cardSerial, s -> new ArrayList<>());
    }

}
