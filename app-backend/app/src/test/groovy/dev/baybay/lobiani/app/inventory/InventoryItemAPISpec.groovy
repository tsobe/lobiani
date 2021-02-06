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

    @Autowired
    TestRestTemplate restTemplate

    PollingConditions conditions = new PollingConditions(timeout: 5)

    def definedItemIds = []

    void cleanup() {
        definedItemIds.each { id ->
            deleteItem id
            conditions.call {
                getItemEntity(id).statusCode == HttpStatus.NOT_FOUND
            }
        }
    }

    def "defined item should be retrieved eventually"() {
        given:
        def item = newItem()

        when:
        def response = defineItem item
        def id = response.body.id

        then:
        response.statusCode == HttpStatus.ACCEPTED

        and:
        assertDefinedItemHasSlug response.body, item.slug

        and:
        conditions.eventually {
            def definedItemResponse = getItemEntity id
            definedItemResponse.statusCode == HttpStatus.OK
            assertDefinedItemHasSlug definedItemResponse.body, item.slug
        }
    }

    def "NotFound is returned when item isn't defined"() {
        given:
        def undefinedItemId = UUID.randomUUID()

        when:
        def response = getItemEntity undefinedItemId

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "defined items are retrieved eventually"() {
        given:
        def item = newItem()

        and:
        itemDefined item

        expect:
        conditions.eventually {
            def response = getItemsEntity()
            response.statusCode == HttpStatus.OK
            def definedItems = response.body
            definedItems.size() == 1
            assertDefinedItemHasSlug definedItems[0], item.slug
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
    def "BadRequest is returned for invalid slug '#invalidSlug'"() {
        given:
        def invalidItem = newItem invalidSlug

        when:
        def response = defineItem invalidItem

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Slug must consist of lowercase alpha-numeric and dash('-') characters"

        where:
        invalidSlug << ['Uppercase', 'space cowboy', 'blah#', 'meh?']
    }

    def "defined item is deleted"() {
        given:
        def item = newItem()

        and:
        def id = itemDefined item

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
        def response = deleteItem undefinedItemId

        then:
        response.statusCode == HttpStatus.ACCEPTED
    }

    def "item is added to stock"() {
        given:
        def item = newItem()

        and:
        def id = itemDefined item

        when:
        def response = addItemToStock id, 10

        then:
        response.statusCode == HttpStatus.ACCEPTED

        and:
        conditions.eventually {
            getItemEntity(id).body.stockLevel == 10
        }
    }

    @Unroll
    def "BadRequest is returned when #invalidAmount items are added to stock"() {
        given:
        def item = newItem()

        and:
        def id = itemDefined item

        when:
        def response = addItemToStock id, invalidAmount

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Amount must be a positive number"

        where:
        invalidAmount << [0, -10]
    }

    @Ignore
    def "BadRequest is returned when item with same slug is already defined"() {
        given:
        def item = newItem()

        and:
        itemDefined item

        when:
        def response = defineItem item

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Item with slug ${item.slug} is already defined"
    }

    def "defined item should be retrieved by slug eventually"() {
        given:
        def memento = newItem "memento"
        def matrix = newItem "the-matrix-trilogy"

        and:
        itemDefined memento
        itemDefined matrix

        expect:
        conditions.eventually {
            def response = getItemsEntityBySlug matrix.slug
            response.statusCode == HttpStatus.OK
            def definedItems = response.body
            definedItems.size() == 1
            assertDefinedItemHasSlug definedItems[0], matrix.slug
        }
    }

    def "empty result is returned when queried by slug and no items are defined"() {
        given:
        def undefinedItemSlug = "the-matrix-trilogy"

        when:
        def response = getItemsEntityBySlug undefinedItemSlug

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    static def newItem(slug = "the-matrix-trilogy") {
        return [slug: slug]
    }

    ResponseEntity<Object> defineItem(item) {
        def response = restTemplate.postForEntity URI, item, Object
        def id = response.body.id
        if (id) {
            definedItemIds.add id
        }
        response
    }

    def itemDefined(item) {
        defineItem(item).body.id
    }

    ResponseEntity<Object> getItemEntity(id) {
        restTemplate.getForEntity"$URI/$id", Object
    }

    ResponseEntity<List> getItemsEntity() {
        restTemplate.getForEntity URI, List
    }

    ResponseEntity<List> getItemsEntityBySlug(slug) {
        restTemplate.getForEntity"$URI?slug=${slug}", List
    }

    ResponseEntity<Object> deleteItem(id) {
        restTemplate.exchange"$URI/$id", HttpMethod.DELETE, null, Object
    }

    ResponseEntity<Object> addItemToStock(id, amount) {
        restTemplate.postForEntity"$URI/${id}/stock", [amount: amount], Object
    }

    static void assertDefinedItemHasSlug(definedItem, slug) {
        assert definedItem.id != null
        assert definedItem.slug == slug
        assert definedItem.stockLevel == 0
    }
}
