package kt.school.starlord.di.module

import kt.school.starlord.di.Qualifier
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.model.data.mapper.converter.DocumentToCategoriesWithSubcategoriesConverter
import kt.school.starlord.model.data.mapper.converter.ElementToProductConverter
import kt.school.starlord.model.data.mapper.converter.ProductToUiEntityConverter
import kt.school.starlord.model.data.mapper.converter.RoomProductToProductConverter
import kt.school.starlord.model.data.mapper.converter.StringToUrlConverter
import kt.school.starlord.model.data.mapper.converter.localized.DoubleToLocalizedMoneyConverter
import kt.school.starlord.model.data.mapper.converter.localized.EpochMilliToLocalizedTimePassedConverter
import kt.school.starlord.model.data.mapper.converter.localized.EpochMilliToRussianLocalizedTimePassedConverter
import kt.school.starlord.model.data.mapper.converter.localized.LocalizedTimePassedToEpochMilliConverter
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

/**
 * Contains instructions of how to instantiate Mapper.
 */
val mapperModule = module {
    single {
        val epochMilliToLocalizedTimePassedConverter = EpochMilliToLocalizedTimePassedConverter(get())
        val epochMilliToRussianLocalizedTimePassedConverter = EpochMilliToLocalizedTimePassedConverter(
            get(Qualifier.LOCALIZED) { parametersOf(Locale("ru")) }
        )

        Mapper(
            setOf(
                EpochMilliToRussianLocalizedTimePassedConverter(epochMilliToRussianLocalizedTimePassedConverter),
                RoomProductToProductConverter(epochMilliToLocalizedTimePassedConverter),
                epochMilliToLocalizedTimePassedConverter,
                ProductToUiEntityConverter(
                    epochMilliToLocalizedTimePassedConverter,
                    DoubleToLocalizedMoneyConverter(get())
                ),
                DocumentToCategoriesWithSubcategoriesConverter(),
                ElementToProductConverter(
                    LocalizedTimePassedToEpochMilliConverter()
                ),
                StringToUrlConverter(),
                object : BaseConverter<RoomCategory, Category>(RoomCategory::class.java, Category::class.java) {
                    override fun convert(value: RoomCategory) = Category(value.name)
                },
                object : BaseConverter<Category, RoomCategory>(Category::class.java, RoomCategory::class.java) {
                    override fun convert(value: Category) = RoomCategory(value.name)
                },
                object :
                    BaseConverter<RoomSubcategory, Subcategory>(RoomSubcategory::class.java, Subcategory::class.java) {
                    override fun convert(value: RoomSubcategory) =
                        Subcategory(value.name, value.categoryName, value.count, value.link)
                },
                object :
                    BaseConverter<Subcategory, RoomSubcategory>(Subcategory::class.java, RoomSubcategory::class.java) {
                    override fun convert(value: Subcategory) =
                        RoomSubcategory(value.name, value.categoryName, value.count, value.link)
                },
                object : BaseConverter<Product, RoomProduct>(Product::class.java, RoomProduct::class.java) {
                    override fun convert(value: Product): RoomProduct {
                        return RoomProduct(
                            id = value.id,
                            subcategoryName = value.subcategoryName,
                            title = value.title,
                            description = value.description,
                            type = value.type,
                            location = value.location,
                            image = value.image,
                            owner = value.owner,
                            price = value.price,
                            lastUpdate = value.lastUpdate.epochMilli.value,
                            commentsCount = value.commentsCount,
                            isPaid = value.isPaid
                        )
                    }
                }
            )
        )
    }
}
