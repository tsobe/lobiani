package dev.baybay.lobiani.app.admin.product

import dev.baybay.lobiani.app.sales.Price

class Product(val id: String, val slug: String, val title: String, val description: String) {

    var price: Price? = null
}
