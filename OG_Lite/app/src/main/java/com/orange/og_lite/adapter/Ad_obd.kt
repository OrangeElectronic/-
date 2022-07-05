package com.orange.og_lite.adapter

import android.content.Context
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Command
import com.orange.og_lite.Command.Companion.handler
import com.orange.og_lite.Dialog.Da_SoftKeyboard
import com.orange.og_lite.Frag.Frag_Function_OBD_ID_copy
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.KeyboardUtil
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.textchange
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.beans.Bs_SoftKeyboard
import kotlinx.android.synthetic.main.adapter_ad_function_show_read.view.*
import kotlinx.android.synthetic.main.da_softkeyboard.*
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.*
import java.lang.reflect.Method
import java.util.logging.Handler


class Ad_obd(public var beans: ObdBeans, var frag: Frag_Function_OBD_ID_copy) :
    JzAdapter(R.layout.adapter_ad_function_show_read) {
    var canShowKeyBoard = false
    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity
    var focus = -1
    var position = -1
    var resourse = JzActivity.getControlInstance().getRootActivity().resources
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        holder.mView.Vehice_ID.setOnClickListener { }
        holder.mView.New_ID.setOnClickListener { }
        //holder.mView.Vehice_ID.isCursorVisible = false
        //holder.mView.New_ID.isCursorVisible = false
        holder.mView.Vehice_ID.visibility = View.VISIBLE
        holder.mView.New_ID.visibility = View.VISIBLE
        holder.mView.Vehice_ID.setTextColor(holder.mView.resources.getColor(R.color.Black))
        holder.mView.New_ID.setTextColor(holder.mView.resources.getColor(R.color.Black))

//        if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
//            Log.e("Touch", "KEY_IN")
//            holder.mView.New_ID.setOnTouchListener { view, motionEvent ->
//                if (position * 2 == focus) {
//
//                    holder.mView.Vehice_ID.setBackgroundResource(R.color.green)
//                    holder.mView.Vehice_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
//                    //this.notifyDataSetChanged()
//                } else if ((position * 2 + 1) == focus) {
//                    holder.mView.New_ID.setBackgroundResource(R.color.green)
//                    holder.mView.New_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
//                    //this.notifyDataSetChanged()
//                }
//                true
//            }
//            holder.mView.Vehice_ID.setOnTouchListener { view, motionEvent ->
//                if (position * 2 == focus) {
//                    holder.mView.Vehice_ID.setBackgroundResource(R.color.green)
//                    holder.mView.Vehice_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
//                    //this.notifyDataSetChanged()
//                } else if ((position * 2 + 1) == focus) {
//                    holder.mView.New_ID.setBackgroundResource(R.color.green)
//                    holder.mView.New_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
//                    //this.notifyDataSetChanged()
//                }
//                true
//            }
//        }

        when (PublicBeans.position) {
            PublicBeans.OBD學碼 -> {
                holder.mView.Vehice_ID.isEnabled = false
                holder.mView.Vehice_ID.setBackgroundResource(if (beans.OldSemsor[position].isEmpty()) R.color.color_grey else R.color.white)
                holder.mView.New_ID.setOnClickListener {
                    Log.e("focus", "$position")
                    if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
                        val imm =
                            JzActivity.getControlInstance().getRootActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager?
                        imm!!.showSoftInput(holder.mView.New_ID, 0);
                    }
                }
            }
            PublicBeans.OBD複製 -> {
                holder.mView.Vehice_ID.isEnabled = false
                holder.mView.Vehice_ID.setBackground(holder.mView.context.resources.getDrawable(R.color.white))
                holder.mView.New_ID.setOnClickListener {
                    Log.e("focus", "$position")
                    if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
                        val imm =
                            JzActivity.getControlInstance().getRootActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager?
                        imm!!.showSoftInput(holder.mView.New_ID, 0);
                    }
                }
            }
            PublicBeans.複製ID -> {
                holder.mView.Vehice_ID.isEnabled = false
                holder.mView.New_ID.setOnClickListener {
                    Log.e("focus", "$position")
                    if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
                        val imm =
                            JzActivity.getControlInstance().getRootActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager?
                        imm!!.showSoftInput(holder.mView.New_ID, 0);
                    }
                }
                holder.mView.Vehice_ID.setOnClickListener {
                    Log.e("focus", "$position")
                    if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
                        val imm =
                            JzActivity.getControlInstance().getRootActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager?
                        imm!!.showSoftInput(holder.mView.Vehice_ID, 0);
                    }
                }
            }
        }
        holder.mView.Tire_check.setVisibility(View.VISIBLE)
        holder.mView.New_ID.isEnabled = beans.CanEdit
        holder.mView.Tire_check.setVisibility(View.GONE)
        holder.mView.Tire_text.text = beans.wheelPosition[position]
        holder.mView.Vehice_ID.setText(beans.OldSemsor[position])
        holder.mView.New_ID.setText(beans.NewSensor[position])
        holder.mView.Vehice_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_black))
        holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_black))
        when (beans.state[position]) {
            ObdBeans.PROGRAM_FALSE -> {
                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_fail)
                when (PublicBeans.position) {
                    PublicBeans.OBD複製 -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))
                    }
                    PublicBeans.OBD學碼 -> {
                        holder.mView.Vehice_ID.setTextColor(
                            holder.mView.context.resources.getColor(
                                R.color.color_orange
                            )
                        )
                    }
                    PublicBeans.複製ID -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))
                    }
                }
                if (PublicBeans.position == PublicBeans.OBD學碼) {
                    holder.mView.Tire_check.visibility = View.VISIBLE
                    beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                        R.mipmap.img_wheel_fail
                    )
                } else {
                    beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                        R.mipmap.img_wheel_fail
                    )
                }
            }
            ObdBeans.PROGRAM_WAIT -> {
                beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                    R.mipmap.img_wheel_n
                )
                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.color.white)
                when (PublicBeans.position) {
                    PublicBeans.OBD複製 -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))
                    }
                    PublicBeans.OBD學碼 -> {
                        holder.mView.Vehice_ID.setTextColor(
                            holder.mView.context.resources.getColor(
                                R.color.color_orange
                            )
                        )
                    }
                    PublicBeans.複製ID -> {
                        holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_orange))
                    }
                }
            }
            ObdBeans.PROGRAM_SUCCESS -> {
                holder.mView.New_ID.setTextColor(holder.mView.context.resources.getColor(R.color.color_black))
                holder.mView.Tire_check.visibility = View.VISIBLE
                holder.mView.Tire_check.setImageResource(R.mipmap.icon_check_sensor_ok)
                holder.mView.Vehice_ID.setTextColor(holder.itemView.context.resources.getColor(R.color.color_black));
                holder.mView.New_ID.setTextColor(holder.itemView.context.resources.getColor(R.color.color_black));
                if (PublicBeans.position == PublicBeans.OBD學碼) {
                    holder.mView.Tire_check.visibility = View.VISIBLE
                    beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                        R.mipmap.img_tirel_ok
                    )
                } else {
                    beans.Tire_img[positionArray.indexOf(beans.wheelPosition[position])].setBackgroundResource(
                        R.mipmap.img_tirel_ok
                    )
                }
            }
        }
        holder.mView.New_ID.textchange = textchange {
            Log.e("isEmpty1","true")
            beans.NewSensor[position] = holder.mView.New_ID.text.toString()
            frag.checkComplete()
            //checkComplete(holder)
        }
        holder.mView.Vehice_ID.textchange = textchange {
            Log.e("isEmpty2","true")
            beans.OldSemsor[position] = holder.mView.Vehice_ID.text.toString()
            frag.checkComplete()
            //checkComplete(holder)
        }

