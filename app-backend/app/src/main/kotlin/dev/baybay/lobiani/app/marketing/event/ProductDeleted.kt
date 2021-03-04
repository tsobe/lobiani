package dev.baybay.lobiani.app.marketing.event

import dev.baybay.lobiani.app.marketing.ProductIdentifier
import java.io.Serializable

data class ProductDeleted(val id: ProductIdentifier) : Serializable {

    companion object {
        private const val serialVersionUID = 1
    }
}
