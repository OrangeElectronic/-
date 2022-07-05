package com.orange.og_lite.Frag

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.tool.LanguageUtil
import com.orange.og_lite.R
import com.orange.og_lite.Util.*
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__set__languages.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Set_Languages(var changePlace:Int) : JzFragement(R.layout.fragment_frag__setting_pager__set__languages) {

    var Arealist = ArrayList<String>()
    var LanguageList = ArrayList<String>()
    var areaposition=0
    var languageposition=0

    override fun viewInit() {
        rootview.fixLanguage()
        //JzActivity.getControlInstance().getPro("Languages", LOCALE_ENGLISH)
        Arealist.add("jz.440".getFix()) //"Select"
        Arealist.add("jz.441".getFix()) //"EU"
        Arealist.add("jz.442".getFix()) //"North America"
        Arealist.add("jz.443".getFix()) //"台灣"
        Arealist.add("jz.444".getFix()) //"中國大陸"
        val arrayAdapter = ArrayAdapter<String>(activity!!,
            R.layout.language_spinner, Arealist)
        rootview.Area_spinner.adapter=arrayAdapter
        LanguageList.add("jz.440".getFix()) //"Select"
        LanguageList.add("jz.239".getFix()) //"繁體中文"
        LanguageList.add("jz.238".getFix()) //"简体中文"
        LanguageList.add("jz.241".getFix()) //"Deutsche"
        LanguageList.add("jz.237".getFix()) //"English"
        LanguageList.add("jz.240".getFix()) //"Italiano"
        LanguageList.add("jz.453".getFix()) //"Slovinčina"
        LanguageList.add("jz.454".getFix()) //"DANISH"

        val lanAdapter = ArrayAdapter<String>(activity!!,
            R.layout.language_spinner, LanguageList)
        rootview.Languages_spinner.adapter=lanAdapter
        rootview.Area_spinner.setSelection(JzActivity.getControlInstance().getPro("aIndex",0),true)
        rootview.Languages_spinner.setSelection(JzActivity.getControlInstance().getPro("lIndex",0),true)
        rootview.Area_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
            {
                areaposition=pos
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {

            }
        }

        rootview.Languages_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
            {
                languageposition=pos
                //JzActivity.getControlInstance().changePage(Frag_SettingPager_Set_Languages(),"Frag_SettingPager_Set_Languages",false)
                when (rootview.Languages_spinner.selectedItem.toString()) {

                    "繁體中文" -> {
                        SetLan_live(LOCALE_TAIWAIN)
                    }
                    "简体中文" -> {
                        SetLan_live( LOCALE_CHINESE)
                    }
                    //Deutsche
                    "Deutsch" -> {
                        SetLan_live(LOCALE_DE)
                    }
                    "English" -> {
                        SetLan_live(LOCALE_ENGLISH)
                    }
                    "Italiano" -> {
                        SetLan_live(LOCALE_ITALIANO)
                    }
                    "Slovinčina" -> {
                        SetLan_live(LOCALE_SK)
                    }
                    "DANISH" -> {
                        SetLan_live(LOCALE_DA)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {

            }
        }

        rootview.Set_up_button.setOnClickListener {
            if (rootview.Area_spinner.selectedItem.toString().equals("Select") || rootview.Languages_spinner.selectedItem.toString().equals(
                "Select"
            )
        ) {
            return@setOnClickListener
        }
            Log.e("Area_spinner",rootview.Area_spinner.selectedItem.toString())
            Log.e("Area_spinner_getPro",JzActivity.getControlInstance().getPro("Area", rootview.Area_spinner.selectedItem.toString()))
            if(JzActivity.getControlInstance().getPro("Area", rootview.Area_spinner.selectedItem.toString()) != rootview.Area_spinner.selectedItem.toString()){
                Log.e("Area_spinner_Item",rootview.Area_spinner.selectedItem.toString())

                when (rootview.Area_spinner.selectedItem.toString()) {
                    "EU" -> {
                        Util_FileDowload.country = "EU"
                    }
                    "North America" -> {
                        Util_FileDowload.country = "US"
                    }
                    "台灣" -> {
                        Util_FileDowload.country = "TW"
                    }
                    "中國大陸" -> {
                        Util_FileDowload.country = "TW"
                    }
                }
                Log.e("Area_spinner_area",Util_FileDowload.country)
                Util_FileDowload.clearInit()
            }

            JzActivity.getControlInstance().setPro("Area", rootview.Area_spinner.selectedItem.toString());
            JzActivity.getControlInstance().setPro("Language",  rootview.Languages_spinner.selectedItem.toString());
            Log.d("Language", rootview.Languages_spinner.selectedItem.toString())
            when (rootview.Languages_spinner.selectedItem.toString()) {
                "繁體中文" -> {
                    SetLan(LOCALE_TAIWAIN)
                }
                "简体中文" -> {
                    SetLan(LOCALE_CHINESE)
                }
                //Deutsche
                "Deutsch" -> {
                    SetLan(LOCALE_DE)
                }
                "English" -> {
                    SetLan(LOCALE_ENGLISH)
                }
                "Italiano" -> {
                    SetLan(LOCALE_ITALIANO)
                }
                "Slovinčina" -> {
                    SetLan(LOCALE_SK)
                }
                "DANISH" -> {
                    SetLan(LOCALE_DA)
                }
            }
        }
    }

    open fun SetLan_live(value:String){
        when(value){
            LOCALE_ENGLISH->{
            JzActivity.getControlInstance().setLanguage(Locale("en"))
            }
            LOCALE_CHINESE->{
                JzActivity.getControlInstance().setLanguage(Locale("zh-rCN") )}
            LOCALE_TAIWAIN->{
                JzActivity.getControlInstance().setLanguage(Locale("tw"))}
            LOCALE_ITALIANO->{
                JzActivity.getControlInstance().setLanguage(Locale("it"))}
            LOCALE_DE->{
                JzActivity.getControlInstance().setLanguage(Locale("de"))}
            LOCALE_SK->{ 
                JzActivity.getControlInstance().setLanguage(Locale("sk"))}
            LOCALE_DA->{
                JzActivity.getControlInstance().setLanguage(Locale("da"))}
        }
        JzActivity.getControlInstance().setPro("aIndex",rootview.Area_spinner.selectedItemPosition)
        JzActivity.getControlInstance().setPro("lIndex",rootview.Languages_spinner.selectedItemPosition)
        if(changePlace == 0)
        {
            JzActivity.getControlInstance().changePage(
                Frag_SettingPager_Set_Languages(
                    0
                ),"Frag_SettingPager_Set_Languages_first_Live",false)
        }
        else
        {
            JzActivity.getControlInstance().changePage(
                Frag_SettingPager_Set_Languages(
                    1
                ),"Frag_SettingPager_Set_Languages_Live",false)
        }
    }

    open fun SetLan(value:String){
        JzActivity.getControlInstance().setPro("Languages",value)
        JzActivity.getControlInstance().setPro("aIndex",rootview.Area_spinner.selectedItemPosition)
        JzActivity.getControlInstance().setPro("lIndex",rootview.Languages_spinner.selectedItemPosition)
        if(JzActivity.getControlInstance().getPro("Area", rootview.Area_spinner.selectedItem.toString()) != rootview.Area_spinner.selectedItem.toString()){
            Log.e("Area_spinner_Item",rootview.Area_spinner.selectedItem.toString())

            when (rootview.Area_spinner.selectedItem.toString()) {
                "EU" -> {
                    Util_FileDowload.country = "EU"
                }
                "North America" -> {
                    Util_FileDowload.country = "US"
                }
                "台灣" -> {
                    Util_FileDowload.country = "TW"
                }
                "中國大陸" -> {
                    Util_FileDowload.country = "TW"
                }
            }
            Log.e("Area_spinner_area",Util_FileDowload.country)
            Util_FileDowload.clearInit()
        }
        if(changePlace == 0)
        {
            JzActivity.getControlInstance().changePage(
                Frag_SettingPager_PrivaryPolicy(
                    0
                ),"Frag_SettingPager_PrivaryPolicy_first",false)
        }
        else
        {
            JzActivity.getControlInstance().goMenu()
        }
    }

    val LOCALE_ENGLISH="en"
    val LOCALE_CHINESE="zh"
    val LOCALE_TAIWAIN="tw"
    val LOCALE_ITALIANO="it"
    val LOCALE_DE="de"
    val LOCALE_SK="sk"
    val LOCALE_DA="da"

}