//        holder.mView.New_ID.setOnFocusChangeListener { view, b ->
//                if(b&&focus!=position * 2 + 1)
//                {
//                    Log.e("FocusChange","FocusChange N_${focus}P_${position * 2 + 1}")
//                    if (position * 2 + 1 == focus) {
//                        focus = -1
//                        handler.post {    this.notifyDataSetChanged()  }
//                        return@setOnFocusChangeListener
//                    }
//                    focus = position * 2 + 1
//                    this.position = position
//                    handler.post {    this.notifyDataSetChanged()  }
//                    return@setOnFocusChangeListener
//                }
//
//        }
//        holder.mView.Vehice_ID.setOnFocusChangeListener { view, b ->
//                if(b&&focus!=position * 2 )
//                {
//                    Log.e("FocusChange","FocusChange V_${focus}P_${position * 2}")
//                    if (position * 2 == focus) {
//                        focus = -1
//                        handler.post {    this.notifyDataSetChanged()  }
//                        return@setOnFocusChangeListener
//                    }
//                    focus = position * 2
//                    this.position = position
//                    handler.post {    this.notifyDataSetChanged()  }
//
//                    return@setOnFocusChangeListener
//                }
//
//        }
        holder.mView.New_ID.isFocusable = false
        holder.mView.New_ID.isEnabled = true
        holder.mView.Vehice_ID.isFocusable = false
        holder.mView.Vehice_ID.isEnabled = true
        holder.mView.New_ID.setTextIsSelectable(false)
        holder.mView.Vehice_ID.setTextIsSelectable(false)
