package dev.baybay.lobiani.app.admin.inventory.web

import dev.baybay.lobiani.app.inventory.command.DefineInventoryItem

data class InventoryItemDefinition(val slug: String) {

    fun asCommand(): DefineInventoryItem {
        return DefineInventoryItem(slug)
    }
}
