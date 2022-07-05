package com.orange.tpms.helper

import android.content.Context
import android.content.res.Resources
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.Util.getFix

import java.util.ArrayList
import java.util.Arrays

class UnitHelper(context: Context) : BaseHelper() {

    private val tempList = ArrayList<String>()//温度单位列表
    private val tireList = ArrayList<String>()//压力单位列表
    private val numeralList = ArrayList<String>()//数字单位列表

    /* *********************************  获取温度单位  ************************************** */

    private var onGetTempListener: OnGetTempListener? = null

    /* *********************************  获取压力单位  ************************************** */

    private var onGetPressureListener: OnGetPressureListener? = null

    /* *********************************  获取数字单位  ************************************** */

    private var onGetNumeralListener: OnGetNumeralListener? = null

    init {
        val res = context.resources
        val tempArray = res.getStringArray(R.array.unite_temp)
        for(i in tempArray.indices){
            tempArray[i]=tempArray[i].getFix()
        }
        if (tempArray.isNotEmpty()) {
            tempList.addAll(listOf(*tempArray))
        }
        val numeralArray = res.getStringArray(R.array.unite_numeral)
        for(i in numeralArray.indices){
            numeralArray[i]=numeralArray[i].getFix()
        }
        if (numeralArray.isNotEmpty()) {
            numeralList.addAll(listOf(*numeralArray))
        }
        val tireArray = res.getStringArray(R.array.unite_tire)
        for(i in tireArray.indices){
            tireArray[i]=tireArray[i].getFix()
        }
        if (tireArray.isNotEmpty()) {
            tireList.addAll(listOf(*tireArray))
        }
    }

    /**
     * 读取单位
     * @param context 上下文
     */
    fun getUnit(context: Context) {
        val unit = SettingShare.getUnit(context)
        val tempArray = SettingShare.TemperatureUnitEnum.values()
        val tireArray = SettingShare.TirePressureUnitEnum.values()
        val numeralArray = SettingShare.NumeralSystemUnitEnum.values()
        if (tempArray.size > 0) {
            for (i in tempArray.indices) {
                if (unit.temperatureUnit == tempArray[i]) {
                    getTempNext(i, tempList)
                }
            }
        }
        if (tireArray.size > 0) {//读取温度的单位
            for (i in tireArray.indices) {
                if (unit.tirePressureUnit == tireArray[i]) {
                    getPressureNext(i, tireList)
                }
            }
        }
        if (numeralArray.size > 0) {//读取数字的单位
            for (i in numeralArray.indices) {
                if (unit.numeralSystemUnit == numeralArray[i]) {
                    getNumeralNext(i, numeralList)
                }
            }
        }
    }

    /**
     * 设置温度的单位
     * @param context 上下文
     */
    fun setTemp(context: Context, index: Int) {
        val tempArray = SettingShare.TemperatureUnitEnum.values()
        if (tempArray.size > index) {
            SettingShare.setTemperatureUnit(context, tempArray[index])
        }
    }

    /**
     * 设置压力的单位
     * @param context 上下文
     */
    fun setPressure(context: Context, index: Int) {
        val tireArray = SettingShare.TirePressureUnitEnum.values()
        if (tireArray.size > index) {
            SettingShare.setTirePressureUnit(context, tireArray[index])
        }
    }

    /**
     * 设置数字的单位
     * @param context 上下文
     */
    fun setNumeral(context: Context, index: Int) {
        val numeralArray = SettingShare.NumeralSystemUnitEnum.values()
        if (numeralArray.size > index) {
            SettingShare.setNumeralSystemUnit(context, numeralArray[index])
        }
    }

    fun getTempNext(select: Int, arrayList: List<String>?) {
        if (arrayList == null || arrayList.size <= select) {
            return
        }
        if (onGetTempListener != null) {
            runMainThread { onGetTempListener!!.onGetTemp(select, arrayList) }
        }
    }

    fun setOnGetTempListener(onGetTempListener: OnGetTempListener) {
        this.onGetTempListener = onGetTempListener
    }

    interface OnGetTempListener {
        fun onGetTemp(select: Int, arrayList: List<String>?)
    }

    fun getPressureNext(select: Int, arrayList: List<String>?) {
        if (arrayList == null || arrayList.size <= select) {
            return
        }
        if (onGetPressureListener != null) {
            runMainThread { onGetPressureListener!!.onGetPressure(select, arrayList) }
        }
    }

    fun setOnGetPressureListener(onGetPressureListener: OnGetPressureListener) {
        this.onGetPressureListener = onGetPressureListener
    }

    interface OnGetPressureListener {
        fun onGetPressure(select: Int, arrayList: List<String>?)
    }

    fun getNumeralNext(select: Int, arrayList: List<String>?) {
        if (arrayList == null || arrayList.size <= select) {
            return
        }
        if (onGetNumeralListener != null) {
            runMainThread { onGetNumeralListener!!.onGetNumeral(select, arrayList) }
        }
    }

    fun setOnGetNumeralListener(onGetNumeralListener: OnGetNumeralListener) {
        this.onGetNumeralListener = onGetNumeralListener
    }

    interface OnGetNumeralListener {
        fun onGetNumeral(select: Int, arrayList: List<String>?)
    }
}


