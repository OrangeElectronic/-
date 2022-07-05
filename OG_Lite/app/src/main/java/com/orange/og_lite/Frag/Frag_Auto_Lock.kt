package com.orange.og_lite.Frag

import android.widget.ArrayAdapter
import com.orange.og_lite.Util.fixLanguage
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.Command
import com.orange.og_lite.R
import kotlinx.android.synthetic.main.auto_lock.view.*
import java.util.ArrayList
import com.orange.og_lite.Util.jzString
class Frag_Auto_Lock :JzFragement(R.layout.auto_lock){
    override fun viewInit() {
        Auto_LockList.clear()
        Auto_LockList.add(resources.jzString(R.string.array_autolock_1))
        Auto_LockList.add(resources.jzString(R.string.array_autolock_3))
        Auto_LockList.add(resources.jzString(R.string.array_autolock_5))
        Auto_LockList.add(resources.jzString(R.string.array_autolock_ten))
        Auto_LockList.add(resources.jzString(R.string.array_autolock_thirty))
        val lanAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, Auto_LockList)
        rootview.spinner.adapter=lanAdapter
        when(JzActivity.getControlInstance().getPro("time",300)){
            60->{
                rootview.spinner.setSelection(0)
            }
            180->{
                rootview.spinner.setSelection(1)
            }
            300->{
                rootview.spinner.setSelection(2)
            }
            600->{
                rootview.spinner.setSelection(3)
            }
            1800->{
                rootview.spinner.setSelection(4)
            }
        }
        rootview.setup.setOnClickListener {
            JzActivity.getControlInstance().showDiaLog(R.layout.data_loading,false,false,"data_loading")
            JzActivity.getControlInstance().setPro("time", arrayOf(60,180,300,600,1800)[rootview.spinner.selectedItemPosition])
            Thread{
                Command.setClose(JzActivity.getControlInstance().getPro("time",60))
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog()
                    JzActivity.getControlInstance().goBack()
                }
            }.start()


        }
    }
    var Auto_LockList= ArrayList<String>()
}