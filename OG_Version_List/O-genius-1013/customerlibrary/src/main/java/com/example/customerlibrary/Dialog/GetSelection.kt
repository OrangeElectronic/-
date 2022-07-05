package com.example.customerlibrary.Dialog

import android.app.Dialog
import android.provider.ContactsContract
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerlibrary.AdminBeans
import com.example.customerlibrary.ChatPage
import com.example.customerlibrary.R
import com.example.customerlibrary.beans.PublicBeans
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.callback.SetupDialog
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
            val st=PublicBeans.instance.getAnser(item[position].id)
            val frag=JzActivity.getControlInstance().getNowPage()
            if(frag is ChatPage){
                if(frag.it.id.size==0){
                    frag.it.insert("0",AdminBeans.admin,"nodata",item[position].name, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),"nodata","我自己")
                    frag.it.insert("0",frag.admin,"nodata",st, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),"nodata","歐洲客服")
                }else{
                    frag.it.insert(frag.it.id[0],AdminBeans.admin,"nodata",item[position].name, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),"nodata","我自己")
                    frag.it.insert(frag.it.id[0],frag.admin,"nodata",st, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),"nodata","歐洲客服")
                }
                frag.cell.notifyDataSetChanged()
            }
        }
    }

    override fun sizeInit(): Int {
        return item.size
    }

}