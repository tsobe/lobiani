package dev.baybay.lobiani.app.product.query

import dev.baybay.lobiani.app.sales.model.Price

class Product(val id: String, val slug: String, val title: String, val description: String) {

    var price: Price? = null
}
