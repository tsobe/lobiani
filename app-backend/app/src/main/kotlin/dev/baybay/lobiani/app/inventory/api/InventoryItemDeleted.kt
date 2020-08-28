package dev.baybay.lobiani.app.inventory.api

import java.io.Serializable
import java.util.*

data class InventoryItemDeleted(val id: UUID, val slug: String) : Serializable
