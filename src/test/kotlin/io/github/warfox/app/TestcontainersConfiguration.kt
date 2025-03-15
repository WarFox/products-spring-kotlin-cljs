package io.github.warfox.app

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    fun kafkaContainer(): KafkaContainer {
        return KafkaContainer(DockerImageName.parse("apache/kafka-native:3.8.0"))
    }

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
    }

}

val postgresTestContainer: PostgreSQLContainer<*> by lazy {
    TestcontainersConfiguration().postgresContainer().apply {
        withDatabaseName("test")
        withUsername("product")
        withPassword("product")
    }.also { it.start() }
}

val kafkaTestContainer: KafkaContainer by lazy {
    TestcontainersConfiguration().kafkaContainer().also { it.start() }
}
