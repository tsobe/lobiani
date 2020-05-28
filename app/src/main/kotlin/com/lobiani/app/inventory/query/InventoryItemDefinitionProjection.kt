package com.lobiani.app.inventory.query

import com.lobiani.app.inventory.api.InventoryItemDefined
import com.lobiani.app.inventory.api.InventoryItemDefinitionDeleted
import com.lobiani.app.inventory.api.QueryAllInventoryItemDefinitions
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class InventoryItemDefinitionProjection {

    private val definitions = mutableListOf<InventoryItemDefinition>()

    @EventHandler
    fun on(e: InventoryItemDefined) {
        definitions.add(InventoryItemDefinition(e.id, e.slug))
    }

    @EventHandler
    fun on(e: InventoryItemDefinitionDeleted) {
        definitions.removeIf { it.id == e.id }
    }

    @QueryHandler
    fun allDefinitions(q: QueryAllInventoryItemDefinitions): MutableList<InventoryItemDefinition> {
        return definitions
    }

}
