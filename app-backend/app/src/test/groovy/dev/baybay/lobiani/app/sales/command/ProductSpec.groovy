package dev.baybay.lobiani.app.sales.command

import dev.baybay.lobiani.app.sales.command.api.AssignPriceToProduct
import dev.baybay.lobiani.app.sales.command.api.DefineProductInSales
import dev.baybay.lobiani.app.sales.model.Price
import dev.baybay.lobiani.app.sales.model.PriceAssignedToProduct
import dev.baybay.lobiani.app.sales.model.ProductDefinedInSales
import dev.baybay.lobiani.testutil.AggregateSpec

class ProductSpec extends AggregateSpec {

    void setup() {
        useAggregate SalesProduct
    }

    def "should define product"() {
        given:
        def id = UUID.randomUUID()
        def marketingProductID = UUID.randomUUID()

        when:
        actingWith new DefineProductInSales(id, marketingProductID)

        then:
        expectSuccess()

        and:
        expectEvent new ProductDefinedInSales(id, marketingProductID)
    }

    def "should assign price"() {
        given:
        def id = UUID.randomUUID()
        def marketingProductID = UUID.randomUUID()

        and:
        pastEvent new ProductDefinedInSales(id, marketingProductID)

        and:
        def price = new Price(17, "EUR")

        when:
        actingWith new AssignPriceToProduct(id, price)

        then:
        expectSuccess()

        and:
        expectEvent new PriceAssignedToProduct(id, marketingProductID, price)
    }
}
