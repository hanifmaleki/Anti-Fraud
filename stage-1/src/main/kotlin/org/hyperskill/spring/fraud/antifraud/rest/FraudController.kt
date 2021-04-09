package org.hyperskill.spring.fraud.antifraud.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/antifraud/transaction")
class FraudController {

    @GetMapping
    fun getTransactionValidity(@RequestParam(name = "amount", required = true) transactionAmount: Long): ResponseEntity<String> {
        return ResponseEntity.ok("A")
    }
}