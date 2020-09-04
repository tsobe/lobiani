package dev.baybay.lobiani.inventory

import geb.spock.GebSpec

class InventorySpec extends GebSpec {

    def "new inventory item defined, added to stock and then deleted"() {
        given:
        def slug = "the-matrix-trilogy-blu-ray"
        def itemsPage = to InventoryItemsPage


        when:
        itemsPage.openNewItemPage()

        then:
        at NewInventoryItemPage


        def newItemPage = page as NewInventoryItemPage

        when:
        newItemPage.enterSlug(slug)
        newItemPage.save()

        then:
        at itemsPage
        waitFor { itemsPage.hasItem slug }


        when:
        itemsPage.addItemToStock(slug, 100)

        then:
        at itemsPage
        waitFor { itemsPage.getItemStockLevel(slug) == 100 }


        when:
        itemsPage.addItemToStock(slug, 10)

        then:
        at itemsPage
        waitFor { itemsPage.getItemStockLevel(slug) == 110 }


        when:
        itemsPage.deleteItem slug

        then:
        at itemsPage
        waitFor { !itemsPage.hasItem(slug) }


        cleanup:
        if (itemsPage?.hasItem(slug)) {
            itemsPage.deleteItem slug
        }
    }

}
