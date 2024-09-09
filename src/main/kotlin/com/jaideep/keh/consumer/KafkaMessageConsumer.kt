package com.jaideep.keh.consumer

import com.jaideep.keh.dto.User
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Service


@Service
@Slf4j
class KafkaMessageConsumer {
    private val logger: Logger = LoggerFactory.getLogger(KafkaMessageConsumer::class.java)

    @KafkaListener(topics = ["user_data"], groupId = "con-grp-user")
    @RetryableTopic(
        attempts = "9",
        backoff = Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000),
        exclude = [java.lang.NullPointerException::class]
    )
    fun getUserDataOne(user: User) {
        if (user.email == "4LN0BUDJMTH5232Q") throw RuntimeException("Invalid user id : ${user.email}")
        logger.info("Message consumed = [$user] with offset")
    }

//    @KafkaListener(topics = ["user_data"], groupId = "con-grp-user")
//    fun getUserDataTwo(user: User) {
//        if (user.email == "4LN0BUDJMTH5232Q") throw RuntimeException("Invalid user id : ${user.email}")
//        logger.info("Message consumed = [$user] with offset")
//    }
//
//    @KafkaListener(topics = ["user_data"], groupId = "con-grp-user")
//    fun getUserDataThree(user: User) {
//        if (user.email == "4LN0BUDJMTH5232Q") throw RuntimeException("Invalid user id : ${user.email}")
//        logger.info("Message consumed = [$user] with offset")
//    }

    @DltHandler
    fun listenDLT(
        user: User,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.OFFSET) offset: Long
    ) {
        logger.info("DLT Received : {$user}, from {$topic}, offset {$offset}")
    }
}