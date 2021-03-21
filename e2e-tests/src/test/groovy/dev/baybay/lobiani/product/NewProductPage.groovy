package dev.baybay.lobiani.product

import dev.baybay.lobiani.admin.AdminProtectedPageBase
import dev.baybay.lobiani.admin.LoadingButtonModule

class NewProductPage extends AdminProtectedPageBase {

    static url = "/products/new"

    static at = { waitFor { !saveBtn.empty } }

    static content = {
        slugInput { $("[data-slug]") }
        titleInput { $("[data-title]") }
        descriptionInput { $("[data-description]") }
        saveBtn { $("[data-save]").module(LoadingButtonModule) }
    }

    def enterData(product) {
        slugInput.value(product.slug)
        titleInput.value(product.title)
        descriptionInput.value(product.description)
    }

    def save() {
        saveBtn.click()
    }
}
