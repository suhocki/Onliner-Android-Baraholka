package kt.school.starlord.domain.entity.product

import com.squareup.moshi.JsonClass

/**
 * Product price describes how much costs selected product and determines if is bargaining available.
 */
@JsonClass(generateAdapter = true)
data class Price(
    val amount: Double,
    val hasPrice: Boolean,
    val isBargainAvailable: Boolean
)
