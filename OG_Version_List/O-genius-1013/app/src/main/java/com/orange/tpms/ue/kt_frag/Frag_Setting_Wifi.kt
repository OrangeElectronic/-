package com.orange.tpms.ue.kt_frag


import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.de.rocket.ue.layout.PercentRelativeLayout
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.WifiAdapter
import com.orange.tpms.helper.WifiConnectHelper
import com.orange.tpms.ue.receiver.WifiConnectReceiver
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orange.tpms.widget.EditDialogWidget
import com.orango.electronic.orange_og_lib.Util.Systool
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.data_loading.*
import java.lang.Exception
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Setting_Wifi : RootFragement(R.layout.fragment_frag__setting__wifi) {
    private val Permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    private val permissionRequestCode = 1001
    lateinit var prlConnectedWifi: PercentRelativeLayout//ConnectedWifi
    lateinit var tvConnectedWifi: TextView//ConnectedWifi
    lateinit var rvWifi: androidx.recyclerview.widget.RecyclerView//Wifi列表
    lateinit var ivWifiCheck: ImageView//Wifi开关
    lateinit var edwPassword: EditDialogWidget//Loading
    lateinit var wifiAdapter: WifiAdapter//适配器
    lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager//列表表格布局
    lateinit var wifiConnectHelper: WifiConnectHelper//Helper

    override fun viewInit() {
        rootview.fixLanguage()
        prlConnectedWifi=rootview.findViewById(R.id.prl_connected)
        tvConnectedWifi=rootview.findViewById(R.id.tv_connected)
        rvWifi=rootview.findViewById(R.id.rv_wifi)
        ivWifiCheck=rootview.findViewById(R.id.iv_check)
        edwPassword=rootview.findViewById(R.id.edw_password)
        initView()
        if (!isGPSOpen()) {
            //跳转到手机原生设置页面,打开定位功能
            //关闭定位功能无法正常运行
            Systool.openGPS(true)
            initHelper()
        } else {
            initHelper()
        }
        checkPermissions()
    }
    /**
     * 初始化页面
     */
    private fun initView() {
        //开关wifi
        ivWifiCheck.setOnClickListener { view ->
            ivWifiCheck.isSelected = !ivWifiCheck.isSelected
            wifiConnectHelper.switchWifi(activity, ivWifiCheck.isSelected)
        }
        //配置RecyclerView,每行是哪个元素
            linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        rvWifi.layoutManager = linearLayoutManager
        wifiAdapter = WifiAdapter(activity)
        rvWifi.adapter = wifiAdapter
        wifiAdapter.setOnItemClickListener { pos, scanResult ->
            edwPassword.setObject(scanResult)
            if (WifiConnectHelper.hasPassword(activity, scanResult)) {
                edwPassword.show()
            } else {
                wifiConnectHelper.connectWifi(activity, scanResult.SSID)
            }
        }
        //输入密码框
        edwPassword.setDoneListener { content ->
            edwPassword.hide()
            if (!TextUtils.isEmpty(content)) {
                val scanResult = edwPassword.getObject() as ScanResult
                if (scanResult != null) {
                    wifiConnectHelper.connectWifi(activity, scanResult.SSID, content)
                }
            }else{
                val scanResult = edwPassword.getObject() as ScanResult
                if(!WifiConnectHelper.hasPassword(context,scanResult)){
                    wifiConnectHelper.connectWifi(activity, scanResult.SSID, content)
                }
            }
        }
    }
    private fun isGPSOpen(): Boolean {
        val isOpen: Boolean
        val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isOpen
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

    override fun onPause() {
        super.onPause()
        if(::wifiConnectHelper.isInitialized){wifiConnectHelper.onDestroyView(activity)}
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Systool.openGPS(false)
        }catch (e:Exception){e.printStackTrace()}
    }
    private fun onPermissionGranted(permission: String) {
        Log.d("權限",permission)
    }
    /**
     * 初始化Helper
     */
    private fun initHelper() {
        wifiConnectHelper = WifiConnectHelper()
        //开始请求
        wifiConnectHelper.setOnPreRequestListener {
            JzActivity.getControlInstance().showDiaLog(false,true, object : SetupDialog(R.layout.data_loading) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    if(event.keyCode==4){
                        Cmd_Og.cancel=true
                    }
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.pass.visibility=View.VISIBLE
                    rootview.pass.text=resources!!.jzString(R.string.app_wifi_connecting)
                }

            },"data_loading")
        }
        //结束请求
        wifiConnectHelper.setOnFinishRequestListener { JzActivity.getControlInstance().closeDiaLog() }
        //wifi连接成功
        wifiConnectHelper.setOnConnecteFailedListener {

        }
        //wifi连接失败
        wifiConnectHelper.setOnConnecteSuccessListener {
            //读取当前连接的wifi
            wifiConnectHelper.getConnectedSSID(activity)
        }
        //wifi扫描失败
        wifiConnectHelper.setOnScanFailedListener { wifiList ->
            wifiAdapter.items = wifiList
            wifiAdapter.notifyDataSetChanged()
        }
        //wifi扫描成功
        wifiConnectHelper.setOnScanSuccessListener { wifiList ->
            wifiAdapter.items = wifiList
            wifiAdapter.notifyDataSetChanged()
            //读取当前连接的wifi
            wifiConnectHelper.getConnectedSSID(activity)
        }
        //wifi开关状态改变
        wifiConnectHelper.setOnWifiStateListener { state ->
            if (state == WifiConnectReceiver.WIFI_STATE.WIFI_STATE_DISABLED.toInt()) {
                ivWifiCheck.isSelected = false
                prlConnectedWifi.visibility = View.GONE
                //清空列表
                wifiAdapter.clean()
                wifiAdapter.notifyDataSetChanged()
            } else if (state == WifiConnectReceiver.WIFI_STATE.WIFI_STATE_ENABLED.toInt()) {
                ivWifiCheck.isSelected = true
                //开始扫描
                wifiConnectHelper.startScan(activity)
            }
        }
        //当前连接的wifi回调
        wifiConnectHelper.setConnectedSSIDListener { ssid ->
            prlConnectedWifi.visibility = View.VISIBLE
            tvConnectedWifi.text = ssid
        }
        //初始化注册广播
        wifiConnectHelper.initViewFinish(activity)
    }
}
