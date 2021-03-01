package dev.baybay.lobiani.app.product.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DeleteProduct(@TargetAggregateIdentifier val id: ProductIdentifier) {

    constructor(id: UUID) : this(ProductIdentifier(id))
}
