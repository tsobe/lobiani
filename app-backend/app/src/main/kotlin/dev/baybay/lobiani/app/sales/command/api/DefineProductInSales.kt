package dev.baybay.lobiani.app.sales.command.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DefineProductInSales(@TargetAggregateIdentifier val id: UUID, val marketingProductID: UUID)
