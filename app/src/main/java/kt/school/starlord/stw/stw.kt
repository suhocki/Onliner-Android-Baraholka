package kt.school.starlord.stw

import kt.school.starlord.entity.Subcategory
import org.jsoup.Jsoup

import java.io.IOException


    fun CategoryFill():MutableList<Subcategory> {
        val url = "http://baraholka.onliner.by"
        val document = Jsoup.connect(url).get()
        val links = document.select("div.cm-onecat li a")
        val nums = document.select("div.cm-onecat sup")
        var index = 0
        val list = mutableListOf<Subcategory>()
        for (link in links) {
            list += listOf(Subcategory(link = "http://baraholka.onliner.by" + link.attr("href").substring(1), count = nums[index].text().toInt(), name = link.text()))
            index++
        }
        return list
    }
