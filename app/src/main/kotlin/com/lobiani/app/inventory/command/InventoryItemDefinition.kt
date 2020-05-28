package com.lobiani.app.inventory.command

import com.lobiani.app.inventory.api.DefineNewInventoryItem
import com.lobiani.app.inventory.api.DeleteInventoryItemDefinition
import com.lobiani.app.inventory.api.InventoryItemDefined
import com.lobiani.app.inventory.api.InventoryItemDefinitionDeleted
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateLifecycle.markDeleted
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class InventoryItemDefinition {

    @AggregateIdentifier
    private lateinit var id: UUID
    private lateinit var slug: String

    constructor()

    @CommandHandler
    constructor(c: DefineNewInventoryItem) {
        apply(InventoryItemDefined(c.id, c.slug))
    }

    @CommandHandler
    private fun delete(c: DeleteInventoryItemDefinition) {
        apply(InventoryItemDefinitionDeleted(id, slug))
    }

    @EventSourcingHandler
    private fun on(e: InventoryItemDefined) {
        id = e.id
        slug = e.slug
    }

    @EventSourcingHandler
    private fun on(e: InventoryItemDefinitionDeleted) {
        markDeleted()
    }
}
