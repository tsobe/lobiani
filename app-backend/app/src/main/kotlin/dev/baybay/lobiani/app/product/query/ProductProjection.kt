package dev.baybay.lobiani.app.product.query

import dev.baybay.lobiani.app.product.api.*
import dev.baybay.lobiani.app.sales.model.PriceAssignedToProduct
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductProjection {

    private var products = mutableListOf<Product>()

    @EventHandler
    fun on(e: ProductDefined) {
        products.add(Product(e.id, e.slug, e.title, e.description))
    }

    @EventHandler
    fun on(e: ProductDeleted) {
        products.removeIf { it.id == e.id }
    }

    @EventHandler
    fun on(e: PriceAssignedToProduct) {
        products.find { it.id == e.id.value }?.price = e.price
    }

    @QueryHandler
    fun handle(q: QueryProductByID): Product? {
        return products.find { it.id == q.id }
    }

    @QueryHandler
    fun handle(q: QueryProductBySlug): Product? {
        return products.find { it.slug == q.slug }
    }

    @QueryHandler
    fun handle(q: QueryAllProducts): List<Product> {
        return products
    }
}
