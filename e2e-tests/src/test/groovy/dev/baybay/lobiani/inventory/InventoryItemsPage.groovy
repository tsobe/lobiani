package dev.baybay.lobiani.inventory

import geb.Page

class InventoryItemsPage extends Page {

    static url = "/items"

    static at = { waitFor { !$("[data-items]").empty } }

    static content = {
        newItem { $("[data-new-item]") }
        items { $("[data-items] [data-item]").moduleList InventoryItemModule }
    }

    def openNewItemPage() {
        newItem.click()
    }

    def hasItem(slug) {
        items.any { it.slug == slug }
    }

    def deleteItem(slug) {
        items.find { it.slug == slug }.delete()
    }

    def addItemToStock(slug, amount) {
        items.find { it.slug == slug }.addToStock(amount)
    }

    def getItemStockLevel(slug) {
        items.find { it.slug == slug }.currentStockLevel
    }
}
