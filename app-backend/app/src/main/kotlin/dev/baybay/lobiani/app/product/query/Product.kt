package dev.baybay.lobiani.app.product.query

import dev.baybay.lobiani.app.sales.model.Price
import java.util.*

class Product(val id: UUID, val slug: String, val title: String, val description: String) {

    var price: Price? = null
}
