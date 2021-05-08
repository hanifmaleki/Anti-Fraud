package antifraud.controller;

import antifraud.model.ResultEnum;
import antifraud.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {

    @GetMapping
    ResponseEntity<String> isTransactionValid(@RequestBody Transaction transaction) {
        int amount = transaction.getAmount();

        if (amount <= 200L) {
            return ResponseEntity.ok(ResultEnum.ALLOWED.name());
        }

        if (amount <= 1500L) {
            return ResponseEntity.ok(ResultEnum.MANUAL_PROCESSING.name());
        }
        return ResponseEntity.ok(ResultEnum.PROHIBITED.name());
    }

}
