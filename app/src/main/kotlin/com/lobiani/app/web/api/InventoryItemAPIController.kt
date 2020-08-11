package com.lobiani.app.web.api

import com.lobiani.app.inventory.api.*
import com.lobiani.app.inventory.query.InventoryItem
import org.axonframework.commandhandling.gateway.CommandGateway
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
    fun defineNewItem(@RequestBody slug: String): InventoryItem {
        val id = UUID.randomUUID()
        commandGateway.sendAndWait<String>(DefineInventoryItem(id, slug))
        return InventoryItem(id, slug)
    }

    @PostMapping("/{id}/stock")
    fun addToStock(@PathVariable id: UUID, @RequestBody count: Int) {
        commandGateway.sendAndWait<AddInventoryItemToStock>(AddInventoryItemToStock(id, Quantity.count(count)))
    }

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: UUID) {
        commandGateway.sendAndWait<DeleteInventoryItem>(DeleteInventoryItem(id))
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(): ResponseEntity<Void> {
        return ResponseEntity.notFound().build<Void>()
    }

}
