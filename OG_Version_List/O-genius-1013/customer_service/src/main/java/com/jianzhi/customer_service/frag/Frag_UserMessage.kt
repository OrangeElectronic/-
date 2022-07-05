package com.jianzhi.customer_service.frag

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jzlifttool.LifeTimer
import com.example.jztaskhandler.runner
import com.jianzhi.customer_service.adapter.Ad_Qustion
import com.jianzhi.customer_service.adapter.Qustion
import com.facebook.drawee.backends.pipeline.Fresco
import com.jianzhi.customer_service.*
import com.jianzhi.customer_service.adapter.Ad_Message
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.jzchi.jzframework.callback.permission_C
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.chatpage.*
import kotlinx.android.synthetic.main.chatpage.view.*
import java.io.ByteArrayOutputStream
import java.io.InputStream


class Frag_UserMessage(
    var admin: String,
    var ip: String,
    var background: Int = R.mipmap.background
) : JzFragement(R.layout.chatpage) {
    var item = ArrayList<message>()
    var adapter = Ad_Message(item)
    var it=ArrayList<Qustion>()
    var adapterq = Ad_Qustion(it)
    lateinit var timer: LifeTimer
    override fun viewInit() {
        Fresco.initialize(JzActivity.getControlInstance().getRootActivity())
        JzActivity.getControlInstance().closeDiaLog()
        ServerCommand.serial = admin
        ServerCommand.server = ip
        rootview.re2.setBackgroundResource(background)

        rootview.re2.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, true)
        rootview.re2.adapter = adapter
        rootview.camera.setOnClickListener {
            tool()
        }
        rootview.back.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().showBottomSheetDialog(false,true,object : SetupDialog(R.layout.all_qustion){
                override fun dismess() {
                    Log.e("showBottomSheetDialog","dismiss")
                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.requstion.layoutManager= GridLayoutManager(JzActivity.getControlInstance().getRootActivity(),3)
                    rootview.requstion.adapter=adapterq
                    adapter.notifyDataSetChanged()
                }
            },"chatpage")
//         rootview.qustion.visibility=if(rootview.qustion.visibility==View.VISIBLE) View.GONE else View.VISIBLE
        }
        rootview.send.setOnClickListener {
            if(rootview.sender.text.isEmpty()){
                return@setOnClickListener
            }
                ServerCommand.controlInstance().sendMessage(rootview.sender.text.toString(),
                    object : ServerInTerface.vertifycallback {
                        override fun success() {
                            rootview.sender.setText("")
                        }

                        override fun internetError() {

                        }

                        override fun vertifyFalse() {
                        }
                    })
        }
        rootview.sender.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        getMessage()

    }


    var progress = false
    var isbtn = false
    var rungetMessage = false
    fun getMessage() {
        if (isbtn || rungetMessage) {
            return
        }
        rungetMessage = true
        val size = item.size
        ServerCommand.controlInstance().getMessage(item, object : ServerInTerface.vertifycallback {
            override fun success() {
                isbtn = item.size == size
                rungetMessage = false
                if(item.size>=0){
                    rootview.animation_view.visibility= View.GONE
                }
                if(!::timer.isInitialized){
                    timer = LifeTimer(lifecycle)
                    timer.schedule(0, 1000 * 2, runner {
                        getTopMessage()
                    })
                }
                adapter.notifyDataSetChanged()
            }

            override fun internetError() {
                rungetMessage = false
                if(JzActivity.getControlInstance().getNowPageTag() == "Frag_UserMessage"){getMessage()}
            }

            override fun vertifyFalse() {
                rungetMessage = false
                if(JzActivity.getControlInstance().getNowPageTag() == "Frag_UserMessage"){getMessage()}
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(::timer.isInitialized){
            timer .start()
        }
        }

    var run = false
    fun getTopMessage() {
        if (run) {
            return
        }
        run = true
        ServerCommand.controlInstance()
            .getTopMessage(item, object : ServerInTerface.vertifycallback {
                override fun success() {
                    adapter.notifyDataSetChanged()
                    run = false
                }

                override fun internetError() {
                    run = false
                }

                override fun vertifyFalse() {
                    run = false
                }
            })

    }

    fun tool() {

        JzActivity.getControlInstance().permissionRequest(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), object :
                permission_C {
                var size = 0
                override fun requestFalse(a: String) {
                    JzActivity.getControlInstance().toast("此操作必須取相簿權限")
                }

                override fun requestSuccess(a: String) {
                    size += 1
                    if (size == 2) {
                        Util_Album_SetUp().SetMatisse(this@Frag_UserMessage)
                    }
                }
            }, 10
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainResult(data)
            ServerCommand.controlInstance()
                .uploadImage(result[0], object : ServerInTerface.vertifycallback {
                    override fun success() {
                        JzActivity.getControlInstance().toast("上傳成功")
                    }

                    override fun internetError() {
                        JzActivity.getControlInstance().toast("上傳失敗")
                    }

                    override fun vertifyFalse() {
                        JzActivity.getControlInstance().toast("上傳失敗")
                    }
                })
        }
    }

    fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 8192
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}