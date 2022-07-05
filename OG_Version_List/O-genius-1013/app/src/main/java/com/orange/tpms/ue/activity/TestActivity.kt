package com.orange.tpms.ue.activity

import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.DiaClass
import com.orange.jzchi.jzframework.JzActivity

class TestActivity:JzActivity() {
    override fun changePageListener(tag: String, frag: Fragment) {

    }

    override fun dialogLinstener(dialog: DiaClass, tag: String) {
    }

    override fun keyEventListener(event: KeyEvent): Boolean {
        return true
    }

    override fun savedInstanceAble(): Boolean {
        return true
    }

    override fun viewInit(rootview: View) {
    }
}