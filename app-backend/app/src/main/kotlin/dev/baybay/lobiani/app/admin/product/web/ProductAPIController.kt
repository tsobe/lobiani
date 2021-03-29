package dev.baybay.lobiani.app.admin.product.web

import dev.baybay.lobiani.app.admin.product.Product
import dev.baybay.lobiani.app.admin.product.query.QueryAllProducts
import dev.baybay.lobiani.app.admin.product.query.QueryProductByID
import dev.baybay.lobiani.app.admin.product.query.QueryProductBySlug
import dev.baybay.lobiani.app.marketing.command.DeleteProduct
import dev.baybay.lobiani.app.sales.Price
import dev.baybay.lobiani.app.sales.ProductIdentifier
import dev.baybay.lobiani.app.sales.command.AssignPriceToProduct
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException
import dev.baybay.lobiani.app.marketing.ProductIdentifier as MarketingProductIdentifier

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
        commandGateway.sendAndWait<Void>(DeleteProduct(MarketingProductIdentifier(id)))
    }
}
