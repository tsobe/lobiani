package dev.baybay.lobiani.app.shopping

class Product {

    var id: String? = null
    var slug: String? = null
    var title: String? = null
    var description: String? = null
    var stockLevel: Int = 0
    var price: Price? = null
    var inventoryItemId: String? = null
    val published: Boolean get() = id != null && slug != null && title != null && description != null && price != null
}
