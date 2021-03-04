package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.Price
import dev.baybay.lobiani.app.sales.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier

class AssignPriceToProduct(@TargetAggregateIdentifier val id: ProductIdentifier, val price: Price)
