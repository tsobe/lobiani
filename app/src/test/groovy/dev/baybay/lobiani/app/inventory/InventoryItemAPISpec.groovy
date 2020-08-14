package dev.baybay.lobiani.app.inventory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryItemAPISpec extends Specification {

    private static final URI = "/api/inventory-items"
    private static final SLUG = "the-matrix-trilogy"

    @Autowired
    TestRestTemplate restTemplate

    @Shared
    GenericContainer container = new GenericContainer("axoniq/axonserver")
            .withExposedPorts(8024, 8124)
            .waitingFor(Wait.forHttp("/actuator/info").forPort(8024))
            .withStartupTimeout(Duration.ofSeconds(30))

    def id

    void setupSpec() {
        def port = container.getMappedPort(8124)
        System.setProperty("axon.axonserver.servers", "${container.getHost()}:$port")
    }

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

    @Unroll
    def "BadRequest is returned when #count items are added to stock"() {
        given:
        itemDefined()

        when:
        def response = addItemToStock(id, count)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        where:
        count << [0, -10]
    }

    ResponseEntity<Object> defineItem() {
        restTemplate.postForEntity(URI, [slug: SLUG], Object)
    }

    def itemDefined() {
        id = restTemplate.postForObject(URI, [slug: SLUG], Object).id
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
        restTemplate.postForEntity("$URI/$id/stock", [count: count], Object)
    }

    static void assertItem(item) {
        assert item.id != null
        assert item.slug == SLUG
        assert item.stockLevel == 0
    }
}
