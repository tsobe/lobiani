package dev.baybay.lobiani.app.common

import org.axonframework.serialization.RevisionResolver
import org.axonframework.serialization.SerialVersionUIDRevisionResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {

    @Bean
    fun revisionResolver(): RevisionResolver {
        return SerialVersionUIDRevisionResolver()
    }
}
