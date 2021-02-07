package dev.baybay.lobiani.product

import geb.Module

class ProductModule extends Module {

    static content = {
        slug { $("[data-slug]").text() }
        deleteBtn { $("[data-delete]") }
    }

    def delete() {
        deleteBtn.click()
    }
}
