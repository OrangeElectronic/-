package com.orange.Frag

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.activity_start_date.view.*
import java.util.*


class Frag_Edit_StartDate : JzFragement(R.layout.activity_start_date) {
    var year = 0
    var month = 0
    var dayOfMonth = 0
    var hour = 0
    var minute = 0
    override fun viewInit() {
        rootview.fixLanguage()
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = (c.get(Calendar.MONTH) + 1)
        hour = rootview.Startime.hour
        minute = rootview.Startime.minute
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
        rootview.Startime.setOnTimeChangedListener { view, hourOfDay, minute ->
            this.hour = hourOfDay
            this.minute = minute
        }
        rootview.next.setOnClickListener {
            UpdateTime()
            JzActivity.getControlInstance().goMenu()
        }
    }


    fun UpdateTime(): String {
        val time = "$year-$month-$dayOfMonth $hour:$minute:00"
        Log.d("time", time)
        val c = Calendar.getInstance()
        c.set(year, month, dayOfMonth, hour, minute, 0)
        val am =
            JzActivity.getControlInstance().getRootActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setTime(c.timeInMillis)
        return time
    }
}
