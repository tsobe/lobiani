package dev.baybay.lobiani.app.admin

import dev.baybay.lobiani.testutil.APISpec
import dev.baybay.lobiani.testutil.AdminAPITestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

import static dev.baybay.lobiani.testutil.TestDataHelper.newProduct

class ProductAPISpec extends APISpec {

    @Autowired
    AdminAPITestClient apiTestClient

    PollingConditions conditions = new PollingConditions(timeout: 5)

    def definedProductIds = []

    void cleanup() {
        apiTestClient.cleanup()
    }

    def "should return defined product eventually"() {
        given:
        def product = newProduct "the-matrix-trilogy"

        when:
        def response = apiTestClient.defineProduct product
        def id = response.body.id

        then:
        response.statusCode == HttpStatus.OK

        and:
        conditions.eventually {
            def definedProductResponse = apiTestClient.getProductEntity id
            definedProductResponse.statusCode == HttpStatus.OK
            assertProduct definedProductResponse.body, product
        }
    }

    def "should return NotFound when product with given id is not defined"() {
        given:
        def nonExistingID = UUID.randomUUID()

        when:
        def response = apiTestClient.getProductEntity nonExistingID

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "product should not exist when deleted"() {
        given:
        def product = newProduct()

        and:
        def id = productDefined product

        when:
        def response = apiTestClient.deleteProduct id

        then:
        response.statusCode == HttpStatus.OK

        and:
        conditions.eventually {
            apiTestClient.getProductEntity(id).statusCode == HttpStatus.NOT_FOUND
        }
    }

    def "should return NotFound when deleting undefined product"() {
        given:
        def undefinedProductId = UUID.randomUUID()

        when:
        def response = apiTestClient.deleteProduct undefinedProductId

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "should return defined products eventually"() {
        given:
        def product = newProduct()

        and:
        productDefined product

        expect:
        conditions.eventually {
            def response = apiTestClient.getProductsEntity()
            response.statusCode == HttpStatus.OK
            def definedProducts = response.body
            definedProducts.size() == 1
            assertProduct definedProducts[0], product
        }
    }

    def "should return empty result when no products are defined"() {
        when:
        def response = apiTestClient.getProductsEntity()

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    @Unroll
    def "should return BadRequest for invalid slug '#invalidSlug'"() {
        when:
        def invalidProduct = newProduct invalidSlug

        and:
        def response = apiTestClient.defineProduct invalidProduct

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Slug must consist of lowercase alpha-numeric and dash('-') characters"

        where:
        invalidSlug << ['Uppercase', 'space cowboy', 'blah#', 'meh?']
    }

    def "should retrieve defined item by slug eventually"() {
        given:
        def matrix = newProduct "the-matrix-trilogy"
        def memento = newProduct "memento"

        and:
        productDefined matrix
        productDefined memento

        expect:
        conditions.eventually {
            def response = apiTestClient.getProductsEntityBySlug matrix.slug
            response.statusCode == HttpStatus.OK
            def definedProducts = response.body
            definedProducts.size() == 1
            assertProduct definedProducts[0], matrix
        }
    }

    def "should return empty list when queried by slug and no products are defined"() {
        given:
        def undefinedProductSlug = "the-matrix-trilogy"

        when:
        def response = apiTestClient.getProductsEntityBySlug undefinedProductSlug

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    def "should have price when assigned"() {
        given:
        def product = newProduct()

        and:
        def id = productDefined product

        and:
        def price = [value: 17, currency: "EUR"]

        when:
        def response = apiTestClient.assignPriceToProduct id, price

        then:
        response.statusCode == HttpStatus.OK

        and:
        conditions.eventually {
            def definedProductResponse = apiTestClient.getProductEntity id
            definedProductResponse.statusCode == HttpStatus.OK
            def definedProduct = definedProductResponse.body
            definedProduct.price == price
        }
    }

    def productDefined(product) {
        apiTestClient.defineProduct(product).body.id
    }

    static void assertProduct(actual, expected) {
        assert actual.id != null
        assert actual.slug == expected.slug
        assert actual.title == expected.title
        assert actual.description == expected.description
    }
}
