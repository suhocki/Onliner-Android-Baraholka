package kt.school.starlord.domain

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory


interface CategoriesRepository {
    fun getCategories(): MutableList<Subcategory>
    }
