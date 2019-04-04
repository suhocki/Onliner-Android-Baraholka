package kt.school.starlord.stw

import kt.school.starlord.entity.Subcategory
import org.jsoup.Jsoup

import java.io.IOException

//import java.awt.SystemColor.text

object JSoupLinksEx {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val url = "http://baraholka.onliner.by"

        val document = Jsoup.connect(url).get()
        val links = document.select("div.cm-onecat li a")

        val list = mutableListOf<Subcategory>()

        for (link in links) {

//            System.out.println("http://baraholka.onliner.by" + link.attr("href").substring(1))
//            System.out.println("text : " + link.text())
            list += listOf(Subcategory("http://baraholka.onliner.by" + link.attr("href").substring(1),null ,link.text()))
//            println(list)
        }
    }
}