package dev.baybay.lobiani.app.product.upcasting

import dev.baybay.lobiani.app.product.api.ProductDeleted
import org.axonframework.serialization.SimpleSerializedType
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation
import org.axonframework.serialization.upcasting.event.SingleEventUpcaster
import org.dom4j.Document
import org.dom4j.Element
import org.springframework.stereotype.Component

@Component
class ProductDeletedNullTo1Upcaster : SingleEventUpcaster() {

    companion object {
        @JvmStatic
        val serializedObjectType: String = ProductDeleted::class.java.typeName
        const val REVISION = "1"
    }

    private val type = SimpleSerializedType(serializedObjectType, null)

    override fun canUpcast(intermediateRepresentation: IntermediateEventRepresentation): Boolean {
        return intermediateRepresentation.type == type
    }

    override fun doUpcast(intermediateRepresentation: IntermediateEventRepresentation): IntermediateEventRepresentation {
        return intermediateRepresentation.upcastPayload(
            SimpleSerializedType(serializedObjectType, REVISION),
            Document::class.java,
        ) {
            replaceWithValue(it.rootElement, "id")
            it
        }
    }

    private fun replaceWithValue(rootElement: Element, name: String) {
        val element = rootElement.element(name)
        val value = element.data.toString()
        rootElement.remove(element)
        rootElement.addElement(name)
            .addElement("value")
            .text = value
    }
}
