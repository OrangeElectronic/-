package com.orange.tpms.ue.kt_frag

//import com.google.firebase.ml.vision.FirebaseVision
//import com.google.firebase.ml.vision.common.FirebaseVisionImage

import android.R.attr.path
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.bumptech.glide.Glide
import com.example.jzpictureselector.PictureSelector
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.postRequest2
import com.orange.tpms.service.ChangePageService
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.beans.SensorData.Companion.call
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.plate_recognize.view.*
import java.io.ByteArrayOutputStream
import java.lang.Exception


class Frag_Plate_Recognize(var callback: (a: String?) -> Unit) :
    JzFragement(R.layout.plate_recognize) {
    companion object { var plateMemory:Array<MutableMap<String,Any>> = arrayOf() }
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
        rootview.nextBt.setOnClickListener {
            callback(rootview.platetext.text.toString())
        }
        rootview.scan.setOnClickListener {
            return@setOnClickListener
            PictureSelector.getPicture(this)
        }
        rootview.nextBt.setOnClickListener {
            if (rootview.platetext.text.isEmpty()) {
                return@setOnClickListener
            }
            val plate = rootview.platetext.text.toString()
            EmptyDialog(R.layout.data_loading).show()
            Thread {
                val re = "${serverRout}/SelectMysql".postRequest(10 * 1000, "select * from plate_recognize.plate_memory where plate='$plate'")
                handler.post {
                    if (re == null) {
                        JzActivity.getControlInstance().toast("jz.210".getFix())
                    } else {
                        JzActivity.getControlInstance().closeDiaLog()
                        plateMemory=Gson().fromJson(re,object :TypeToken<Array<MutableMap<String,Any>>>(){}.type)
                        PublicBean.selectPlate=plate
                        if(plateMemory.isEmpty()){
                            JzActivity.getControlInstance().changePage(Frag_SelectMake(), "Frag_SelectMake", true)
                        }else{
                            PublicBean.selectMmy=Gson().fromJson(plateMemory[0]["mmy"].toString(),object : TypeToken<PublicBean.SelectMmy>() {}.type)
                            ChangePageService.changePage()
                        }

                    }
                }
            }.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureSelector.requestCode && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainResult(data)
            EmptyDialog(R.layout.data_loading).show()
            Thread {
                val bitmap: Bitmap = Glide.with(this)
                    .asBitmap()
                    .load(result[0])
                    .submit(200, 200)
                    .get()
                val stream = ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.toByteArray();
                bitmap.recycle();
                val re = "${serverRout}/UploadImg".postRequest(
                    20000,
                    Base64.encodeToString(stream.toByteArray(), 0)
                )
                if (re == null || re == "false") {
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog()
                        JzActivity.getControlInstance().toast("jz.517".getFix())
                    }
                } else {
                    val gson = Gson().fromJson<MutableMap<String, String>>(re,
                        object : TypeToken<MutableMap<String, String>>() {}.type)
                    Log.e(
                        "imageRout==",
                        "${serverRout}${gson["url"]}".replace(
                            "file=",
                            "path="
                        )
                    )
                    val plate = plateRecognize(
                        "${serverRout}${gson["url"]}".replace(
                            "file=",
                            "path="
                        )
                    )

                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog()
                        if (plate == null) {
                            JzActivity.getControlInstance().toast("jz.517".getFix())
                        } else {
                            rootview.platetext.setText("$plate")
                        }
                    }
                }

            }.start()
        }
    }

    private fun filterCaptcha(s: String): String? {
        val temp = s.replace(" ", "-").replace("[^A-Z0-9-]".toRegex(), "?")
        if (checkCount(temp) <= 1 && !temp.contains("?")) {
            return temp
        }
        return null
    }

    fun checkCount(a: String): Int {
        var count = 0
        for (i in a) {
            if (i == "-".elementAt(0)) {
                count += 1
            }
        }
        return count
    }

    //車牌辨識
    fun plateRecognize(rout: String): String? {
        try {
            val re =
                "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyAmavL6Y2PFnYBwbiWdGc8cFDukQdcpTxI".postRequest2(
                    10 * 1000,
                    """  
             {
  "requests": [
    {
      "image": {
        "source": {
          "imageUri": "$rout"
        }
       },
       "features": [
         {
           "type": "TEXT_DETECTION"
         }
       ]
    }
  ]
}
"""
                )
            Log.e("plateRe", "re--$re")
            val parse = JsonParser()
            val json: JsonObject = parse.parse(re) as JsonObject //將String轉成json
            for (a in (((json.get("responses") as JsonArray).get(0) as JsonObject).getAsJsonArray("textAnnotations"))) {
                return try {
                    a.asJsonObject.get("description").toString().replace("\"", "")
                        .replace("\\n", "")
                } catch (e: Exception) {
                    null
                }
            }
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}

