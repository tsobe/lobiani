package dev.baybay.lobiani.app.marketing.command

import dev.baybay.lobiani.app.marketing.ProductIdentifier
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class DeleteProduct(@TargetAggregateIdentifier val id: ProductIdentifier)
