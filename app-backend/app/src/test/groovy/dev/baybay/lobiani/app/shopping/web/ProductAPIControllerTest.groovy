package dev.baybay.lobiani.app.shopping.web

import dev.baybay.lobiani.app.shopping.Price
import dev.baybay.lobiani.app.shopping.PublishedProduct
import dev.baybay.lobiani.app.shopping.query.QueryAllPublishedProducts
import org.axonframework.queryhandling.QueryGateway
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class ProductAPIControllerTest extends Specification {

    QueryGateway queryGateway = Stub()

    def "should return products when QueryBus returns results"() {
        given:
        def id = "id"
        def slug = "slug"
        def title = "title"
        def description = "description"
        def stockLevel = 17
        def price = new Price(new BigDecimal(17), Currency.getInstance("EUR"))
        def product = new PublishedProduct(id, slug, title, description, stockLevel, price)

        and:
        queryGateway.query(_ as QueryAllPublishedProducts, _) >> CompletableFuture.completedFuture([product])

        and:
        def controller = new ProductAPIController(queryGateway)

        when:
        def products = controller.getProducts()

        then:
        products.size() == 1

        and:
        products[0] == product
    }
}
