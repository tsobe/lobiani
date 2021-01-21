package dev.baybay.lobiani.app.inventory.command

import dev.baybay.lobiani.app.inventory.api.*
import dev.baybaydev.lobiani.testutil.AggregateSpec

class InventoryItemSpec extends AggregateSpec {

    def id = UUID.randomUUID()
    def slug = "the-matrix-trilogy"

    void setup() {
        useAggregate InventoryItem
    }

    def "should define new inventory item"() {
        when:
        actingWith new DefineInventoryItem(id, slug)

        then:
        expectSuccess()

        and:
        expectEvent new InventoryItemDefined(id, slug)
    }

    def "should delete item"() {
        given:
        pastEvent new InventoryItemDefined(id, slug)

        when:
        actingWith new DeleteInventoryItem(id)

        then:
        expectEvent new InventoryItemDeleted(id, slug)
    }

    def "should add inventory item to stock"() {
        given:
        pastEvent new InventoryItemDefined(id, slug)

        when:
        actingWith new AddInventoryItemToStock(id, Quantity.count(10))

        then:
        expectSuccess()

        and:
        expectEvent new InventoryItemAddedToStock(id, Quantity.count(10))
    }
}
