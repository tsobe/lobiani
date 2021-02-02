package dev.baybay.lobiani.app.inventory.web

import dev.baybay.lobiani.app.inventory.api.DefineInventoryItem

data class InventoryItemDefinition(val slug: String) {

    fun asCommand(): DefineInventoryItem {
        return DefineInventoryItem(slug)
    }
}
