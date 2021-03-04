package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class DefineProduct(@TargetAggregateIdentifier val id: ProductIdentifier)
