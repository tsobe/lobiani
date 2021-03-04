package dev.baybay.lobiani.app.sales

import dev.baybay.lobiani.app.sales.command.AssignPriceToProduct
import dev.baybay.lobiani.app.sales.command.DefineProduct
import dev.baybay.lobiani.app.sales.event.PriceAssignedToProduct
import dev.baybay.lobiani.app.sales.event.ProductDefined
import dev.baybay.lobiani.testutil.AggregateSpec

class SalesProductSpec extends AggregateSpec {

    void setup() {
        useAggregate SalesProduct
    }

    def "should define product"() {
        given:
        def id = new ProductIdentifier()

        when:
        actingWith new DefineProduct(id)

        then:
        expectSuccess()

        and:
        expectEvent new ProductDefined(id)
    }

    def "should assign price"() {
        given:
        def id = new ProductIdentifier()

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
