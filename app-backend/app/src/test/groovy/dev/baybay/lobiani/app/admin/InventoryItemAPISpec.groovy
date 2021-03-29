package dev.baybay.lobiani.app.admin

import dev.baybay.lobiani.testutil.APISpec
import dev.baybay.lobiani.testutil.AdminAPITestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

import static dev.baybay.lobiani.testutil.TestDataHelper.newInventoryItem

class InventoryItemAPISpec extends APISpec {

    @Autowired
    AdminAPITestClient apiTestClient

    PollingConditions conditions = new PollingConditions(timeout: 5)

    void cleanup() {
        apiTestClient.cleanup()
    }

    def "defined item should be retrieved eventually"() {
        given:
        def item = newInventoryItem()

        when:
        def response = apiTestClient.defineItem item
        def id = response.body.id

        then:
        response.statusCode == HttpStatus.OK

        and:
        assertDefinedItemHasSlug response.body, item.slug

        and:
        conditions.eventually {
            def definedItemResponse = apiTestClient.getItemEntity id
            definedItemResponse.statusCode == HttpStatus.OK
            assertDefinedItemHasSlug definedItemResponse.body, item.slug
        }
    }

    def "NotFound is returned when item isn't defined"() {
        given:
        def undefinedItemId = UUID.randomUUID()

        when:
        def response = apiTestClient.getItemEntity undefinedItemId

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "defined items are retrieved eventually"() {
        given:
        def item = newInventoryItem()

        and:
        itemDefined item

        expect:
        conditions.eventually {
            def response = apiTestClient.getItemsEntity()
            response.statusCode == HttpStatus.OK
            def definedItems = response.body
            definedItems.size() == 1
            assertDefinedItemHasSlug definedItems[0], item.slug
        }
    }

    def "empty result is returned when no items are defined"() {
        when:
        def response = apiTestClient.getItemsEntity()

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    @Unroll
    def "BadRequest is returned for invalid slug '#invalidSlug'"() {
        given:
        def invalidItem = newInventoryItem invalidSlug

        when:
        def response = apiTestClient.defineItem invalidItem

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Slug must consist of lowercase alpha-numeric and dash('-') characters"

        where:
        invalidSlug << ['Uppercase', 'space cowboy', 'blah#', 'meh?']
    }

    def "defined item is deleted"() {
        given:
        def item = newInventoryItem()

        and:
        def id = itemDefined item

        when:
        apiTestClient.deleteItem(id)

        then:
        conditions.eventually {
            apiTestClient.getItemEntity(id).statusCode == HttpStatus.NOT_FOUND
        }
    }

    def "NotFound is returned when deleting undefined item"() {
        given:
        def undefinedItemId = UUID.randomUUID()

        when:
        def response = apiTestClient.deleteItem undefinedItemId

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "item is added to stock"() {
        given:
        def item = newInventoryItem()

        and:
        def id = itemDefined item

        when:
        def response = apiTestClient.addItemToStock id, 10

        then:
        response.statusCode == HttpStatus.OK

        and:
        conditions.eventually {
            apiTestClient.getItemEntity(id).body.stockLevel == 10
        }
    }

    @Unroll
    def "BadRequest is returned when #invalidAmount items are added to stock"() {
        given:
        def item = newInventoryItem()

        and:
        def id = itemDefined item

        when:
        def response = apiTestClient.addItemToStock id, invalidAmount

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        and:
        response.body.message == "Amount must be a positive number"

        where:
        invalidAmount << [0, -10]
    }

    def "defined item should be retrieved by slug eventually"() {
        given:
        def memento = newInventoryItem "memento"
        def matrix = newInventoryItem "the-matrix-trilogy"

        and:
        itemDefined memento
        itemDefined matrix

        expect:
        conditions.eventually {
            def response = apiTestClient.getItemsEntityBySlug matrix.slug
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
        def response = apiTestClient.getItemsEntityBySlug undefinedItemSlug

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.body.empty
    }

    def itemDefined(item) {
        apiTestClient.defineItem(item).body.id
    }

    static void assertDefinedItemHasSlug(definedItem, slug) {
        assert definedItem.id != null
        assert definedItem.slug == slug
        assert definedItem.stockLevel == 0
    }
}
