package com.orango.electronic.orange_og_lib.Util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.permission_C
import com.orango.electronic.jzutil.*
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion


class DataUpload {
    var self_gps: Location? = null
    var manager =
        JzActivity.getControlInstance().getRootActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun startgps() {
        JzActivity.getControlInstance().permissionRequest(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), object :
                permission_C {
                override fun requestFalse(a: String?) {

                }

                override fun requestSuccess(a: String?) {
                }
            }, 1024
        )
        manager =
            JzActivity.getControlInstance().getRootActivity()
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    fun stopgps() {
    }

    fun start() {
        try {
            Log.e("dataUploader", "dataUploader")
            uploadProgram()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //上傳現在位置
    @SuppressLint("MissingPermission")
    fun uploadLocation() {
        val admin = JzActivity.getControlInstance().getPro("admin", "nodata")
        if (admin == "nodata") {
            return
        }
        Log.e("gps", "uploadLocation")
        var gps = manager.getLastKnownLocation("gps")
        if (gps != null) {
            val lat = gps.latitude
            val lon = gps.longitude
            Log.e("gps", "lat:$lat lon:$lon")
        } else {
            gps = manager.getLastKnownLocation("network")
            if (gps != null) {
                val lat = gps.latitude
                val lon = gps.longitude
                Log.e("gps", "lat:$lat lon:$lon")
            }
        }
        var ip = ""
        var lat = 0.0
        var lon = 0.0
        var company = ""
        if (gps != null) {
            lat = gps.latitude
            lon = gps.longitude }
        try {
            ip = "https://ifconfig.me/".getWebResource(1000 * 10)!!.split("ip_address\">")[1].split("</strong>")[0]
            PublicBean.lastIP = ip
            company = "https://www.ez2o.com/App/Net/IP".getWebResource(1000 * 10)!!.split("</strong><td>")[1].split("<tr")[0]
            val countryData = "http://www.geoplugin.net/json.gp?ip=".getWebResource(15 * 1000)
            if (countryData != null) {
                try {
                    val json = Gson().fromJson<MutableMap<String, Any>>(countryData,object : TypeToken<MutableMap<String, Any>>() {}.type)
                    PublicBean.lastCountry = json["geoplugin_countryName"].toString()
                    PublicBean.lastState = json["geoplugin_city"].toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val data = "DataListVersion".getObject<FileJsonVersion>() ?: FileJsonVersion("EU")
        MysqDatabase.ip.postRequest(
            15 * 1000,
            "REPLACE INTO `ogmemory`.deviceinformation (lan,lon,serial,admin,place,mcuversion,apkversion,type,ip,Dbversion,harwareversion,area,beta,country) " +
                    "VALUES (${lat},${lon},${OgPublic.deviceID},'$admin','${company}','${OgPublic.MCU_NUMBER}','${OgPublic.versionCodes}','Ogenius','$ip','${data.mmyVersion}','${OgPublic.hardWareVersion}','${FileDowload.country}',${if (OgPublic.beta) 1 else 0},'${PublicBean.lastCountry}')"
        )
        MysqDatabase.ip.postRequest(
            15 * 1000,
            "insert ignore into  `ogmemory`.`register_day` (admin,serial) values ('" + OgPublic.admin + "','" + OgPublic.deviceID + "')"
        )
    }


    //上傳燒錄紀錄
    fun uploadProgram() {
        var size = 0
        val sql = OgPublic.getInstance.sqlite
        sql.exsql(
            "CREATE TABLE if not exists `updateResult` (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  `sql` varchar NOT NULL\n)"
        )
        sql.query("select * from `updateResult` order by id desc", Sql_Result {
            size += 1
            try {
                if (size < 50) {
                    val sqlc = String(it.getString(1).hexToByte())
                    var a = MysqDatabase.ip.postRequest(15 * 1000, sqlc)
                    if (!sqlc.contains("ogmemory")) {
                        a = "success"
                    }
                    if (a != null) {
                        OgPublic.getInstance.sqlite.exsql(
                            "delete from `updateResult` where  id=${
                                it.getString(
                                    0
                                )
                            }"
                        )
                    }
                } else {
                    OgPublic.getInstance.sqlite.exsql(
                        "delete from `updateResult` where  id=${
                            it.getString(
                                0
                            )
                        }"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }


}