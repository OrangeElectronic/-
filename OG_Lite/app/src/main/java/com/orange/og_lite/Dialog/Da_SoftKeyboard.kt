package com.orange.og_lite.Dialog

import android.app.Dialog
import android.text.InputFilter
import android.util.Log
import android.view.KeyEvent
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Frag.Frag_Function_OBD_ID_copy
import com.orange.og_lite.Frag.Frag_Function_Program
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.KeyboardUtil
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.adapter.Ad_Function_Program
import com.orange.og_lite.adapter.Ad_SoftKeyboard
import com.orange.og_lite.adapter.Ad_obd
import com.orange.og_lite.beans.Bs_SoftKeyboard
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Keyboardtext
import kotlinx.android.synthetic.main.da_softkeyboard.*
import kotlinx.android.synthetic.main.fragment_frag__function__id_copy.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.*

class Da_SoftKeyboard(val ad: JzAdapter) : SetupDialog(R.layout.da_softkeyboard) {
    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity

    var myitem = Bs_SoftKeyboard()
    lateinit var adapter: Ad_SoftKeyboard
    lateinit var diaroot: Dialog
    //var JzAdapter = ad

    override fun dismess() {
        ChangeEidtText(diaroot)
        fag_check()
    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {

        diaroot = rootview!!

        KeyboardUtil.hideEditTextKeyboard(rootview.ID)
        rootview.ID.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(8))
        myitem = Bs_SoftKeyboard()
        adapter = Ad_SoftKeyboard(this, myitem)

        rootview.ID.setText(PublicBeans.DialogKeytext)
        rootview.ID.requestFocus()
        rootview.ID.setSelection(rootview.ID.text.toString().length)
        rootview.ShowKeyboard.layoutManager =
            LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
        rootview.ShowKeyboard.adapter = adapter

        //myitem.add("","","輸入","<-")
        myitem.add("1", "2", "3", "A")
        myitem.add("4", "5", "6", "B")
        myitem.add("7", "8", "9", "C")
        myitem.add("0", "F", "E", "D")

        rootview.delete.setOnClickListener {

            if (myitem.EndSelect != myitem.MainSelect) {
                rootview.ID.text?.delete(myitem.MainSelect, myitem.EndSelect)
            } else {
                if (myitem.MainSelect - 1 != -1) {
                    rootview.ID.text?.delete(myitem.MainSelect - 1, myitem.MainSelect)
                }
            }
        }

        rootview.enter.setOnClickListener {
            ChangeEidtText(rootview)
            fag_check()
        }

        rootview.ID.Keyboardtext = object : Keyboardtext {
            override fun end(a: Int) {
                Log.e("Keyboard_end", a.toString())

                myitem.EndSelect = a
            }

            override fun callback() {
                //Log.e("after",a.toString())
            }

            override fun start(a: Int) {
                Log.e("Keyboard_start", a.toString())

                myitem.MainSelect = a

            }
        }
    }

    fun ChangeEidtText(rootview: Dialog) {

        if (PublicBeans.position == PublicBeans.燒錄) {
            if (ad is Ad_Function_Program) {
                Log.e("ad.position", ad.position.toString())
                if (PublicBeans.DialogKeyboard)
                    if (ad.focus == ad.position) {
                        PublicBeans.DialogKeyboard = false
                        ad.myitem.programId[ad.position] = rootview.ID.text.toString()
                        ad.notifyDataSetChanged()
                        val frag = JzActivity.getControlInstance().findFragByTag("Frag_Function_Program")
                        if (frag is Frag_Function_Program) {
                            frag.checkComplete()
                        }
                        JzActivity.getControlInstance().closeDiaLog()
                    }
            }
        } else {
            if (ad is Ad_obd) {
                if (PublicBeans.DialogKeyboard) {
                    if (ad.position * 2 == ad.focus) {
                        PublicBeans.DialogKeyboard = false
                        ad.beans.OldSemsor[ad.position] = rootview.ID.text.toString()
                        ad.notifyDataSetChanged()
                        JzActivity.getControlInstance().closeDiaLog()
                    } else {
                        PublicBeans.DialogKeyboard = false
                        ad.beans.NewSensor[ad.position] = rootview.ID.text.toString()
                        ad.notifyDataSetChanged()
                        JzActivity.getControlInstance().closeDiaLog()
                    }
                }
            }
        }

    }

    fun fag_check() {

        if (PublicBeans.position == PublicBeans.燒錄) {

        } else {
            val frag = JzActivity.getControlInstance().findFragByTag("Frag_Function_OBD_ID_copy")
            if (frag is Frag_Function_OBD_ID_copy) {
                frag.checkComplete()
            }
        }

//        val frag = JzActivity.getControlInstance().findFragByTag("Frag_Function_Program")
//        if(frag is Frag_Function_Program) {
//            frag.checkComplete()
//        }
    }

}