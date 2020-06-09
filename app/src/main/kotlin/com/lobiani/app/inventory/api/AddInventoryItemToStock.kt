package com.lobiani.app.inventory.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class AddInventoryItemToStock(@TargetAggregateIdentifier val inventoryItemId: UUID, val quantity: Quantity)
