package kt.school.starlord.model.data.mapper.converter

import android.view.View
import kt.school.starlord.domain.mapper.BaseConverter
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.ui.subcategories.entity.UiSubcategory

/**
 * Converts Subcategory entity from domain layer to Subcategory entity from UI layer.
 */
class SubcategoryToUiSubcategoryConverter :
    BaseConverter<Subcategory, UiSubcategory>(Subcategory::class, UiSubcategory::class) {

    override fun convert(value: Subcategory): UiSubcategory {
        return UiSubcategory(
            name = value.name,
            count = value.count?.toString() ?: EMPTY_STRING,
            id = value.id,
            countVisibility = if (value.count == null) View.GONE else View.VISIBLE
        )
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}
