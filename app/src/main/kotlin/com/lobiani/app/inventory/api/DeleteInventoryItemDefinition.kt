package com.lobiani.app.inventory.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DeleteInventoryItemDefinition(
        @TargetAggregateIdentifier
        val id: UUID)
