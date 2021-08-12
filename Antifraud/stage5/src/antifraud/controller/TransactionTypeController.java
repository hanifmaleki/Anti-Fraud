package antifraud.controller;

import antifraud.model.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/transaction-type")
@Secured({"ROLE_ADMIN"})
public class TransactionTypeController {
    @PostMapping
    public ResponseEntity
    addTransactionType(@RequestBody TransactionType transactionType) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<TransactionType>> get() {
        return null;
    }

    @DeleteMapping("/{typeName}")
    public ResponseEntity remove(@PathVariable String typeName) {
        return null;
    }
}
