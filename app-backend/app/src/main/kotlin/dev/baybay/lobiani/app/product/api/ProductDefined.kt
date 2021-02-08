package dev.baybay.lobiani.app.product.api

import java.io.Serializable
import java.util.*

data class ProductDefined(val id: UUID,
                          val slug: String,
                          val title: String,
                          val description: String) : Serializable
