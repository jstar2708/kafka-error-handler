package com.jaideep.keh.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaConfig {
    @Bean
    fun createNewTopic(): NewTopic {
        return NewTopic("user_data", 3, 1)
    }
}