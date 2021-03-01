package dev.baybay.lobiani.app.product.api

import java.io.Serializable

data class ProductDeleted(val id: ProductIdentifier) : Serializable {

    companion object {
        private const val serialVersionUID = 1
    }
}
