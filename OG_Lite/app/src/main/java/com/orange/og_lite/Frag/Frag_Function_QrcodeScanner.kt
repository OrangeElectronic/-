package com.orange.og_lite.Frag

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.orange.og_lite.Util.jzString
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__qrcode_scanner.view.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A simple [Fragment] subclass.
 */
public interface scanback{
    fun callback(content:String)
}
class Frag_Function_QrcodeScanner(val back: scanback, val changePlace:Int) : JzFragement(
    R.layout.fragment_frag__function__qrcode_scanner
), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    var ALL_FORMATS: ArrayList<BarcodeFormat> = ArrayList(1)
    lateinit var mMainActivity: MainActivity
    override fun viewInit() {
        rootview.fixLanguage()
        RequestPermission()
        mMainActivity=activity!! as MainActivity
        mScannerView = ZXingScannerView(activity)
        ALL_FORMATS.add(BarcodeFormat.DATA_MATRIX)
        mScannerView!!.setFormats(ALL_FORMATS)
        mScannerView!!.resumeCameraPreview(this)
        mScannerView!!.setAutoFocus(true)
        mScannerView!!.setAspectTolerance(0.0f)

        rootview.frame.addView(mScannerView)
    }

    override fun handleResult(rawResult: Result?) {
        if(changePlace == 0)
        {mmy_query(rawResult!!.text.split("*")[0])}
        else
        {JzActivity.getControlInstance().goBack()
            back.callback(rawResult!!.text)}



    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    fun RequestPermission() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(activity!!)
                    .setCancelable(false)
                    .setTitle("需要相機權限")
                    .setMessage("需要相機權限才能掃描BARCODE!")
                    .setPositiveButton(
                        "確認"
                    ) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.CAMERA),
                            1
                        )
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), 1)
            }
        }
    }

    fun mmy_query(mmy_number : String) {
        var nothung=true
            //資料查詢
            mMainActivity.item.create().query(  if(PublicBeans.position==PublicBeans.OBD複製||PublicBeans.position==PublicBeans.OBD學碼)  "select * from `Summary table` where `MMY number` = " + "'"  + mmy_number + "' and  `OBD2` not in('NA') " else "select * from `Summary table` where `MMY number` = " + "'"  + mmy_number + "'", Sql_Result {
                nothung=false
                //Callback回調，會迴圈跑到所有資料載入完
                val result1 = it.getString(0)
                val result2 = it.getString(1)
                val result3 = it.getString(2)

                handler.post {

                    PublicBeans.make = result1
                    PublicBeans.model = result2
                    PublicBeans.year = result3

                   PublicBeans.changeSwitch()
                }

            });
        if(nothung)JzActivity.getControlInstance()
            .toast(R.string.app_invalid_sensor_qrcode)
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

}
