package com.lobiani.app.inventory.command

import com.lobiani.app.inventory.api.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class InventoryItemTest {

    private lateinit var fixture: FixtureConfiguration<InventoryItem>
    private val slug = "the-matrix-trilogy-blu-ray"
    private val id: UUID = UUID.randomUUID()

    @BeforeEach
    internal fun setUp() {
        fixture = AggregateTestFixture(InventoryItem::class.java)
    }

    @Test
    internal fun `should define new inventory item`() {
        fixture.givenNoPriorActivity()
                .`when`(DefineInventoryItem(id, slug))
                .expectSuccessfulHandlerExecution()
                .expectEvents(InventoryItemDefined(id, slug))
    }

    @Test
    internal fun `should delete inventory item`() {
        fixture.given(InventoryItemDefined(id, slug))
                .`when`(DeleteInventoryItem(id))
                .expectSuccessfulHandlerExecution()
                .expectEvents(InventoryItemDeleted(id, slug))
    }

    @Test
    internal fun `should add inventory item to stock`() {
        val count = Quantity.count(10)
        fixture.given(InventoryItemDefined(id, slug))
                .`when`(AddInventoryItemToStock(id, count))
                .expectSuccessfulHandlerExecution()
                .expectEvents(InventoryItemAddedToStock(id, count))
    }
}
