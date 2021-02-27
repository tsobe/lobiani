package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.command.api.AssignPriceToProduct
import dev.baybay.lobiani.app.sales.command.api.DefineProductInSales
import dev.baybay.lobiani.app.sales.model.PriceAssignedToProduct
import dev.baybay.lobiani.app.sales.model.ProductDefinedInSales
import dev.baybay.lobiani.app.sales.model.ProductIdentifier
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class SalesProduct {

    @AggregateIdentifier
    private lateinit var id: ProductIdentifier

    constructor()

    @CommandHandler
    constructor(c: DefineProductInSales) {
        apply(ProductDefinedInSales(c.id))
    }

    @CommandHandler
    fun handle(c: AssignPriceToProduct) {
        apply(PriceAssignedToProduct(id, c.price))
    }

    @EventSourcingHandler
    fun on(e: ProductDefinedInSales) {
        id = e.id
    }
}
