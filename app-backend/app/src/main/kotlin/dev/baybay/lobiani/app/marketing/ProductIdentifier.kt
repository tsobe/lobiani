package dev.baybay.lobiani.app.marketing

import java.util.*

data class ProductIdentifier(val value: UUID) {

    val stringValue: String get() = value.toString()

    constructor() : this(UUID.randomUUID())

    override fun toString(): String {
        return value.toString()
    }
}
