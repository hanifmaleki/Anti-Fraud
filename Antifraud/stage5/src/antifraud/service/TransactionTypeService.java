package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.DuplicateDataException;
import antifraud.exception.InvalidDataException;
import antifraud.model.TransactionType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionTypeService {

    private final Map<String, TransactionType> types = new HashMap<>();

    public void addTransactionType(TransactionType transactionType) {
        if (types.containsKey(transactionType.getName())) {
            throw new DuplicateDataException(String.format(transactionType.getName()));
        }
        if (transactionType.getMaxManuall() <= transactionType.getMaxAllowed()) {
            throw new InvalidDataException("The Max Allowed value should be less than Max Manual");
        }
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
}
