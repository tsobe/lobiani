package dev.baybay.lobiani.app.inventory.event

import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import java.io.Serializable

data class InventoryItemDeleted(val id: InventoryItemIdentifier) : Serializable
