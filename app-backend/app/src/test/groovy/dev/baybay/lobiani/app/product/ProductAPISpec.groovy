package dev.baybay.lobiani.app.product

import dev.baybay.lobiani.app.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig)
class ProductAPISpec extends Specification {

    private static final URI = "/api/products"
    private static final SLUG = "the-matrix-trilogy"
    private static final TITLE = "The Matrix Trilogy"
    private static final DESCRIPTION = "Let's see how deep this Rabbit hole goes"
    private static final PRODUCT = [slug: SLUG, title: TITLE, description: DESCRIPTION]

    @Autowired
    TestRestTemplate restTemplate

    PollingConditions conditions = new PollingConditions(timeout: 5)

    def definedItemIds = []

    void cleanup() {
        definedItemIds.forEach { id ->
            deleteProduct id
            conditions.call {
                getProductEntity id == HttpStatus.NOT_FOUND
            }
        }
    }

    def "should return defined product eventually"() {
        when:
        def response = defineProduct PRODUCT
        def id = response.body.id

        then:
        response.statusCode == HttpStatus.OK

        and:
        conditions.eventually {
            def r = getProductEntity id
            r.statusCode == HttpStatus.OK
            assertProduct r.body
        }
    }

    def "should return NotFound when product with given id is not defined"() {
        given:
        def nonExistingID = UUID.randomUUID()

        when:
        def response = getProductEntity nonExistingID

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "product should not exist when deleted"() {
        given:
        def id = productDefined PRODUCT

        when:
        def response = deleteProduct id

        then:
        response.statusCode == HttpStatus.OK

        and:
        conditions.eventually {
            def r = getProductEntity id
            r.statusCode == HttpStatus.NOT_FOUND
        }
    }

    def "should return defined products eventually"() {
        given:
        productDefined PRODUCT

        expect:
        conditions.eventually {
            def r = getProductsEntity()
            r.statusCode == HttpStatus.OK
            def products = r.body
            products.size() == 1
            assertProduct products[0]
        }
    }

    def "should return empty result when no products are defined"() {
        when:
        def response = getProductsEntity()

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    @Unroll
    def "should return BadRequest for invalid slug '#slug'"() {
        when:
        def product = [slug: slug, title: TITLE, description: DESCRIPTION]
        def response = defineProduct product

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Slug must consist of lowercase alpha-numeric and dash('-') characters"

        where:
        slug << ['Uppercase', 'space cowboy', 'blah#', 'meh?']
    }

    ResponseEntity<Object> defineProduct(product) {
        def response = restTemplate.postForEntity URI, product, Object
        def id = response.body.id
        definedItemIds.add id
        return response
    }

    def productDefined(product) {
        def response = defineProduct PRODUCT
        response.body.id
    }

    ResponseEntity<List> getProductsEntity() {
        restTemplate.getForEntity "$URI", List
    }

    ResponseEntity<Object> getProductEntity(id) {
        restTemplate.getForEntity"$URI/$id", Object
    }

    ResponseEntity<Object> deleteProduct(id) {
        restTemplate.exchange("$URI/$id", HttpMethod.DELETE, null, Object)
    }

    static void assertProduct(product) {
        assert product.slug == SLUG
        assert product.title == TITLE
        assert product.description == DESCRIPTION
    }
}
