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
    fun allItems(q: QueryAllInventoryItems): MutableList<InventoryItem> {
        return items
    }

    @QueryHandler
    fun byID(q: QueryInventoryItemByID): InventoryItem? {
        return items.firstOrNull { i -> i.id == q.id }
    }

    @QueryHandler
    fun bySlug(q: QueryInventoryItemBySlug): InventoryItem? {
        return items.firstOrNull { i -> i.slug == q.slug }
    }

}
