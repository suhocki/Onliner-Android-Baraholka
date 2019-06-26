package kt.school.starlord.ui.categories

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

object MockedData {
    val categories = listOf(Category("categoryName1"), Category("categoryName2"))

    val categoriesWithSubcategories = mapOf(
        Category("categoryName3") to listOf(
            Subcategory("subcategoryName1", "categoryName3", 5, "link1"),
            Subcategory("subcategoryName2", "categoryName3", 2, "link2")
        ),
        Category("categoryName4") to listOf(
            Subcategory("subcategoryName3", "categoryName4", 3, "link3")
        )
    )
}
