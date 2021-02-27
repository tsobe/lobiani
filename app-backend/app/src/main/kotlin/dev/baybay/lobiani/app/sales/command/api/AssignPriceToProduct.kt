package dev.baybay.lobiani.app.sales.command.api

import dev.baybay.lobiani.app.sales.model.Price
import dev.baybay.lobiani.app.sales.model.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier

class AssignPriceToProduct(@TargetAggregateIdentifier val id: ProductIdentifier, val price: Price)
