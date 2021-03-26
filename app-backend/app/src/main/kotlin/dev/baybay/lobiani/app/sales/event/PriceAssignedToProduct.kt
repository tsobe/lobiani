package dev.baybay.lobiani.app.sales.event

import dev.baybay.lobiani.app.sales.Price
import dev.baybay.lobiani.app.sales.ProductIdentifier
import java.io.Serializable

data class PriceAssignedToProduct(
    val productId: ProductIdentifier,
    val price: Price
) : Serializable
