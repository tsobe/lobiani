package dev.baybay.lobiani.app.sales

import java.math.BigDecimal
import java.util.*

data class Price(val value: BigDecimal, val currency: Currency) {
    constructor(value: Double, currency: String) : this(BigDecimal.valueOf(value), Currency.getInstance(currency))
}
