package com.orange.tpms.ue.kt_frag

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.jzutil.storeObject
import com.orango.electronic.orange_og_lib.Dialog.Dia_Check_Data
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.frag_select_area.view.*


class Frag_Select_Area(var model: Dia_Check_Data.Md_UserInforMation) :JzFragement(R.layout.frag_select_area){
    var countryList: MutableMap<String, String> = mutableMapOf()
    var regionList: MutableMap<String, Array<String>> = mutableMapOf()
    var selectCountry="Italia"
    override fun viewInit() {
        rootview.fixLanguage()
        countryList["Italia"] = "義大利"
        countryList["Deutschland"] = "德國"
        countryList["United States"] = "美國"
        countryList["台灣"] = "台灣"
        countryList["日本"] = "日本"
        countryList["jz.514".getFix()] = "其他"
        regionList["jz.514".getFix()]= arrayOf("NA")
        regionList["Schweiz"]= arrayOf(
            "Zürich",
            "Kanton Bern",
            "Luzern",
            "Kanton Uri",
            "Schwyz",
            "Obwalden",
            "Nidwalden",
            "Glarus",
            "Zug",
            "Freiburg",
            "Solothurn",
            "Basel-City",
            "Baselland",
            "Kanton Schaffhausen",
            "Appenzell Ausserrhoden",
            "appenzell innerrhoden",
            "St.Gallen",
            "Grisons/Graubünden",
            "Aargau",
            "Thurgau",
            "Tessin",
            "Vaud",
            "Kanton Wallis",
            "Neuenburg",
            "Kanton Genf",
            "Jura"
        )
        regionList["台灣"] = arrayOf(
            "臺北市",
            "新北市",
            "基隆市",
            "桃園市",
            "新竹市",
            "新竹縣",
            "宜蘭縣",
            "苗栗縣",
            "臺中市",
            "彰化縣",
            "南投縣",
            "雲林縣",
            "嘉義市",
            "嘉義縣",
            "臺南市",
            "高雄市",
            "屏東縣",
            "澎湖縣",
            "花蓮縣",
            "臺東縣",
            "金門縣",
            "連江縣"
        )
        regionList["Italia"] =
            arrayOf(
                "Abruzzo",
                "Valle d'Aosta",
                "Puglia",
                "Basilicata",
                "Calabria",
                "Campania",
                "Emilia-Romagna",
                "Friuli-Venezia Giulia",
                "Lazio",
                "Liguria",
                "Lombardia",
                "Marche",
                "Molise",
                "Piemonte",
                "Sardegna",
                "Sicilia",
                "Trentino-Alto Adige",
                "Toscana",
                "Umbria",
                "Veneto"
            )
        regionList["Deutschland"] = arrayOf(
            "Baden-Württemberg",
            "Bayern",
            "Berlin",
            "Brandenburg",
            "Bremen",
            "Hamburg",
            "Hessen",
            "Mecklenburg-Vorpommern",
            "Niedersachsen",
            "Nordrhein-Westfalen",
            "Rheinland-Pfalz",
            "Saarland",
            "Sachsen",
            "Sachsen-Anhalt",
            "Schleswig-Holstein",
            "Thüringen"
        )
        regionList["United States"] = arrayOf(
            "Washington, D.C.",
            "Maine",
            "New Hampshire",
            "Massachusetts",
            "Rhode Island",
            "Connecticut",
            "New Jersey",
            "Delaware",
            "Maryland",
            "West Virginia",
            "New York",
            "Pennsylvania",
            "Virginia",
            "North Carolina",
            "South Carolina",
            "Georgia",
            "Florida",
            "Michigan",
            "Ohio",
            "Indiana",
            "Kentucky",
            "Tennessee",
            "Mississippi",
            "Alabama",
            "Illinois",
            "Minnesota",
            "Iowa",
            "Missouri",
            "Arkansas",
            "Louisiana",
            "North Dakota",
            "South Dakota",
            "Nebraska",
            "Kansas",
            "Oklahoma",
            "Texas",
            "Montana",
            "Wyoming",
            "Colorado",
            "Mexico",
            "Idaho",
            "Utah",
            "Arizona",
            "Washington",
            "Oregon",
            "Nevada",
            "California"
        )
        regionList["日本"] = arrayOf(
            "あいちけん",
            "あきたけん",
            "あおもりけん",
            "ちばけん",
            "えひめけん",
            "ふくいけん",
            "ふくおかけん",
            "ふくしまけん",
            "ぎふけん",
            "ぐんまけん",
            "ひろしまけん",
            "ほっかいどう",
            "ひょうごけん",
            "いばらきけん",
            "いしかわけん",
            "いわてけん",
            "かがわけん",
            "かごしまけん",
            "かながわけん",
            "こうちけん",
            "くまもとけん",
            "きょうとふ",
            "みえけん",
            "みやぎけん",
            "みやざきけん",
            "ながのけん",
            "ながさきけん",
            "ならけん",
            "にいがたけん",
            "おおいたけん",
            "おかやまけん",
            "おきなわけん",
            "おおさかふ",
            "さがけん",
            "さいたまけん",
            "しがけん",
            "しまねけん",
            "しずおかけん",
            "とちぎけん",
            "とくしまけん",
            "とうきょうと",
            "とっとりけん",
            "とやまけん",
            "わかやまけん",
            "やまがたけん",
            "やまぐちけん",
            "やまなしけん"
        )
        rootview.areaSpinner.adapter = ArrayAdapter<String>(
            JzActivity.getControlInstance().getRootActivity(),
            com.orango.electronic.orange_og_lib.R.layout.spinner,
            ArrayList<String>(countryList.keys)
        )
        rootview.regionSpinner.adapter = ArrayAdapter<String>(
            JzActivity.getControlInstance().getRootActivity(),
            com.orango.electronic.orange_og_lib.R.layout.spinner,
            ArrayList<String>(regionList[selectCountry]!!.toList())
        )
        rootview.areaSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                selectCountry= ArrayList<String>(countryList.keys)[position]
                rootview.regionSpinner.adapter = ArrayAdapter<String>(
                    JzActivity.getControlInstance().getRootActivity(),
                    com.orango.electronic.orange_og_lib.R.layout.spinner,
                    ArrayList<String>(regionList[selectCountry]!!.toList())
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
        rootview.enter.setOnClickListener {
            model.countrySelect=Gson().toJson(CountrySelect(countryList[rootview.areaSpinner.selectedItem.toString()]!!,rootview.regionSpinner.selectedItem.toString()))
            model.storeObject("needUpdate")
            model.storeObject("Md_UserInforMation")
            JzActivity.getControlInstance().setHome(Frag_home(),"Frag_home")
        }

    }
    data class CountrySelect(var country:String,var state:String)

}