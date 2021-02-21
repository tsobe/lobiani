package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.command.api.AssignPriceToProduct
import dev.baybay.lobiani.app.sales.command.api.DefineProductInSales
import dev.baybay.lobiani.app.sales.model.PriceAssignedToProduct
import dev.baybay.lobiani.app.sales.model.ProductDefinedInSales
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class SalesProduct {

    @AggregateIdentifier
    private lateinit var id: UUID
    private lateinit var marketingProductID: UUID

    constructor()

    @CommandHandler
    constructor(c: DefineProductInSales) {
        apply(ProductDefinedInSales(c.id, c.marketingProductID))
    }

    @CommandHandler
    fun handle(c: AssignPriceToProduct) {
        apply(PriceAssignedToProduct(id, marketingProductID, c.price))
    }

    @EventSourcingHandler
    fun on(e: ProductDefinedInSales) {
        id = e.id
        marketingProductID = e.marketingProductID
    }
}
