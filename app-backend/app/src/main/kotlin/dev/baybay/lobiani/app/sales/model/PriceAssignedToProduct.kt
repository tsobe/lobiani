package dev.baybay.lobiani.app.sales.model

import java.io.Serializable

data class PriceAssignedToProduct(
    val id: ProductIdentifier,
    val price: Price
) : Serializable
