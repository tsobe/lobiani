package dev.baybay.lobiani.app.marketing.command

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.marketing.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*
import javax.validation.Valid

data class DefineProduct(
    @TargetAggregateIdentifier
    val id: ProductIdentifier,
    @get:Valid
    val slug: Slug,
    val title: String,
    val description: String
) {

    constructor(id: UUID, slug: String, title: String, description: String) : this(
        ProductIdentifier(id),
        Slug(slug),
        title,
        description
    )

    constructor(slug: String, title: String, description: String) : this(UUID.randomUUID(), slug, title, description)
}
