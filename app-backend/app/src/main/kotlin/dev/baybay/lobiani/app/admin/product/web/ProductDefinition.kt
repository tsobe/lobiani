package dev.baybay.lobiani.app.admin.product.web

import dev.baybay.lobiani.app.marketing.command.DefineProduct

data class ProductDefinition(val slug: String, val title: String, val description: String) {

    fun asCommand(): DefineProduct {
        return DefineProduct(slug, title, description)
    }
}
