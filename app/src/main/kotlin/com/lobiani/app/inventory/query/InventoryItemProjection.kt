package com.lobiani.app.inventory.query

import com.lobiani.app.inventory.api.InventoryItemDefined
import com.lobiani.app.inventory.api.InventoryItemDeleted
import com.lobiani.app.inventory.api.QueryAllInventoryItems
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

    @QueryHandler
    fun allItems(q: QueryAllInventoryItems): MutableList<InventoryItem> {
        return items
    }

}
