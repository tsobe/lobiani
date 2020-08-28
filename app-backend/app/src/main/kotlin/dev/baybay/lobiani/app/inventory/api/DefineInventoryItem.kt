package dev.baybay.lobiani.app.inventory.api

import com.fasterxml.jackson.annotation.JsonCreator
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DefineInventoryItem(
        @TargetAggregateIdentifier
        val id: UUID,
        val slug: String) {

    @JsonCreator
    constructor(slug: String) : this(UUID.randomUUID(), slug)
}
