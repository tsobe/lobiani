package dev.baybay.lobiani.app.inventory.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DeleteInventoryItem(@TargetAggregateIdentifier val id: UUID)
