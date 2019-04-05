package kt.school.starlord.stw

import kt.school.starlord.entity.Subcategory
import org.jsoup.Jsoup

import java.io.IOException

object JSoupLinksEx {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val url = "http://baraholka.onliner.by"

        val document = Jsoup.connect(url).get()
        val links = document.select("div.cm-onecat li a")
        val nums = document.select("div.cm-onecat sup")
        var index = 0
        val list = mutableListOf<Subcategory>()
        for (link in links) {
            list += listOf(Subcategory("http://baraholka.onliner.by" + link.attr("href").substring(1),nums[index].text().toInt() ,link.text()))
            index++
        }
    }
}