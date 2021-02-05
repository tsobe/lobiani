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

    @Autowired
    TestRestTemplate restTemplate

    PollingConditions conditions = new PollingConditions(timeout: 5)

    def definedProductIds = []

    void cleanup() {
        definedProductIds.forEach { id ->
            deleteProduct id
            conditions.call {
                getProductEntity id == HttpStatus.NOT_FOUND
            }
        }
    }

    def "should return defined product eventually"() {
        given:
        def product = newProduct "the-matrix-trilogy"

        when:
        def response = defineProduct product
        def id = response.body.id

        then:
        response.statusCode == HttpStatus.OK

        and:
        conditions.eventually {
            def r = getProductEntity id
            r.statusCode == HttpStatus.OK
            assertProduct r.body, product
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
        def product = newProduct "the-matrix-trilogy"
        def id = productDefined product

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
        def product = newProduct "the-matrix-trilogy"
        productDefined product

        expect:
        conditions.eventually {
            def r = getProductsEntity()
            r.statusCode == HttpStatus.OK
            def products = r.body
            products.size() == 1
            assertProduct products[0], product
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
        def product = newProduct slug
        def response = defineProduct product

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Slug must consist of lowercase alpha-numeric and dash('-') characters"

        where:
        slug << ['Uppercase', 'space cowboy', 'blah#', 'meh?']
    }

    def "should retrieve defined item by slug eventually"() {
        given:
        def product = newProduct "the-matrix-trilogy"
        def anotherProduct = newProduct "memento"
        productDefined product
        productDefined anotherProduct

        expect:
        conditions.eventually {
            def response = getProductsEntityBySlug product.slug
            response.statusCode == HttpStatus.OK
            def products = response.body
            products.size() == 1
            assertProduct products[0], product
        }
    }

    def "should return empty list when queried by slug and no products are defined"() {
        when:
        def response = getProductsEntityBySlug "the-matrix-trilogy"

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    static def newProduct(slug) {
        [slug       : slug,
         title      : "$slug-title",
         description: "$slug-description"]
    }

    ResponseEntity<Object> defineProduct(product) {
        def response = restTemplate.postForEntity URI, product, Object
        def id = response.body.id
        definedProductIds.add id
        response
    }

    def productDefined(product) {
        def response = defineProduct product
        response.body.id
    }

    ResponseEntity<List> getProductsEntity() {
        restTemplate.getForEntity "$URI", List
    }

    ResponseEntity<List> getProductsEntityBySlug(slug) {
        restTemplate.getForEntity "$URI?slug=$slug", List
    }

    ResponseEntity<Object> getProductEntity(id) {
        restTemplate.getForEntity "$URI/$id", Object
    }

    ResponseEntity<Object> deleteProduct(id) {
        restTemplate.exchange "$URI/$id", HttpMethod.DELETE, null, Object
    }

    static void assertProduct(actual, expected) {
        assert actual.id != null
        assert actual.slug == expected.slug
        assert actual.title == expected.title
        assert actual.description == expected.description
    }
}
