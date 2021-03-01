package dev.baybay.lobiani.app.product.api

import dev.baybay.lobiani.app.common.Slug
import java.io.Serializable

data class ProductDefined(
    val id: ProductIdentifier,
    val slug: Slug,
    val title: String,
    val description: String
) : Serializable {

    companion object {
        private const val serialVersionUID = 1
    }
}
