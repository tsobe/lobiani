package dev.baybay.lobiani.product

import dev.baybay.lobiani.admin.BaseAdminSpec
import spock.lang.Stepwise

@Stepwise
class ProductSpec extends BaseAdminSpec {

    public static final String SLUG = "the-matrix-trilogy"

    def "should navigate to new product page"() {
        given:
        def productsPage = to ProductsPage

        when:
        productsPage.openNewProductPage()

        then:
        at NewProductPage
    }

    def "product should be visible when defined"() {
        given:
        def newProductPage = page as NewProductPage

        and:
        def product = [slug       : SLUG,
                       title      : "The Matrix trilogy",
                       description: "This is Matrix"]
        and:
        newProductPage.enterData product

        when:
        newProductPage.save()

        then:
        def productsPage = at ProductsPage

        and:
        waitFor { productsPage.hasProduct product.slug }
    }

    def "product should not be visible when deleted"() {
        given:
        def definedProductSlug = SLUG

        and:
        def productsPage = page as ProductsPage

        when:
        productsPage.deleteProduct definedProductSlug

        then:
        waitFor { !productsPage.hasProduct(definedProductSlug) }
    }

    void cleanupSpec() {
        if (!(page instanceof ProductsPage)) {
            return
        }
        def productsPage = page as ProductsPage
        if (productsPage.hasProduct(SLUG)) {
            productsPage.deleteProduct SLUG
        }
    }
}
