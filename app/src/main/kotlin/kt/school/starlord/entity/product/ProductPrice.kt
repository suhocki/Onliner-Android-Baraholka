package kt.school.starlord.entity.product

import com.squareup.moshi.JsonClass

/**
 * Product price describes how much costs selected product and determines if is bargaining available.
 */
@JsonClass(generateAdapter = true)
data class ProductPrice(
    val amount: Double? = null,
    val isBargainAvailable: Boolean = false
)