//        if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
//            holder.mView.New_ID.isFocusable = true
//            holder.mView.New_ID.isFocusable = false
//            holder.mView.Vehice_ID.isFocusable = false
//            holder.mView.New_ID.isEnabled = true
//            if (PublicBeans.position == PublicBeans.複製ID) {
//                //holder.mView.Vehice_ID.isFocusable = true
//                holder.mView.Vehice_ID.isEnabled = true
//                holder.mView.Vehice_ID.setTextIsSelectable(true)
//            }
//            holder.mView.New_ID.setTextIsSelectable(true)
//        } else {
//            holder.mView.New_ID.isFocusable = false
//            holder.mView.New_ID.isEnabled = true
//            holder.mView.Vehice_ID.isFocusable = false
//            holder.mView.Vehice_ID.isEnabled = true
//            if (PublicBeans.position == PublicBeans.複製ID) {
//                holder.mView.Vehice_ID.setTextIsSelectable(true)
//            }
//            holder.mView.New_ID.setTextIsSelectable(false)
//            holder.mView.Vehice_ID.setTextIsSelectable(false)
//        }

        //var handler:Handler

        holder.mView.New_ID.setOnClickListener {
            if (PublicBeans.selectWay == PublicBeans.SCAN) {
                PublicBeans.getSensor(object : PublicBeans.sensorBack {
                    override fun callback(content: String) {
                        holder.mView.New_ID.setText(content)
                        toEmpty()
                    }
                })
                return@setOnClickListener
            }
            Log.e("New_IDfocus", "${position * 2 + 1}")

            if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
//                val imm =
//                    JzActivity.getControlInstance().getRootActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//                imm!!.showSoftInput(holder.mView.New_ID, 0);

//                JzActivity.getControlInstance().getRootActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//        try {
//            val cls = EditText::class.java
//            val setShowSoftInputOnFocus: Method
//            setShowSoftInputOnFocus = cls.getMethod(
//                "setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType
//            )
//            setShowSoftInputOnFocus.setAccessible(true)
//            setShowSoftInputOnFocus.invoke(holder.mView.New_ID.text, false)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
            }

            if (position * 2 + 1 == focus) {
                focus = -1
                this.notifyDataSetChanged()
                return@setOnClickListener
            }
            focus = position * 2 + 1
            this.position = position

            //insertID(position)
//                Select_Tire(holder,position)
            this.notifyDataSetChanged()
        }

        holder.mView.Vehice_ID.setOnClickListener {
            if (PublicBeans.selectWay == PublicBeans.SCAN) {
                PublicBeans.getSensor(object : PublicBeans.sensorBack {
                    override fun callback(content: String) {
                        holder.mView.Vehice_ID.setText(content)
                        toEmpty()
                    }
                })
                return@setOnClickListener
            }
            if (PublicBeans.position == PublicBeans.OBD學碼 || PublicBeans.position == PublicBeans.OBD複製) {
                return@setOnClickListener
            }

            if (PublicBeans.position == PublicBeans.複製ID) {
                Log.e("Vehicefocus", "${position * 2}")
                if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
//                    val imm =
//                        JzActivity.getControlInstance().getRootActivity().getSystemService(
//                            Context.INPUT_METHOD_SERVICE
//                        ) as InputMethodManager?
//                    imm!!.showSoftInput(holder.mView.Vehice_ID, 0);

//                    JzActivity.getControlInstance().getRootActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//                    try {
//                        val cls = EditText::class.java
//                        val setShowSoftInputOnFocus: Method
//                        setShowSoftInputOnFocus = cls.getMethod(
//                            "setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType
//                        )
//                        setShowSoftInputOnFocus.setAccessible(true)
//                        setShowSoftInputOnFocus.invoke(holder.mView.Vehice_ID.text, false)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }

//                    holder.mView.Vehice_ID.requestFocus()
                }
            }

            if (position * 2 == focus) {
                focus = -1
                this.notifyDataSetChanged()
                return@setOnClickListener
            }
            focus = position * 2
            this.position = position

            //insertID(position)
//                Select_Tire(holder,position)
            this.notifyDataSetChanged()
        }
