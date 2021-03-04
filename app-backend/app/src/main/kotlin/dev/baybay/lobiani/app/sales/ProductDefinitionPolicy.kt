package dev.baybay.lobiani.app.sales

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import dev.baybay.lobiani.app.marketing.event.ProductDefined as ProductDefinedInMarketing
import dev.baybay.lobiani.app.sales.command.DefineProduct as DefineProductInSales

@Component
class ProductDefinitionPolicy {

    @EventHandler
    fun on(e: ProductDefinedInMarketing, commandGateway: CommandGateway) {
        commandGateway.send<Void>(DefineProductInSales(ProductIdentifier(e.id.value)))
    }
}
