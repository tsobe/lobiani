package dev.baybay.lobiani.app.sales.model

import java.io.Serializable
import java.util.*

data class PriceAssignedToProduct(
    val id: UUID,
    val marketingProductID: UUID,
    val price: Price
) : Serializable
