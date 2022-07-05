package com.orango.electronic.orange_og_lib.HttpCommand

import android.util.Log

import com.orango.electronic.orange_og_lib.Callback.Register_C
import com.orango.electronic.orange_og_lib.Callback.Reset_C
import com.orango.electronic.orange_og_lib.Callback.Sign_In_C

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.ArrayList

import android.content.ContentValues.TAG
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout

object Fuction {
    val timeout = 10000
    val wsdl = "${serverRout}/App_Asmx/ToolApp.asmx"
//    val wsdl = "http://192.168.43.219/App_Asmx/ToolApp.asmx"
    fun _req(url_String: String, data: String, timeout: Int): RetNode {
        try {
            Log.d(TAG + "_post", "url: $url_String")
            Log.d(TAG + "_post", "data: $data")
            val url = URL(url_String)
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8")
            conn.setRequestProperty("Content-Length", "" + data.toByteArray().size)
            conn.requestMethod = "POST"
            conn.useCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.connectTimeout = timeout
            conn.readTimeout = timeout
            val dos = DataOutputStream(conn.outputStream)
            dos.write(data.toByteArray(StandardCharsets.UTF_8))
            dos.flush()
            val retNode = RetNode()
            retNode.status = conn.responseCode
            retNode.data = ""
            if (retNode.status == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8"))
                var  line=reader.readLine()
                val strBuf = StringBuffer()

                while (line != null) {
                    strBuf.append(line)
                    line=reader.readLine()
                }
                retNode.data = strBuf.toString()
                reader.close()
            }
            dos.close()
            Log.d(TAG + "_post", "-------------respond data--------------")
            Log.d(TAG + "_post", "Data:" + retNode.data)
            Log.d(TAG + "_post", "---------------------------------------")
            Log.d(TAG + "_post", "status: " + retNode.status)
            Log.d(TAG + "_post", "-------------data end--------------")
            return retNode
        } catch (e: Exception) {
            val retNode = RetNode()
            retNode.status = -1
            retNode.data = ""
            Log.d("_post", e.message.toString())
            return retNode
        }
    }

    fun ResetPassword(admin: String, caller: Reset_C) {
        try {
            val sb = StringBuffer()
            sb.append(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        " <soap12:Body>\n" +
                        " <SysResetPwd xmlns=\"http://tempuri.org/\">\n" +
                        " <UserID>" + admin + "</UserID>\n" +
                        " </SysResetPwd>\n" +
                        " </soap12:Body>\n" +
                        "</soap12:Envelope>"
            )
            caller.Result(_req(wsdl, sb.toString(), timeout).status == 200)
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Result(false)
        }

    }

