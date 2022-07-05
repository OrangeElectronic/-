package com.orange.og_lite.Dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Command.Companion.handler
import com.orange.og_lite.Frag.Frag_Function_OBD_ID_copy
import com.orange.og_lite.MainActivity
import com.orange.og_lite.Page_MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.da_tool.*
import java.text.FieldPosition

class Da_InsertAndRemove_tool(var p:Int) : SetupDialog(R.layout.da_tool)
{

    override fun dismess() {

        if(PublicBeans.position == PublicBeans.OBD學碼) {
            val frag = JzActivity.getControlInstance().findFragByTag("Frag_Function_OBD_ID_copy")
            if (frag is Frag_Function_OBD_ID_copy) {
                if(!frag.myitem.state.contains(ObdBeans.PROGRAM_FALSE) && !frag.myitem.state.contains(ObdBeans.PROGRAM_SUCCESS))
                handler.post {
                    frag.write_OBD學碼()
                }
            }
        }
    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {
when(p){
    0->{
        when(PublicBeans.position) {
            PublicBeans.OBD學碼 -> {
                rootview.imageView21.setImageResource(R.mipmap.img_insert_tool)
                rootview.textView44.text = "jz.445".getFix()
            }
            PublicBeans.OBD複製 -> {
                rootview.imageView21.setImageResource(R.mipmap.img_remove_tool)
                rootview.textView44.text ="jz.410".getFix()
            }
        }
    }
    1->{
        rootview.imageView21.setImageResource(R.mipmap.img_insert_tool)
        //rootview.textView44.text = "Insert OBD "
        rootview.textView44.text = "jz.445".getFix()
    }
   -1->{
       rootview.imageView21.setImageResource(R.mipmap.img_remove_tool)
       //rootview.textView44.text = "disconnected from OBD"
       rootview.textView44.text ="jz.410".getFix()
   }
}



    }

}