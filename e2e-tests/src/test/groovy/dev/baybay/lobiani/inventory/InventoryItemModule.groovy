package dev.baybay.lobiani.inventory

class InventoryItemModule extends geb.Module {

    static content = {
        slug { $("[data-slug]").text() }
        currentStockLevel { $("[data-stock-level]").text() as Integer }
        increaseStockLevelBy { $("[data-amount]")  }
        addToStockButton { $("[data-add-to-stock]")  }
        deleteAction { $("[data-delete]") }
    }

    def delete() {
        deleteAction.click()
    }

    def addToStock(amount) {
        increaseStockLevelBy.value(amount)
        addToStockButton.click()
    }
}
