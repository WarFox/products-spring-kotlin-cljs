package io.github.warfox.app.repository

import io.github.warfox.app.BaseIntegrationTest
import io.github.warfox.app.fixtures.productArb
import io.github.warfox.app.repository.jooq.JooqProductRepository
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test

class JooqProductRegistryTest(private val productRegistry: JooqProductRepository): BaseIntegrationTest() {

    @Test
    fun `Save, get and list`() {
        Arb.productArb().next().let { product ->
            productRegistry.createOrUpdate(product) shouldBe 1
            productRegistry.getProduct(product.id) shouldBe product
            productRegistry.listProducts() shouldBe listOf(product)
        }
    }

}
