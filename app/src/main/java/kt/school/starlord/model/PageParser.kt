package kt.school.starlord.model

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

class PageParser {

    fun parsePage(loadedPage: String): List<Category> {
        val allCategories = mutableListOf<Category>()
        /* get from the page all "oneCat" divisions */
        val allData: Sequence<MatchResult> = """<h3>(.|\n)*?</div>""".toRegex().findAll(loadedPage)

        for (oneCat in allData) {

            /* create and fill array of subCategories for all categories */
            val subCategories = mutableListOf<Subcategory>()
            /* get from page all subcategories divisions */
            val subCatsDivs: Sequence<MatchResult> = """<li>(.|\n)*?</li>""".toRegex().findAll(oneCat.value)

            for (oneSubCat in subCatsDivs) {
                /* create subCategory and add it to array of subCategories of current category */
                val subCat = Subcategory(
                        name = """">((.|\n)*?)</a""".toRegex().find(oneSubCat.value)?.groups?.get(1)?.value.toString(),
                        count = """<sup>((.|\n)*?)</sup>""".toRegex().find(oneSubCat.value)?.groups?.get(1)?.value.toString().trim().toInt(),//need to be overwriting without trim
                        link = """<a href="((.|\n)*?)"""".toRegex().find(oneSubCat.value)?.groups?.get(1)?.value.toString()
                )
                subCategories.add(subCat)
            }

            /* create category and add it to array of categories */
            val category = Category(
                    name = """<h3>((.|\n)*?)</h3>""".toRegex().find(oneCat.value)?.groups?.get(1)?.value.toString(),
                    subCategories = subCategories
            )
            allCategories.add(category)

        }
        return allCategories
    }
}