//            if(PublicBeans.position == PublicBeans.複製ID){
//                if(position * 2==focus){
//                    holder.mView.Vehice_ID.setBackgroundResource(R.color.color_red)
//                }
//                if((position *2 +1)==focus){
//                    holder.mView.New_ID.setBackgroundResource(R.color.color_red)
//                }
//            }

        //Log.e("focus","Da_SoftKeyboard")
        Log.e("focus","Da_SoftKeyboard : "+ focus)
        if (position * 2 == focus) {
            holder.mView.Vehice_ID.setBackgroundResource(R.color.green)
            holder.mView.Vehice_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
            holder.mView.Vehice_ID.isEnabled = true

            Log.e("focus","Da_SoftKeyboard")

            if (PublicBeans.selectWay == PublicBeans.KEY_IN && PublicBeans.DialogKeyboard) {
                holder.mView.Vehice_ID.isFocusable = true
                holder.mView.Vehice_ID.setTextIsSelectable(true)
                holder.mView.Vehice_ID.requestFocus()
                PublicBeans.DialogKeytext = holder.mView.Vehice_ID.text.toString()
                 if(canShowKeyBoard){
                     JzActivity.getControlInstance().closeDiaLog("Da_SoftKeyboard")
                     JzActivity.getControlInstance().showBottomSheetDialog(true,true, Da_SoftKeyboard(this),"Da_SoftKeyboard")
                 }
            }
            //this.notifyDataSetChanged()
        } else if ((position * 2 + 1) == focus) {

            Log.e("focus","Da_SoftKeyboard")

            holder.mView.New_ID.setBackgroundResource(R.color.green)
            holder.mView.New_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
            holder.mView.New_ID.isEnabled = true
            if (PublicBeans.selectWay == PublicBeans.KEY_IN && PublicBeans.DialogKeyboard) {
                holder.mView.New_ID.isFocusable = true
                holder.mView.New_ID.setTextIsSelectable(true)
                holder.mView.New_ID.requestFocus()
                PublicBeans.DialogKeytext = holder.mView.New_ID.text.toString()
                if(canShowKeyBoard){
                    JzActivity.getControlInstance().closeDiaLog("Da_SoftKeyboard")
                    JzActivity.getControlInstance().showBottomSheetDialog(true,true, Da_SoftKeyboard(this),"Da_SoftKeyboard")
                }
            }
            //this.notifyDataSetChanged()
        }

        if(!PublicBeans.DialogKeyboard)
        {
            PublicBeans.DialogKeyboard = true
        }

//        if(holder.mView.Vehice_ID.isCursorVisible)
//        {
//            holder.mView.Vehice_ID.setBackgroundResource(R.color.green)
//            holder.mView.Vehice_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
//        }
//        if(holder.mView.New_ID.isCursorVisible)
//        {
//            holder.mView.New_ID.setBackgroundResource(R.color.green)
//            holder.mView.New_ID.setTextColor(holder.mView.resources.getColor(R.color.white))
//        }

        holder.mView.New_ID.filters =
            arrayOf<InputFilter>(InputFilter.AllCaps(), InputFilter.LengthFilter(beans.idcount))
        holder.mView.Vehice_ID.filters =
            arrayOf<InputFilter>(InputFilter.AllCaps(), InputFilter.LengthFilter(beans.idcount))

        KeyboardUtil.hideEditTextKeyboard(holder.mView.New_ID)
        KeyboardUtil.hideEditTextKeyboard(holder.mView.Vehice_ID)
    }

