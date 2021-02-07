package dev.baybay.lobiani.inventory

import dev.baybay.lobiani.admin.BaseAdminSpec
import spock.lang.Stepwise

@Stepwise
class InventorySpec extends BaseAdminSpec {

    public static final String SLUG = "the-matrix-trilogy"

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
        def newItemPage = page as NewInventoryItemPage

        and:
        newItemPage.enterSlug SLUG

        when:
        newItemPage.save()

        then:
        def itemsPage = at InventoryItemsPage

        and:
        waitFor { itemsPage.hasItem SLUG }
    }

    def "should increase stock level when items are added to stock"() {
        given:
        def itemsPage = page as InventoryItemsPage

        when:
        itemsPage.addItemToStock SLUG, 100

        then:
        waitFor { itemsPage.getItemStockLevel(SLUG) == 100 }
    }

    def "item should not be visible when deleted"() {
        given:
        def itemsPage = page as InventoryItemsPage

        when:
        itemsPage.deleteItem SLUG

        then:
        waitFor { !itemsPage.hasItem(SLUG) }
    }

    void cleanupSpec() {
        if (!page instanceof InventoryItemsPage) {
            return
        }
        def itemsPage = page as InventoryItemsPage
        if (itemsPage.hasItem(SLUG)) {
            itemsPage.deleteItem SLUG
        }
    }
}
