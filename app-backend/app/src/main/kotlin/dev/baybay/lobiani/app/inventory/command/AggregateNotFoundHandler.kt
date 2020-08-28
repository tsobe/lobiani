package dev.baybay.lobiani.app.inventory.command

import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.InterceptorChain
import org.axonframework.messaging.MessageHandlerInterceptor
import org.axonframework.messaging.unitofwork.UnitOfWork
import org.axonframework.modelling.command.AggregateNotFoundException

/**
 * Special interceptor designed to handle [AggregateNotFoundException] in a way to make
 * it accessible for (potentially) remote command initiators
 *
 * See [this](https://docs.axoniq.io/reference-guide/v/4.3/operations-guide/production-considerations/exceptions)
 * for more info
 */
class AggregateNotFoundHandler : MessageHandlerInterceptor<CommandMessage<*>> {

    override fun handle(unitOfWork: UnitOfWork<out CommandMessage<*>>?, interceptorChain: InterceptorChain?) {
        try {
            interceptorChain?.proceed()
        } catch (e: AggregateNotFoundException) {
            throw CommandExecutionException("Aggregate not found", e, e)
        }
    }

}
