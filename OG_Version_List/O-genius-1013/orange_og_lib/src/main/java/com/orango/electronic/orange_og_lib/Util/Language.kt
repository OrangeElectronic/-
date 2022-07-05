package com.orango.electronic.orange_og_lib.Util

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.orango.electronic.orange_og_lib.Util.jzString
import com.example.jzonlinedata.function
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion
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
    try {
        if(OgPublic.getInstance.testLan){
            return this
        }
        var b = this
        if (!b.contains("jz.")) {
            return b
        }
        b = b.replace("jz.", "")
        if (OgPublic.getInstance.onlinelanDB.db.isOpen&&(FileJsonVersion.getLocal().lanVersion!="no")) {
            return loadNew(b)
        } else {
            return loadOriginal(b)
        }
    }catch (e:Exception){e.printStackTrace()
        var b = this
        if (!b.contains("jz.")) {
            return b
        }
        b = b.replace("jz.", "")
        return loadOriginal(b)
    }

}

fun loadNew(b: String): String {
    var b = b
    try {
        OgPublic.getInstance.onlinelanDB.query(
            "select `${JzActivity.getControlInstance().getLanguage()}` from language  where id=$b"
        ) {
            b = it.getString(0)
        }
        return b
    } catch (e: Exception) {
        try {
            OgPublic.getInstance.onlinelanDB.query(
                "select `en` from language where id=$b"
            ) {
                b = it.getString(0)
            }
            Log.e("leakLanguage", "" + JzActivity.getControlInstance().getLanguage())
            return b
        } catch (e: Exception) {
            return loadOriginal(b)
        }
    }
}

fun loadOriginal(b: String): String {
    var b = b
    try {
        Log.e("dataBase","loadOriginal")
        OgPublic.getInstance.languageDB.query(
            "select `${JzActivity.getControlInstance().getLanguage()}` from language  where id=$b",
            Sql_Result {
                b = it.getString(0)
            })
        return b
    } catch (e: Exception) {
        try {
            OgPublic.getInstance.languageDB.query(
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
