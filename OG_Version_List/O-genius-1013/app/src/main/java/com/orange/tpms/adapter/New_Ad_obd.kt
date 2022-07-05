package com.orange.tpms.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.text.InputFilter
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.model.ObdBeans
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orange.tpms.ue.kt_frag.Frag_Function_New_OBD_ID_copy
import com.orange.tpms.utils.KeyboardUtil
import com.orango.electronic.jzutil.hexToByte
import com.orango.electronic.orange_og_lib.Callback.textchange
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.OggUtils
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.adapter_ad_function_show_read.view.*
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.*
import java.lang.Exception


class New_Ad_obd(public val beans: ObdBeans, var frag: Frag_Function_New_OBD_ID_copy) :
    JzAdapter(R.layout.adapter_ad_function_show_read) {
    val isDex = PublicBean.isDec()
    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as KtActivity
    var focus = if (frag.sensorInitial.isEmpty()) 0 else 1
    var position = 0
    var resourse = JzActivity.getControlInstance().getRootActivity().resources
    var togglSingleProgram = true
    var positionArray = arrayOf(
        resourse.jzString(R.string.app_fl),
        resourse.jzString(R.string.app_fr),
        resourse.jzString(R.string.app_rr),
        resourse.jzString(R.string.app_rl),
        "SP"
    )

    override fun sizeInit(): Int {
        return beans.wheelPosition.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        holder.mView.Vehice_ID.setOnClickListener { }
        holder.mView.New_ID.setOnClickListener { }
        holder.mView.Vehice_ID.visibility = View.VISIBLE
        holder.mView.New_ID.visibility = View.VISIBLE
        holder.mView.Vehice_ID.setTextColor(holder.mView.resources!!.getColor(R.color.Black))
        holder.mView.New_ID.setTextColor(holder.mView.resources!!.getColor(R.color.Black))
        when (PublicBean.position) {
            PublicBean.OBD_RELEARM -> {
                holder.mView.Vehice_ID.isEnabled = false
                holder.mView.Vehice_ID.setBackgroundResource(if (beans.OldSemsor[position].id.isEmpty() || (beans.state[position] == ObdBeans.PROGRAM_WAIT)) R.color.color_grey else R.color.white)
                holder.mView.New_ID.setOnClickListener { Log.e("focus", "$position") }
            }
            PublicBean.ID_COPY_OBD -> {
                holder.mView.Vehice_ID.isEnabled = false
                holder.mView.Vehice_ID.setBackground(holder.mView.context.resources!!.getDrawable(R.color.white))
                holder.mView.New_ID.setOnClickListener { Log.e("focus", "$position") }
            }
            PublicBean.複製傳感器 -> {
                holder.mView.Vehice_ID.isEnabled = false
                holder.mView.New_ID.setOnClickListener { Log.e("focus", "$position") }
                holder.mView.Vehice_ID.setOnClickListener { Log.e("focus", "$position") }
            }
        }
        holder.mView.Tire_check.setVisibility(View.VISIBLE)
        holder.mView.New_ID.isEnabled = beans.CanEdit
        holder.mView.Tire_check.setVisibility(View.GONE)
        holder.mView.Tire_text.text = beans.wheelPosition[position]
        holder.mView.Tire_text.setBackgroundColor(
            JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.gray)
        )
        holder.mView.Tire_text.setTextColor(
            JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.black)
        )
        if (PublicBean.wheelCount() == 2) {
            holder.mView.Tire_text.text = arrayOf("F", "R")[position]
        }
        if (PublicBean.position != PublicBean.OBD_RELEARM || (beans.state[position] != ObdBeans.PROGRAM_WAIT)) {
            if (isDex && beans.OldSemsor[position].id.isNotEmpty()) {
                var temp = beans.OldSemsor[position].id
                while (temp.length < 8) {
                    temp = "0${temp}"
                }
                holder.mView.Vehice_ID.setText("" + OggUtils.byteArrayToInt(temp.hexToByte()))
            } else {
                holder.mView.Vehice_ID.setText(beans.OldSemsor[position].id)
            }
        }

        if (isDex) {
            var temp = beans.NewSensor[position].id
            if (temp.isNotEmpty()) {
                while (temp.length < 8) {
                    temp = "0${temp}"
                }
                holder.mView.New_ID.setText("" + OggUtils.byteArrayToInt(temp.hexToByte()))
            } else {
                holder.mView.New_ID.setText("")
            }

        } else {
            holder.mView.New_ID.setText(beans.NewSensor[position].id)
        }


        holder.mView.Vehice_ID.setTextColor(holder.mView.context.resources!!.getColor(R.color.color_black))
        holder.mView.New_ID.setTextColor(holder.mView.context.resources!!.getColor(R.color.color_black))
        when (beans.state[position]) {
            ObdBeans.PROGRAM_FALSE -> {
                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_fail)
                when (PublicBean.position) {
                    PublicBean.ID_COPY_OBD -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources!!.getColor(R.color.color_orange))
                    }
                    PublicBean.OBD_RELEARM -> {
                        holder.mView.Vehice_ID.setTextColor(
                            holder.mView.context.resources!!.getColor(
                                R.color.color_orange
                            )
                        )
                    }
                    PublicBean.複製傳感器 -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources!!.getColor(R.color.color_orange))
                    }
                }
                try {
                    if (PublicBean.position == PublicBean.OBD_RELEARM) {
                        holder.mView.Tire_check.visibility = View.VISIBLE
                        beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                            R.mipmap.img_wheel_fail
                        )
                    } else {
                        beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                            R.mipmap.img_wheel_fail
                        )
                    }
                } catch (e: Exception) {

                }

            }
            ObdBeans.PROGRAM_WAIT -> {
                try {
                    beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                        R.mipmap.img_wheel_n
                    )
                } catch (e: Exception) {

                }

                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.color.white)
                when (PublicBean.position) {
                    PublicBean.ID_COPY_OBD -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources!!.getColor(R.color.color_orange))
                    }
                    PublicBean.OBD_RELEARM -> {
                        holder.mView.Vehice_ID.setTextColor(
                            holder.mView.context.resources!!.getColor(R.color.color_orange)
                        )
                    }
                    PublicBean.複製傳感器 -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources!!.getColor(R.color.color_orange))
                    }
                }
            }
            ObdBeans.PROGRAM_SUCCESS -> {
                holder.mView.New_ID.setTextColor(holder.mView.context.resources!!.getColor(R.color.color_black))
                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_ok)
                holder.mView.Vehice_ID.setTextColor(holder.itemView.context.resources!!.getColor(R.color.color_black));
                holder.mView.New_ID.setTextColor(holder.itemView.context.resources!!.getColor(R.color.color_black));
                try {
                    if (PublicBean.position == PublicBean.OBD_RELEARM) {
                        holder.mView.Tire_check.visibility = View.VISIBLE
                        beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                            R.mipmap.img_tirel_ok
                        )
                    } else {
                        beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                            R.mipmap.img_tirel_ok
                        )
                    }
                } catch (e: Exception) {

                }
            }
        }
        holder.mView.New_ID.textchange = textchange {
            frag.checkComplete()
        }
        holder.mView.Vehice_ID.textchange = textchange {
            frag.checkComplete()
        }

        holder.mView.New_ID.isFocusable = false
        holder.mView.New_ID.isEnabled = true
        holder.mView.Vehice_ID.isFocusable = false
        holder.mView.Vehice_ID.isEnabled = true
        holder.mView.New_ID.setTextIsSelectable(false)
        holder.mView.Vehice_ID.setTextIsSelectable(false)
        holder.mView.New_ID.setOnClickListener {
            if (beans.OldSemsor[position].id.isNotEmpty() && (beans.state[position] != ObdBeans.PROGRAM_SUCCESS)) {
                beans.selectRows = position
                frag.switchBtn()
                this.notifyDataSetChanged()
            }

            return@setOnClickListener
        }

        holder.mView.Vehice_ID.setOnClickListener {
            if (frag.sensorInitial.isNotEmpty()) {
                return@setOnClickListener
            }
            if (PublicBean.position == PublicBean.OBD_RELEARM || PublicBean.position == PublicBean.ID_COPY_OBD) {
                return@setOnClickListener
            }

            if (PublicBean.position == PublicBean.複製傳感器) {
                Log.e("Vehicefocus", "${position * 2}")
            }

            if (position * 2 == focus) {
                this.notifyDataSetChanged()
                return@setOnClickListener
            }
            focus = position * 2
            this.position = position
            this.notifyDataSetChanged()
        }
        holder.mView.New_ID.filters =
            arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(beans.idcount))
        holder.mView.Vehice_ID.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(beans.idcount))
        KeyboardUtil.hideEditTextKeyboard(holder.mView.New_ID)
        KeyboardUtil.hideEditTextKeyboard(holder.mView.Vehice_ID)
        if (beans.OldSemsor.size > 1) {
            if (beans.selectRows == position || (beans.state[position] == ObdBeans.PROGRAM_SUCCESS)) {
                holder.mView.New_ID.setBackgroundColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.white)
                )
                holder.mView.Vehice_ID.setBackgroundColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.white)
                )

            } else {
                holder.mView.New_ID.setBackgroundColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.gray)
                )
                holder.mView.Vehice_ID.setBackgroundColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.gray)
                )
            }
        }
        if(frag.single){
            holder.mView.New_ID.setOnClickListener {}
            holder.mView.Vehice_ID.setOnClickListener {  }
        }
        if (frag.single && (beans.state[position] == ObdBeans.PROGRAM_WAIT)) {
            if (togglSingleProgram) {
                holder.mView.Vehice_ID.setBackgroundColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.green)
                )
                holder.mView.Vehice_ID.setTextColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.white)
                )
                frag.rootview.sending_data.text = "jz.404".getFix()
            } else {
                holder.mView.Vehice_ID.setBackgroundColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.white)
                )
                holder.mView.Vehice_ID.setTextColor(
                    JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.color_black)
                )
                frag.rootview.sending_data.text = "jz.117".getFix()
            }

            holder.mView.Vehice_ID.setOnClickListener {
                togglSingleProgram = !togglSingleProgram
                notifyDataSetChanged()
            }
        }

    }

    fun insertID(position: Int) {
        if (beans.state.contains(ObdBeans.PROGRAM_FALSE) || beans.state.contains(ObdBeans.PROGRAM_SUCCESS)) {
            return
        }
        if (position * 2 == focus) {
            when (PublicBean.position) {
                PublicBean.OBD_RELEARM -> {
                }
                PublicBean.ID_COPY_OBD -> {
                }
                PublicBean.複製傳感器 -> {
                    EmptyDialog(R.layout.data_loading).show()
                    Thread {
                        val a = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                        if (a.success) {
                            removeOld(a.id)
                            beans.OldSemsor[position] = a
                            this.position = -1
                            focus = -1
                        }
                        JzActivity.getControlInstance().getHandler().post {
                            OgPublic.getInstance.playBeepSoundAndVibrate()
                            if (!a.success) {
                                JzActivity.getControlInstance().toast("jz.300".getFix())
                            }
                            JzActivity.getControlInstance().closeDiaLog("data_loading")
                            frag.checkComplete()
                            this@New_Ad_obd.notifyDataSetChanged()
                        }
                    }.start()
                }
            }
        } else if ((position * 2 + 1) == focus) {
            when (PublicBean.position) {
                PublicBean.OBD_RELEARM -> {
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.data_loading, false, false, "data_loading")
                    Thread {
                        val a = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                        if (a.success) {
                            removeNew(a.id)
                            beans.NewSensor[position] = a
                            this.position = -1
                            focus = -1
                        }
                        JzActivity.getControlInstance().getHandler().post {
                            OgPublic.getInstance.playBeepSoundAndVibrate()
                            if (!a.success) {
                                JzActivity.getControlInstance().toast("jz.300".getFix())
                            }
                            JzActivity.getControlInstance().closeDiaLog("data_loading")
                            frag.checkComplete()
                            this@New_Ad_obd.notifyDataSetChanged()
                        }
                    }.start()
                }
                PublicBean.ID_COPY_OBD -> {
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.data_loading, false, false, "data_loading")
                    Thread {
                        Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
                        val a = Cmd_Og.GetPr("00", 1, PublicBean.getHEX(), false)
                        for (i in a.sensorData) {
                            if (!checkSensor(i)) {
                                return@Thread
                            }
                            removeNew(i.id)
                            beans.NewSensor[position] = i
                            this.position = -1
                            focus = -1
                        }
                        JzActivity.getControlInstance().getHandler().post {
                            OgPublic.getInstance.playBeepSoundAndVibrate()
                            if (a.sensorData.size != 1) {
                                JzActivity.getControlInstance().toast("jz.300".getFix())
                            }
                            JzActivity.getControlInstance().closeDiaLog("data_loading")
                            frag.checkComplete()
                            this@New_Ad_obd.notifyDataSetChanged()
                        }
                    }.start()
                }
                PublicBean.複製傳感器 -> {
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.data_loading, false, false, "data_loading")
                    if (PublicBean.onlyCopy()) {
                        Thread {
                            val a = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                            if (a.success) {
                                removeNew(a.id)
                                beans.NewSensor[position] = a
                                this.position = -1
                                focus = -1
                            }
                            JzActivity.getControlInstance().getHandler().post {
                                OgPublic.getInstance.playBeepSoundAndVibrate()
                                if (!a.success) {
                                    JzActivity.getControlInstance().toast("jz.300".getFix())
                                }
                                JzActivity.getControlInstance().closeDiaLog("data_loading")
                                frag.checkComplete()
                                this@New_Ad_obd.notifyDataSetChanged()
                            }
                        }.start()
                        return
                    }
                    Thread {
                        Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
                        val a = Cmd_Og.GetPr("00", 1, PublicBean.getHEX(), false)
                        if ((a.type != "success") && (a.type != "cancel")) {
                            Log.e("update", a.type)
                            MysqDatabase.insertCopyResult(Cmd_Og.tx_memory.toString(), a.type)
                        }
                        for (i in a.sensorData) {
                            if (!checkSensor(i)) {
                                return@Thread
                            }
                            removeNew(i.id)
                            beans.NewSensor[position] = i
                            this.position = -1
                            focus = -1
                        }
                        JzActivity.getControlInstance().getHandler().post {
                            OgPublic.getInstance.playBeepSoundAndVibrate()
                            if (a.sensorData.size != 1) {
                                JzActivity.getControlInstance().toast("jz.300".getFix())
                            }
                            JzActivity.getControlInstance().closeDiaLog("data_loading")
                            frag.checkComplete()
                            this@New_Ad_obd.notifyDataSetChanged()
                        }
                    }.start()
                }
            }
        }

    }

    fun checkSensor(sensorData: SensorData): Boolean {
        if (sensorData.senesorType == "S2") {
            if (!PublicBean.getSIISensor()) {
                JzActivity.getControlInstance().getHandler().post {
                    JzActivity.getControlInstance().closeDiaLog()
                    if (!PublicBean.getSIISensor()) {
                        JzActivity.getControlInstance()
                            .showDiaLog(
                                true,
                                false,
                                object :
                                    SetupDialog(com.orango.electronic.orange_og_lib.R.layout.errorsensor) {
                                    override fun dismess() {
                                        JzActivity.getControlInstance().goMenu()
                                    }

                                    override fun keyevent(event: KeyEvent): Boolean {
                                        return true
                                    }

                                    override fun setup(rootview: Dialog) {
                                        rootview.findViewById<TextView>(com.orango.electronic.orange_og_lib.R.id.textView4)
                                            .text = "jz.464".getFix()
                                    }
                                },
                                "single_program"
                            )
                    }
                }
                return false
            } else if (beans.wheelPosition.size > 1) {
                JzActivity.getControlInstance().getHandler().post {
                    JzActivity.getControlInstance().closeDiaLog()
                    JzActivity.getControlInstance()
                        .showDiaLog(true, false, object : SetupDialog(R.layout.single_program) {
                            override fun dismess() {
                                JzActivity.getControlInstance().goBack()
                            }

                            override fun keyevent(event: KeyEvent): Boolean {
                                return true
                            }

                            override fun setup(rootview: Dialog) {
                                rootview.findViewById<TextView>(R.id.textView4).text =
                                    "jz.463".getFix()
                            }
                        }, "single_program")
                }
                return false
            }
        }
        return true
    }

    fun getEmpty(): Int {
        for (i in 0 until beans.NewSensor.size) {
            if (PublicBean.position == PublicBean.複製傳感器) {
                if (beans.OldSemsor[i].id.isEmpty()) {
                    return i * 2
                }
            }
            if (beans.NewSensor[i].id.isEmpty()) {
                return i * 2 + 1
            }
        }
        return -1
    }

    fun removeNew(a: String) {
        for (i in 0 until beans.NewSensor.size) {
            if (beans.NewSensor[i].id == a) {
                beans.NewSensor[i].id = ""
            }
        }
    }

    fun removeOld(a: String) {
        for (i in 0 until beans.OldSemsor.size) {
            if (beans.OldSemsor[i].id == a) {
                beans.OldSemsor[i].id = ""
            }
        }
    }
}