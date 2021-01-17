package dev.baybay.lobiani.app.inventory.api

import com.fasterxml.jackson.annotation.JsonCreator
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*
import javax.validation.constraints.AssertTrue

data class DefineInventoryItem(
        @TargetAggregateIdentifier
        val id: UUID,
        val slug: String) {

    @JsonCreator
    constructor(slug: String) : this(UUID.randomUUID(), slug)

    @AssertTrue(message = "Slug must consist of lowercase alpha-numeric and dash('-') characters")
    fun hasValidSlug(): Boolean {
        return slug.matches("^[a-z0-9-]+$".toRegex())
    }
}
