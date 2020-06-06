package com.lobiani.app.inventory.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DeleteInventoryItem(
        @TargetAggregateIdentifier
        val id: UUID)
