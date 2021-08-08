package antifraud.controller;

import antifraud.model.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/transaction-type")
@Secured({"ROLE_ADMIN"})
public class TransactionTypeController {
    @PostMapping public ResponseEntity
    addTransactionType(){
        return null;
    }

    @GetMapping
    public ResponseEntity<List<TransactionType>> get (){
        return null;
    }

    @PostMapping
    public ResponseEntity remove(){
        return null;
    }
}
