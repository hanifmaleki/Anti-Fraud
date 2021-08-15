package data;

import antifraud.model.TransactionType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static data.TestDataProvider.BASE_ADDRESS;

public class TransactionTypeDataProvider {
    public static final String TRANSACTION_TYPE_ADDRESS = BASE_ADDRESS + "/transaction-type";

    //TransactionTypes
    public final TransactionType foodTransactionType;
    public final TransactionType insuranceTransactionType;
    public final TransactionType realWareTransactionType;
    public final TransactionType onlineWareTransactionType;
    public final TransactionType transferTransactionType;
    public final TransactionType invalidTransactionTypeAllowedIsNotLowerThanMax;
    public final TransactionType invalidTransactionTypeWithoutMaxManual;
    public final TransactionType invalidTransactionTypeWithoutMaxAllowed;
    public final TransactionType invalidTransactionTypeWithoutName;

    public TransactionTypeDataProvider() {
        foodTransactionType = TransactionType
                .builder()
                .name("food")
                .maxManuall(150)
                .maxAllowed(300)
                .build();

        insuranceTransactionType = TransactionType
                .builder()
                .name("insurance")
                .maxManuall(400)
                .maxAllowed(1200)
                .build();

        realWareTransactionType = TransactionType
                .builder()
                .name("real-ware")
                .maxManuall(200)
                .maxAllowed(500)
                .build();

        onlineWareTransactionType = TransactionType
                .builder()
                .name("online-ware")
                .maxManuall(300)
                .maxAllowed(800)
                .build();

        transferTransactionType = TransactionType
                .builder()
                .name("transfer")
                .maxManuall(300)
                .maxAllowed(900)
                .build();

        invalidTransactionTypeAllowedIsNotLowerThanMax = TransactionType
                .builder()
                .name("invalid_trx_tpe")
                .maxManuall(300)
                .maxAllowed(300)
                .build();

        invalidTransactionTypeWithoutMaxManual = TransactionType
                .builder()
                .name("invalid_trx_tpe")
                .maxManuall(300)
                .build();

        invalidTransactionTypeWithoutMaxAllowed = TransactionType
                .builder()
                .name("invalid_trx_tpe")
                .maxManuall(300)
                .build();

        invalidTransactionTypeWithoutName = TransactionType
                .builder()
                .maxManuall(300)
                .maxAllowed(300)
                .build();

    }


    public TransactionType getRandomValidTransactionType() {
        final List<TransactionType> transactionTypeList = getAllValidTransactionTypeStream().collect(Collectors.toList());
        final int index = new Random().nextInt(transactionTypeList.size());
        return transactionTypeList.get(index);
    }

    public Stream<TransactionType> getAllValidTransactionTypeStream() {
        return Stream.of(foodTransactionType,
                insuranceTransactionType,
                realWareTransactionType,
                onlineWareTransactionType,
                transferTransactionType);
    }

    public Stream<TransactionType> getAllInvalidTransactionTypeStream() {
        return Stream.of(
                invalidTransactionTypeAllowedIsNotLowerThanMax,
                invalidTransactionTypeWithoutMaxManual,
                invalidTransactionTypeWithoutMaxAllowed,
                invalidTransactionTypeWithoutName);


    }
}
