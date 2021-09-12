package antifraud.controller;


import antifraud.service.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antifraud/transaction-type")
@Secured({"ROLE_ADMIN"})
public class TransactionTypeController {
    @PostMapping
    public ResponseEntity    addTransactionType() {
        return null;
    }


    @PostMapping
    public ResponseEntity remove(String typeName) {
        return null;
    }

    @GetMapping
    public ResponseEntity getOne(String typeName) {
        return null;
    }

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
