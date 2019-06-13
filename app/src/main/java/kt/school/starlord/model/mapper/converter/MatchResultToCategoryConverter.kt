package kt.school.starlord.model.mapper.converter

import kt.school.starlord.entity.Category
import kt.school.starlord.model.mapper.BaseConverter

class MatchResultToCategoryConverter : BaseConverter<MatchResult, Category>(
    MatchResult::class.java,
    Category::class.java
) {

    override fun convert(value: MatchResult): Category {
        val regex = """<h3>((.|\n)*?)</h3>""".toRegex()
        return Category(regex.find(value.value)?.groups?.get(1)?.value.toString())
    }
}

