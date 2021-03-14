package dev.baybay.lobiani.app.shopping

import dev.baybay.lobiani.app.inventory.event.InventoryItemAddedToStock
import dev.baybay.lobiani.app.inventory.event.InventoryItemDefined
import dev.baybay.lobiani.app.marketing.event.ProductDefined
import dev.baybay.lobiani.app.marketing.event.ProductDeleted
import dev.baybay.lobiani.app.sales.event.PriceAssignedToProduct
import dev.baybay.lobiani.app.shopping.query.QueryAllPublishedProducts
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component("shoppingProductProjection")
class ProductProjection {

    private val products = mutableListOf<Product>()

    @EventHandler
    fun on(e: ProductDefined) {
        products.find { it.slug == e.slug.value }?.apply {
            id = e.id.stringValue
            slug = e.slug.value
            title = e.title
            description = e.description
        }
    }

    @EventHandler
    fun on(e: PriceAssignedToProduct) {
        products.find { it.id == e.id.stringValue }?.price = Price(e.price.value, e.price.currency)
    }

    @EventHandler
    fun on(e: InventoryItemDefined) {
        products.add(Product().apply {
            slug = e.slug
            inventoryItemId = e.id.toString()
        })
    }

    @EventHandler
    fun on(e: InventoryItemAddedToStock) {
        products.find { it.inventoryItemId == e.inventoryItemId.toString() }?.apply {
            stockLevel += e.quantity.value
        }
    }

    @EventHandler
    fun on(e: ProductDeleted) {
        products.removeIf { it.id == e.id.stringValue }
    }

    @QueryHandler
    fun handle(q: QueryAllPublishedProducts): List<Product> {
        return products.filter { it.published }
    }
}
