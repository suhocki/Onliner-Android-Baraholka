package kt.school.starlord.di

import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.global.TimeUnit
import kt.school.starlord.domain.entity.global.Timestamp
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.model.data.mapper.converter.DocumentToCategoriesWithSubcategoriesConverter
import kt.school.starlord.model.data.mapper.converter.ElementToProductConverter
import kt.school.starlord.model.data.mapper.converter.ProductToUiEntityConverter
import kt.school.starlord.model.data.mapper.converter.StringToTimestamp
import kt.school.starlord.model.data.mapper.converter.StringToUrlConverter
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.model.data.mapper.converter.DoubleToLocalizedMoneyConverter
import kt.school.starlord.model.data.mapper.converter.LongToLocalizedTimePassedConverter
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.koin.dsl.module

/**
 * Contains instructions of how to instantiate Mapper.
 */
val mapperModule = module {
    single {
        Mapper(
            setOf(
                ProductToUiEntityConverter(
                    LongToLocalizedTimePassedConverter(get()),
                    DoubleToLocalizedMoneyConverter(get())
                ),
                DocumentToCategoriesWithSubcategoriesConverter(),
                ElementToProductConverter(StringToTimestamp()),
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
                object : BaseConverter<RoomProduct, Product>(RoomProduct::class.java, Product::class.java) {
                    override fun convert(value: RoomProduct) =
                        Product(
                            id = value.id,
                            title = value.title,
                            description = value.description,
                            type = value.type,
                            location = value.location,
                            image = value.image,
                            owner = value.owner,
                            price = value.price,
                            timestamp = Timestamp(value.lastUpdate, TimeUnit.fromMillis(value.updateInterval)),
                            commentsCount = value.commentsCount,
                            isPaid = value.isPaid,
                            subcategoryName = value.subcategoryName
                        )
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
                            lastUpdate = value.timestamp.time,
                            updateInterval = value.timestamp.interval.millis,
                            commentsCount = value.commentsCount,
                            isPaid = value.isPaid
                        )
                    }
                }
            )
        )
    }
}
