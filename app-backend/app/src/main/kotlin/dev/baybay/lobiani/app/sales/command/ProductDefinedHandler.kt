package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.product.api.ProductDefined
import dev.baybay.lobiani.app.sales.command.api.DefineProductInSales
import dev.baybay.lobiani.app.sales.model.ProductIdentifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class ProductDefinedHandler {

    @EventHandler
    fun on(e: ProductDefined, commandGateway: CommandGateway) {
        commandGateway.send<Void>(DefineProductInSales(ProductIdentifier(e.id)))
    }
}
