package dev.baybay.lobiani.shopping

class ProductModule extends geb.Module {

    static content = {
        title { $("[data-title]").text() }
    }

    def getSlug() {
        attr("data-product")
    }
}
