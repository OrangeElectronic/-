package com.orange.tpms.ue.kt_frag

import android.media.Image
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.service.ChangePageService
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.jzutil.listView
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_check_hsn.view.*
import kotlinx.android.synthetic.main.root_item.*
import kotlinx.android.synthetic.main.select_item.view.*

class Frag_Check_Hsn(var item: ArrayList<PublicBean.mmySelect>) :
    RootFragement(R.layout.frag_check_hsn) {
    var focus = 0
        set(value) {
            field = value
            adaper.notifyDataSetChanged()
        }
    var adaper = Adapter()
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.re.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootview.re.adapter = adaper
        if (item.size == 1) {
            PublicBean.selectMmy.make = item[0].make
            PublicBean.selectMmy.model = item[0].model
            PublicBean.selectMmy.year = item[0].year
            PublicBean.selectMmy.displayMake=item[0].displayMake
            PublicBean.selectMmy.displayModel=item[0].displayModel
            PublicBean.selectMmy.displayYear=item[0].displayYear
            ChangePageService.changePage()
        }
    }

    inner class Adapter : JzAdapter(R.layout.select_item) {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mView.text_item.text = "${item[position].displayMake}/${item[position].displayModel}/${item[position].displayYear}"
            holder.mView.setOnClickListener {
                PublicBean.selectMmy.make = item[position].make
                PublicBean.selectMmy.model = item[position].model
                PublicBean.selectMmy.year = item[position].year
                PublicBean.selectMmy.displayMake=item[position].displayMake
                PublicBean.selectMmy.displayModel=item[position].displayModel
                PublicBean.selectMmy.displayYear=item[position].displayYear
                if(item[position].dir=="INDIRECT"){
                    JzActivity.getControlInstance().changePage(
                        Frag_Relearm_Detail(),
                        "Frag_Relearm_Detail",
                        true
                    )
                }else{
                    JzActivity.getControlInstance().changePage(Frag_Confirm_Hsn(),"Frag_Confirm_Hsn",true)
                }
            }
            if (position == focus) {
                holder.mView.background = context!!.getDrawable(R.color.color_orange)
            } else {
                holder.mView.background = context!!.getDrawable(R.color.color_unite_bg)
            }
        }

        override fun sizeInit(): Int {
            return item.size
        }

    }

    override fun onTop() {
        if (focus - 1 >= 0) {
            focus -= 1
        }
    }

    override fun onDown() {
        if (focus + 1 <= item.size) {
            focus += 1
        }
    }

    override fun enter() {
        PublicBean.selectMmy.make = item[focus].make
        PublicBean.selectMmy.model = item[focus].model
        PublicBean.selectMmy.year = item[focus].year
        ChangePageService.changePage()
    }
}