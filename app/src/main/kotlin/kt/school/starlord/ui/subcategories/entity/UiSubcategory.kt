package kt.school.starlord.ui.subcategories.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kt.school.starlord.ui.global.annotation.Visibility

/**
 * Subcategory entity for UI layer.
 */
@Parcelize
data class UiSubcategory(
    val name: String,
    val count: String,
    val id: Long,
    @Visibility val countVisibility: Int
) : Parcelable
