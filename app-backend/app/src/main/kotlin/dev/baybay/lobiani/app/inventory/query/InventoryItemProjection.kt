package dev.baybay.lobiani.app.inventory.query

import dev.baybay.lobiani.app.inventory.api.*
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class InventoryItemProjection {

    private val items = mutableListOf<InventoryItem>()

    @EventHandler
    fun on(e: InventoryItemDefined) {
        items.add(InventoryItem(e.id, e.slug))
    }

    @EventHandler
    fun on(e: InventoryItemDeleted) {
        items.removeIf { it.id == e.id }
    }

    @EventHandler
    fun on(e: InventoryItemAddedToStock) {
        items.find { it.id == e.inventoryItemId }?.increaseStockLevelBy(e.quantity.value)
    }

    @QueryHandler
    fun handle(q: QueryAllInventoryItems): List<InventoryItem> {
        return items
    }

    @QueryHandler
    fun handle(q: QueryInventoryItemByID): InventoryItem? {
        return items.find { it.id == q.id }
    }

    @QueryHandler
    fun handle(q: QueryInventoryItemBySlug): InventoryItem? {
        return items.find { it.slug == q.slug }
    }

}
