package com.orange.zhengxin

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.jzbarcodescnner.BarCodeView
import com.jianzhi.jzbarcodescnner.callback
import com.orange.glitter.R

class ScannerActivity : AppCompatActivity() {
    var handler= Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        getPermission(arrayOf(android.Manifest.permission.CAMERA),object :permission_C{
            override fun requestSuccess(a: String?) {
                Log.e("ScannerActivity", "requestSuccess$a")
                val scanView= BarCodeView(
                    findViewById<FrameLayout>(R.id.frame),
                    arrayOf(BarcodeFormat.CODE_128,BarcodeFormat.QR_CODE,BarcodeFormat.DATA_MATRIX),
                    object : callback {
                        override fun result(text: String) {
                    GlitterActivity.instance().webRoot.evaluateJavascript("glitter.qrScanBack('$text')",null)
                            this@ScannerActivity.finish()
                        }
                    })
                scanView.start()
            }

            override fun requestFalse(a: String?) {
                Toast.makeText(this@ScannerActivity,"掃描BarCode需要相機權限",Toast.LENGTH_SHORT).show()
            }
        })
        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            this@ScannerActivity.finish()
        }
    }
    var permissionRequestCode=100
    var permissionCaller=object :permission_C{
        override fun requestSuccess(a: String?) {

        }

        override fun requestFalse(a: String?) {
        }
    }
    private fun getPermission(Permissions: Array<String>, caller: permission_C) {
        permissionCaller = caller
        val permissionDeniedList = ArrayList<String>()
        for (permission in Permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                caller.requestSuccess(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            val deniedPermissions = permissionDeniedList.toTypedArray()
            ActivityCompat.requestPermissions(this, deniedPermissions, 100)
        }
    }
    interface permission_C {
        fun requestSuccess(a: String?)
        fun requestFalse(a: String?)
    }
    /**
     * 請求成功
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("ScannerActivity", "requestCode$requestCode")
        Log.e("ScannerActivity", "grantResults${Gson().toJson(grantResults)}")
        when (requestCode) {
            permissionRequestCode ->{
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults.indices) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            permissionCaller.requestSuccess(permissions[i])
                        } else {
                            permissionCaller.requestFalse(permissions[i])
                        }
                    }
                }
            }

        }
    }
}