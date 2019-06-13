package kt.school.starlord.model.mapper.converter

import kt.school.starlord.entity.Category
import kt.school.starlord.model.mapper.BaseConverter
import kt.school.starlord.model.room.entity.RoomCategory

class CategoryToRoomCategoryConverter : BaseConverter<Category, RoomCategory>(
    Category::class.java,
    RoomCategory::class.java
) {

    override fun convert(value: Category): RoomCategory {
        return RoomCategory(name = value.name)
    }
}
