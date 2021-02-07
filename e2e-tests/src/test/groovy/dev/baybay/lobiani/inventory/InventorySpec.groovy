package dev.baybay.lobiani.inventory

import dev.baybay.lobiani.admin.BaseAdminSpec
import spock.lang.Stepwise

@Stepwise
class InventorySpec extends BaseAdminSpec {

    def "should navigate to new item page"() {
        given:
        def itemsPage = to InventoryItemsPage

        when:
        itemsPage.openNewItemPage()

        then:
        at NewInventoryItemPage
    }

    def "item should be visible when defined"() {
        given:
        def slug = "the-matrix-trilogy"

        and:
        def newItemPage = page as NewInventoryItemPage

        and:
        newItemPage.enterSlug slug

        when:
        newItemPage.save()

        then:
        at InventoryItemsPage

        def itemsPage = to InventoryItemsPage

        and:
        waitFor { itemsPage.hasItem slug }
    }

    def "should increase stock level when items are added to stock"() {
        given:
        def definedItemSlug = "the-matrix-trilogy"

        and:
        def itemsPage = page as InventoryItemsPage

        when:
        itemsPage.addItemToStock definedItemSlug, 100

        then:
        waitFor { itemsPage.getItemStockLevel(definedItemSlug) == 100 }
    }

    def "item should not be visible when deleted"() {
        given:
        def definedItemSlug = "the-matrix-trilogy"

        and:
        def itemsPage = page as InventoryItemsPage

        when:
        itemsPage.deleteItem definedItemSlug

        then:
        waitFor { !itemsPage.hasItem(definedItemSlug) }
    }
}
