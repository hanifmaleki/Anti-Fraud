package antifraud.controller;

import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import antifraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/antifraud/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    ResponseEntity<TransactionResponse> isTransactionValid(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.getTransactionValidity(transaction));
    }

}
