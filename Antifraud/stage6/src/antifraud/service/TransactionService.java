package antifraud.service;

import antifraud.dao.TransactionDao;
import antifraud.exception.DataNotFoundException;
import antifraud.exception.InvalidDataException;
import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import antifraud.model.TransactionQuery;
import antifraud.model.TransactionResponse;
import antifraud.service.rules.RuleEngine;
import antifraud.service.rules.TransactionAmountRule;
import antifraud.service.rules.TransactionRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDao transactionDao;
    private final TransactionTypeService typeService;
    private final RuleEngine ruleEngine;


    public TransactionResponse getTransactionValidity(Transaction transaction) {

        String typeName = transaction.getType();
        typeService.getTransactionTypeByName(typeName)
                .orElseThrow(() -> new DataNotFoundException("Could not find type " + typeName));
        transactionDao.save(transaction);
        TransactionQuery transactionQuery = createTransactionRequest(transaction);
        return ruleEngine.getTransactionValidity(transactionQuery);

    }

    private TransactionQuery createTransactionRequest(Transaction transaction) {
        final List<Transaction> transactions = transactionDao.findByCardNumber(transaction.getCardSerial());
        final int ipCount = transactions
                .stream()
                .collect(Collectors.toMap(Transaction::getIpAddress, Function.identity()))
                .size();
        final int countryCount = transactions
                .stream()
                .collect(Collectors.toMap(Transaction::getCountryCode, Function.identity()))
                .size();

        return TransactionQuery
                .builder()
                .transaction(transaction)
                .countryCount(countryCount)
                .ipCount(ipCount)
                .build();
    }


    public List<Transaction> getTransactionHistory(String cardNumber) {
        return transactionDao.findByCardNumber(cardNumber);
    }

    public Transaction getTransactionById(Long trxId) {
        return transactionDao.fundById(trxId)
                .orElseThrow(() -> new DataNotFoundException("Could not find transaction with id " + trxId));
    }


    public Transaction correctTransction(Long id, String feedback) {
        final ResultEnum resultEnum = ResultEnum.valueOf(feedback);
        final Transaction transaction = getTransactionById(id);
        if(transaction.getFeedback()!=null){
            throw new InvalidDataException("There has been already a feedback for this transaction");
        }
        if (resultEnum == transaction.getResult()) {
            throw new InvalidDataException("Input result is same as the actual result");
        }
        applyFeedback(transaction, resultEnum);
        return transaction;
    }

    private void applyFeedback(Transaction transaction, ResultEnum resultEnum) {
        final TransactionResponse transactionValidity = getTransactionValidity(transaction);
        final Optional<TransactionRule> amountRule = transactionValidity.getNotApplyingRules()
                .stream()
                .filter(rule -> rule instanceof TransactionAmountRule)
                .findFirst();
        if(amountRule.isPresent()){
            //TODO move to transaction type
            final String type = transaction.getType();
        }
        transaction.setFeedback(resultEnum);
    }
}
