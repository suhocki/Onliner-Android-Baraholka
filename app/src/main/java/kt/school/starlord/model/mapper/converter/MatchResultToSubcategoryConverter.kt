package kt.school.starlord.model.mapper.converter

import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.BaseConverter

class MatchResultToSubcategoryConverter : BaseConverter<MatchResult, Subcategory>(
    MatchResult::class.java,
    Subcategory::class.java
) {

    override fun convert(value: MatchResult): Subcategory {
        return Subcategory(
            name = """">((.|\n)*?)</a""".toRegex()
                .find(value.value)?.groups?.get(1)?.value.toString(),
            count = """<sup>((.|\n)*?)</sup>""".toRegex()
                .find(value.value)?.groups?.get(1)?.value.toString().trim().toInt(),
            link = """<a href="((.|\n)*?)"""".toRegex()
                .find(value.value)?.groups?.get(1)?.value.toString()
        )
    }
}
