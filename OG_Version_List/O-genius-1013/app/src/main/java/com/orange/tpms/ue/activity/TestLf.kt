package com.orange.tpms.ue.activity

import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.DiaClass
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.ue.testFrag.Frag_Test_Source
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.OgPublic

class TestLf : JzActivity() {
    override fun changePageListener(tag: String, frag: Fragment) {

    }

    override fun dialogLinstener(dialog: DiaClass, tag: String) {

    }


    override fun keyEventListener(event: KeyEvent): Boolean {
        return true
    }

    override fun savedInstanceAble(): Boolean {
        return false
    }

    override fun viewInit(rootview: View) {
        OgPublic.getInstance
        MysqDatabase
//        getControlInstance().setHome(kt_splash(), "kt_splash")
//        Thread {
//            try {
//                while (!getControlInstance().isFrontDesk()) {
//                    Thread.sleep(100)
//                }
//                handler.post { getControlInstance().setHome(kt_splash(), "kt_splash") }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }.start()
        JzActivity.getControlInstance().setHome(Frag_Test_Source(), "Frag_Test_Source")
    }

}