package antifraud.controller;

import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import antifraud.model.TransactionType;
import antifraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antifraud/transaction")
@Secured({"ROLE_SUPPORT", "ROLE_ADMIN", "ROLE_USER"})
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    ResponseEntity<TransactionResponse> isTransactionValid(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.getTransactionValidity(transaction));
    }

    //TODO Add a method for transaction history

    //TODO Change the method of getting transactions

    //TODO to enrich the message


    class TransactionQueryRequest{
        private Transaction transaction;
        private Integer ipCount;
        private Integer countryCount;
    }

    class NarrowTransaction{
        private int ammount;
        private int ipAdress;
        private int country;
        private TransactionType type;
    }


}