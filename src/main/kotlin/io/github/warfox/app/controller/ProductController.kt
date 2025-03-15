package io.github.warfox.app.controller

import io.github.warfox.app.domain.toProductV1
import io.github.warfox.app.model.ProductV1
import io.github.warfox.app.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@Controller
class ProductController(val service: ProductService) {
    @RequestMapping("/products")
    fun listProducts(): ResponseEntity<List<ProductV1>> {
        return service.listProducts().let {
            ResponseEntity.ok(it?.map { it.toProductV1() })
        }
    }

    @RequestMapping("/products/{id}")
    fun getProduct(id: UUID): ResponseEntity<ProductV1> {
        return ResponseEntity.ok(service.getProduct(id)?.toProductV1())
    }

}
