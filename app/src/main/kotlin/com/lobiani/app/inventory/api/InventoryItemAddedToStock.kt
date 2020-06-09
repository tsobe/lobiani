package com.lobiani.app.inventory.api

import java.io.Serializable
import java.util.*

data class InventoryItemAddedToStock(val inventoryItemId: UUID, val quantity: Quantity) : Serializable
