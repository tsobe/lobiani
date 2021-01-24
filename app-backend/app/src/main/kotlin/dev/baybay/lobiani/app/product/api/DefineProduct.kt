package dev.baybay.lobiani.app.product.api

import dev.baybay.lobiani.app.common.Slug
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*
import javax.validation.Valid

data class DefineProduct(
        @TargetAggregateIdentifier
        val id: UUID,
        @get:Valid
        val slug: Slug,
        val title: String,
        val description: String) {

    constructor(id: UUID, slug: String, title: String, description: String) : this(id, Slug(slug), title, description)

    constructor(slug: String, title: String, description: String) : this(UUID.randomUUID(), slug, title, description)
}
