package dev.baybay.lobiani.app.product.command

import dev.baybay.lobiani.app.product.api.DefineProduct
import dev.baybay.lobiani.app.product.api.DeleteProduct
import dev.baybay.lobiani.app.product.api.ProductDefined
import dev.baybay.lobiani.app.product.api.ProductDeleted
import dev.baybaydev.lobiani.testutil.AggregateSpec

class ProductSpec extends AggregateSpec {

    def id = UUID.randomUUID()
    def slug = "the-matrix-trilogy"
    def title = "The Matrix Trilogy"
    def description = "This is Matrix"

    void setup() {
        useAggregate Product
    }

    def "should define new product"() {
        when:
        actingWith new DefineProduct(id, slug, title, description)

        then:
        expectSuccess()

        and:
        expectEvent new ProductDefined(id, slug, title, description)
    }

    def "should delete defined product"() {
        given:
        pastEvent new ProductDefined(id, slug, title, description)

        when:
        actingWith new DeleteProduct(id)

        then:
        expectSuccess()

        and:
        expectEvent new ProductDeleted(id)
    }
}
