package dev.baybay.lobiani.app.sales.model

import java.math.BigDecimal
import java.util.*

data class Price(val value: BigDecimal, val currency: Currency) {
    constructor(value: Double, currency: String) : this(BigDecimal.valueOf(value), Currency.getInstance(currency))
}
