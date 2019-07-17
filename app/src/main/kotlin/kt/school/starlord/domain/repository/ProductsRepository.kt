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
}
