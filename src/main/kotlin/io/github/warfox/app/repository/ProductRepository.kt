package io.github.warfox.app.repository

import io.github.warfox.app.domain.Product
import java.util.UUID

interface ProductRepository {
    fun getProduct(productId: UUID): Product?
    fun listProducts(): List<Product>?
    fun createOrUpdate(product: Product): Int
}
