package com.orango.electronic.orange_og_lib.HttpCommand

import com.orango.electronic.orange_og_lib.OgPublic.Companion.DataVersion

class SensorRecord {
    var SensorID = ""
    var Car_SensorID = ""
    var IsSuccess = ""
    var ModelNo = "OG"
    var enum_BurnResult = "最後檢查"
    val DB_Version get() =  if (DataVersion.length > 19) DataVersion.substring(16) else DataVersion
    companion object{var SensorCode_Version = "V00.01"}

}
