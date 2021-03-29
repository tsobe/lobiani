package dev.baybay.lobiani.app.inventory.command

import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import dev.baybay.lobiani.app.inventory.Quantity
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.constraints.AssertTrue

data class AddInventoryItemToStock(
    @TargetAggregateIdentifier val inventoryItemId: InventoryItemIdentifier,
    val quantity: Quantity
) {

    @AssertTrue(message = "Amount must be a positive number")
    fun hasValidQuantity(): Boolean {
        return quantity.value > 0
    }
}
