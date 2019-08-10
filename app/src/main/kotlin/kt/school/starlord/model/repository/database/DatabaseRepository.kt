package kt.school.starlord.model.repository.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.repository.CategoriesCacheRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.entity.category.Category
import kt.school.starlord.entity.product.Product
import kt.school.starlord.entity.product.ProductWithMetadata
import kt.school.starlord.entity.subcategory.Subcategory
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.data.room.DaoManager
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory

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

    override fun getProductsLiveData(subcategoryName: String): LiveData<List<Product>> {
        return daoManager.productDao.getProducts(subcategoryName, BuildConfig.PAGE_SIZE).map { roomProducts ->
            roomProducts.map { mapper.map<Product>(it) }
        }
    }

    override suspend fun updateProducts(subcategoryName: String, products: List<Product>) {
        val roomProducts = products.mapIndexed { position, product ->
            val productWithMetadata = ProductWithMetadata(product, subcategoryName, position)
            mapper.map<RoomProduct>(productWithMetadata)
        }
        daoManager.productDao.replaceAll(subcategoryName, roomProducts)
    }
}
