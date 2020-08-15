package dev.baybay.lobiani.app

import dev.baybay.lobiani.app.inventory.command.AggregateNotFoundHandler
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

    @Bean
    fun beanValidator(commandBus: CommandBus): BeanValidationInterceptor<CommandMessage<*>> {
        val beanValidationInterceptor = BeanValidationInterceptor<CommandMessage<*>>()
        commandBus.registerDispatchInterceptor(beanValidationInterceptor)
        return beanValidationInterceptor
    }

    @Bean
    fun aggregateNotFound(commandBus: CommandBus): AggregateNotFoundHandler {
        val aggregateNotFoundHandler = AggregateNotFoundHandler()
        commandBus.registerHandlerInterceptor(aggregateNotFoundHandler)
        return aggregateNotFoundHandler
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
