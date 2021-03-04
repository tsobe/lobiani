package dev.baybay.lobiani.app.sales.event

import dev.baybay.lobiani.app.sales.Price
import dev.baybay.lobiani.app.sales.ProductIdentifier
import java.io.Serializable

data class PriceAssignedToProduct(
    val id: ProductIdentifier,
    val price: Price
) : Serializable
