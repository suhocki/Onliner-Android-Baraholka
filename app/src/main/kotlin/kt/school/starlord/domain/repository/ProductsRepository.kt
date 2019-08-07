package kt.school.starlord.domain.repository

import androidx.lifecycle.LiveData
import kt.school.starlord.entity.product.Product

/**
 * Defines access methods for products.
 */
interface ProductsRepository {

    /**
     * Loads data that represents products by subcategory name.
     *
     * @return products
     */
    fun getProducts(subcategoryName: String): LiveData<List<Product>>

    /**
     * Deletes existing products of selected subcategoryName and puts a new ones.
     */
    suspend fun updateProducts(subcategoryName: String, products: List<Product>)
}
