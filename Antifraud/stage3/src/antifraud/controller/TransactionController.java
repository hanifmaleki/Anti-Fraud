package antifraud.controller;

import antifraud.model.ResultEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {

    @GetMapping
    ResponseEntity<String> isTransactionValid(@RequestParam Long amount) {
        if (amount <= 200L) {
            return ResponseEntity.ok(ResultEnum.ALLOWED.name());
        }

        if (amount <= 1500L) {
            return ResponseEntity.ok(ResultEnum.MANUAL_PROCESSING.name());
        }
        return ResponseEntity.ok(ResultEnum.PROHIBITED.name());
    }

}
