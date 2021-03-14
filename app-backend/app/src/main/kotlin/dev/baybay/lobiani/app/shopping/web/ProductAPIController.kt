package dev.baybay.lobiani.app.shopping.web

import dev.baybay.lobiani.app.shopping.PublishedProduct
import dev.baybay.lobiani.app.shopping.query.QueryAllPublishedProducts
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("ShoppingProductAPIController")
@RequestMapping("/shopping/api/products")
class ProductAPIController(private val queryGateway: QueryGateway) {

    @GetMapping
    fun getProducts(): List<PublishedProduct> {
        return queryGateway.query(
            QueryAllPublishedProducts(), ResponseTypes.multipleInstancesOf(PublishedProduct::class.java)
        ).get()
    }
}
