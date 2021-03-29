package dev.baybay.lobiani.app.inventory

data class Quantity(val value: Int, val unit: QuantityUnit) {

    companion object {
        @JvmStatic
        fun count(value: Int) : Quantity {
            return Quantity(value, QuantityUnit.COUNT)
        }
    }
}
