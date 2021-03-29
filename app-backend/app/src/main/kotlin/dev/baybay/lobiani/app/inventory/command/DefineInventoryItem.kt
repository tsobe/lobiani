package dev.baybay.lobiani.app.inventory.command

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid

data class DefineInventoryItem(
    @TargetAggregateIdentifier
    val id: InventoryItemIdentifier,
    @get:Valid
    val slug: Slug
) {

    constructor(slug: String) : this(InventoryItemIdentifier(), Slug(slug))
}