    fun ValidateUser(admin: String, password: String, caller: Sign_In_C) {
        try {
            val sb = StringBuffer()
            sb.append(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        " <soap12:Body>\n" +
                        " <ValidateUser xmlns=\"http://tempuri.org/\">\n" +
                        " <UserID>" + admin + "</UserID>\n" +
                        " <Pwd>" + password + "</Pwd>\n" +
                        " </ValidateUser>\n" +
                        " </soap12:Body>\n" +
                        "</soap12:Envelope>"
            )
            val respnse = _req(wsdl, sb.toString(), timeout)
            if(respnse.data.isEmpty()){caller.wifierror()}else{
                if(respnse.data.contains("ValidateUserResult>")){
                    caller.result(
                        respnse.data.substring(
                            respnse.data.indexOf("<ValidateUserResult>") + 20,
                            respnse.data.indexOf("</ValidateUserResult>")
                        ) == "true"
                    )
                }else{
                    caller.result(false)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    fun Register(
        admin: String,
        password: String,
        SerialNum: String,
        storetype: String,
        companyname: String,
        firstname: String,
        lastname: String,
        phone: String,
        State: String,
        city: String,
        streat: String,
        zp: String,
        caller: Register_C,
        type: String,
        Country: String
    ) {
        try {
            val sb = StringBuffer()
            sb.append(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        " <soap12:Body>\n" +
                        " <Register xmlns=\"http://tempuri.org/\">\n" +
                        " <Reg_UserInfo>\n" +
                        " <UserID>" + admin + "</UserID>\n" +
                        " <UserPwd>" + password + "</UserPwd>\n" +
                        " </Reg_UserInfo>\n" +
                        " <Reg_StoreInfo>\n" +
                        " <StoreType>" + storetype + "</StoreType>\n" +
                        " <CompName>" + companyname + "</CompName>\n" +
                        " <FirstName>" + firstname + "</FirstName>\n" +
                        " <LastName>" + lastname + "</LastName>\n" +
                        " <Contact_Tel>" + phone + "</Contact_Tel>\n" +
                        " <Continent>NA</Continent>\n" +
                        " <Country>" + Country + "</Country>\n" +
                        " <State>" + State + "</State>\n" +
                        " <City>" + city + "</City>\n" +
                        " <Street>" + streat + "</Street>\n" +
                        " <CompTel>" + phone + "</CompTel>\n" +
                        " </Reg_StoreInfo>\n" +
                        " <Reg_DeviceInfo>\n" +
                        " <SerialNum>" + SerialNum + "</SerialNum>\n" +
                        " <DeviceType>" + type + "</DeviceType>\n" +
                        " <ModelNum>PA001</ModelNum>\n" +
                        " <AreaNo>" + zp + "</AreaNo>\n" +
                        " <Firmware_1_Copy>EU-1.0</Firmware_1_Copy>\n" +
                        " <Firmware_2_Copy>EU-1.0</Firmware_2_Copy>\n" +
                        " <Firmware_3_Copy>EU-1.0</Firmware_3_Copy>\n" +
                        " <DB_Copy>EU-1.0 </DB_Copy>\n" +
                        " <MacAddress>00</MacAddress>\n" +
                        " <IpAddress>00</IpAddress>\n" +
                        " </Reg_DeviceInfo>\n" +
                        " </Register>\n" +
                        " </soap12:Body>\n" +
                        "</soap12:Envelope>"
            )
            val respnse = _req(wsdl, sb.toString(), timeout)
            if (respnse.status != 200) {
                caller.WifiError()
            }
            if (respnse.data.substring(
                    respnse.data.indexOf("<RegisterResult>") + 16,
                    respnse.data.indexOf("</RegisterResult>")
                ) == "true"
            ) {
                caller.Result(true)
            } else {
                caller.Result(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.WifiError()
        }

    }

    fun Upload_ProgramRecord(
        make: String,
        model: String,
        year: String,
        startime: String,
        Endtime: String,
        SreialNum: String,
        Devicetype: String,
        Mode: String,
        SensorCount: Int,
        position: String,
        idrecord: ArrayList<SensorRecord>
    ) {
        try {
            val sb = StringBuffer()
            sb.append(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        " <soap12:Body>\n" +
                        " <Upload_VersionUpdateRecord xmlns=\"http://tempuri.org/\">\n" +
                        " <SerialNum>" + SreialNum + "</SerialNum>\n" +
                        " <data>\n" +
                        " <Device_BurnVersionUpdate>\n" +
                        " <DeviceInfo>\n" +
                        " <enum_DeviceType>" + Devicetype + "</enum_DeviceType>\n" +
                        " <SerialNum>" + SreialNum + "</SerialNum>\n" +
                        " <enum_SensorMode>" + Mode + "</enum_SensorMode>\n" +
                        " <DateTime_Start>" + startime + "</DateTime_Start>\n" +
                        " <DateTime_End>" + Endtime + "</DateTime_End>\n" +
                        " <SensorCount>" + SensorCount + "</SensorCount>\n" +
                        " <enum_BurnPosition>" + position + "</enum_BurnPosition>\n" +
                        " </DeviceInfo>\n" +
                        " <CarInfo>\n" +
                        " <Type>" + make + "</Type>\n" +
                        " <Model>" + model + "</Model>\n" +
                        " <Year>" + year + "</Year>\n" +
                        " <CarNum>nodata</CarNum>\n" +
                        " </CarInfo>\n" +
                        " <TireInfo>\n" +
                        " <TireBrand>nodata</TireBrand>\n" +
                        " <TireModel>nodata</TireModel>\n" +
                        " <TireProcDate>nodata</TireProcDate>\n" +
                        " </TireInfo>\n" +
                        " <ConsumerInfo>\n" +
                        " <Name>nodata</Name>\n" +
                        " <Age>0</Age>\n" +
                        " <Sex>男</Sex>\n" +
                        " <Tel>nodata</Tel>\n" +
                        " <Email>nodata</Email>\n" +
                        " <Continent>nodata</Continent>\n" +
                        " <Country>nodata</Country>\n" +
                        " <State>nodata</State>\n" +
                        " <City>nodata</City>\n" +
                        " <Street>nodata</Street>\n" +
                        " </ConsumerInfo>\n" +
                        " <Record>\n"
            )
            for (record in idrecord) {
                sb.append(
                    "<VersionUpdate_Record>\n"
                            + " <SensorID>" + record.SensorID + "</SensorID>\n"
                            + "<IsSuccess>" + record.IsSuccess + "</IsSuccess>\n"
                            + "<ModelNo >" + record.ModelNo + "</ModelNo >\n"
                            + "<enum_BurnResult>" + record.enum_BurnResult + "</enum_BurnResult>\n"
                            + "<DB_Version>" + OgPublic.DataVersion + "</DB_Version>\n"
                            + "<SensorCode_Version>" +"" + "</SensorCode_Version>\n"
                            + "</VersionUpdate_Record>\n"
                )
            }
            sb.append(
                " </Record>\n" +
                        " </Device_BurnVersionUpdate>\n" +
                        " </data>\n" +
                        " </Upload_VersionUpdateRecord>\n" +
                        " </soap12:Body>\n" +
                        "</soap12:Envelope>"
            )
            val respnse = _req(wsdl, sb.toString(), timeout)
            if (respnse.status == -1) {
                insertXML(sb.toString())
            }
        } catch (e: Exception) {
            Log.d("upload", e.message.toString())
        }

    }

    fun Upload_IDCopyRecord(
        make: String,
        model: String,
        year: String,
        startime: String,
        Endtime: String,
        SreialNum: String,
        Devicetype: String,
        Mode: String,
        SensorCount: Int,
        position: String,
        idrecord: ArrayList<SensorRecord>
    ) {
        try {
            val sb = StringBuffer()
            sb.append(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        " <soap12:Body>\n" +
                        " <Upload_IDCopyRecord xmlns=\"http://tempuri.org/\">\n" +
                        " <SerialNum>" + SreialNum + "</SerialNum>\n" +
                        " <data>\n" +
                        " <Device_BurnIDCopy>\n" +
                        " <DeviceInfo>\n" +
                        " <enum_DeviceType>" + Devicetype + "</enum_DeviceType>\n" +
                        " <SerialNum>" + SreialNum + "</SerialNum>\n" +
                        " <enum_SensorMode>" + Mode + "</enum_SensorMode>\n" +
                        " <DateTime_Start>" + startime + "</DateTime_Start>\n" +
                        " <DateTime_End>" + Endtime + "</DateTime_End>\n" +
                        " <SensorCount>" + SensorCount + "</SensorCount>\n" +
                        " <enum_BurnPosition>" + position + "</enum_BurnPosition>\n" +
                        " </DeviceInfo>\n" +
                        " <CarInfo>\n" +
                        " <Type>" + make + "</Type>\n" +
                        " <Model>" + model + "</Model>\n" +
                        " <Year>" + year + "</Year>\n" +
                        " <CarNum>nodata</CarNum>\n" +
                        " </CarInfo>\n" +
                        " <TireInfo>\n" +
                        " <TireBrand>nodata</TireBrand>\n" +
                        " <TireModel>nodata</TireModel>\n" +
                        " <TireProcDate>nodata</TireProcDate>\n" +
                        " </TireInfo>\n" +
                        " <ConsumerInfo>\n" +
                        " <Name>nodata</Name>\n" +
                        " <Age>0</Age>\n" +
                        " <Sex>男</Sex>\n" +
                        " <Tel>nodata</Tel>\n" +
                        " <Email>nodata</Email>\n" +
                        " <Continent>nodata</Continent>\n" +
                        " <Country>nodata</Country>\n" +
                        " <State>nodata</State>\n" +
                        " <City>nodata</City>\n" +
                        " <Street>nodata</Street>\n" +
                        " </ConsumerInfo>\n" +
                        " <Record>\n"
            )
            for (record in idrecord) {
                sb.append(
                    "<IDCopy_Record>\n"
                            + " <SensorID>" + record.SensorID + "</SensorID>\n"
                            + " <Car_SensorID>" + record.Car_SensorID + "</Car_SensorID>\n"
                            + "<IsSuccess>" + record.IsSuccess + "</IsSuccess>\n"
                            + "<ModelNo >" + record.ModelNo + "</ModelNo >\n"
                            + "<enum_BurnResult>" + record.enum_BurnResult + "</enum_BurnResult>\n"
                            + "</IDCopy_Record>\n"
                )
            }
            sb.append(
                " </Record>\n" +
                        " </Device_BurnIDCopy>\n" +
                        " </data>\n" +
                        " </Upload_IDCopyRecord>\n" +
                        " </soap12:Body>\n" +
                        "</soap12:Envelope>"
            )
            val respnse = _req(wsdl, sb.toString(), timeout)
            Log.d("upload", respnse.data.toString())
            if (respnse.status == -1) {
                insertXML(sb.toString())
            }
        } catch (e: Exception) {
            Log.d("upload", e.message.toString())
        }

    }

    fun AddIfNotValid(serialnum: String, type: String) {
        try {
            val data =
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        "     <soap12:Body>\n" +
                        "     <Register xmlns=\"http://tempuri.org/\">\n" +
                        "     <Reg_UserInfo>\n" +
                        "     <UserID>" + OgPublic.admin + "</UserID>\n" +
                        "     <UserPwd>" + OgPublic.password + "</UserPwd>\n" +
                        "     </Reg_UserInfo>\n" +
                        "     <Reg_StoreInfo>\n" +
                        "     <StoreType>Distributor</StoreType>\n" +
                        "     <CompName>spare</CompName>\n" +
                        "     <FirstName>spare</FirstName>\n" +
                        "     <LastName>spare</LastName>\n" +
                        "     <Contact_Tel>spare</Contact_Tel>\n" +
                        "     <Continent>spare</Continent>\n" +
                        "     <Country>spare</Country>\n" +
                        "     <State>spare</State>\n" +
                        "     <City>spare</City>\n" +
                        "     <Street>spare</Street>\n" +
                        "     <CompTel>spare</CompTel>\n" +
                        "     </Reg_StoreInfo>\n" +
                        "     <Reg_DeviceInfo>\n" +
                        "     <SerialNum>" + serialnum + "</SerialNum>\n" +
                        "     <DeviceType>" + type + "</DeviceType>\n" +
                        "     <ModelNum>PA001</ModelNum>\n" +
                        "     <AreaNo></AreaNo>\n" +
                        "     <Firmware_1_Copy>EU-1.0</Firmware_1_Copy>\n" +
                        "     <Firmware_2_Copy>EU-1.0</Firmware_2_Copy>\n" +
                        "     <Firmware_3_Copy>EU-1.0</Firmware_3_Copy>\n" +
                        "     <DB_Copy>EU-1.0 </DB_Copy>\n" +
                        "     <MacAddress>00</MacAddress>\n" +
                        "     <IpAddress>00</IpAddress>\n" +
                        "     </Reg_DeviceInfo>\n" +
                        "     </Register>\n" +
                        "     </soap12:Body>\n" +
                        "    </soap12:Envelope>"
            val response = _req(wsdl, data, timeout)
            if (response.status == -1) {
                insertXML(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun insertXML(xml:String){
        val sqlmemnory=OgPublic.getInstance.sqlite
        sqlmemnory.exsql(
            "CREATE TABLE if not exists `xmlmemory` (\n" +
                    "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  `tx`  NOT NULL)"
        )
        sqlmemnory.exsql("insert into `xmlmemory` (xml) values ('$xml')")
    }
}
