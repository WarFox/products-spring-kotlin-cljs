package io.github.warfox.app.domain

import io.github.warfox.app.model.ProductV1
import io.github.warfox.app.model.ProductV1Request
import java.time.Instant
import java.util.UUID

data class Product(
    val id: UUID,
    val name: String,
    val description: String,
    val priceInCents: Long,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun Product.toProductV1() = ProductV1(
    id = id,
    name = name,
    description = description,
    priceInCents = priceInCents,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)

fun ProductV1Request.toDomain() = Product(
    id = id ?: UUID.randomUUID(),
    name = name,
    description = description ?: "",
    priceInCents = priceInCents,
    createdAt = Instant.now(),
    updatedAt = Instant.now()
)
