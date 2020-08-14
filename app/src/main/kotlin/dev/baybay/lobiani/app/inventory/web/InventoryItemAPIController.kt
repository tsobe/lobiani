package dev.baybay.lobiani.app.inventory.web

import dev.baybay.lobiani.app.inventory.api.*
import dev.baybay.lobiani.app.inventory.query.InventoryItem
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.interceptors.JSR303ViolationException
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@RestController
@RequestMapping("/api/inventory-items")
class InventoryItemAPIController(private val commandGateway: CommandGateway,
                                 private val queryGateway: QueryGateway) {

    @GetMapping
    fun getAllItems(): List<InventoryItem> {
        return queryGateway.query(QueryAllInventoryItems(),
                ResponseTypes.multipleInstancesOf(InventoryItem::class.java)).get()
    }

    @GetMapping("/{id}")
    fun getItem(@PathVariable id: UUID): InventoryItem {
        return getAllItems().first { it.id == id }
    }

    @PostMapping
    fun defineNewItem(@RequestBody defineInventoryItem: DefineInventoryItem): InventoryItem {
        ensureItemIsNotDefined(defineInventoryItem.slug)
        commandGateway.sendAndWait<String>(defineInventoryItem)
        return InventoryItem(defineInventoryItem.id, defineInventoryItem.slug)
    }

    @PostMapping("/{id}/stock")
    fun addToStock(@PathVariable id: UUID, @RequestBody stock: Stock) {
        commandGateway.sendAndWait<AddInventoryItemToStock>(
                AddInventoryItemToStock(id, Quantity.count(stock.count)))
    }

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: UUID) {
        commandGateway.sendAndWait<DeleteInventoryItem>(DeleteInventoryItem(id))
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoResult(): ResponseEntity<Void> {
        return notFound()
    }

    @ExceptionHandler(JSR303ViolationException::class)
    fun handleValidationFailure(e: JSR303ViolationException): ResponseEntity<APIError> {
        return badRequest(e.violations.first().message)
    }

    @ExceptionHandler(CommandExecutionException::class)
    fun handleCommandExecution(e: CommandExecutionException): ResponseEntity<Void> {
        return notFound()
    }

    @ExceptionHandler(ItemAlreadyDefinedException::class)
    fun handleDuplicateItem(e: ItemAlreadyDefinedException): ResponseEntity<APIError> {
        return badRequest("Item with slug ${e.slug} is already defined")
    }

    private fun ensureItemIsNotDefined(slug: String) {
        if (getAllItems().any { i -> i.slug == slug }) {
            throw ItemAlreadyDefinedException(slug)
        }
    }

    private fun notFound() = ResponseEntity.notFound().build<Void>()

    private fun badRequest(message: String): ResponseEntity<APIError> {
        return ResponseEntity.badRequest().body(APIError(message))
    }

    data class Stock(val count: Int)

    data class APIError(val message: String)

    class ItemAlreadyDefinedException(val slug: String) : RuntimeException()
}
