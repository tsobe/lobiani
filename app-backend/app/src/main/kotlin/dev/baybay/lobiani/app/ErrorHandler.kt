package dev.baybay.lobiani.app

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoResult(): ResponseEntity<Void> {
        return notFound()
    }

    private fun notFound() = ResponseEntity.notFound().build<Void>()
}
