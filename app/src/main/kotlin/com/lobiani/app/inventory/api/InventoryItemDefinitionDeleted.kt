package com.lobiani.app.inventory.api

import java.io.Serializable
import java.util.*

data class InventoryItemDefinitionDeleted(val id: UUID, val slug: String) : Serializable
