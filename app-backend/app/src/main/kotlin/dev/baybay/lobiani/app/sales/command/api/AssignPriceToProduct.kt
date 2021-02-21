package dev.baybay.lobiani.app.sales.command.api

import dev.baybay.lobiani.app.sales.model.Price
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

class AssignPriceToProduct(@TargetAggregateIdentifier val id: UUID, val price: Price)
