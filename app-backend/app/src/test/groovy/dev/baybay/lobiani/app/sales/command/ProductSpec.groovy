package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.command.api.AssignPriceToProduct
import dev.baybay.lobiani.app.sales.command.api.DefineProduct
import dev.baybay.lobiani.app.sales.model.Price
import dev.baybay.lobiani.app.sales.model.PriceAssignedToProduct
import dev.baybay.lobiani.app.sales.model.ProductDefined
import dev.baybay.lobiani.app.sales.model.ProductIdentifier
import dev.baybay.lobiani.testutil.AggregateSpec

class ProductSpec extends AggregateSpec {

    void setup() {
        useAggregate SalesProduct
    }

    def "should define product"() {
        given:
        def id = new ProductIdentifier(UUID.randomUUID())

        when:
        actingWith new DefineProduct(id)

        then:
        expectSuccess()

        and:
        expectEvent new ProductDefined(id)
    }

    def "should assign price"() {
        given:
        def id = new ProductIdentifier(UUID.randomUUID())

        and:
        pastEvent new ProductDefined(id)

        and:
        def price = new Price(17, "EUR")

        when:
        actingWith new AssignPriceToProduct(id, price)

        then:
        expectSuccess()

        and:
        expectEvent new PriceAssignedToProduct(id, price)
    }
}
