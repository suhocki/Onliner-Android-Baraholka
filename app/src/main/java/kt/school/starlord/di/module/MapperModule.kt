package kt.school.starlord.di.module

import kt.school.starlord.di.Qualifier
import kt.school.starlord.model.mapper.Converter
import kt.school.starlord.model.mapper.Mapper
import kt.school.starlord.model.mapper.converter.CategoryToRoomCategoryConverter
import kt.school.starlord.model.mapper.converter.MatchResultToCategoryConverter
import kt.school.starlord.model.mapper.converter.MatchResultToSubcategoryConverter
import kt.school.starlord.model.mapper.converter.RoomCategoryToCategoryConverter
import kt.school.starlord.model.mapper.converter.RoomSubcategoryToSubcategoryConverter
import kt.school.starlord.model.mapper.converter.SubcategoryToRoomSubcategoryConverter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mapperModule = module {

    factory<Set<Converter<*, *>>>(named(Qualifier.CONVERTERS)) {
        setOf(
            RoomCategoryToCategoryConverter(),
            CategoryToRoomCategoryConverter(),
            RoomSubcategoryToSubcategoryConverter(),
            SubcategoryToRoomSubcategoryConverter(),
            MatchResultToCategoryConverter(),
            MatchResultToSubcategoryConverter()
        )
    }

    single {
        val converters = get<Set<Converter<*, *>>>(named(Qualifier.CONVERTERS))
        Mapper(converters)
    }
}