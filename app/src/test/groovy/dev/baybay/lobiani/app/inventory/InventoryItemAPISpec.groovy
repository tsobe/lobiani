package dev.baybay.lobiani.app.inventory


import dev.baybay.lobiani.app.TestAxonConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Ignore
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestAxonConfig)
class InventoryItemAPISpec extends Specification {

    private static final URI = "/api/inventory-items"
    private static final SLUG = "the-matrix-trilogy"

    @Autowired
    TestRestTemplate restTemplate

    def id

    void cleanup() {
        id && deleteItem(id)
    }

    def "item is defined"() {
        when:
        def response = defineItem()
        def item = response.body
        id = item.id

        then:
        response.statusCode.is2xxSuccessful()
        assertItem(item)
    }

    def "defined item is retrieved"() {
        given:
        itemDefined()

        when:
        def response = getItemEntity(id)
        def item = response.body

        then:
        response.statusCode.is2xxSuccessful()
        assertItem(item)
    }

    def "NotFound is returned when item isn't defined"() {
        given:
        def undefinedItemId = UUID.randomUUID()

        when:
        def response = getItemEntity(undefinedItemId)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "defined items are retrieved"() {
        given:
        itemDefined()

        when:
        def response = getItemsEntity()
        def items = response.body

        then:
        response.statusCode.is2xxSuccessful()
        items.size() == 1
        assertItem(items[0])
    }

    def "empty result is returned when item isn't defined"() {
        when:
        def response = getItemsEntity()
        def items = response.body

        then:
        response.statusCode.is2xxSuccessful()
        items.empty
    }

    def "defined item is deleted"() {
        given:
        itemDefined()

        when:
        deleteItem(id)

        then:
        getItemEntity(id).statusCode == HttpStatus.NOT_FOUND
    }

    @Ignore
    def "NotFound is returned when deleting undefined item"() {
        given:
        def undefinedItemId = UUID.randomUUID()

        when:
        def response = deleteItem(undefinedItemId)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "item is added to stock"() {
        given:
        itemDefined()

        when:
        def response = addItemToStock(id, 10)

        then:
        response.statusCode.is2xxSuccessful()

        when:
        response = getItemEntity(id)
        def item = response.body

        then:
        response.statusCode.is2xxSuccessful()
        item.stockLevel == 10
    }

    ResponseEntity<Object> defineItem() {
        restTemplate.postForEntity(URI, SLUG, Object)
    }

    def itemDefined() {
        id = restTemplate.postForObject(URI, SLUG, Object).id
    }

    ResponseEntity<Object> getItemEntity(id) {
        restTemplate.getForEntity("$URI/$id", Object)
    }

    ResponseEntity<List> getItemsEntity() {
        restTemplate.getForEntity(URI, List)
    }

    ResponseEntity<Object> deleteItem(id) {
        restTemplate.exchange("$URI/$id", HttpMethod.DELETE, null, Object)
    }

    ResponseEntity<Object> addItemToStock(id, count) {
        restTemplate.postForEntity("$URI/$id/stock", count, Object)
    }

    static void assertItem(item) {
        assert item.id != null
        assert item.slug == SLUG
        assert item.stockLevel == 0
    }
}
