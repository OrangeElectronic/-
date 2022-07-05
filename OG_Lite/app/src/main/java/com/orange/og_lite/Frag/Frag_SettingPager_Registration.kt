package com.orange.og_lite.Frag

import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.jzfixlanguage.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_Http_Command_Function
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__registration.view.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Registration : JzFragement(R.layout.fragment_frag__setting_pager__registration) {

    var isrunn = false

    var Arealist1 = ArrayList<String>()
    var Arealist2 = ArrayList<String>()
    var Arealist3 = ArrayList<String>()

    override fun viewInit() {
        rootview.fixLanguage()
        Arealist1.clear()
        Arealist2.clear()
        Arealist3.clear()

        Arealist1.add("jz.440".getFix()) //"Select"
        Arealist1.add("jz.441".getFix()) //"EU"
        Arealist1.add("jz.442".getFix()) //"North America"
        Arealist1.add("jz.443".getFix()) //"台灣"
        Arealist1.add("jz.444".getFix()) //"中國大陸"
        val arrayAdapter1 = ArrayAdapter<String>(activity!!,
            R.layout.area_spinner, Arealist1)
        rootview.country_spinner.adapter = arrayAdapter1

        Arealist2.add("jz.208".getFix())
        Arealist2.add("jz.209".getFix())
        val arrayAdapter2 = ArrayAdapter<String>(activity!!,
            R.layout.area_spinner, Arealist2)
        rootview.company_spinner.adapter = arrayAdapter2

        rootview.cancel.setOnClickListener { JzActivity.getControlInstance().goBack() }

        rootview.next.setOnClickListener {
            if (isrunn) {
                return@setOnClickListener
            }

            var email = rootview.email.text.toString()
            var password = rootview.password.text.toString()
            var repeatpassword = rootview.repeatpassword.text.toString()
            var serialnumber =  rootview.serialnumber.text.toString()
            var firstname =  rootview.firstname.text.toString()
            var lastname =  rootview.lastname.text.toString()
            var company =  rootview.company.text.toString()
            var phone =  rootview.phone.text.toString()
            var store = rootview.company_spinner.selectedItem.toString()
            var country = rootview.country_spinner.selectedItem.toString()
            var streat =  rootview.streat.text.toString()
            var city = rootview.city.text.toString()
            var state = rootview.state.text.toString()
            var zpcode =  rootview.zpcode.text.toString()

            JzActivity.getControlInstance().showDiaLog(R.layout.data_loading,false,false,"dataloading")

            if (!password.equals(repeatpassword)) {
                Toast.makeText(act, resources.jzString(R.string.confirm_password), Toast.LENGTH_SHORT).show()
            }
            Thread {
                isrunn = true
                var a = 0
                if(store.equals(resources.jzString(R.string.Distributor))){
                    a = Util_Http_Command_Function.Register(email, password,"Distributor",serialnumber, company, firstname, lastname, phone, state, country, city, streat, zpcode)
                    //a=Fuction.Register(email,password,"empty","Distributor",company,firstname,lastname,phone,state,city,streat,zpcode)
                }else{
                    a = Util_Http_Command_Function.Register(email, password,"Retailer", serialnumber, company, firstname, lastname, phone, state, country, city, streat, zpcode)
                    //a=Fuction.Register(email,password,"empty","Retailer",company,firstname,lastname,phone,state,city,streat,zpcode)
                }

                handler.post {
                    JzActivity.getControlInstance().closeDiaLog()
                    if (a == -1) {
                        Toast.makeText(act, resources.jzString(R.string.error), Toast.LENGTH_SHORT).show()
                    } else if (a == 1) {
                        Toast.makeText(act, resources.jzString(R.string.be_register), Toast.LENGTH_SHORT).show()
                    } else {
                        //val profilePreferences = act.getSharedPreferences("Frag_SettingPager_Setting", Context.MODE_PRIVATE)
                        JzActivity.getControlInstance().setPro("admin", email)
                        JzActivity.getControlInstance().setPro("password", password)
                        JzActivity.getControlInstance().changePage(
                            Frag_MainActivity_Home(),
                            "Frag_MainActivity_Home", false)
                    }
                }
                isrunn = false
            }.start()

        }

    }

}
