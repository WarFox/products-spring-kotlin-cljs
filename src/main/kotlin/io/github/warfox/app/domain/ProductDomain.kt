package io.github.warfox.app.domain

import io.github.warfox.app.model.ProductV1
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
    createdAt = createdAt,
    updatedAt = updatedAt
)
