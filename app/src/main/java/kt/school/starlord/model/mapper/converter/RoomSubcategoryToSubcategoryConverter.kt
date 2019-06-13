package kt.school.starlord.model.mapper.converter

import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.BaseConverter
import kt.school.starlord.model.room.entity.RoomSubcategory

class RoomSubcategoryToSubcategoryConverter : BaseConverter<RoomSubcategory, Subcategory>(
    RoomSubcategory::class.java,
    Subcategory::class.java
) {

    override fun convert(value: RoomSubcategory): Subcategory {
        return Subcategory(value.name, count = value.count, link = value.link)
    }
}
