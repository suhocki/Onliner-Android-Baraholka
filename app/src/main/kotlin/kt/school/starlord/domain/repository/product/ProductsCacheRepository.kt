package kt.school.starlord.domain.repository.product

import androidx.paging.DataSource
import kt.school.starlord.domain.entity.product.Product

/**
 * Defines access methods for products that is located in cache.
 */
interface ProductsCacheRepository {

    /**
     * Loads products live data from cache.
     */
    fun getCachedProducts(subcategoryId: Long): DataSource.Factory<Int, Product>

    /**
     * Deletes existing products of selected subcategoryId and puts a new ones.
     */
    suspend fun updateProducts(subcategoryId: Long, products: List<Product>)
}
