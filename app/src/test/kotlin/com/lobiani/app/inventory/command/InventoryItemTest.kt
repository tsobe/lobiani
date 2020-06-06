package com.lobiani.app.inventory.command

import com.lobiani.app.inventory.api.DefineInventoryItem
import com.lobiani.app.inventory.api.DeleteInventoryItem
import com.lobiani.app.inventory.api.InventoryItemDefined
import com.lobiani.app.inventory.api.InventoryItemDeleted
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class InventoryItemTest {

    private lateinit var fixture: FixtureConfiguration<InventoryItem>
    private val slug = "the-matrix-trilogy-blu-ray"

    @BeforeEach
    internal fun setUp() {
        fixture = AggregateTestFixture(InventoryItem::class.java)
    }

    @Test
    internal fun `should define new inventory item`() {
        val id = UUID.randomUUID()
        fixture.givenNoPriorActivity()
                .`when`(DefineInventoryItem(id, slug))
                .expectSuccessfulHandlerExecution()
                .expectEvents(InventoryItemDefined(id, slug))
    }

    @Test
    internal fun `should delete inventory item`() {
        val id = UUID.randomUUID()
        fixture.given(InventoryItemDefined(id, slug))
                .`when`(DeleteInventoryItem(id))
                .expectSuccessfulHandlerExecution()
                .expectEvents(InventoryItemDeleted(id, slug))
    }
}
