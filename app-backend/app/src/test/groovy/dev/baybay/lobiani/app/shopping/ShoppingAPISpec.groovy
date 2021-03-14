package dev.baybay.lobiani.app.shopping

import dev.baybay.lobiani.testutil.APISpec
import dev.baybaydev.lobiani.testutil.AdminAPITestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static dev.baybay.lobiani.testutil.TestDataHelper.newInventoryItem
import static dev.baybay.lobiani.testutil.TestDataHelper.newProduct

class ShoppingAPISpec extends APISpec {

    private static final URI = "/shopping/api/products"

    @Autowired
    TestRestTemplate restTemplate
    @Autowired
    AdminAPITestClient adminAPITestClient

    void cleanup() {
        adminAPITestClient.cleanup()
    }

    def "product should not be published when only inventory item is defined"() {
        given:
        def inventoryItem = newInventoryItem()

        and:
        itemDefined inventoryItem

        when:
        def responseEntity = getProductsEntity()

        then:
        responseEntity.statusCode == HttpStatus.OK

        and:
        def publishedProducts = responseEntity.body
        publishedProducts.empty
    }

    def "product should not be published when inventory item and product without price are defined"() {
        given:
        def slug = "the-matrix-trilogy"
        def inventoryItem = newInventoryItem slug
        def product = newProduct slug

        and:
        itemDefined inventoryItem
        productDefined product

        when:
        def responseEntity = getProductsEntity()

        then:
        responseEntity.statusCode == HttpStatus.OK

        and:
        def publishedProducts = responseEntity.body
        publishedProducts.empty
    }

    def "product should be published and out of stock when inventory item and product with price are defined"() {
        given:
        def slug = "the-matrix-trilogy"
        def inventoryItem = newInventoryItem slug
        def product = newProduct slug
        def price = [value: 17, currency: "EUR"]

        and:
        itemDefined inventoryItem
        def productId = productDefined product
        priceAssignedToProduct productId, price

        when:
        def responseEntity = getProductsEntity()

        then:
        responseEntity.statusCode == HttpStatus.OK

        and:
        def publishedProducts = responseEntity.body
        publishedProducts.size() == 1

        and:
        def publishedProduct = publishedProducts[0]
        publishedProduct.slug == slug
        publishedProduct.price == price
        publishedProduct.stockLevel == 0
    }

    def "product should not be published when inventory item and product with price are defined and then deleted"() {
        given:
        def slug = "the-matrix-trilogy"
        def inventoryItem = newInventoryItem slug
        def product = newProduct slug
        def price = [value: 17, currency: "EUR"]

        and:
        itemDefined inventoryItem
        def productId = productDefined product
        priceAssignedToProduct productId, price

        and:
        productDeleted(productId)

        when:
        def responseEntity = getProductsEntity()

        then:
        responseEntity.statusCode == HttpStatus.OK

        and:
        def publishedProducts = responseEntity.body
        publishedProducts.empty
    }

    def "product should be published and in stock when inventory item with stock and product with price are defined"() {
        given:
        def slug = "athe-matrix-trilogy"
        def inventoryItem = newInventoryItem slug
        def product = newProduct slug
        def price = [value: 17, currency: "EUR"]
        def stockAmount = 17

        and:
        def itemId = itemDefined inventoryItem
        itemAddedToStock(itemId, stockAmount)
        def productId = productDefined product
        priceAssignedToProduct productId, price

        when:
        def responseEntity = getProductsEntity()

        then:
        responseEntity.statusCode == HttpStatus.OK

        and:
        def publishedProducts = responseEntity.body
        publishedProducts.size() == 1

        and:
        def publishedProduct = publishedProducts[0]
        publishedProduct.slug == slug
        publishedProduct.price == price
        publishedProduct.stockLevel == stockAmount
    }

    private def itemDefined(LinkedHashMap<String, String> inventoryItem) {
        return adminAPITestClient.defineItem(inventoryItem).body.id
    }

    private ResponseEntity<Object> itemAddedToStock(itemId, amount) {
        adminAPITestClient.addItemToStock itemId, amount
    }

    private def productDefined(LinkedHashMap<String, String> product) {
        adminAPITestClient.defineProduct(product).body.id
    }

    private def priceAssignedToProduct(productId, price) {
        adminAPITestClient.assignPriceToProduct productId, price
    }

    private def productDeleted(productId) {
        adminAPITestClient.deleteProduct productId
    }

    private ResponseEntity<List> getProductsEntity() {
        restTemplate.getForEntity URI, List
    }
}
