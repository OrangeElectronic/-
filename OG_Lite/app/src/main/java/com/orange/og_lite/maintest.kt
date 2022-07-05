package com.orange.og_lite

import org.jsoup.Jsoup
import com.example.customerlibrary.util.Util_Tool.url





object maintest{
    @JvmStatic
    fun main(args: Array<String>) {
//        System.out.println(filterCaptcha("VVV-9999"))
//Log.e("filterCaptcha",filterCaptcha("VVV-9999"))
        val doc = Jsoup.connect("https://bento2.orange-electronic.com/Orange Cloud/Beta/Drive/OG_Lite_OBD/").get()
        doc.attr("href");
        System.out.println( doc.getElementsByTag("a")[1].text())
    }

}