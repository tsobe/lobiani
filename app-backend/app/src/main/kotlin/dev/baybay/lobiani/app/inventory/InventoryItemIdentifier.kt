package dev.baybay.lobiani.app.inventory

import java.io.Serializable
import java.util.*

data class InventoryItemIdentifier(val value: UUID) : Serializable {

    val stringValue: String get() = value.toString()

    constructor() : this(UUID.randomUUID())

    override fun toString(): String {
        return value.toString()
    }
}
