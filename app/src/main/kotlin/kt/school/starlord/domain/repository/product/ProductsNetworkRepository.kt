package kt.school.starlord.domain.repository.product

/**
 * Loads products presented in HTML format.
 */
interface ProductsNetworkRepository {

    /**
     * @return products in HTML format.
     */
    suspend fun downloadProductsPage(forumId: Long): String
}
