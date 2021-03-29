package dev.baybay.lobiani.app.marketing.command

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.marketing.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid

data class DefineProduct(
    @TargetAggregateIdentifier
    val id: ProductIdentifier,
    @get:Valid
    val slug: Slug,
    val title: String,
    val description: String
) {

    constructor(slug: String, title: String, description: String)
            : this(ProductIdentifier(), Slug(slug), title, description)
}
