package kt.school.starlord.di

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

class PageParser {
    var allCategories: ArrayList<Category> = ArrayList()

    fun parsePage(loadedPage: String): ArrayList<Category> {
        /* get from the page all "oneCat" divisions */
        val allData: Sequence<MatchResult> = Regex("""<h3>(.|\n)*?</div>""").findAll(loadedPage)

        for (oneCat in allData) {

            /* create and fill array of subCategories for all categories */
            val subCategories: ArrayList<Subcategory> = ArrayList()
            /* get from page all subcategories divisions */
            val subCatsDivs: Sequence<MatchResult> = Regex("""<li>(.|\n)*?</li>""").findAll(oneCat.value)

            for (oneSubCat in subCatsDivs) {
                /* create subCategory and add it to array of subCategories of current category */
                val subCat = Subcategory(
                    name = Regex("""">((.|\n)*?)</a""").find(oneSubCat.value)?.groups?.get(1)?.value.toString(),
                    count = Regex("""<sup>((.|\n)*?)</sup>""").find(oneSubCat.value)?.groups?.get(1)?.value.toString().trim().toInt(),//need to be overwriting without trim
                    link = Regex("""<a href="((.|\n)*?)"""").find(oneSubCat.value)?.groups?.get(1)?.value.toString()
                )
                subCategories.add(subCat)
            }

            /* create category and add it to array of categories */
            val category = Category(
                name = Regex("""<h3>((.|\n)*?)</h3>""").find(oneCat.value)?.groups?.get(1)?.value.toString(),
                subCategories = subCategories
            )
            allCategories.add(category)

        }
        return allCategories
    }
}