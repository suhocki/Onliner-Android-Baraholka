package kt.school.starlord.model.mapper.converter

import kt.school.starlord.entity.Category
import kt.school.starlord.model.mapper.BaseConverter
import kt.school.starlord.model.room.entity.RoomCategory

class RoomCategoryToCategoryConverter : BaseConverter<RoomCategory, Category>(
    RoomCategory::class.java,
    Category::class.java
) {

    override fun convert(value: RoomCategory): Category {
        return Category(value.name)
    }
}

