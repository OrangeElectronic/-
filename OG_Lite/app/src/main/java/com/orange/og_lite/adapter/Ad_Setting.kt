package com.orange.og_lite.adapter

import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.orange.og_lite.Util.fixLanguage
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.*
import com.orange.og_lite.Dialog.Da_Logout
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.beans.SettingItem
import kotlinx.android.synthetic.main.adapter_ad_setting.view.*

class Ad_Setting(val myitem: SettingItem) : JzAdapter(R.layout.adapter_ad_setting) {
    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (myitem.tag[position] == ("Frag_SelectMmyPage_Make")) {
            holder.mView.Setting_button.visibility = View.GONE
            holder.mView.big_button.visibility = View.VISIBLE
        } else {
            holder.mView.Setting_button.visibility = View.VISIBLE
            holder.mView.big_button.visibility = View.GONE
        }

        holder.mView.imageView6.setBackgroundResource(myitem.item[position])

        holder.mView.textView17.text = myitem.item2[position]

        holder.mView.textView37.text = myitem.item3[position]
        holder.mView.textView37.visibility = myitem.item3_view[position]

        if (myitem.change[position] != null) {
            holder.mView.Setting_R.setOnClickListener {
                if (myitem.tag[position] == "reset") {
                    JzActivity.getControlInstance()
                        .showDiaLog(true, false, object : SetupDialog(R.layout.logout) {
                            override fun dismess() {

                            }

                            override fun keyevent(event: KeyEvent): Boolean {
                                return true
                            }

                            override fun setup(rootview: Dialog) {
                                rootview.findViewById<TextView>(R.id.textView16).text = JzActivity.getControlInstance().getRootActivity().resources.jzString(R.string.app_reset_all_settings)
                                rootview.findViewById<TextView>(R.id.yes).text = "jz.62".getFix()

                                rootview.findViewById<TextView>(R.id.cancel).setOnClickListener {
                                    JzActivity.getControlInstance().closeDiaLog()
                                }
                                rootview.findViewById<TextView>(R.id.yes).setOnClickListener {
                                    JzActivity.getControlInstance().closeDiaLog()
                                    JzActivity.getControlInstance().clearPro()
                                    JzActivity.getControlInstance().restart(MainActivity::class.java)
                                }
                            }

                        }, "reset")
                    return@setOnClickListener
                }
                if(myitem.tag[position] == "logout")
                {
                    JzActivity.getControlInstance()
                        .showDiaLog(true, false, Da_Logout(), "logout")
                    return@setOnClickListener
                }
                //Frag_SettingPager_Set_Languages.changePlace = 1
                if (myitem.tag[position] == ("Frag_Function_QrcodeScanner") || myitem.tag[position] == ("Frag_SelectMmyPage_Make") ||
                    myitem.tag[position] == ("Frag_SelectMmyPage_MyFavorite") || myitem.tag[position] == ("Frag_Function_Detecting")
                ) {
                    if (!mMainActivity.manager.Ble_Helper.isConnect() || !mMainActivity.manager.Ble_Helper.bleadapter.isEnabled) {
                        JzActivity.getControlInstance().showDiaLog(false, false,
                            Da_Scan_ble(object : connectBack {
                                override fun connec() {
                                    JzActivity.getControlInstance().changePage(
                                        myitem.change[position]!!,

                                        myitem.tag[position],
                                        true
                                    )
                                }
                            }), "Da_Scan_ble"
                        )
                    } else {
                        JzActivity.getControlInstance()
                            .changePage(
                                myitem.change[position]!!,

                                myitem.tag[position],
                                true
                            )
                    }
                } else {
                    JzActivity.getControlInstance()
                        .changePage(
                            myitem.change[position]!!,

                            myitem.tag[position],
                            true
                        )
                }
            }
        }

    }

    override fun sizeInit(): Int {
        return myitem.item.size
    }

}