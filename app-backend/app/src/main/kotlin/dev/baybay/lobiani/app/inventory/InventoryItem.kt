package dev.baybay.lobiani.app.inventory

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.inventory.command.AddInventoryItemToStock
import dev.baybay.lobiani.app.inventory.command.DefineInventoryItem
import dev.baybay.lobiani.app.inventory.command.DeleteInventoryItem
import dev.baybay.lobiani.app.inventory.event.InventoryItemAddedToStock
import dev.baybay.lobiani.app.inventory.event.InventoryItemDefined
import dev.baybay.lobiani.app.inventory.event.InventoryItemDeleted
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateLifecycle.markDeleted
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class InventoryItem {

    @AggregateIdentifier
    private lateinit var id: InventoryItemIdentifier
    private lateinit var slug: Slug

    constructor()

    @CommandHandler
    constructor(c: DefineInventoryItem) {
        apply(InventoryItemDefined(c.id, c.slug))
    }

    @CommandHandler
    fun handle(c: DeleteInventoryItem) {
        apply(InventoryItemDeleted(id))
    }

    @CommandHandler
    fun handle(c: AddInventoryItemToStock) {
        apply(InventoryItemAddedToStock(c.inventoryItemId, c.quantity))
    }

    @EventSourcingHandler
    fun on(e: InventoryItemDefined) {
        id = e.id
        slug = e.slug
    }

    @EventSourcingHandler
    fun on(e: InventoryItemDeleted) {
        markDeleted()
    }
}
