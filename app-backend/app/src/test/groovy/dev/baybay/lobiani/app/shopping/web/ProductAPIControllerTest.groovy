package dev.baybay.lobiani.app.shopping.web

import dev.baybay.lobiani.app.shopping.Price
import dev.baybay.lobiani.app.shopping.PublishedProduct
import dev.baybay.lobiani.app.shopping.query.QueryAllPublishedProducts
import org.axonframework.commandhandling.CommandBus
import org.axonframework.queryhandling.QueryGateway
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ProductAPIController)
class ProductAPIControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc
    @SpringBean
    CommandBus commandBus = Mock()
    @SpringBean
    QueryGateway queryGateway = Stub()

    def "products endpoint should be publicly accessible"() {
        given:
        queryGateway.query(_ as QueryAllPublishedProducts, _) >> CompletableFuture.completedFuture([])

        when:
        def result = mockMvc.perform(get("/shopping/api/products"))

        then:
        result.andExpect(status().isOk())
    }

    def "should return products when QueryBus returns results"() {
        given:
        def slug = "slug"
        def title = "title"
        def description = "description"
        def stockLevel = 17
        def priceVal = 17
        def currency = "EUR"
        def price = new Price(new BigDecimal(priceVal), Currency.getInstance(currency))
        def product = new PublishedProduct(slug, title, description, stockLevel, price)

        and:
        queryGateway.query(_ as QueryAllPublishedProducts, _) >> CompletableFuture.completedFuture([product])

        when:
        def result = mockMvc.perform(get("/shopping/api/products"))

        then:
        result.andExpect(jsonPath("\$.length()").value(1))
                .andExpect(jsonPath("\$[0].slug").value(slug))
                .andExpect(jsonPath("\$[0].title").value(title))
                .andExpect(jsonPath("\$[0].description").value(description))
                .andExpect(jsonPath("\$[0].stockLevel").value(stockLevel))
                .andExpect(jsonPath("\$[0].price.value").value(priceVal))
                .andExpect(jsonPath("\$[0].price.currency").value(currency))
    }
}
