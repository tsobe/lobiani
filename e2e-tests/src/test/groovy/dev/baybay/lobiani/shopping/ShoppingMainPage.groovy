package dev.baybay.lobiani.shopping

import geb.Page

class ShoppingMainPage extends Page {

    static url = System.getProperty("test.shopping.baseUrl")

    static content = {
        products { $("[data-product]").moduleList ProductModule }
    }

    def hasProduct(slug) {
        products.any { it.slug == slug }
    }
}
