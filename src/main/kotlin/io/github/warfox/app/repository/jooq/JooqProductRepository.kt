package io.github.warfox.app.repository.jooq

import io.github.warfox.app.domain.Product
import io.github.warfox.app.repository.ProductRepository
import org.jooq.DSLContext
import org.jooq.generated.Tables
import org.jooq.generated.tables.records.ProductsRecord
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JooqProductRepository(val dslContext: DSLContext): ProductRepository {
    override fun getProduct(productId: UUID): Product? {
        Tables.PRODUCTS.let {
            return dslContext.selectFrom(it)
                .where(it.PRODUCT_ID.eq(productId))
                .fetchOne { it.toDomain() }
        }
    }

    override fun listProducts(): List<Product>? {
        Tables.PRODUCTS.let { product ->
            return dslContext.selectFrom(product)
                .fetch { it.toDomain() }
        }
    }

    override fun createOrUpdate(product: Product): Int  =
        with(Tables.PRODUCTS) {
            dslContext.insertInto(this)
                .set(this.PRODUCT_ID, product.id)
                .set(this.NAME, product.name)
                .set(this.DESCRIPTION, product.description)
                .set(this.PRICE_IN_CENTS, product.priceInCents)
                .set(this.CREATED_AT, product.createdAt)
                .set(this.UPDATED_AT, product.updatedAt)
                .onDuplicateKeyUpdate()
                .set(this.UPDATED_AT, product.updatedAt)
                .execute()
        }
    }

fun ProductsRecord.toDomain(): Product {
    return Product(
        id = this.productId,
        name = this.name,
        description = this.description,
        priceInCents = this.priceInCents,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
