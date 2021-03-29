package dev.baybay.lobiani.app.shopping

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.inventory.InventoryItemIdentifier
import dev.baybay.lobiani.app.inventory.Quantity
import dev.baybay.lobiani.app.inventory.event.InventoryItemAddedToStock
import dev.baybay.lobiani.app.inventory.event.InventoryItemDefined
import dev.baybay.lobiani.app.marketing.ProductIdentifier
import dev.baybay.lobiani.app.marketing.event.ProductDefined
import dev.baybay.lobiani.app.marketing.event.ProductDeleted
import dev.baybay.lobiani.app.sales.Price as SalesPrice
import dev.baybay.lobiani.app.sales.ProductIdentifier as SalesProductIdentifier
import dev.baybay.lobiani.app.sales.event.PriceAssignedToProduct
import dev.baybay.lobiani.app.shopping.query.QueryAllPublishedProducts
import dev.baybay.lobiani.testutil.ProjectionSpec
import org.axonframework.messaging.responsetypes.ResponseTypes

class ProductProjectionSpec extends ProjectionSpec {

    @Override
    def getProjection() {
        new ProductProjection()
    }

    def "no product should be published initially"() {
        expect:
        queryPublishedProducts().empty
    }

    def "no product should be published when only inventory item is defined"() {
        given:
        event newInventoryItemDefined()

        when:
        def products = queryPublishedProducts()

        then:
        products.empty
    }

    def "no product should be published when only product is defined"() {
        given:
        event newProductDefined()

        when:
        def products = queryPublishedProducts()

        then:
        products.empty
    }

    def "no product should be published when inventory item and product are defined"() {
        given:
        def slug = "the-matrix-trilogy"

        and:
        event newInventoryItemDefined(slug)

        and:
        event newProductDefined(slug)

        when:
        def products = queryPublishedProducts()

        then:
        products.empty
    }

    def "product should be published and out of stock when product with price is defined"() {
        given:
        def productDefined = newProductDefined()
        def priceAssignedToProduct = newPriceAssignedToProduct productDefined.id.value
        def price = priceAssignedToProduct.price

        and:
        event productDefined

        and:
        event priceAssignedToProduct

        when:
        def products = queryPublishedProducts()

        then:
        products.size() == 1

        and:
        def publishedProduct = products[0]
        publishedProduct.slug == productDefined.slug.value
        publishedProduct.title == productDefined.title
        publishedProduct.description == productDefined.description
        publishedProduct.price != null
        publishedProduct.price.value == price.value
        publishedProduct.price.currency == price.currency
        publishedProduct.stockLevel == 0
    }

    def "product should be published and out of stock when product with price and inventory item are defined"() {
        given:
        def slug = "the-matrix-trilogy"
        def productDefined = newProductDefined slug
        def priceAssignedToProduct = newPriceAssignedToProduct productDefined.id.value
        def price = priceAssignedToProduct.price
        def inventoryItemDefined = newInventoryItemDefined slug

        and:
        event productDefined

        and:
        event priceAssignedToProduct

        and:
        event inventoryItemDefined

        when:
        def products = queryPublishedProducts()

        then:
        products.size() == 1

        and:
        def publishedProduct = products[0]
        publishedProduct.slug == productDefined.slug.value
        publishedProduct.title == productDefined.title
        publishedProduct.description == productDefined.description
        publishedProduct.price != null
        publishedProduct.price.value == price.value
        publishedProduct.price.currency == price.currency
        publishedProduct.stockLevel == 0
    }

    def "product should be published and in stock when product with price and inventory item with stock are defined"() {
        given:
        def slug = "the-matrix-trilogy"
        def productDefined = newProductDefined(slug)
        def priceAssignedToProduct = newPriceAssignedToProduct(productDefined.id.value)
        def price = priceAssignedToProduct.price
        def inventoryItemDefined = newInventoryItemDefined(slug)
        def stockAmount = 17

        and:
        event productDefined

        and:
        event priceAssignedToProduct

        and:
        event inventoryItemDefined

        and:
        event new InventoryItemAddedToStock(inventoryItemDefined.id, Quantity.count(stockAmount))

        when:
        def products = queryPublishedProducts()

        then:
        products.size() == 1

        and:
        def publishedProduct = products[0]
        publishedProduct.slug == productDefined.slug.value
        publishedProduct.title == productDefined.title
        publishedProduct.description == productDefined.description
        publishedProduct.price != null
        publishedProduct.price.value == price.value
        publishedProduct.price.currency == price.currency
        publishedProduct.stockLevel == stockAmount
    }

    def "no product should be published when product with price is defined and then deleted"() {
        given:
        def productDefined = newProductDefined()

        and:
        event productDefined

        and:
        event newPriceAssignedToProduct(productDefined.id.value)

        and:
        event new ProductDeleted(productDefined.id)

        when:
        def products = queryPublishedProducts()

        then:
        products.empty
    }

    List<PublishedProduct> queryPublishedProducts() {
        query new QueryAllPublishedProducts(), ResponseTypes.multipleInstancesOf(PublishedProduct)
    }

    static InventoryItemDefined newInventoryItemDefined(slug = "the-matrix-trilogy") {
        new InventoryItemDefined(new InventoryItemIdentifier(), new Slug(slug))
    }

    static ProductDefined newProductDefined(slug = "the-matrix-trilogy") {
        new ProductDefined(new ProductIdentifier(), new Slug(slug), "$slug-title", "$slug-description")
    }

    static PriceAssignedToProduct newPriceAssignedToProduct(UUID id) {
        new PriceAssignedToProduct(
                new SalesProductIdentifier(id),
                new SalesPrice(17, "EUR")
        )
    }
}
