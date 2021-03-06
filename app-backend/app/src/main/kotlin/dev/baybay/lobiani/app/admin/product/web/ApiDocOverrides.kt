package dev.baybay.lobiani.app.admin.product.web

import dev.baybay.lobiani.app.sales.Price
import io.swagger.v3.oas.models.media.NumberSchema
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.StringSchema
import org.springdoc.core.SpringDocUtils
import org.springframework.stereotype.Component

/**
 * A place to override API documentation for domain objects without polluting them with OpenAPI annotations
 */
@Component
class ApiDocOverrides {

    companion object {
        init {
            SpringDocUtils.getConfig().replaceWithSchema(
                Price::class.java, ObjectSchema()
                    .name("Price")
                    .addProperties("value", NumberSchema().example(17))
                    .addProperties("currency", StringSchema().example("EUR"))
            )
        }
    }
}
