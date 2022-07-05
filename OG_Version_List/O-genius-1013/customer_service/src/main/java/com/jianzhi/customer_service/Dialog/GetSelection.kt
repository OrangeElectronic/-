package com.example.customerlibrary.Dialog

import android.app.Dialog
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.jianzhi.customer_service.R
import com.jianzhi.customer_service.beans.PublicBeans
import com.jianzhi.customer_service.frag.Frag_UserMessage
import com.jianzhi.customer_service.message
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.jzutil.CalculateTime
import kotlinx.android.synthetic.main.chatpage.view.*
import kotlinx.android.synthetic.main.layout_all_qustion.view.*
import kotlinx.android.synthetic.main.selectqustion.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GetSelection(var item: ArrayList<PublicBeans.robotItem>) :
    SetupDialog(R.layout.selectqustion) {
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {
        rootview.re2.layoutManager = LinearLayoutManager(
            JzActivity.getControlInstance().getRootActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        rootview.re2.adapter = SelectionAdapter(item)
    }

}

class SelectionAdapter(var item: ArrayList<PublicBeans.robotItem>) :
    JzAdapter(R.layout.layout_all_qustion) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.name.text = item[position].name
        holder.mView.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            val st= PublicBeans.instance.getAnser(item[position].id)
            val frag=JzActivity.getControlInstance().getNowPage()
            if(frag is Frag_UserMessage){
                if(st.isNotEmpty()){
                    if(frag.adapter.item.size>0){
                        frag.adapter.item.add(0,message(frag.adapter.item[0].id,"root",st,"nodata",SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),"nodata","robot"))
                    }else{
                        frag.adapter.item.add(0,message("-1","root",st,"nodata",SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),"nodata","robot"))
                    }
                }
                frag.adapter.notifyDataSetChanged()
            }
        }
    }


    override fun sizeInit(): Int {
        return item.size
    }

}