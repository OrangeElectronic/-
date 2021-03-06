package com.orange.og_lite.Frag

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.orange.og_lite.Util.jzString
import com.jzsql.lib.mmySql.InitCaller
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.adapter.Ad_MyFavorite
import com.orange.og_lite.beans.MyFavoriteItem
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__my_favorite.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_MyFavorite : JzFragement(R.layout.fragment_frag__setting_pager__my_favorite) {

    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity
    var myitem = MyFavoriteItem()

    override fun viewInit() {
        rootview.fixLanguage()
        refresh=true
        myitem = MyFavoriteItem()

        rootview.MyFavoriteView.layoutManager= GridLayoutManager(activity, 1)
        //rootview.Select_Make_View.adapter= Ad_Make(utilMmySqlItemDAO.getMake()!!)
        rootview.MyFavoriteView.adapter= Ad_MyFavorite(myitem)
        mmy_query()
        rootview.Add_vehicle.setOnClickListener {  JzActivity.getControlInstance()
            .changePage(
                Frag_SettingPager_AddMyFavorite(),
                "Frag_SettingPager_AddMyFavorite", true)
        }

    }

    fun mmyname(): String {
        try {
            val url = URL("https://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/")
            val conn = url.openConnection() as HttpURLConnection
            // val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8") as Reader?)
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8"))
            var line: String? = null
            val strBuf = StringBuffer()
            line=reader.readLine()
            while (line != null) {
                strBuf.append(line)
                line=reader.readLine()
            }
            val arg = strBuf.toString().split("HREF=\"".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (a in arg) {
                if (a.contains(".db")) {
                    return a.substring(a.indexOf(">") + 1, a.indexOf("<"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "nodata"
    }


    fun mmy_query() {
        (activity as MainActivity).MyFavorite_Memory.MakeArray.clear()
        (activity as MainActivity).MyFavorite_Memory.ModelArray.clear()
        (activity as MainActivity).MyFavorite_Memory.YearArray.clear()

        Thread {
            mMainActivity.item_favorite.exsql(
                "CREATE TABLE   IF NOT EXISTS myFavoritetable (\n" +
                        "    id   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    make VARCHAR NOT NULL,\n" +
                        "    model VARCHAR NOT NULL,\n" +
                        "    year VARCHAR NOT NULL\n"+
                        ");\n"
            )

            //????????????
            mMainActivity.item_favorite.create().query("select * from myFavoritetable  order by id desc", Sql_Result {
                //Callback?????????????????????????????????????????????
                val result1 = it.getString(1)
                val result2 = it.getString(2)
                val result3 = it.getString(3)

                handler.post {

                    (activity as MainActivity).MyFavorite_Memory.MakeArray.add(result1)
                    (activity as MainActivity).MyFavorite_Memory.ModelArray.add(result2)
                    (activity as MainActivity).MyFavorite_Memory.YearArray.add(result3)

                    myitem.add(result1 + "/" + result2 + "/" + result3)


                    (rootview.MyFavoriteView.adapter as Ad_MyFavorite).notifyDataSetChanged()
                }

            });
        }.start()
    }
}
