package kt.school.starlord.model.repository.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.DataSource
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.global.RussianLocalizedTimePassed
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.CategoriesCacheRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.model.data.room.DaoManager
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
) : CategoriesCacheRepository, SubcategoriesRepository, ProductsCacheRepository {

    override fun getCategoriesLiveData(): LiveData<List<Category>> {
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

    override fun getProductsCached(subcategoryName: String): DataSource.Factory<Int, Product> =
        daoManager.productDao
            .getProducts(subcategoryName)
            .map { mapper.map<Product>(it) }

    override suspend fun updateProducts(subcategoryName: String, newProducts: List<Product>) {
        val productDao = daoManager.productDao
        val epochMilli = Instant.now().toEpochMilli()

        val oldProducts = productDao.getProductsByIds(newProducts.map { it.id }, subcategoryName)

        val productsToSave = newProducts.map { newProduct ->

            oldProducts.find { oldProduct -> oldProduct.id == newProduct.id }?.let { oldProduct ->

                val oldLocalizedTimePassed =
                    mapper.map<RussianLocalizedTimePassed>(epochMilli - oldProduct.lastUpdate)
                val newLocalizedTimePassed = RussianLocalizedTimePassed(newProduct.localizedTimePassed.value)

                if (oldProduct.lastUpdate >= newProduct.epochMilli ||
                    oldLocalizedTimePassed == newLocalizedTimePassed
                ) {
                    // old lastUpdate epoch millis should stay unchanged.
                    newProduct.epochMilli = oldProduct.lastUpdate
                }
            }

            mapper.map<RoomProduct>(newProduct).apply { this.subcategoryName = subcategoryName }
        }

        productDao.insertProducts(productsToSave)
    }
}
