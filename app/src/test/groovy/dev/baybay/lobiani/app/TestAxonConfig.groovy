package dev.baybay.lobiani.app

import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.queryhandling.QueryBus
import org.axonframework.queryhandling.QueryInvocationErrorHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.axonframework.queryhandling.SimpleQueryBus
import org.axonframework.spring.config.AxonConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestAxonConfig {

    @Bean
    EventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore, "eventStore"))
                .build()
    }

    @Bean
    SimpleQueryBus queryBus(AxonConfiguration axonConfiguration,
                            TransactionManager transactionManager,
                            QueryInvocationErrorHandler eh) {
        return SimpleQueryBus.builder()
                .messageMonitor(axonConfiguration.messageMonitor(QueryBus, "queryBus"))
                .transactionManager(transactionManager)
                .errorHandler(eh)
                .queryUpdateEmitter(axonConfiguration.getComponent(QueryUpdateEmitter))
                .build()
    }

    @Bean
    EventStorageEngine inMemoryEventStorageEngine() {
        return new InMemoryEventStorageEngine()
    }

    @Bean
    CommandBus commandBus() {
        return SimpleCommandBus.builder().build()
    }
}
