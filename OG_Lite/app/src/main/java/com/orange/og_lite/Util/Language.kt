package com.orange.og_lite.Util

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.jzfixlanguage.function
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.MainActivity
import java.lang.Exception

fun View.fixLanguage() {
    function.getAllChildViews(this)
    for (i in function.getAllChildViews(this)) {
        Log.e("view", "$i")
        if (i is EditText) {
            i.setText(i.text.toString().getFix())
            if(i.hint!=null){ i.hint = i.hint.toString().getFix()}
        }else if (i is TextView) {
            i.text = i.text.toString().getFix()
        } else if (i is Button) {
            i.text = i.text.toString().getFix()
        }
    }
}

fun Resources.jzString(int: Int): String {
    val a = getString(int).getFix()
    return a
}

fun String.getFix(): String {
    val db=JzActivity.getControlInstance().getRootActivity() as MainActivity
    if(db.testLan){
        return this
    }
    var b = this
    if (!b.contains("jz.")) {
        return b
    }
    b = b.replace("jz.", "")
    if (db.onlinelanDB.db.isOpen&&(JzActivity.getControlInstance().getPro("lanName","no")!="no")) {
        return loadNew(b)
    } else {
        return loadOriginal(b)
    }
}

fun loadNew(b: String): String {
    val db=JzActivity.getControlInstance().getRootActivity() as MainActivity
    var b = b
    try {
        db.onlinelanDB.query(
            "select `${JzActivity.getControlInstance().getLanguage()}` from language  where id=$b",
            Sql_Result {
                b = it.getString(0)
            })
        return b
    } catch (e: Exception) {
        try {
            db.onlinelanDB.query(
                "select `en` from language where id=$b",
                Sql_Result {
                    b = it.getString(0)
                })
            Log.e("leakLanguage", "" + JzActivity.getControlInstance().getLanguage())
            return b
        } catch (e: Exception) {
            return loadOriginal(b)
        }
    }
}

fun loadOriginal(b: String): String {
    val db=JzActivity.getControlInstance().getRootActivity() as MainActivity
    var b = b
    try {
        db.languageDB.query(
            "select `${JzActivity.getControlInstance().getLanguage()}` from language  where id=$b",
            Sql_Result {
                b = it.getString(0)
            })
        return b
    } catch (e: Exception) {
        try {
            db.languageDB.query(
                "select `en` from language where id=$b",
                Sql_Result {
                    b = it.getString(0)
                })
            Log.e("leakLanguage", "" + JzActivity.getControlInstance().getLanguage())
            return b
        } catch (e: Exception) {
            return b
        }
    }
}
