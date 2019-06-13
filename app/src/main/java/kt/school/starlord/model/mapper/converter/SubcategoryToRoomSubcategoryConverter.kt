package kt.school.starlord.model.mapper.converter

import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.BaseConverter
import kt.school.starlord.model.room.entity.RoomSubcategory

class SubcategoryToRoomSubcategoryConverter : BaseConverter<Subcategory, RoomSubcategory>(
    Subcategory::class.java,
    RoomSubcategory::class.java
) {

    override fun convert(value: Subcategory): RoomSubcategory {
        return RoomSubcategory(
            categoryName = value.requireCategoryName(),
            name = value.name,
            count = value.count,
            link = value.link
        )
    }
}
