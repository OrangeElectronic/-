package com.orango.electronic.orange_og_lib.Frag


import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.Systool
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.WifiConnector.WifiConnectHelper
import com.orango.electronic.orange_og_lib.WifiConnector.WifiConnectReceiver
import com.orango.electronic.orange_og_lib.WifiConnector.WifiUtils
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.frag_wifi.view.*
import java.lang.Exception
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Wifi(var callback: connectBack) : JzFragement(
    R.layout.frag_wifi
) {
    var cango = false
    private val Permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val permissionRequestCode = 1001
    lateinit var wifiHelper: WifiConnectHelper
    private var isStartConnected: Boolean = false
    lateinit var spWifiName: Spinner
    lateinit var etWifiPassword: EditText //密码
    private var wifiName: String? = null//wifi name
    private var hasList = false//是否已经拿到列表
    var WifiList:MutableMap<String, ScanResult> = mutableMapOf()
    lateinit var arrayAdapter: ArrayAdapter<String>
    override fun viewInit() {
        rootview.fixLanguage()
        cango = false
        spWifiName = rootview.findViewById(R.id.sp_wifi_name)
        etWifiPassword = rootview.findViewById(R.id.et_wifi_password)
        etWifiPassword.setText(JzActivity.getControlInstance().getPro("wifiPassword",""))
        arrayAdapter = ArrayAdapter<String>(
            JzActivity.getControlInstance().getRootActivity(),
            R.layout.spinner, WifiList.keys.toTypedArray()
        )
        rootview.sp_wifi_name.adapter = arrayAdapter
        if (!isGPSOpen()) {
            //跳转到手机原生设置页面,打开定位功能
            //关闭定位功能无法正常运行
            Systool.openGPS(true)
            initHelper()
        } else {
            initHelper()
        }
        checkPermissions()
        rootview.bt_cancel.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }

        rootview.bt_select.setOnClickListener {
            if(!WifiConnectHelper.hasPassword(context,WifiList[rootview.sp_wifi_name.selectedItem.toString()])){
                cango = true
                wifiHelper.connectWifi(activity, rootview.sp_wifi_name.selectedItem.toString())
                isStartConnected = true
            }else{
                if (!TextUtils.isEmpty(rootview.sp_wifi_name.selectedItem.toString()) && !etWifiPassword.text.isEmpty()) {
                    val connetedWifi = WifiUtils.getInstance(activity).connectedSSID
                    if (connetedWifi == rootview.sp_wifi_name.selectedItem.toString()) {
                        callback.result(true)
                    } else {
                        cango = true
                        wifiHelper.connectWifi(
                            activity,
                            rootview.sp_wifi_name.selectedItem.toString(),
                            etWifiPassword.text.toString()
                        )
                        isStartConnected = true
                    }
                } else {
                    JzActivity.getControlInstance()
                        .toast(resources!!.jzString(R.string.app_content_empty))
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        try {
            Systool.openGPS(false)
            wifiHelper.onDestroyView(activity)
        }catch (e: Exception){e.printStackTrace()}
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequestCode ->
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults.indices) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i])
                        }
                    }
                }
        }
    }

    private fun checkPermissions() {
        val permissionDeniedList = ArrayList<String>()
        for (permission in Permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(JzActivity.getControlInstance().getRootActivity(), permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            val deniedPermissions = permissionDeniedList.toTypedArray()
            ActivityCompat.requestPermissions(JzActivity.getControlInstance().getRootActivity(), deniedPermissions, permissionRequestCode)
        }
    }

    private fun onPermissionGranted(permission: String) {
        Log.d("權限", permission)
    }

    private fun isGPSOpen(): Boolean {
        val isOpen: Boolean
        val locationManager =
            activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isOpen
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == permissionRequestCode && isGPSOpen()) {
            //打开定位就执行
            initHelper()
        } else {
            //关闭定位功能无法正常运行
                Systool.openGPS(true)
            initHelper()
        }
    }

    private fun initHelper() {
        wifiHelper = WifiConnectHelper()
        //开始连接
        wifiHelper.setOnPreRequestListener {
            JzActivity.getControlInstance()
                .showDiaLog(false, true, object : SetupDialog(R.layout.data_loading) {
                    override fun setup(rootview: Dialog) {
                        rootview.pass.visibility = View.VISIBLE
                        rootview.pass.text = resources!!.jzString(R.string.app_wifi_connecting)
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun dismess() {

                    }

                }, "data_loading")
        }
        //连接完成
        wifiHelper.setOnFinishRequestListener {
            Log.e("wifiHelper","setOnFinishRequestListener")
            JzActivity.getControlInstance().closeDiaLog() }
        //连接成功
        wifiHelper.setOnConnecteSuccessListener {
            Log.e("wifiHelper","setOnConnecteSuccessListener")
            if (cango && JzActivity.getControlInstance().getNowPageTag() != "Frag_Sign_in") {
                JzActivity.getControlInstance().setPro("wifiPassword",etWifiPassword.text.toString())
                callback.result(true)
            }
        }
        //连接失败
        wifiHelper.setOnConnecteFailedListener {
            Log.e("wifiHelper","setOnConnecteFailedListener")
            JzActivity.getControlInstance().closeDiaLog()
            if (isStartConnected) {
                JzActivity.getControlInstance()
                    .toast(resources!!.jzString(R.string.app_connected_failed))
                isStartConnected = false
                WifiUtils.getInstance(context).closeWifi()
            }
        }
        //wifi扫描失败
        wifiHelper.setOnScanFailedListener { wifiList ->
            //            Log.d("wifi",""+wifiList)
//            JzActivity.getControlInstance().toast(R.string.app_wiFi_scan_failed)
//            spWifiName.setVisibility(View.GONE)
            spWifiName.visibility = View.VISIBLE
            for (scanResult in wifiList) {
                var name = scanResult.SSID
                if (!TextUtils.isEmpty(name)) {
                    if (name.contains("\"")) {
                        name = name.replace("\"", "")
                    }
                }
                if (!WifiList.keys.toTypedArray().contains(name)) {
                    WifiList[name]= scanResult
                    arrayAdapter = ArrayAdapter<String>(
                        JzActivity.getControlInstance().getRootActivity(),
                        R.layout.spinner, WifiList.keys.toTypedArray()
                    )
                    rootview.sp_wifi_name.adapter = arrayAdapter
                }
            }
        }
        //wifi扫描成功
        wifiHelper.setOnScanSuccessListener { wifiList ->
            spWifiName.visibility = View.VISIBLE
            for (scanResult in wifiList) {
                var name = scanResult.SSID
                if (!TextUtils.isEmpty(name)) {
                    if (name.contains("\"")) {
                        name = name.replace("\"", "")
                    }
                }
                if (!WifiList.keys.toTypedArray().contains(name)) {
                    WifiList[name]= scanResult
                    arrayAdapter = ArrayAdapter<String>(
                        JzActivity.getControlInstance().getRootActivity(),
                        R.layout.spinner, WifiList.keys.toTypedArray()
                    )
                    rootview.sp_wifi_name.adapter = arrayAdapter
                }
            }
        }
        //wifi开关状态改变
        wifiHelper.setOnWifiStateListener { state ->
            if (state == WifiConnectReceiver.WIFI_STATE.WIFI_STATE_ENABLED.toInt()) {
                wifiHelper.startScan(activity)
            }
        }
        //初始化注册广播
        wifiHelper.initViewFinish(activity)
        wifiHelper.switchWifi(activity, true)
    }


}

interface connectBack {
    fun result(a: Boolean)
}
