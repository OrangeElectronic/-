package com.example.customerlibrary

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customerlibrary.Adapter.Ad_InMail
import com.example.customerlibrary.Adapter.Ad_Qustion
import com.example.customerlibrary.Adapter.Qustion
import com.example.customerlibrary.beans.Messageitem
import com.example.customerlibrary.beans.PublicBeans
import com.example.customerlibrary.callback.FireBase_C
import com.example.customerlibrary.callback.GetPost_C
import com.example.customerlibrary.callback.Socket_C
import com.example.customerlibrary.util.Util_Album_SetUp
import com.example.customerlibrary.util.Util_Firebase_Upload
import com.facebook.drawee.backends.pipeline.Fresco
import com.jzsql.lib.mmySql.JzSqlHelper
import com.orange.EventBus.updateMessage
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.jzchi.jzframework.callback.permission_C
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.all_qustion.*
import kotlinx.android.synthetic.main.chatpage.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChatPage(var admin:String,var pick:String,var lan:JzSqlHelper?=null):JzFragement(R.layout.chatpage),
    FireBase_C {
    companion object{
        var iscustomer=false
        fun getEU():ChatPage{
            iscustomer=true
            return ChatPage("eucustomer","歐洲客服")
        }
        fun getUS():ChatPage{
            iscustomer=true
            return ChatPage("uscustomer","美國客服")
        }
        fun getTW():ChatPage{
            iscustomer=true
            return ChatPage("twcustomer","台灣客服")
        }
    }
    var command=Command.getInstance()
    var it = Messageitem()
    var cell = Ad_InMail(it)
    var item=ArrayList<Qustion>()
    var adapter = Ad_Qustion(item)
    override fun viewInit() {
        Fresco.initialize(context)
        rootview.re2.layoutManager = LinearLayoutManager(act, RecyclerView.VERTICAL, true)
        rootview.re2.adapter = cell
        Command.getInstance().creatAdmin(admin,object : Command.createResult {
            override fun result(a: Boolean) {
                if(a){
                    logdata("0")
                }
            }
        })
        rootview.newmessage.setOnClickListener {
            rootview.re2.scrollToPosition(0)
            rootview.newmessage.visibility= View.GONE
            cell.notifyDataSetChanged()
        }
        rootview.re2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(!rootview.re2.canScrollVertically(1)){
                    rootview.newmessage.visibility= View.GONE
                    cell.notifyDataSetChanged()
                }
                super.onScrollStateChanged(recyclerView, newState)
                val b = recyclerView.canScrollVertically(1)
                if (!b && !it.button && it.admin.size > 49) {
                    logdata(it.id[it.admin.size - 1])
                }
            }
        })
        rootview.close.setOnClickListener {
            image = "nodata"
            rootview.showimage.visibility = View.GONE
        }
        rootview.back.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
        rootview.camera.setOnClickListener {
            tool()
        }
        rootview.send.setOnClickListener {
            if (rootview.sender.text.isEmpty()) {
                JzActivity.getControlInstance().toast("訊息不得為空")
                return@setOnClickListener
            }
            if (image.equals("nodata")) {
                send()
            } else {
                Util_Firebase_Upload.upload(file, this)
            }
        }
        rootview.back.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().showBottomSheetDialog(false,true,object :SetupDialog(R.layout.all_qustion){
                override fun dismess() {
Log.e("showBottomSheetDialog","dismiss")
                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.requstion.layoutManager= GridLayoutManager(act,3)
                    rootview.requstion.adapter=adapter
                    adapter.notifyDataSetChanged()
                }
            },"chatpage")
//         rootview.qustion.visibility=if(rootview.qustion.visibility==View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    override fun UpdateSuccess(uri: Uri?) {
        image = (uri.toString())
        send()
    }

    override fun UpdateFalse() {
        JzActivity.getControlInstance().toast("上傳失敗！")
    }

    fun logdata(take: String) {
//        command.GetMail(take, it, admin, GetPost_C {
//            cell.notifyDataSetChanged()
//        })
    }

    fun send() {
        val a = rootview.sender.text.toString()
        command.SendMail(admin, image, a, GetPost_C {
            image = "nodata"
            if (it) {
                JzActivity.getControlInstance().toast("傳送成功")
                this.it = Messageitem()
                cell = Ad_InMail(this.it)
                rootview.re2.adapter = cell
                rootview.sender.setText("")
                rootview.showimage.visibility = View.GONE
                logdata("0")
            } else {
                JzActivity.getControlInstance().toast("傳送失敗")
            }
        })
    }

    var image = "nodata"
    lateinit var file: Uri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainResult(data)
            rootview.im.setImageURI(result[0])
            file = result[0]
            image = "havedata"
            rootview.showimage.visibility = View.VISIBLE
        }
    }

    fun tool() {

        JzActivity.getControlInstance().permissionRequest(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA), object :
            permission_C {
            var resize=0
            override fun requestFalse(a: String) {
                JzActivity.getControlInstance().toast("此操作必須取相簿權限")
            }

            override fun requestSuccess(a: String) {
                resize+=1
                if(resize==2){Util_Album_SetUp().SetMatisse(this@ChatPage)}

            }
        }, 10)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun gettop(a: updateMessage){
        if(a.admin==admin){
            if(it.id.size>0){
               command.refreshMail(it.id[0], it, admin, GetPost_C {
                    if(it){
                        if(rootview.re2.canScrollVertically(1)){
                            rootview.newmessage.visibility= View.VISIBLE
                        }else{
                            cell.notifyDataSetChanged()
                        }
                        Log.e("scrollstate",""+rootview.re2.canScrollVertically(1))
                    }else{
                        if(fragId== JzActivity.fragid){
                            gettop(updateMessage(admin))
                        }
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        if(haveRootView()){
            gettop(updateMessage(admin))
        }
    }
    override fun onPause() {
        super.onPause()
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }
}
