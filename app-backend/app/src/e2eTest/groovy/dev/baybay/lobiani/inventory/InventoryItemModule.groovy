package dev.baybay.lobiani.inventory

class InventoryItemModule extends geb.Module {

    static content = {
        slug { $(".slug").text() }
        currentStockLevel { $(".stock-level").text() as Integer }
        increaseStockLevelBy { $("input[name=\"amount\"]")  }
        addToStockButton { $(".add-to-stock")  }
        deleteAction { $(".delete") }
    }

    def delete() {
        deleteAction.click()
    }

    def addToStock(amount) {
        increaseStockLevelBy.value(amount)
        addToStockButton.click()
    }
}
