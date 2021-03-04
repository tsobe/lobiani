package dev.baybay.lobiani.app.common

import com.thoughtworks.xstream.XStream
import dev.baybay.lobiani.app.inventory.event.InventoryItemAddedToStock
import dev.baybay.lobiani.app.inventory.event.InventoryItemDefined
import dev.baybay.lobiani.app.inventory.event.InventoryItemDeleted
import dev.baybay.lobiani.app.marketing.event.ProductDefined
import dev.baybay.lobiani.app.marketing.event.ProductDeleted
import dev.baybay.lobiani.app.sales.event.PriceAssignedToProduct
import org.axonframework.serialization.RevisionResolver
import org.axonframework.serialization.SerialVersionUIDRevisionResolver
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.xml.CompactDriver
import org.axonframework.serialization.xml.XStreamSerializer
import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig : BeanClassLoaderAware {

    lateinit var classLoader: ClassLoader

    @Bean
    fun revisionResolver(): RevisionResolver {
        return SerialVersionUIDRevisionResolver()
    }

    override fun setBeanClassLoader(classLoader: ClassLoader) {
        this.classLoader = classLoader
    }

    @Bean
    fun eventSerializer(revisionResolver: RevisionResolver): Serializer {
        val xStream = XStream(CompactDriver())
        createAliasesForRefactoredEvents(xStream)
        xStream.classLoader = classLoader
        return XStreamSerializer.builder()
            .revisionResolver(revisionResolver)
            .xStream(xStream)
            .build()
    }

    private fun createAliasesForRefactoredEvents(xStream: XStream) {
        // Temporary workaround for backwards compatibility
        xStream.alias("dev.baybay.lobiani.app.product.api.ProductDefined", ProductDefined::class.java)
        xStream.alias("dev.baybay.lobiani.app.product.api.ProductDeleted", ProductDeleted::class.java)
        xStream.alias(
            "dev.baybay.lobiani.app.sales.model.ProductDefined",
            dev.baybay.lobiani.app.sales.event.ProductDefined::class.java
        )
        xStream.alias("dev.baybay.lobiani.app.sales.model.PriceAssignedToProduct", PriceAssignedToProduct::class.java)
        xStream.alias("dev.baybay.lobiani.app.inventory.api.InventoryItemDefined", InventoryItemDefined::class.java)
        xStream.alias("dev.baybay.lobiani.app.inventory.api.InventoryItemDeleted", InventoryItemDeleted::class.java)
        xStream.alias(
            "dev.baybay.lobiani.app.inventory.api.InventoryItemAddedToStock",
            InventoryItemAddedToStock::class.java
        )
    }
}
