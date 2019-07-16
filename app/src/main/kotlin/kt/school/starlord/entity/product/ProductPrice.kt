package kt.school.starlord.entity.product

/**
 * Product price describes how much costs selected product and determines if is bargaining available.
 */
data class ProductPrice(
    val amount: Double? = null,
    val isBargainAvailable: Boolean = false
)
