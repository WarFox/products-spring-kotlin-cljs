package io.github.warfox.app.domain

import java.util.UUID

data class Product(
    val productId: UUID,
    val name: String,
    val description: String,
    val price: Double,
)
