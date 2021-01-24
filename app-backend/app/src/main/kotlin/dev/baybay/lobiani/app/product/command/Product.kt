package dev.baybay.lobiani.app.product.command

import dev.baybay.lobiani.app.product.api.DefineProduct
import dev.baybay.lobiani.app.product.api.DeleteProduct
import dev.baybay.lobiani.app.product.api.ProductDefined
import dev.baybay.lobiani.app.product.api.ProductDeleted
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateLifecycle.markDeleted
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class Product {

    @AggregateIdentifier
    private lateinit var id: UUID

    constructor()

    @CommandHandler
    constructor(c: DefineProduct) {
        apply(ProductDefined(c.id, c.slug.value, c.title, c.description))
    }

    @CommandHandler
    fun handle(c: DeleteProduct) {
        apply(ProductDeleted(id))
    }

    @EventSourcingHandler
    fun on(e: ProductDefined) {
        id = e.id
    }

    @EventSourcingHandler
    fun on(e: ProductDeleted) {
        markDeleted()
    }
}