package dev.baybay.lobiani.app

import dev.baybaydev.lobiani.testutil.AdminAPITestClient
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {

    @Bean
    EventStorageEngine inMemoryEventStorageEngine() {
        new InMemoryEventStorageEngine()
    }

    @Bean
    AdminAPITestClient adminApiTestClient(TestRestTemplate restTemplate) {
        return new AdminAPITestClient(restTemplate)
    }
}
