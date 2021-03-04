package dev.baybay.lobiani.app.inventory.event

import dev.baybay.lobiani.app.inventory.Quantity
import java.io.Serializable
import java.util.*

data class InventoryItemAddedToStock(val inventoryItemId: UUID, val quantity: Quantity) : Serializable
