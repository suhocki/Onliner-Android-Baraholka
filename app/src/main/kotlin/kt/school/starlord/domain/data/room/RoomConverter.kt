package kt.school.starlord.domain.data.room

import androidx.room.TypeConverter

/**
 * Base type converter for Room library.
 * Type parameter TO needs to be suitable Room database primitive.
 */
interface RoomConverter<FROM, TO> {
    /**
     * Converts input value to json.
     */
    @TypeConverter
    fun toRoomType(value: FROM): TO

    /**
     * Converts json to object.
     */
    @TypeConverter
    fun fromRoomType(value: TO): FROM?
}
