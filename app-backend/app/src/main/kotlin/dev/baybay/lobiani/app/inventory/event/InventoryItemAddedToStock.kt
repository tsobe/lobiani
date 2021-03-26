package dev.baybay.lobiani.app.inventory.event

import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import dev.baybay.lobiani.app.inventory.Quantity
import java.io.Serializable

data class InventoryItemAddedToStock(val inventoryItemId: InventoryItemIdentifier, val quantity: Quantity) :
    Serializable
