package com.jaideep.keh.producer

import com.jaideep.keh.dto.User
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Slf4j
@Service
@RequiredArgsConstructor
class KafkaMessageProducer {
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, Any>
    private val logger: Logger = LoggerFactory.getLogger(KafkaMessageProducer::class.java)

    fun sendData(user: User) {
        val future = kafkaTemplate.send("user_data", user)
        future.whenComplete { res, ex ->
            if (ex != null) {
                logger.error("Exception occurred : ${ex.message}")
            } else {
                logger.info("Sent message=[${user}] with offset=[${res.recordMetadata.offset()}]\n")
            }
        }
    }
}