package dev.baybaydev.lobiani.testutil

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.util.concurrent.PollingConditions

class AdminAPITestClient {

    private static final INVENTORY_ITEMS_URI = "/api/inventory-items"
    private static final PRODUCTS_URI = "/api/products"

    private TestRestTemplate restTemplate

    PollingConditions conditions = new PollingConditions(timeout: 5)
    def definedItemIds = []
    def definedProductIds = []

    AdminAPITestClient(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    ResponseEntity<Object> defineItem(item) {
        def response = restTemplate.postForEntity INVENTORY_ITEMS_URI, item, Object
        def id = response.body.id
        if (id) {
            definedItemIds.add id
        }
        response
    }

    ResponseEntity<Object> getItemEntity(id) {
        restTemplate.getForEntity"$INVENTORY_ITEMS_URI/$id", Object
    }

    ResponseEntity<List> getItemsEntity() {
        restTemplate.getForEntity INVENTORY_ITEMS_URI, List
    }

    ResponseEntity<List> getItemsEntityBySlug(slug) {
        restTemplate.getForEntity"$INVENTORY_ITEMS_URI?slug=${slug}", List
    }

    ResponseEntity<Object> deleteItem(id) {
        restTemplate.exchange"$INVENTORY_ITEMS_URI/$id", HttpMethod.DELETE, null, Object
    }

    ResponseEntity<Object> addItemToStock(id, amount) {
        restTemplate.postForEntity"$INVENTORY_ITEMS_URI/${id}/stock", [amount: amount], Object
    }

    ResponseEntity<Object> defineProduct(product) {
        def response = restTemplate.postForEntity PRODUCTS_URI, product, Object
        def id = response.body.id
        if (id) {
            definedProductIds.add id
        }
        response
    }

    ResponseEntity<List> getProductsEntity() {
        restTemplate.getForEntity "$PRODUCTS_URI", List
    }

    ResponseEntity<List> getProductsEntityBySlug(slug) {
        restTemplate.getForEntity "$PRODUCTS_URI?slug=$slug", List
    }

    ResponseEntity<Object> getProductEntity(id) {
        restTemplate.getForEntity "$PRODUCTS_URI/$id", Object
    }

    ResponseEntity<Object> deleteProduct(id) {
        restTemplate.exchange "$PRODUCTS_URI/$id", HttpMethod.DELETE, null, Object
    }

    ResponseEntity<Void> assignPriceToProduct(id, price) {
        def priceEntity = new HttpEntity<Object>(price)
        restTemplate.exchange "$PRODUCTS_URI/$id/price", HttpMethod.PUT, priceEntity, Void
    }

    def cleanup() {
        deleteDefinedItems()
        deleteDefinedProducts()
    }

    private void deleteDefinedItems() {
        definedItemIds.each { id ->
            deleteItem id
            conditions.call {
                getItemEntity(id).statusCode == HttpStatus.NOT_FOUND
            }
        }
        definedItemIds.clear()
    }

    private void deleteDefinedProducts() {
        definedProductIds.forEach { id ->
            deleteProduct id
            conditions.call {
                getProductEntity(id).statusCode == HttpStatus.NOT_FOUND
            }
        }
        definedProductIds.clear()
    }
}
