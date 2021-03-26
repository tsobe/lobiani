package dev.baybay.lobiani.app.admin.inventory

data class InventoryItem(val id: String, val slug: String) {

    var stockLevel: Int = 0
        private set

    fun increaseStockLevelBy(amount: Int) {
        stockLevel += amount
    }

}
