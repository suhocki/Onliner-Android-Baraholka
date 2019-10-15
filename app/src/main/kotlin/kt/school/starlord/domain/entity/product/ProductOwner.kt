package kt.school.starlord.domain.entity.product

import com.squareup.moshi.JsonClass

/**
 * Entity that contains brief information about owner.
 */
@JsonClass(generateAdapter = true)
data class ProductOwner(
    val name: String,
    val id: Long
)
