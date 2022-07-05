package com.orange.og_lite.Frag


import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.orange.og_lite.Util.jzString
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.adapter.Ad_Make
import com.orange.og_lite.beans.PublicBeans
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import kotlinx.android.synthetic.main.fragment_frag__select_mmy_page__make.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SelectMmyPage_Make : JzFragement(R.layout.fragment_frag__select_mmy_page__make) {


    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity
    var name = ArrayList<Util_MmySql_module>()
    lateinit var adapter: Ad_Make

    override fun viewInit() {
        rootview.fixLanguage()
        val a = JzActivity.getControlInstance().getPro("Languages", "")
        Log.e("Languages", a)
        name = ArrayList<Util_MmySql_module>()
        adapter = Ad_Make(name)
        rootview.Select_Make_View.layoutManager = GridLayoutManager(activity, 3)
        rootview.Select_Make_View.adapter = adapter
        query_make()
        rootview.imageView27.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
    }


    fun query_make() {
        name.clear()
        //mMainActivity.item.query("select distinct `Make`,`Make_img` from `Summary table` ", Sql_Result { result ->
        var sql = ""
        sql =
            "select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA')  replacer  order by `Make` asc"
        when (PublicBeans.position) {
            PublicBeans.OBD學碼 -> {
                sql = sql.replace("replacer", "and `OGL Auto` != '4'")
            }
            PublicBeans.OBD複製 -> {
                sql = sql.replace("replacer", "and `OGL CopyID`='True' and OBD2 not in('NA')")
            }
            PublicBeans.燒錄 -> {
                sql = sql.replace("replacer", "and `OGL CopyID`='True' ")
            }
            PublicBeans.複製ID -> {
                sql = sql.replace("replacer", "and `OGL Programe`='True' ")
            }
            PublicBeans.讀取 -> {
                sql = sql.replace("replacer", "and `OGL Read`='True'  ")
            }
        }
        sql = sql.replace("replacer", "")
        mMainActivity.item.query(sql, Sql_Result { result ->
            val module = Util_MmySql_module()
            //val data = result.resources.jzString(0)
            module.make = result.getString(0)
            module.image = result.getString(1)
            if (name.filter { it.make == result.getString(0) }.isEmpty()) {
                name.add(module)
            }

        })
        adapter.notifyDataSetChanged()
    }

}
