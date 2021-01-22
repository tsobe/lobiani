package dev.baybay.lobiani.app.product.api

import com.fasterxml.jackson.annotation.JsonCreator
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DefineProduct(
        @TargetAggregateIdentifier
        val id: UUID,
        val slug: String,
        val title: String,
        val description: String) {

    @JsonCreator
    constructor(slug: String, title: String, description: String) : this(UUID.randomUUID(), slug, title, description)
}
