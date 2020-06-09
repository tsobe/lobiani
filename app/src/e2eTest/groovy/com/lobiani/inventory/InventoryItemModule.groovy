package com.lobiani.inventory

class InventoryItemModule extends geb.Module {

    static content = {
        slug { $(".slug").text() }
        currentStockLevel { $(".stock-level").text() as Integer }
        increaseStockLevelBy { $("input[name=\"count\"]")  }
        addToStockButton { $(".add-to-stock")  }
        deleteAction { $(".delete") }
    }

    def delete() {
        deleteAction.click()
    }

    def addToStock(count) {
        increaseStockLevelBy.value(count)
        addToStockButton.click()
    }
}
