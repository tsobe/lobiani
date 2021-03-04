package dev.baybay.lobiani.app.inventory

data class Quantity(private val _value: Int) {

    val value: Int get() = _value

    companion object {
        @JvmStatic
        fun count(value: Int) : Quantity {
            return Quantity(value)
        }
    }

}
