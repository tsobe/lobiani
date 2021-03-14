package dev.baybay.lobiani.app.common

import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.messaging.interceptors.JSR303ViolationException
import org.axonframework.modelling.command.AggregateNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(): ResponseEntity<Void> {
        return notFound()
    }

    @ExceptionHandler(JSR303ViolationException::class)
    fun handle(e: JSR303ViolationException): ResponseEntity<APIError> {
        return badRequest(e.violations.first().message)
    }

    @ExceptionHandler(CommandExecutionException::class)
    fun handle(e: CommandExecutionException): ResponseEntity<Void> {
        if (e.cause is AggregateNotFoundException) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    private fun notFound() = ResponseEntity.notFound().build<Void>()

    private fun badRequest(message: String): ResponseEntity<APIError> {
        return ResponseEntity.badRequest().body(APIError(message))
    }
}
