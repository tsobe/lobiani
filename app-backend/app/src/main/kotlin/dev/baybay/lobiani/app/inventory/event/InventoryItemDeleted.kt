package dev.baybay.lobiani.app.inventory.event

import java.io.Serializable
import java.util.*

data class InventoryItemDeleted(val id: UUID, val slug: String) : Serializable
