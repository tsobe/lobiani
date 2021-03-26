package dev.baybay.lobiani.app.admin.product

import dev.baybay.lobiani.app.marketing.event.*
import dev.baybay.lobiani.app.admin.product.query.QueryAllProducts
import dev.baybay.lobiani.app.admin.product.query.QueryProductByID
import dev.baybay.lobiani.app.admin.product.query.QueryProductBySlug
import dev.baybay.lobiani.app.sales.event.PriceAssignedToProduct
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductProjection {

    private var products = mutableListOf<Product>()

    @EventHandler
    fun on(e: ProductDefined) {
        products.add(Product(e.id.stringValue, e.slug.value, e.title, e.description))
    }

    @EventHandler
    fun on(e: ProductDeleted) {
        products.removeIf { it.id == e.id.stringValue }
    }

    @EventHandler
    fun on(e: PriceAssignedToProduct) {
        products.find { it.id == e.productId.stringValue }?.price = e.price
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
