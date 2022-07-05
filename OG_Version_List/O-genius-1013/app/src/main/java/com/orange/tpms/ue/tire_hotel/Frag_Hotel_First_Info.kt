package com.orange.tpms.ue.tire_hotel

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.ue.kt_frag.Frag_TireHotel_Detail
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.frag_hotel_list_info.view.*

class Frag_Hotel_First_Info : JzFragement(R.layout.frag_hotel_list_info) {
    companion object{
        var isInital :Boolean get() {
            return JzActivity.getControlInstance().getPro("Frag_Hotel_First_Info_inital",false)}
        set(value) {
            JzActivity.getControlInstance().setPro("Frag_Hotel_First_Info_inital",value)
        }
    }
    var dataList = arrayOf(
        InfoList("jz.491".getFix(), "jz.492".getFix(), "tm01"),
        InfoList("jz.491".getFix(), "jz.493".getFix(), "tm02"),
        InfoList("jz.491".getFix(), "jz.494".getFix(), "tm03"),
        InfoList("jz.491".getFix(), "jz.495".getFix(), "tm04"),
        InfoList("jz.496".getFix(), "jz.497".getFix(), "tm05"),
        InfoList("jz.496".getFix(), "jz.498".getFix(), "tm06"),
        InfoList("jz.496".getFix(), "jz.499".getFix(), "tm07"),
        InfoList("jz.496".getFix(), "jz.500".getFix(), "tm08")
    )
    var index = 0
    override fun viewInit() {
        isInital=true
        toFocus(0)
        rootview.button11.setOnClickListener {
            index++
            if(index<dataList.size){
                toFocus(index)
            }else{
                JzActivity.getControlInstance()
                    .changePage(Frag_TireHotel_List(), "Frag_TireHotel_List", false)
            }
        }
        rootview.button8.setOnClickListener {
            JzActivity.getControlInstance()
                .changePage(Frag_TireHotel_List(), "Frag_TireHotel_List", false)
        }
    }

    fun toFocus(a: Int) {
        rootview.imageView36.setImageResource(
            resources.getIdentifier(
                dataList[a].img,
                "mipmap",
                JzActivity.getControlInstance().getRootActivity().packageName
            )
        )
        rootview.tt.text = dataList[a].title
        rootview.textView43.text=dataList[a].content
    }
}

data class InfoList(var title: String, var content: String, var img: String)