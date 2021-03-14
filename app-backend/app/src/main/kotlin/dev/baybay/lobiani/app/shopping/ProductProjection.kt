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
        getProduct(e.slug.value).apply {
            id = e.id.stringValue
            slug = e.slug.value
            title = e.title
            description = e.description
        }
    }

    @EventHandler
    fun on(e: PriceAssignedToProduct) {
        findById(e.id.stringValue)?.price = Price(e.price.value, e.price.currency)
    }

    @EventHandler
    fun on(e: InventoryItemDefined) {
        getProduct(e.slug).apply {
            slug = e.slug
            inventoryItemId = e.id.toString()
        }
    }

    @EventHandler
    fun on(e: InventoryItemAddedToStock) {
        findByInventoryItemId(e.inventoryItemId.toString())?.apply {
            stockLevel += e.quantity.value
        }
    }

    @EventHandler
    fun on(e: ProductDeleted) {
        products.removeIf { it.id == e.id.stringValue }
    }

    @QueryHandler
    fun handle(q: QueryAllPublishedProducts): List<PublishedProduct> {
        return products
            .filter { it.published }
            .map {
                PublishedProduct(it.id!!, it.slug!!, it.title!!, it.description!!, it.stockLevel, it.price!!)
            }
    }

    private fun getProduct(slug: String): Product {
        var product = products.find { it.slug == slug }
        if (product == null) {
            product = Product()
            products.add(product)
        }
        return product
    }

    private fun findById(id: String) =
        products.find { it.id == id }

    private fun findByInventoryItemId(id: String) =
        products.find { it.inventoryItemId == id }
}
