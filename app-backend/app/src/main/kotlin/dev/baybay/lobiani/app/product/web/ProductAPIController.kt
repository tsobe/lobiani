package dev.baybay.lobiani.app.product.web

import dev.baybay.lobiani.app.product.api.DeleteProduct
import dev.baybay.lobiani.app.product.api.QueryAllProducts
import dev.baybay.lobiani.app.product.api.QueryProductByID
import dev.baybay.lobiani.app.product.api.QueryProductBySlug
import dev.baybay.lobiani.app.product.query.Product
import dev.baybay.lobiani.app.sales.command.api.AssignPriceToProduct
import dev.baybay.lobiani.app.sales.model.Price
import dev.baybay.lobiani.app.sales.model.ProductIdentifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@RestController
@RequestMapping("/api/products")
class ProductAPIController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

    @GetMapping
    fun getProducts(@RequestParam(required = false) slug: String?): List<Product> {
        if (slug != null) {
            return listOfNotNull(
                queryGateway.query(QueryProductBySlug(slug), ResponseTypes.instanceOf(Product::class.java)).get()
            )
        }
        return queryGateway.query(QueryAllProducts(), ResponseTypes.multipleInstancesOf(Product::class.java)).get()
    }

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: UUID): Product {
        return queryGateway.query(QueryProductByID(id.toString()), Product::class.java).get()
            ?: throw NoSuchElementException()
    }

    @PostMapping
    fun defineProduct(@RequestBody definition: ProductDefinition): Product {
        val defineProduct = definition.asCommand()
        commandGateway.sendAndWait<Void>(defineProduct)
        return Product(defineProduct.id.stringValue, defineProduct.slug.value, definition.title, definition.description)
    }

    @PutMapping("/{id}/price")
    fun assignPrice(@PathVariable id: UUID, @RequestBody price: Price) {
        commandGateway.sendAndWait<Void>(AssignPriceToProduct(ProductIdentifier(id), price))
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: UUID) {
        commandGateway.sendAndWait<Void>(DeleteProduct(id))
    }
}
