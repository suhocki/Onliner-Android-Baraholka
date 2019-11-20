package kt.school.starlord.di.module

import kt.school.starlord.di.Qualifier
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.model.data.mapper.converter.ProductToUiEntityConverter
import kt.school.starlord.model.data.mapper.converter.RoomProductToProductConverter
import kt.school.starlord.model.data.mapper.converter.StringToUrlConverter
import kt.school.starlord.model.data.mapper.converter.SubcategoryToUiSubcategoryConverter
import kt.school.starlord.model.data.mapper.converter.element.ElementToCategoryConverter
import kt.school.starlord.model.data.mapper.converter.element.ElementToPriceConverter
import kt.school.starlord.model.data.mapper.converter.element.ElementToProductConverter
import kt.school.starlord.model.data.mapper.converter.element.ElementToSubcategoryConverter
import kt.school.starlord.model.data.mapper.converter.localization.LocalizedTimePassedToLongConverter
import kt.school.starlord.model.data.mapper.converter.localization.LongToLocalizedTimePassedConverter
import kt.school.starlord.model.data.mapper.converter.localization.LongToRussianLocalizedTimePassedConverter
import kt.school.starlord.model.data.mapper.converter.localization.PriceToLocalizedMoneyConverter
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import java.util.Locale

/**
 * Contains instructions of how to instantiate Mapper.
 */
val mapperModule = module {
    single {
        Mapper(
            setOf(
                // elements
                ElementToCategoryConverter(),
                ElementToSubcategoryConverter(),
                ElementToPriceConverter(),
                ElementToProductConverter(),

                // localization
                LongToLocalizedTimePassedConverter(get()),
                LongToRussianLocalizedTimePassedConverter(get(Qualifier.LOCALIZED) { parametersOf(Locale("ru")) }),
                PriceToLocalizedMoneyConverter(get()),
                LocalizedTimePassedToLongConverter(),

                // between layers
                RoomProductToProductConverter(),
                SubcategoryToUiSubcategoryConverter(),
                ProductToUiEntityConverter(),

                StringToUrlConverter(),

                object : BaseConverter<RoomCategory, Category>(RoomCategory::class, Category::class) {
                    override fun convert(value: RoomCategory) = Category(value.name)
                },
                object : BaseConverter<Category, RoomCategory>(Category::class, RoomCategory::class) {
                    override fun convert(value: Category) = RoomCategory(value.name)
                },
                object : BaseConverter<RoomSubcategory, Subcategory>(RoomSubcategory::class, Subcategory::class) {
                    override fun convert(value: RoomSubcategory) =
                        Subcategory(value.name, value.count, value.link, value.categoryName)
                },
                object : BaseConverter<Subcategory, RoomSubcategory>(Subcategory::class, RoomSubcategory::class) {
                    override fun convert(value: Subcategory) =
                        RoomSubcategory(
                            name = value.name,
                            categoryName = value.categoryName ?: TODO("Add a category."),
                            count = value.count,
                            link = value.link
                        )
                },
                object : BaseConverter<Product, RoomProduct>(Product::class, RoomProduct::class) {
                    override fun convert(value: Product): RoomProduct {
                        return RoomProduct(
                            id = value.id,
                            subcategoryName = value.subcategoryName ?: TODO("Add a subcategory."),
                            title = value.title,
                            description = value.description,
                            type = value.type,
                            location = value.location,
                            image = value.image,
                            owner = value.owner,
                            price = value.price,
                            lastUpdate = value.lastUpdate,
                            commentsCount = value.commentsCount,
                            isPaid = value.isPaid
                        )
                    }
                }
            )
        )
    }
}
