package dev.baybay.lobiani.app.inventory.api

data class Quantity(private val _value: Int) {

    val value: Int get() = _value

    companion object {
        fun count(value: Int) : Quantity {
            return Quantity(value)
        }
    }

}
