package com.orange.zhengxin.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import com.orange.jzchi.jzframework.JzActivity

class GpsUtil{
    var mLocationManager: LocationManager = JzActivity.getControlInstance().getRootActivity()
        .getSystemService(Context.LOCATION_SERVICE) as LocationManager

    //取得定位位置
    var lastKnownLocation: Location? = null
        @SuppressLint("MissingPermission")
        get() {
            JzActivity.getControlInstance().getHandler().post {
                mLocationManager.requestLocationUpdates("network", 0.toLong(), 0.toFloat(), object :LocationListener{
                    override fun onLocationChanged(location: Location) {
                        lastKnownLocation=location
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }
                    override fun onProviderEnabled(provider: String) {
                    }
                    override fun onProviderDisabled(provider: String) {
                    }
                });
            }
            return mLocationManager.getLastKnownLocation("network")
        }


    //取得地址
    val address: String
        get() {
            try {
                val geocoder: Geocoder = Geocoder(JzActivity.getControlInstance().getRootActivity())
                val addresses = geocoder.getFromLocation(
                    lastKnownLocation!!.latitude,
                    lastKnownLocation!!.longitude,
                    1
                );
                if (addresses != null && addresses.size > 0) {
                    val address = addresses[0];
                    val addressText = String.format(
                        "%s-%s%s%s%s",
                        address.countryName, //國家
                        address.adminArea, //城市
                        address.locality, //區
                        address.thoroughfare, //路
                        address.subThoroughfare //巷號
                    ).replace("null","");
                    return addressText
                } else {
                    return "Unknown Address"
                }
            } catch (e: Exception) {
                return "Unknown Address"
            }
        }

    /**
     * 判斷GPS是否開啟，GPS或者AGPS開啟一個就認為是開啟的
     */
    fun isOpenGps(): Boolean {
        val locationManager = mLocationManager
        // 通過GPS衛星定位，定位級別可以精確到街（通過24顆衛星定位，在室外和空曠的地方定位準確、速度快）
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通過WLAN或移動網路(3G/2G)確定的位置（也稱作AGPS，輔助GPS定位。主要用於在室內或遮蓋物（建築群或茂密的深林等）密集的地方定位）
        val network =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }
}