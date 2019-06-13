package kt.school.starlord.entity

data class Subcategory(
    val name: String,
    var categoryName: String? = null,
    val count: Int,
    val link: String
) {
    /**
     * Return the id this subcategory is associated with.
     *
     * @throws IllegalStateException if not currently associated with a database.
     */
    fun requireCategoryName(): String {
        if (categoryName == null) {
            throw IllegalStateException("Subcategory $this not exists in database.")
        }
        return categoryName as String
    }
}