package kt.school.starlord.di

import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductWithMetadata
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.data.mapper.converter.DocumentToCategoriesWithSubcategoriesConverter
import kt.school.starlord.model.data.mapper.converter.DocumentToProductsListConverter
import kt.school.starlord.model.data.mapper.converter.ProductToUiProductConverter
import kt.school.starlord.model.data.mapper.converter.StringToInstantConverter
import kt.school.starlord.model.data.mapper.converter.StringToUrlConverter
import kt.school.starlord.model.data.mapper.entity.BaseConverter
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.koin.dsl.module

/**
 * Contains instructions of how to instantiate Mapper.
 */
val mapperModule = module {
    single {
        val converters = setOf(
            ProductToUiProductConverter(get()),
            DocumentToCategoriesWithSubcategoriesConverter(),
            DocumentToProductsListConverter(StringToInstantConverter()),
            StringToUrlConverter(),
            object : BaseConverter<RoomCategory, Category>(RoomCategory::class.java, Category::class.java) {
                override fun convert(value: RoomCategory) = Category(value.name)
            },
            object : BaseConverter<Category, RoomCategory>(Category::class.java, RoomCategory::class.java) {
                override fun convert(value: Category) =
                    RoomCategory(name = value.name)
            },
            object : BaseConverter<RoomSubcategory, Subcategory>(RoomSubcategory::class.java, Subcategory::class.java) {
                override fun convert(value: RoomSubcategory) =
                    Subcategory(
                        value.name,
                        value.categoryName,
                        value.count,
                        value.link
                    )
            },
            object : BaseConverter<Subcategory, RoomSubcategory>(Subcategory::class.java, RoomSubcategory::class.java) {
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
                        lastUpdate = value.lastUpdate,
                        commentsCount = value.commentsCount,
                        isPaid = value.isPaid
                    )
            },
            object : BaseConverter<ProductWithMetadata, RoomProduct>(
                ProductWithMetadata::class.java,
                RoomProduct::class.java
            ) {
                override fun convert(value: ProductWithMetadata): RoomProduct {
                    val product = value.product
                    return RoomProduct(
                        id = product.id,
                        subcategoryName = value.subcategoryName,
                        title = product.title,
                        description = product.description,
                        type = product.type,
                        location = product.location,
                        image = product.image,
                        owner = product.owner,
                        price = product.price,
                        lastUpdate = product.lastUpdate,
                        commentsCount = product.commentsCount,
                        isPaid = product.isPaid
                    )
                }
            }
        )

        Mapper(converters)
    }
}
