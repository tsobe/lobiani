package dev.baybay.lobiani.product

import geb.Module

class ProductModule extends Module {

    static content = {
        slug { $("[data-slug]").text() }
        currentPrice { $("[data-current-price]").text() }
        priceToAssign { $("[data-price-to-assign]") }
        assignPriceBtn { $("[data-assign-price]") }
        deleteBtn { $("[data-delete]") }
    }

    def delete() {
        deleteBtn.click()
    }

    def assignPrice(price) {
        priceToAssign.value price
        assignPriceBtn.click()
    }
}
