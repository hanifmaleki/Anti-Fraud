package antifraud.controller;

import antifraud.model.ResultEnum;
import antifraud.model.Role;
import antifraud.model.Transaction;
import antifraud.model.TransactionResponse;
import antifraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

}