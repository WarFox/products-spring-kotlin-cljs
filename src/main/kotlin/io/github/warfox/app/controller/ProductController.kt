package io.github.warfox.app.controller

import io.github.warfox.app.api.ProductControllerApi
import io.github.warfox.app.domain.toProductV1
import io.github.warfox.app.model.ProductV1
import io.github.warfox.app.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class ProductController(val service: ProductService): ProductControllerApi{
    override fun listProducts(): ResponseEntity<List<ProductV1>> {
        return service.listProducts().let {
            ResponseEntity.ok(it?.map { it.toProductV1() })
        }
    }

    override fun getProduct(id: UUID): ResponseEntity<ProductV1> {
        return ResponseEntity.ok(service.getProduct(id)?.toProductV1())
    }
}
