package antifraud.controller;


import antifraud.model.TransactionType;
import antifraud.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/antifraud/transaction-type")
@Secured({"ROLE_ADMIN"})
@RequiredArgsConstructor
public class TransactionTypeController {

    private final TransactionTypeService transactionTypeService;

    @PostMapping
    public ResponseEntity addTransactionType(@RequestBody TransactionType transactionType) {
        transactionTypeService.addTransactionType(transactionType);
        return ResponseEntity.status(CREATED).build();
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

    //TODO complete these
    public ResponseEntity<TransactionType> getAll() {
        return null;
    }

    @GetMapping("ip-count")
    public ResponseEntity<Double> getIpCount(){
        return null;
    }

    @GetMapping("country-count")
    public ResponseEntity<Double> getCountryCount(){
        return null;
    }
}
