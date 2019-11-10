package kt.school.starlord.domain.entity.subcategory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Entity that appears on subcategories screen.
 *
 * @param link unique part of Url for navigation to selected subcategory products
 */
@Parcelize
data class Subcategory(
    val name: String,
    val count: Int,
    val link: String,
    var categoryName: String? = null
) : Parcelable
