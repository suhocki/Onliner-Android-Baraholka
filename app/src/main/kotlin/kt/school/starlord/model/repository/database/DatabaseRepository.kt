package kt.school.starlord.model.repository.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.DataSource
import kt.school.starlord.domain.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.global.RussianLocalizedTimePassed
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.category.CategoriesCacheRepository
import kt.school.starlord.domain.repository.category.SubcategoriesCacheRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.model.data.room.DaoManager
import kt.school.starlord.model.data.room.dao.ProductDao
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.threeten.bp.Instant

/**
 * Controls Room database.
 */
class DatabaseRepository(
    private val daoManager: DaoManager,
    private val mapper: Mapper
) : CategoriesCacheRepository,
    SubcategoriesCacheRepository, ProductsCacheRepository {

    override fun getCategories(): LiveData<List<Category>> {
        return daoManager.categoryDao.getCategories().map { roomCategories ->
            roomCategories.map { mapper.map<Category>(it) }
        }
    }

    override suspend fun updateCategories(categories: List<Category>) {
        val roomCategories = categories.map { mapper.map<RoomCategory>(it) }
        daoManager.categoryDao.replaceAll(roomCategories)
    }

    override fun getSubcategories(categoryName: String): LiveData<List<Subcategory>> {
        return daoManager.subcategoryDao.getSubcategories(categoryName).map { roomSubcategories ->
            roomSubcategories.map { mapper.map<Subcategory>(it) }
        }
    }

    override suspend fun updateSubcategories(subcategories: List<Subcategory>) {
        val roomSubcategories = subcategories.map { mapper.map<RoomSubcategory>(it) }
        daoManager.subcategoryDao.replaceAll(roomSubcategories)
    }

    override fun getCachedProducts(subcategoryId: Long): DataSource.Factory<Int, Product> =
        daoManager.productDao
            .getProductsSortedByUpdate(subcategoryId)
            .map { mapper.map<Product>(it) }

    override suspend fun updateProducts(subcategoryId: Long, products: List<Product>) {
        val productDao = daoManager.productDao
        val cachedProductsByIds = productDao.getProductsByIds(products.map { it.id }, subcategoryId)
        val epochMilli = Instant.now().toEpochMilli()

        productDao.insertProducts(
            products
                .onEach { product -> fixUpProductData(product, subcategoryId, cachedProductsByIds, epochMilli) }
                .map { mapper.map<RoomProduct>(it) }
                .toMutableList()
                .apply {
                    if (databaseHasGap(productDao, subcategoryId, products)) {
                        val lastUpdate = maxBy { it.lastUpdate }!!.lastUpdate - 1 // -1 ms, to put after previous lastUpdate.
                        add(RoomProduct.createLoadMore(subcategoryId, lastUpdate))
                    } else {
                        productDao.deleteProductById(RoomProduct.ID_LOAD_MORE)
                    }
                }
        )
    }

    private suspend fun databaseHasGap(
        productDao: ProductDao,
        subcategoryId: Long,
        products: List<Product>
    ) = productDao.getMaxLastUpdate(subcategoryId)?.let { it < products.last().lastUpdate } ?: false


    private fun fixUpProductData(
        product: Product,
        subcategoryId: Long,
        cachedProducts: List<RoomProduct>,
        epochMilli: Long
    ) {
        product.subcategoryId = subcategoryId

        cachedProducts.find { cachedProduct -> cachedProduct.id == product.id }
            ?.let { cachedProduct ->
                if (mapper.map<RussianLocalizedTimePassed>(epochMilli - cachedProduct.lastUpdate) ==
                    RussianLocalizedTimePassed(product.localizedTimePassed.value)
                ) {
                    product.lastUpdate = cachedProduct.lastUpdate
                }
            }
    }
}
