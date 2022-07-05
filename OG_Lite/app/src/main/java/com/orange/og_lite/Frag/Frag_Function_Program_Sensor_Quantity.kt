package com.orange.og_lite.Frag


import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__program__sensor__quantity.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Program_Sensor_Quantity : JzFragement(R.layout.fragment_frag__function__program__sensor__quantity) {

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun viewInit() {
        rootview.fixLanguage()
        rootview.read_MMY_Title.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year
        rootview.menu.setOnClickListener { JzActivity.getControlInstance().goMenu() }
        rootview.read_sensor.setOnClickListener {
            if(rootview.sensor_quantity.text.isEmpty()){
                return@setOnClickListener
            }
                JzActivity.getControlInstance().changePage(
                    Frag_Function_Program(
                        if(rootview.sensor_quantity.text.isEmpty()) "4" else (rootview.sensor_quantity.text.toString())
                    ),"Frag_Function_Program",true)
        }
        rootview.sensor_quantity.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable) {
                if(rootview.sensor_quantity.text.isEmpty()){
                    rootview.read_sensor.alpha= 0.7F
                }else{
                    rootview.read_sensor.alpha=1.0F
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

}
