package dev.baybay.lobiani.app.product.web

import dev.baybay.lobiani.app.product.api.DefineProduct
import dev.baybay.lobiani.app.product.api.DeleteProduct
import dev.baybay.lobiani.app.product.api.QueryAllProducts
import dev.baybay.lobiani.app.product.api.QueryProductByID
import dev.baybay.lobiani.app.product.query.Product
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@RestController
@RequestMapping("/api/products")
class ProductAPIController(private val commandGateway: CommandGateway,
                           private val queryGateway: QueryGateway) {

    @GetMapping
    fun getProducts(): List<Product> {
        return queryGateway.query(QueryAllProducts(), ResponseTypes.multipleInstancesOf(Product::class.java)).get()
    }

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: UUID): Product {
        return queryGateway.query(QueryProductByID(id), Product::class.java).get()
                ?: throw NoSuchElementException()
    }

    @PostMapping
    fun defineProduct(@RequestBody defineProduct: DefineProduct): Product {
        commandGateway.sendAndWait<Void>(defineProduct)
        return Product(defineProduct.id, defineProduct.slug, defineProduct.title, defineProduct.description)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: UUID) {
        commandGateway.sendAndWait<Void>(DeleteProduct(id))
    }
}
