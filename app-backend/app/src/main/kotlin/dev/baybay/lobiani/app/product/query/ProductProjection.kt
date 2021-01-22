package dev.baybay.lobiani.app.product.query

import dev.baybay.lobiani.app.product.api.ProductDefined
import dev.baybay.lobiani.app.product.api.ProductDeleted
import dev.baybay.lobiani.app.product.api.QueryAllProducts
import dev.baybay.lobiani.app.product.api.QueryProductByID
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

    @QueryHandler
    fun handle(q: QueryProductByID): Product? {
        return products.find { it.id == q.id }
    }

    @QueryHandler
    fun handle(q: QueryAllProducts): List<Product> {
        return products
    }
}
