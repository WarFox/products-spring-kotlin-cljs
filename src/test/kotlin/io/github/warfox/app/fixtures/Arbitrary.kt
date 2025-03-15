package io.github.warfox.app.fixtures

import io.github.warfox.app.domain.Product
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.positiveLong
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import io.kotest.property.arbs.name
import java.time.Instant
import java.time.temporal.ChronoUnit

fun Arb.Companion.instantInPast(): Arb<Instant> = Arb.instant(
    maxValue = Instant.now().minusSeconds(3600),
    minValue = Instant.now().minus(30, ChronoUnit.DAYS)
).map { it.truncatedTo(ChronoUnit.SECONDS) }

fun Arb.Companion.productArb() = arbitrary {
    Product(
        id = Arb.uuid().bind(),
        name = Arb.name().map { it.toString() }.bind(),
        description = Arb.string().bind(),
        priceInCents = Arb.positiveLong().bind(),
        createdAt = instantInPast().bind(),
        updatedAt = instantInPast().bind()
    )
}
