package dev.baybay.lobiani.app.admin.inventory.web

import dev.baybay.lobiani.app.admin.inventory.InventoryItem
import dev.baybay.lobiani.app.admin.inventory.query.QueryAllInventoryItems
import dev.baybay.lobiani.app.admin.inventory.query.QueryInventoryItemByID
import dev.baybay.lobiani.app.admin.inventory.query.QueryInventoryItemBySlug
import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import dev.baybay.lobiani.app.inventory.Quantity
import dev.baybay.lobiani.app.inventory.command.AddInventoryItemToStock
import dev.baybay.lobiani.app.inventory.command.DeleteInventoryItem
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@RestController
@RequestMapping("/api/inventory-items")
class InventoryItemAPIController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

    @GetMapping
    fun getItems(@RequestParam(required = false) slug: String?): List<InventoryItem> {
        if (slug != null) {
            return listOfNotNull(queryBySlug(slug))
        }
        return queryGateway.query(
            QueryAllInventoryItems(),
            ResponseTypes.multipleInstancesOf(InventoryItem::class.java)
        ).get()
    }

    @GetMapping("/{id}")
    fun getItem(@PathVariable id: UUID): InventoryItem {
        return queryGateway.query(QueryInventoryItemByID(id.toString()), InventoryItem::class.java).get()
            ?: throw NoSuchElementException()
    }

    @PostMapping
    fun defineNewItem(@RequestBody definition: InventoryItemDefinition): InventoryItem {
        val defineInventoryItem = definition.asCommand()
        commandGateway.sendAndWait<Void>(defineInventoryItem)
        return InventoryItem(defineInventoryItem.id.stringValue, defineInventoryItem.slug.value)
    }

    @PostMapping("/{id}/stock")
    fun addToStock(@PathVariable id: UUID, @RequestBody stock: Stock) {
        commandGateway.sendAndWait<Void>(
            AddInventoryItemToStock(InventoryItemIdentifier(id), Quantity.count(stock.amount))
        )
    }

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: UUID) {
        commandGateway.sendAndWait<Void>(DeleteInventoryItem(InventoryItemIdentifier(id)))
    }

    private fun queryBySlug(slug: String) =
        queryGateway.query(QueryInventoryItemBySlug(slug), InventoryItem::class.java).get()
}
