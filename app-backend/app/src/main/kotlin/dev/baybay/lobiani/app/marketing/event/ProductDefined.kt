package dev.baybay.lobiani.app.marketing.event

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.marketing.ProductIdentifier
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
