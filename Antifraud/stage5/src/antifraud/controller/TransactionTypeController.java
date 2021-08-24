package antifraud.controller;

import antifraud.model.TransactionType;
import antifraud.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/transaction-type")
@Secured({"ROLE_ADMIN"})
@Validated
@RequiredArgsConstructor
public class TransactionTypeController {
    private final TransactionTypeService transactionTypeService;

    @PostMapping
    public ResponseEntity addTransactionType(@RequestBody TransactionType transactionType) {
        transactionTypeService.addTransactionType(transactionType);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionType>> get() {
        final List<TransactionType> transactionTypes = transactionTypeService.getTransactionTypes();
        return ResponseEntity.ok(transactionTypes);
    }

    @DeleteMapping("/{typeName}")
    public ResponseEntity<Void> remove(@PathVariable String typeName) {
        transactionTypeService.deleteTransactionType(typeName);
        return ResponseEntity.ok().build();
    }
}
