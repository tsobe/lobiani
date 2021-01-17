package dev.baybay.lobiani.app.inventory

import dev.baybay.lobiani.app.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig)
class InventoryItemAPISpec extends Specification {

    private static final URI = "/api/inventory-items"
    private static final THE_MATRIX_TRILOGY = "the-matrix-trilogy"
    private static final MEMENTO = "memento"

    @Autowired
    TestRestTemplate restTemplate

    PollingConditions conditions = new PollingConditions(timeout: 5)

    def definedItemIds = []

    void cleanup() {
        definedItemIds.each { id ->
            deleteItem(id)
            conditions.call {
                getItemEntity(id).statusCode == HttpStatus.NOT_FOUND
            }
        }
    }

    def "defined item should be retrieved eventually"() {
        when:
        def response = defineItem(THE_MATRIX_TRILOGY)
        def id = response.body.id

        then:
        response.statusCode == HttpStatus.ACCEPTED

        and:
        assertItem(response.body)

        and:
        conditions.eventually {
            def r = getItemEntity(id)
            r.statusCode == HttpStatus.OK
            assertItem(r.body)
        }
    }

    def "NotFound is returned when item isn't defined"() {
        given:
        def undefinedItemId = UUID.randomUUID()

        when:
        def response = getItemEntity(undefinedItemId)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "defined items are retrieved eventually"() {
        given:
        itemDefined(THE_MATRIX_TRILOGY)

        expect:
        conditions.eventually {
            def response = getItemsEntity()
            response.statusCode == HttpStatus.OK
            def items = response.body
            items.size() == 1
            assertItem(items[0])
        }
    }

    def "empty result is returned when no items are defined"() {
        when:
        def response = getItemsEntity()

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    @Unroll
    def "BadRequest is returned for invalid slug '#slug'"() {
        when:
        def response = defineItem(slug)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Slug must consist of lowercase alpha-numeric and dash('-') characters"

        where:
        slug << ['Uppercase', 'space cowboy', 'blah#', 'meh?']
    }

    def "defined item is deleted"() {
        given:
        def id = itemDefined(THE_MATRIX_TRILOGY)

        when:
        deleteItem(id)

        then:
        conditions.eventually {
            getItemEntity(id).statusCode == HttpStatus.NOT_FOUND
        }
    }

    def "Accepted is returned when deleting undefined item"() {
        given:
        def undefinedItemId = UUID.randomUUID()

        when:
        def response = deleteItem(undefinedItemId)

        then:
        response.statusCode == HttpStatus.ACCEPTED
    }

    def "item is added to stock"() {
        given:
        def id = itemDefined(THE_MATRIX_TRILOGY)

        when:
        def response = addItemToStock(id, 10)

        then:
        response.statusCode == HttpStatus.ACCEPTED

        and:
        conditions.eventually {
            getItemEntity(id).body.stockLevel == 10
        }
    }

    @Unroll
    def "BadRequest is returned when #amount items are added to stock"() {
        given:
        def id = itemDefined(THE_MATRIX_TRILOGY)

        when:
        def response = addItemToStock(id, amount)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Amount must be a positive number"

        where:
        amount << [0, -10]
    }

    @Ignore
    def "BadRequest is returned when item with same slug is already defined"() {
        given:
        itemDefined(THE_MATRIX_TRILOGY)

        when:
        def response = defineItem(THE_MATRIX_TRILOGY)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Item with slug $THE_MATRIX_TRILOGY is already defined"
    }

    def "defined item should be retrieved by slug eventually"() {
        given:
        itemDefined(MEMENTO)
        itemDefined(THE_MATRIX_TRILOGY)

        expect:
        conditions.eventually {
            def response = getItemsEntityBySlug(THE_MATRIX_TRILOGY)
            response.statusCode == HttpStatus.OK
            def items = response.body
            items.size() == 1
            assertItem(items[0])
        }
    }

    def "empty result is returned when queried by slug and no items are defined"() {
        when:
        def response = getItemsEntityBySlug(THE_MATRIX_TRILOGY)

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    ResponseEntity<Object> defineItem(slug) {
        def response = restTemplate.postForEntity(URI, [slug: slug], Object)
        def id = response.body.id
        if (id) {
            definedItemIds.add(id)
        }
        return response
    }

    def itemDefined(slug) {
        defineItem(slug).body.id
    }

    ResponseEntity<Object> getItemEntity(id) {
        restTemplate.getForEntity("$URI/$id", Object)
    }

    ResponseEntity<List> getItemsEntity() {
        restTemplate.getForEntity(URI, List)
    }

    ResponseEntity<Object> getItemsEntityBySlug(slug) {
        restTemplate.getForEntity("$URI?slug=${slug}", Object)
    }

    ResponseEntity<Object> deleteItem(id) {
        restTemplate.exchange("$URI/$id", HttpMethod.DELETE, null, Object)
    }

    ResponseEntity<Object> addItemToStock(id, amount) {
        restTemplate.postForEntity("$URI/${id}/stock", [amount: amount], Object)
    }

    static void assertItem(item) {
        assert item.id != null
        assert item.slug == THE_MATRIX_TRILOGY
        assert item.stockLevel == 0
    }
}
