package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.DuplicateDataException;
import antifraud.exception.InvalidDataException;
import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionTypeService {

    // TODO move to transaction type dao
    private final Map<String, TransactionType> types = new HashMap<>();

    public void addTransactionType(TransactionType transactionType) {
        if (types.containsKey(transactionType.getName())) {
            throw new DuplicateDataException(String.format(transactionType.getName()));
        }
        if (transactionType.getMaxManual() <= transactionType.getMaxAllowed()) {
            throw new InvalidDataException("The Max Allowed value should be less than Max Manual");
        }
        transactionType.setCurrentMaxAllowed(Double.valueOf(transactionType.getMaxAllowed()));
        transactionType.setCurrentMaxManual(Double.valueOf(transactionType.getMaxManual()));
        types.put(transactionType.getName(), transactionType);
    }

    public void deleteTransactionType(String name) {
        if (!types.containsKey(name)) {
            throw new DataNotFoundException("There is no transaction type with name" + name);
        }
        types.remove(name);
    }

    public Optional<TransactionType> getTransactionTypeByName(String name) {
        return Optional.ofNullable(types.get(name));
    }

    public List<TransactionType> getTransactionTypes() {
        return new ArrayList(types.values());
    }

    public void applyFeedback(Transaction transaction, ResultEnum feedback) {
        final TransactionType type = getTransactionTypeByName(transaction.getType()).get();
        final double newMaxAllowed = type.getCurrentMaxAllowed() * 0.8 + transaction.getAmount() + 0.2;
        final double newMaxManual = type.getCurrentMaxManual() * 0.8 + transaction.getAmount() + 0.2;
        type.setCurrentMaxAllowed(newMaxAllowed);
        type.setCurrentMaxManual(newMaxManual);
        if (newMaxAllowed < newMaxManual) {
            final double mean = newMaxManual + newMaxAllowed / 2;
            type.setCurrentMaxAllowed(mean);
            type.setCurrentMaxManual(mean);
        }
    }
}
