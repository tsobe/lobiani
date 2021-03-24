package dev.baybay.lobiani.inventory

class InventoryItemModule extends geb.Module {

    static content = {
        slug { $("[data-slug]").text() }
        stockLevel { $("[data-stock-level]").text() as Integer }
        amountInput { $("[data-amount]")  }
        addToStockBtn { $("[data-add-to-stock]")  }
        deleteBtn { $("[data-delete]") }
    }

    def delete() {
        deleteBtn.click()
    }

    def addToStock(amount) {
        amountInput.value amount
        addToStockBtn.click()
    }
}
