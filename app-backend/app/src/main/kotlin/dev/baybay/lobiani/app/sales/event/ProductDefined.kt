package dev.baybay.lobiani.app.sales.event

import dev.baybay.lobiani.app.sales.ProductIdentifier
import java.io.Serializable

data class ProductDefined(val id: ProductIdentifier) : Serializable
