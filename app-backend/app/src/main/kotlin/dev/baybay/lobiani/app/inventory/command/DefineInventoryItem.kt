package dev.baybay.lobiani.app.inventory.command

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*
import javax.validation.Valid

data class DefineInventoryItem(
    @TargetAggregateIdentifier
    val id: InventoryItemIdentifier,
    @get:Valid
    val slug: Slug
) {

    constructor(id: UUID, slug: String) : this(InventoryItemIdentifier(id), Slug(slug))

    constructor(slug: String) : this(UUID.randomUUID(), slug)
}
