package com.jaideep.keh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaErrorHandlingApplication

fun main(args: Array<String>) {
	runApplication<KafkaErrorHandlingApplication>(*args)
}
