package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.DuplicateDataException;
import antifraud.exception.InvalidDataException;
import antifraud.model.TransactionType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TransactionTypeService {

    private final Set<TransactionType> types = new HashSet<>();

    public void addTransactionType(TransactionType transactionType) {
        if (types.contains(transactionType)) {
            throw new DuplicateDataException(String.format(transactionType.getName()));
        }
        if (transactionType.getMaxManuall() <= transactionType.getMaxAllowed()) {
            throw new InvalidDataException("The Max Allowed value should be less than Max Manual");
        }
        types.add(transactionType);
    }

    public void deleteTransactioType(String name) {
        final TransactionType transactionType = TransactionType
                .builder()
                .name(name)
                .build();
        if (!types.remove(transactionType)) {
            throw new DataNotFoundException("There is no transaction type with name" + name);
        }
    }

    public List<TransactionType> getTransactionTypes() {
        return new ArrayList(types);
    }
}
