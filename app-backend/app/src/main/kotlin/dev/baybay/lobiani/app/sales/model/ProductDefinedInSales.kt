package dev.baybay.lobiani.app.sales.model

import java.io.Serializable
import java.util.*

data class ProductDefinedInSales(val id: UUID, val marketingProductID: UUID) : Serializable
