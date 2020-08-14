package dev.baybay.lobiani.inventory

import geb.Page

class InventoryItemsPage extends Page {

    static url = "#/items"

    static at = { !$(".items").empty }

    static content = {
        newItem { $("#new-item") }
        items { $(".items .item").moduleList InventoryItemModule }
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

    def addItemToStock(slug, count) {
        items.find { it.slug == slug }.addToStock(count)
    }

    def getItemStockLevel(slug) {
        items.find { it.slug == slug }.currentStockLevel
    }
}
