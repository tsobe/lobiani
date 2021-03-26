package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.Price
import dev.baybay.lobiani.app.sales.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class AssignPriceToProduct(@TargetAggregateIdentifier val productId: ProductIdentifier, val price: Price)
