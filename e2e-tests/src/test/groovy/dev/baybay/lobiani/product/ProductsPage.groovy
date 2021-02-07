package dev.baybay.lobiani.product

import dev.baybay.lobiani.admin.AdminProtectedPageBase

class ProductsPage extends AdminProtectedPageBase {

    static url = "/products"

    static at = { waitFor { !$("[data-products]").empty } }

    static content = {
        newProduct { $("[data-new-product]") }
        products { $("[data-products] [data-product]").moduleList ProductModule }
    }

    def openNewProductPage() {
        newProduct.click()
    }

    def hasProduct(slug) {
        products.any { it.slug == slug }
    }

    def deleteProduct(slug) {
        products.find { it.slug == slug}.delete()
    }
}
