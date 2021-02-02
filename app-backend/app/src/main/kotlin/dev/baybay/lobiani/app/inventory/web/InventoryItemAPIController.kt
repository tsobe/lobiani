package dev.baybay.lobiani.app.inventory.web

import dev.baybay.lobiani.app.common.APIError
import dev.baybay.lobiani.app.inventory.api.*
import dev.baybay.lobiani.app.inventory.query.InventoryItem
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@RestController
@RequestMapping("/api/inventory-items")
class InventoryItemAPIController(private val commandGateway: CommandGateway,
                                 private val queryGateway: QueryGateway) {

    @GetMapping
    fun getItems(@RequestParam(required = false) slug: String?): List<InventoryItem> {
        if (slug != null) {
            return listOfNotNull(queryBySlug(slug))
        }
        return queryGateway.query(QueryAllInventoryItems(),
                ResponseTypes.multipleInstancesOf(InventoryItem::class.java)).get()
    }

    @GetMapping("/{id}")
    fun getItem(@PathVariable id: UUID): InventoryItem {
        return queryGateway.query(QueryInventoryItemByID(id), InventoryItem::class.java).get()
                ?: throw NoSuchElementException()
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    fun defineNewItem(@RequestBody definition: InventoryItemDefinition): InventoryItem {
        val defineInventoryItem = definition.asCommand()
        ensureItemIsNotDefined(defineInventoryItem.slug.value)
        commandGateway.send<Void>(defineInventoryItem)
        return InventoryItem(defineInventoryItem.id, defineInventoryItem.slug.value)
    }

    @PostMapping("/{id}/stock")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    fun addToStock(@PathVariable id: UUID, @RequestBody stock: Stock) {
        commandGateway.send<Void>(
                AddInventoryItemToStock(id, Quantity.count(stock.amount)))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    fun deleteItem(@PathVariable id: UUID) {
        commandGateway.send<Void>(DeleteInventoryItem(id))
    }

    @ExceptionHandler(ItemAlreadyDefinedException::class)
    fun handleDuplicateItem(e: ItemAlreadyDefinedException): ResponseEntity<APIError> {
        return badRequest("Item with slug ${e.slug} is already defined")
    }

    private fun ensureItemIsNotDefined(slug: String) {
        if (isItemDefined(slug)) {
            throw ItemAlreadyDefinedException(slug)
        }
    }

    private fun isItemDefined(slug: String) =
            queryBySlug(slug) != null

    private fun queryBySlug(slug: String) =
            queryGateway.query(QueryInventoryItemBySlug(slug), InventoryItem::class.java).get()

    private fun badRequest(message: String): ResponseEntity<APIError> {
        return ResponseEntity.badRequest().body(APIError(message))
    }

    class ItemAlreadyDefinedException(val slug: String) : RuntimeException()
}
