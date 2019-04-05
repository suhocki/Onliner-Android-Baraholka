package kt.school.starlord.model

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.stw.CategoryFill


class MockRepository : CategoriesRepository {


         override fun getCategories(): MutableList<Subcategory> = CategoryFill()
    }
