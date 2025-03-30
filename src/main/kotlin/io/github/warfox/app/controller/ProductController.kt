package io.github.warfox.app.controller

import io.github.warfox.app.api.ProductControllerApi
import io.github.warfox.app.domain.toDomain
import io.github.warfox.app.domain.toProductV1
import io.github.warfox.app.model.ProductV1
import io.github.warfox.app.model.ProductV1Request
import io.github.warfox.app.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import java.net.URI
import java.util.UUID

@Controller
@CrossOrigin(origins = arrayOf("http://localhost:8280"))
class ProductController(val service: ProductService) : ProductControllerApi {
    override fun listProducts(): ResponseEntity<List<ProductV1>> {
        return service.listProducts().let {
            ResponseEntity.ok(it?.map { it.toProductV1() })
        }
    }

    override fun createProduct(productV1Request: ProductV1Request): ResponseEntity<ProductV1> {
        return service.createProduct(productV1Request.toDomain()).let { product ->
            when (product) {
                null -> ResponseEntity.internalServerError().build()
                else -> ResponseEntity.created(URI("/products/${product.id}")).body<ProductV1>(product.toProductV1())
            }
        }
    }

    override fun getProduct(id: UUID): ResponseEntity<ProductV1> {
        return service.getProduct(id).let{
            when(it) {
                null -> ResponseEntity.notFound().build()
                else -> ResponseEntity.ok(it.toProductV1())
            }
        }
    }

    override fun deleteProduct(id: UUID): ResponseEntity<Unit> {
        return service.deleteProduct(id).let {
            when(it) {
                null -> ResponseEntity.notFound().build()
                else -> ResponseEntity.noContent().build()
            }
        }
    }
}
