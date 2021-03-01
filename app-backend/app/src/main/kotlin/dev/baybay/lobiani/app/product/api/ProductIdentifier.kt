package dev.baybay.lobiani.app.product.api

import java.util.*

data class ProductIdentifier(val value: UUID) {

    val stringValue: String get() = value.toString()
}
