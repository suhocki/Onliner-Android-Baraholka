package kt.school.starlord.entity.subcategory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Entity that appears on subcategories screen.
 */
@Parcelize
data class Subcategory(
    val name: String,
    var categoryName: String,
    val count: Int,
    val link: String
) : Parcelable
