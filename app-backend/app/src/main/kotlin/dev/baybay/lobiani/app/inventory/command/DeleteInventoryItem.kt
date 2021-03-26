package dev.baybay.lobiani.app.inventory.command

import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class DeleteInventoryItem(@TargetAggregateIdentifier val id: InventoryItemIdentifier)
