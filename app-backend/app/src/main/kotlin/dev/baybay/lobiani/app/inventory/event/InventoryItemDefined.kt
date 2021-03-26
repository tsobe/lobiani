package dev.baybay.lobiani.app.inventory.event

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import java.io.Serializable

data class InventoryItemDefined(val id: InventoryItemIdentifier, val slug: Slug) : Serializable
