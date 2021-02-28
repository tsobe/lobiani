package dev.baybay.lobiani.app.sales.command.api

import dev.baybay.lobiani.app.sales.model.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class DefineProduct(@TargetAggregateIdentifier val id: ProductIdentifier)
