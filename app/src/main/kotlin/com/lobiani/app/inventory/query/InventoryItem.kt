package com.lobiani.app.inventory.query

import java.util.*

data class InventoryItem(val id: UUID, val slug: String) {

    var stockLevel: Int = 0
        private set

    fun increaseStockLevelBy(count: Int) {
        stockLevel += count
    }

}
