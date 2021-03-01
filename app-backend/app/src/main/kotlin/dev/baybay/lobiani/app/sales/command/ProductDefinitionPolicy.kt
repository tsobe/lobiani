package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.model.ProductIdentifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import dev.baybay.lobiani.app.product.api.ProductDefined as ProductDefinedInMarketing
import dev.baybay.lobiani.app.sales.command.api.DefineProduct as DefineProductInSales

@Component
class ProductDefinitionPolicy {

    @EventHandler
    fun on(e: ProductDefinedInMarketing, commandGateway: CommandGateway) {
        commandGateway.send<Void>(DefineProductInSales(ProductIdentifier(e.id.value)))
    }
}