//    fun Select_Tire(holder: ViewHolder,position: Int)
//    {
//        if(ObdBeans.SelectTire == -1) {
//
//            //ObdBeans.SelectTire
//            if (!beans.readable[position] || PublicBeans.selectWay == PublicBeans.KEY_IN) {
//                return
//            }
//            if (beans.state.contains(ObdBeans.PROGRAM_FALSE) || beans.state.contains(ObdBeans.PROGRAM_SUCCESS)) {
//                return
//            }
//            //PublicBeans.programNumber = 1
//            ObdBeans.Selectfocus = focus
//            ObdBeans.SelectTire = position
//            if (position * 2 == focus) {
//                //OldSemsor
//
//                //Log.e("position * 2",position.toString())
//                //holder.mView.Vehice_ID.setText("OldSemsor")
//                //this@Ad_obd.notifyDataSetChanged()
//                if(beans.readable[position])
//                {
//                    holder.mView.Vehice_ID.setBackgroundResource(R.color.color_grey)
//                    Log.e("position * 2","OldSemsor")
//                }
//
//                when (PublicBeans.position) {
//                    PublicBeans.OBD學碼 -> {
//                    }
//                    PublicBeans.OBD複製 -> {
//                    }
//                    PublicBeans.複製ID -> {
//                        //Log.e("position * 2","color_orange")
//                    }
//                }
//            } else if ((position * 2 + 1) == focus) {
//                //NewSensor
//                //Log.e("position * 2 + 1","NewSensor")
//                //Log.e("position * 2 + 1",position.toString())
//
//                //holder.mView.Tire_button.bringToFront()
//                if(beans.readable[position]) {
//                    holder.mView.New_ID.setBackgroundResource(R.color.color_grey)
//                    Log.e("position * 2","NewSensor")
//                }
//            }
//            this@Ad_obd.notifyDataSetChanged()
//            return
//        }
//        else
//        {
//            //holder.mView.Read_R.setBackgroundResource(R.color.white)
//            Log.e("False","OldSemsor")
//            if (position * 2 == focus) {
//                if(beans.readable[position]) {
//                    holder.mView.Vehice_ID.setBackgroundResource(R.color.white)
//                }
//            }
//            else
//            {
//                if(beans.readable[position]) {
//                    holder.mView.New_ID.setBackgroundResource(R.color.white)
//                }
//            }
//            ObdBeans.SelectTire = -1
//            this@Ad_obd.notifyDataSetChanged()
//            return
//        }
//        //frag.checkComplete()
//        //Log.e("position","OldSemsor")
//        //this@Ad_obd.notifyDataSetChanged()
//    }

    fun insertID(position: Int) {
//        if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
//            return
//        }
        JzActivity.getControlInstance().closeDiaLog("Da_SoftKeyboard")

        if (beans.state.contains(ObdBeans.PROGRAM_FALSE) || beans.state.contains(ObdBeans.PROGRAM_SUCCESS)) {
            return
        }
        PublicBeans.programNumber = 1
        if (position * 2 == focus) {
            when (PublicBeans.position) {
                PublicBeans.OBD學碼 -> {
                }
                PublicBeans.OBD複製 -> {
                }
                PublicBeans.複製ID -> {
                    Command.readId(object : Command.Companion.idback {
                        override fun result(id: String, pre: String, tem: String, bat: String) {
                            removeOld(id)
                            beans.OldSemsor[position] = id
                            focus = -1
                            frag.checkComplete()
                            toEmpty()
                            this@Ad_obd.notifyDataSetChanged()
                        }
                    })
                }
            }
        } else if ((position * 2 + 1) == focus) {
            when (PublicBeans.position) {
                PublicBeans.OBD學碼 -> {
                    Command.readId(object : Command.Companion.idback {
                        override fun result(id: String, pre: String, tem: String, bat: String) {
                            removeNew(id)
                            beans.NewSensor[position] = id
                            focus = -1
                            frag.checkComplete()
                            toEmpty()
                            this@Ad_obd.notifyDataSetChanged()
                        }
                    })
                }
                PublicBeans.OBD複製 -> {
                    Command.getPrid(object : Command.Companion.idback {
                        override fun result(id: String, pre: String, tem: String, bat: String) {
                            removeNew(id)
                            beans.NewSensor[position] = id
                            focus = -1
                            frag.checkComplete()
                            toEmpty()
                            this@Ad_obd.notifyDataSetChanged()
                        }
                    })
                }
                PublicBeans.複製ID -> {
                    Command.getPrid(object : Command.Companion.idback {
                        override fun result(id: String, pre: String, tem: String, bat: String) {
                            removeNew(id)
                            beans.NewSensor[position] = id
                            focus = -1
                            frag.checkComplete()
                            toEmpty()
                            this@Ad_obd.notifyDataSetChanged()
                        }
                    })
                }
            }
        }

    }

    fun removeNew(a: String) {
        for (i in 0 until beans.NewSensor.size) {
            if (beans.NewSensor[i] == a) {
                beans.NewSensor[i] = ""
            }
        }
    }

    fun removeOld(a: String) {
        for (i in 0 until beans.OldSemsor.size) {
            if (beans.OldSemsor[i] == a) {
                beans.OldSemsor[i] = ""
            }
        }
    }
    fun toEmpty() {
        var readold = true
        if (PublicBeans.position != PublicBeans.OBD學碼) {
            for (i in 0 until beans.wheelPosition.size) {
                if (beans.OldSemsor[i].isEmpty()) {
                    focus=i*2
                    readold = false
                    position=focus/2
                    break
                }
            }
        }
        if (readold) {
            for (i in 0 until beans.wheelPosition.size) {
                if (beans.NewSensor[i].isEmpty()) {
                    focus=i*2+1
                    position=focus/2
                    break
                }
            }
        }
        notifyDataSetChanged()
    }
}