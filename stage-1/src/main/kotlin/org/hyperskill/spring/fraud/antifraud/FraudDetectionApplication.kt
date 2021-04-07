package org.hyperskill.spring.fraud.antifraud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FraudDetectionApplication

fun main(args: Array<String>) {
	runApplication<FraudDetectionApplication>(*args)
}
