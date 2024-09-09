package com.jaideep.keh.controller

import com.jaideep.keh.dto.User
import com.jaideep.keh.producer.KafkaMessageProducer
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Path

@RestController
@Slf4j
@RequestMapping("/app")
class EventController
{
    @Autowired
    private lateinit var kafkaMessageProducer: KafkaMessageProducer
    private val logger: Logger = LoggerFactory.getLogger(EventController::class.java)
    @PostMapping("/publish")
    fun publishMessage(@RequestBody user: User) : ResponseEntity<Any> {
        try {
            kafkaMessageProducer.sendData(user);
            return ResponseEntity.ok("Message sent successfully")
        }
        catch (ex: Exception) {
            logger.error("Error occurred : ${ex.message}")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @RequestMapping("/loadData")
    fun loadData() : ResponseEntity<Any> {
        try {
            loadDataFromCSV().forEach {
                kafkaMessageProducer.sendData(it)
            }
            return ResponseEntity.ok("Operation done successfully")
        }
        catch (ex: Exception) {
            logger.error("Error occurred : ${ex.message}")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    fun loadDataFromCSV() : List<User> {
        return Files.readAllLines(Path.of("src", "main", "resources", "dat.csv"))
                .map {
                    val stringArr = it.split(",")
                    User(stringArr[0], stringArr[1].toInt(), stringArr[2])
                }
    }
}