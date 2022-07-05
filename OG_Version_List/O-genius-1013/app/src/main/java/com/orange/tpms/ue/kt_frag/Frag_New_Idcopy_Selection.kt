package com.orange.tpms.ue.kt_frag

import android.app.ActionBar
import android.media.Image
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jzpictureselector.PictureSelector
import com.example.jztaskhandler.runner
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.jianzhi.jzblehelper.callback.ConnectResult
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.jzutil.setImage
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.cell_big_item.view.*
import kotlinx.android.synthetic.main.cell_small_item.view.*
import kotlinx.android.synthetic.main.frag_select_mmy.view.*
import kotlinx.android.synthetic.main.root_item.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
* 新版ID COPY選擇
* */
class Frag_New_Idcopy_Selection : RootFragement(R.layout.frag_select_mmy) {
    var focus = 0
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
        }
    var adapter = Adapter()

    data class item(var iconN: Int, var iconC: Int, var title: String, var runner: runner)

    var it: ArrayList<item> = ArrayList()


    override fun viewInit() {
        rootview.titText.text = "${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        it.add(item(R.mipmap.btn_single_sensor_n, R.mipmap.btn_single_sensor_p, "jz.577".getFix()) {
        JzActivity.getControlInstance().changePage(Frag_Function_New_OBD_ID_copy(wheelCount = 1,single = true), "Frag_Function_New_OBD_ID_copy", true)
        })
        it.add(item(R.mipmap.btn_multiple_sensor_n, R.mipmap.btn_multiple_sensor_p, "jz.578".getFix()) {
        JzActivity.getControlInstance().changePage(Frag_Check_Sensor_Read_IdCopy_Selection(), "Frag_Check_Sensor_Read_IdCopy_Selection", true)
        })
        rootview.re.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootview.re.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    inner class Adapter : JzAdapter(R.layout.root_item) {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mView.root.removeAllViews()
                val view = layoutInflater.inflate(
                    R.layout.cell_big_item,
                    holder.mView.root,
                    false
                )
            view.imageView2.setImageResource(if (focus == position) it[position].iconC else it[position].iconN)
            view.tit.text = it[position].title
                val divider = View(context)
                divider.setBackgroundColor(resources.getColor(R.color.gray))
                holder.mView.root.addView(divider)
                divider.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                divider.layoutParams.height = 1
                holder.mView.root.addView(view)
                holder.mView.setOnClickListener {
                    this@Frag_New_Idcopy_Selection.it[position].runner.run()
                }
        }

        override fun sizeInit(): Int {
            return it.size
        }

    }

    override fun enter() {
        it[focus].runner.run()
    }

    override fun onTop() {
        if (focus - 1 >= 0) {
            focus -= 1
        }
        rootview.re.scrollToPosition(focus)
    }

    override fun onDown() {
        if (focus + 1 < it.size) {
            focus += 1
        }
        rootview.re.scrollToPosition(focus)
    }
}