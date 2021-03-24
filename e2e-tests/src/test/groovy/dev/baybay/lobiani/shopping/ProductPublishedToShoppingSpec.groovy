package dev.baybay.lobiani.shopping

import dev.baybay.lobiani.admin.AdminSpec
import dev.baybay.lobiani.product.NewProductPage
import dev.baybay.lobiani.product.ProductsPage
import spock.lang.Stepwise

@Stepwise
class ProductPublishedToShoppingSpec extends AdminSpec {

    private static final String SLUG = "the-matrix-trilogy"

    def "product should be visible when defined"() {
        given:
        def newProductPage = to NewProductPage

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

    def "product should have price assigned"() {
        given:
        def productsPage = at ProductsPage

        when:
        productsPage.assignPrice SLUG, 17

        then:
        waitFor { productsPage.getProduct(SLUG).currentPrice == '17 EUR' }
    }

    def "product should be visible in shopping main page"() {
        when:
        def shoppingMainPage = to ShoppingMainPage

        then:
        waitFor { shoppingMainPage.hasProduct SLUG }
    }

    void cleanupSpec() {
        def productsPage = to ProductsPage
        waitFor { productsPage.hasProduct SLUG }
        productsPage.deleteProduct SLUG
    }
}
