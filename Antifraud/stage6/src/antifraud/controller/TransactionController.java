package antifraud.controller;

import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import antifraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/transaction")
@Secured({"ROLE_SUPPORT", "ROLE_ADMIN", "ROLE_USER"})
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    ResponseEntity<TransactionResponse> isTransactionValid(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.getTransactionValidity(transaction));
    }

    @GetMapping("/history/{cardNumber}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String cardNumber) {
        return null;
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_SUPPORT"})
    ResponseEntity<Transaction> getTransactionById(@PathVariable Long trxId) {
        return null;
    }

    @PutMapping
    @Secured({"ROLE_ADMIN", "ROLE_SUPPORT"})
        //TODO what is response
    ResponseEntity<Void> correctTransaction(@PathVariable Long id, @RequestParam String feedback) {
        return null;
    }


}