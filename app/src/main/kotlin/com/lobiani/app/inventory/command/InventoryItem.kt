package com.lobiani.app.inventory.command

import com.lobiani.app.inventory.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateLifecycle.markDeleted
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class InventoryItem {

    @AggregateIdentifier
    private lateinit var id: UUID
    private lateinit var slug: String

    constructor()

    @CommandHandler
    constructor(c: DefineInventoryItem) {
        apply(InventoryItemDefined(c.id, c.slug))
    }

    @CommandHandler
    private fun delete(c: DeleteInventoryItem) {
        apply(InventoryItemDeleted(id, slug))
    }

    @CommandHandler
    private fun add(c: AddInventoryItemToStock) {
        apply(InventoryItemAddedToStock(c.inventoryItemId, c.quantity))
    }

    @EventSourcingHandler
    private fun on(e: InventoryItemDefined) {
        id = e.id
        slug = e.slug
    }

    @EventSourcingHandler
    private fun on(e: InventoryItemDeleted) {
        markDeleted()
    }
}
