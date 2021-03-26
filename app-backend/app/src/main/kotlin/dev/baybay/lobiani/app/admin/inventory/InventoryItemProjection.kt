package dev.baybay.lobiani.app.admin.inventory

import dev.baybay.lobiani.app.admin.inventory.query.QueryAllInventoryItems
import dev.baybay.lobiani.app.admin.inventory.query.QueryInventoryItemByID
import dev.baybay.lobiani.app.admin.inventory.query.QueryInventoryItemBySlug
import dev.baybay.lobiani.app.inventory.event.*
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class InventoryItemProjection {

    private val items = mutableListOf<InventoryItem>()

    @EventHandler
    fun on(e: InventoryItemDefined) {
        items.add(InventoryItem(e.id.stringValue, e.slug.value))
    }

    @EventHandler
    fun on(e: InventoryItemDeleted) {
        items.removeIf { it.id == e.id.stringValue }
    }

    @EventHandler
    fun on(e: InventoryItemAddedToStock) {
        items.find { it.id == e.inventoryItemId.stringValue }?.increaseStockLevelBy(e.quantity.value)
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
