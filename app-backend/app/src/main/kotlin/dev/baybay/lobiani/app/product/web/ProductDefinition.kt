package dev.baybay.lobiani.app.product.web

import dev.baybay.lobiani.app.product.api.DefineProduct

data class ProductDefinition(val slug: String, val title: String, val description: String) {

    fun asCommand(): DefineProduct {
        return DefineProduct(slug, title, description)
    }
}
