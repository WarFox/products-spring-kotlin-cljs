package io.github.warfox.app.service

import io.github.warfox.app.domain.Product
import io.github.warfox.app.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface ProductService {
    fun getProduct(productId: UUID): Product?
    fun listProducts(): List<Product>?
    fun createProduct(product: Product) : Product?
    fun deleteProduct(productId: UUID): Unit?
}

@Service
@Transactional(readOnly = true)
class DefaultProductService(val repository: ProductRepository) : ProductService {
    override fun getProduct(productId: UUID): Product? {
        return repository.getProduct(productId)
    }

    override fun listProducts(): List<Product>? {
        return repository.listProducts()
    }

    @Transactional(readOnly = false)
    override fun createProduct(product: Product): Product? {
        return when(repository.createOrUpdate(product)) {
            1 -> product
            else -> null
        }
    }

    @Transactional(readOnly = false)
    override fun deleteProduct(productId: UUID): Unit? {
        return when(repository.deleteProduct(productId)) {
            1 -> Unit
            else -> null
        }
    }
}
