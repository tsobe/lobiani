package com.lobiani.app.inventory.command

import com.lobiani.app.inventory.api.DefineNewInventoryItem
import com.lobiani.app.inventory.api.DeleteInventoryItemDefinition
import com.lobiani.app.inventory.api.InventoryItemDefined
import com.lobiani.app.inventory.api.InventoryItemDefinitionDeleted
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class InventoryItemDefinitionTest {

    private lateinit var fixture: FixtureConfiguration<InventoryItemDefinition>
    private val slug = "the-matrix-trilogy-blu-ray"

    @BeforeEach
    internal fun setUp() {
        fixture = AggregateTestFixture(InventoryItemDefinition::class.java)
    }

    @Test
    internal fun `should define new inventory item`() {
        val id = UUID.randomUUID()
        fixture.givenNoPriorActivity()
                .`when`(DefineNewInventoryItem(id, slug))
                .expectSuccessfulHandlerExecution()
                .expectEvents(InventoryItemDefined(id, slug))
    }

    @Test
    internal fun `should delete inventory item definition`() {
        val id = UUID.randomUUID()
        fixture.given(InventoryItemDefined(id, slug))
                .`when`(DeleteInventoryItemDefinition(id))
                .expectSuccessfulHandlerExecution()
                .expectEvents(InventoryItemDefinitionDeleted(id, slug))
    }
}
