package dev.baybay.lobiani.app.marketing.command

import dev.baybay.lobiani.app.marketing.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DeleteProduct(@TargetAggregateIdentifier val id: ProductIdentifier) {

    constructor(id: UUID) : this(ProductIdentifier(id))
}
