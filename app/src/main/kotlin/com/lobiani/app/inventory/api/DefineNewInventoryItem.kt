package com.lobiani.app.inventory.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class DefineNewInventoryItem(
        @TargetAggregateIdentifier
        val id: UUID,
        val slug: String)
