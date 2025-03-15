package io.github.warfox.app

import org.jooq.DSLContext
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [Application::class])
@TestConstructor(autowireMode = ALL)
@TestInstance(PER_CLASS)
@ActiveProfiles("test")
abstract class BaseIntegrationTest {
    companion object {
        @JvmStatic
        @Suppress("unused")
        @DynamicPropertySource
        fun dynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("db.port") { postgresTestContainer.firstMappedPort }
            registry.add("db.name") {postgresTestContainer.databaseName}

            registry.add("spring.kafka.bootstrap-servers") { kafkaTestContainer.bootstrapServers }
            registry.add("kafka-username") { "username" }
            registry.add("kafka-password") { "password" }
        }
    }

    @Autowired
    lateinit var dslContext: DSLContext
}
