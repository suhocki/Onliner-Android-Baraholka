package kt.school.starlord.domain.repository.category

/**
 * Loads categories with subcategories presented in HTML format.
 */
interface CategoriesNetworkRepository {

    /**
     * @return categories and subcategories in HTML format.
     */
    suspend fun downloadCategoriesPage(): String
}
