package dev.baybay.lobiani.app

import dev.baybay.lobiani.app.admin.inventory.query.QueryAllInventoryItems
import dev.baybay.lobiani.app.admin.inventory.InventoryItem
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController(private val queryGateway: QueryGateway) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/healthz")
    fun getStatus() {
        queryGateway.query(
            QueryAllInventoryItems(),
                ResponseTypes.multipleInstancesOf(InventoryItem::class.java)).get()
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<Void> {
        log.warn("Received following error while checking the health status. Marking as unhealthy...", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }
}
