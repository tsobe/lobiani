package com.lobiani.inventory

import geb.spock.GebSpec

class InventorySpec extends GebSpec {

    def "new inventory item defined and then deleted"() {
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
        itemsPage.hasItem slug


        when:
        itemsPage.deleteItem slug

        then:
        at itemsPage
        !itemsPage.hasItem(slug)


        cleanup:
        if (itemsPage?.hasItem(slug)) {
            itemsPage.deleteItem slug
        }
    }

}
