package dev.baybay.lobiani.app.product

import dev.baybay.lobiani.app.common.Slug
import dev.baybay.lobiani.app.product.api.ProductDefined
import dev.baybay.lobiani.app.product.api.ProductIdentifier
import dev.baybay.lobiani.testutil.UpcasterSpec

class ProductDefinedUpcasterTest extends UpcasterSpec {

    void setup() {
        use new ProductDefinedNullTo1Upcaster()
    }

    def "should upcast null to 1"() {
        given:
        def id = "1c883d6b-e484-488a-b7ba-ff0886cbd452"
        def slug = "slug"
        def title = "title"
        def description = "description"
        def nullRevisionSerializedPayload = """
        <dev.baybay.lobiani.app.product.api.ProductDefined>
            <id>$id</id>
            <slug>$slug</slug>
            <title>$title</title>
            <description>$description</description>
        </dev.baybay.lobiani.app.product.api.ProductDefined>
        """
        def oldRevision = null
        def serializedEvent = new StringSerializedObject(
                nullRevisionSerializedPayload,
                ProductDefinedNullTo1Upcaster.serializedObjectType,
                oldRevision
        )

        when:
        ProductDefined upcasted = upcastSinglePayload serializedEvent

        then:
        upcasted.id == new ProductIdentifier(UUID.fromString(id))
        upcasted.slug == new Slug(slug)
        upcasted.title == title
        upcasted.description == description
    }
}
