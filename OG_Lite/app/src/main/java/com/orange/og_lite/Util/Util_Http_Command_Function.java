package com.orange.og_lite.Util;

import android.util.Log;

import com.orange.og_lite.beans.Bs_RetNode;
import com.orange.og_lite.beans.Bs_SensorRecord;
import com.orange.og_lite.beans.PublicBeans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Util_Http_Command_Function {
    public static final String  TAG="Bs_RetNode";
    public static final int timeout=10000;
    public static final String wsdl = "https://bento2.orange-electronic.com/App_Asmx/ToolApp.asmx";
    private static Bs_RetNode _req(String url_String, String data, int timeout) {
        try{ Log.d(TAG + "_post", "url: " + url_String);
            Log.d(TAG + "_post", "data: " + data);
            URL url = new URL(url_String);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            conn.setRequestProperty("Content-Length", ""+data.getBytes().length);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(data.getBytes(StandardCharsets.UTF_8));
            dos.flush();
            Bs_RetNode bsRetNode = new Bs_RetNode();
            bsRetNode.status=conn.getResponseCode();
            bsRetNode.data = "";
            if(bsRetNode.status==200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                StringBuffer strBuf = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line);
                }
                bsRetNode.data=strBuf.toString();
                reader.close();
            }
            dos.close();
            Log.d(TAG+"_post", "-------------respond data--------------");
            Log.d(TAG+"_post", "Data:"+ bsRetNode.data);
            Log.d(TAG+"_post", "---------------------------------------");
            Log.d(TAG+"_post", "status: "+ bsRetNode.status);
            Log.d(TAG+"_post", "-------------data end--------------");
            return bsRetNode;
        }catch (Exception e){
            Bs_RetNode bsRetNode = new Bs_RetNode();
            bsRetNode.status = -1;
            bsRetNode.data = "";
            Log.d("_post",e.getMessage());
            return bsRetNode;
        }
    }
    public static boolean ResetPassword(String admin){
        try{
            StringBuffer sb = new StringBuffer();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    " <soap12:Body>\n" +
                    " <SysResetPwd xmlns=\"http://tempuri.org/\">\n" +
                    " <UserID>"+admin+"</UserID>\n" +
                    " </SysResetPwd>\n" +
                    " </soap12:Body>\n" +
                    "</soap12:Envelope>");

            return _req(wsdl,sb.toString(),timeout).status==200;
        }catch(Exception e){e.printStackTrace();return false;}
    }
    public static boolean ValidateUser(String admin,String password){
        try{
            StringBuffer sb = new StringBuffer();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    " <soap12:Body>\n" +
                    " <ValidateUser xmlns=\"http://tempuri.org/\">\n" +
                    " <UserID>"+admin+"</UserID>\n" +
                    " <Pwd>"+password+"</Pwd>\n" +
                    " </ValidateUser>\n" +
                    " </soap12:Body>\n" +
                    "</soap12:Envelope>");
            Bs_RetNode respnse=_req(wsdl,sb.toString(),timeout);
            return respnse.data.substring(respnse.data.indexOf("<ValidateUserResult>") + 20, respnse.data.indexOf("</ValidateUserResult>")).equals("true");
        }catch(Exception e){e.printStackTrace();return false;}
    }
    public static int Register(String admin,String password,String store,String SerialNum,String companyname,String firstname,String lastname,String phone,String State,String country,String city,String streat,String zp){
        try{
            StringBuffer sb = new StringBuffer();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    " <soap12:Body>\n" +
                    " <Register xmlns=\"http://tempuri.org/\">\n" +
                    " <Reg_UserInfo>\n" +
                    " <UserID>"+admin+"</UserID>\n" +
                    " <UserPwd>"+password+"</UserPwd>\n" +
                    " </Reg_UserInfo>\n" +
                    " <Reg_StoreInfo>\n" +
                    " <StoreType>"+ store +"</StoreType>\n" +
                    " <CompName>"+companyname+"</CompName>\n" +
                    " <FirstName>"+firstname+"</FirstName>\n" +
                    " <LastName>"+lastname+"</LastName>\n" +
                    " <Contact_Tel>"+phone+"</Contact_Tel>\n" +
                    " <Continent>NA</Continent>\n" +
                    " <Country>"+country+"</Country>\n" +
                    " <State>"+State+"</State>\n" +
                    " <City>"+city+"</City>\n" +
                    " <Street>"+streat+"</Street>\n" +
                    " <CompTel>"+companyname+"</CompTel>\n" +
                    " </Reg_StoreInfo>\n" +
                    " <Reg_DeviceInfo>\n" +
                    " <SerialNum>"+SerialNum+"</SerialNum>\n" +
                    " <DeviceType>USBPad</DeviceType>\n" +
                    " <ModelNum>PA001</ModelNum>\n" +
                    " <AreaNo>"+zp+"</AreaNo>\n" +
                    " <Firmware_1_Copy>EU-1.0</Firmware_1_Copy>\n" +
                    " <Firmware_2_Copy>EU-1.0</Firmware_2_Copy>\n" +
                    " <Firmware_3_Copy>EU-1.0</Firmware_3_Copy>\n" +
                    " <DB_Copy>EU-1.0 </DB_Copy>\n" +
                    " <MacAddress>00</MacAddress>\n" +
                    " <IpAddress>00</IpAddress>\n" +
                    " </Reg_DeviceInfo>\n" +
                    " </Register>\n" +
                    " </soap12:Body>\n" +
                    "</soap12:Envelope>");
            Bs_RetNode respnse=_req(wsdl,sb.toString(),timeout);
            if(respnse.status!=200){return -1;}
            if(respnse.data.substring(respnse.data.indexOf("<RegisterResult>")+16,respnse.data.indexOf("</RegisterResult>")).equals("true")){
                return 0;
            }else{
                return 1;
            }
        }catch(Exception e){e.printStackTrace();return -1;}

    }
    public static void Upload_ProgramRecord(String make, String model, String year, String startime, String Endtime, String SreialNum, String Devicetype, String Mode, int SensorCount, String position
            , ArrayList<Bs_SensorRecord> idrecord){try{
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                " <soap12:Body>\n" +
                " <Upload_VersionUpdateRecord xmlns=\"http://tempuri.org/\">\n" +
                " <SerialNum>"+SreialNum+"</SerialNum>\n" +
                " <data>\n" +
                " <Device_BurnVersionUpdate>\n" +
                " <DeviceInfo>\n" +
                " <enum_DeviceType>"+Devicetype+"</enum_DeviceType>\n" +
                " <SerialNum>"+SreialNum+"</SerialNum>\n" +
                " <enum_SensorMode>"+Mode+"</enum_SensorMode>\n" +
                " <DateTime_Start>"+startime+"</DateTime_Start>\n" +
                " <DateTime_End>"+Endtime+"</DateTime_End>\n" +
                " <SensorCount>"+SensorCount+"</SensorCount>\n" +
                " <enum_BurnPosition>"+position+"</enum_BurnPosition>\n" +
                " </DeviceInfo>\n" +
                " <CarInfo>\n" +
                " <Type>"+make+"</Type>\n" +
                " <Model>"+model+"</Model>\n" +
                " <Year>"+year+"</Year>\n" +
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
                " <Sex>???</Sex>\n" +
                " <Tel>nodata</Tel>\n" +
                " <Email>nodata</Email>\n" +
                " <Continent>nodata</Continent>\n" +
                " <Country>nodata</Country>\n" +
                " <State>nodata</State>\n" +
                " <City>nodata</City>\n" +
                " <Street>nodata</Street>\n" +
                " </ConsumerInfo>\n" +
                " <Record>\n");
        for(Bs_SensorRecord record:idrecord){
            sb.append("<VersionUpdate_Record>\n"
                    +" <SensorID>"+record.SensorID+"</SensorID>\n"
                    +"<IsSuccess>"+record.IsSuccess+"</IsSuccess>\n"
                    +"<ModelNo >"+record.ModelNo+"</ModelNo >\n"
                    +"<enum_BurnResult>"+record.enum_BurnResult+"</enum_BurnResult>\n"
                    +"<DB_Version>"+ Bs_SensorRecord.DB_Version+"</DB_Version>\n"
                    +"<SensorCode_Version>"+ Bs_SensorRecord.SensorCode_Version+"</SensorCode_Version>\n"
                    +"</VersionUpdate_Record>\n" );
        }
        sb.append(
                " </Record>\n" +
                        " </Device_BurnVersionUpdate>\n" +
                        " </data>\n" +
                        " </Upload_VersionUpdateRecord>\n" +
                        " </soap12:Body>\n" +
                        "</soap12:Envelope>");
        Bs_RetNode respnse=_req(wsdl,sb.toString(),timeout);
    }catch(Exception e){ Log.d("upload",e.getMessage());}}
    public static void Upload_IDCopyRecord(String make,String model,String year,String startime,String Endtime,String SreialNum,String Devicetype,String Mode,int SensorCount,String position
            ,ArrayList<Bs_SensorRecord> idrecord){try{
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                " <soap12:Body>\n" +
                " <Upload_IDCopyRecord xmlns=\"http://tempuri.org/\">\n" +
                " <SerialNum>"+SreialNum+"</SerialNum>\n" +
                " <data>\n" +
                " <Device_BurnIDCopy>\n" +
                " <DeviceInfo>\n" +
                " <enum_DeviceType>"+Devicetype+"</enum_DeviceType>\n" +
                " <SerialNum>"+SreialNum+"</SerialNum>\n" +
                " <enum_SensorMode>"+Mode+"</enum_SensorMode>\n" +
                " <DateTime_Start>"+startime+"</DateTime_Start>\n" +
                " <DateTime_End>"+Endtime+"</DateTime_End>\n" +
                " <SensorCount>"+SensorCount+"</SensorCount>\n" +
                " <enum_BurnPosition>"+position+"</enum_BurnPosition>\n" +
                " </DeviceInfo>\n" +
                " <CarInfo>\n" +
                " <Type>"+make+"</Type>\n" +
                " <Model>"+model+"</Model>\n" +
                " <Year>"+year+"</Year>\n" +
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
                " <Sex>???</Sex>\n" +
                " <Tel>nodata</Tel>\n" +
                " <Email>nodata</Email>\n" +
                " <Continent>nodata</Continent>\n" +
                " <Country>nodata</Country>\n" +
                " <State>nodata</State>\n" +
                " <City>nodata</City>\n" +
                " <Street>nodata</Street>\n" +
                " </ConsumerInfo>\n" +
                " <Record>\n");
        for(Bs_SensorRecord record:idrecord){
            sb.append("<IDCopy_Record>\n"
                    +" <SensorID>"+record.SensorID+"</SensorID>\n"
                    +" <Car_SensorID>"+record.Car_SensorID+"</Car_SensorID>\n"
                    +"<IsSuccess>"+record.IsSuccess+"</IsSuccess>\n"
                    +"<ModelNo >"+record.ModelNo+"</ModelNo >\n"
                    +"<enum_BurnResult>"+record.enum_BurnResult+"</enum_BurnResult>\n"
                    +"</IDCopy_Record>\n" );
        }
        sb.append(
                " </Record>\n" +
                        " </Device_BurnIDCopy>\n" +
                        " </data>\n" +
                        " </Upload_IDCopyRecord>\n" +
                        " </soap12:Body>\n" +
                        "</soap12:Envelope>");
        Bs_RetNode respnse=_req(wsdl,sb.toString(),timeout);
        Log.d("upload",respnse.data.toString());
    }catch(Exception e){ Log.d("upload",e.getMessage());}}
    public static boolean AddIfNotValid(String serialnum){
        try{
            return Util_Http_Command_Function.Register(PublicBeans.Companion.getAdmin(),PublicBeans.Companion.getPassword(),"Distributor",serialnum,"Distributor","spare","spare","spare","spare","spare","spare","spare","")==0;
        }catch (Exception e){e.printStackTrace();return false;}
    }
    public static boolean resetPassword(String password){
        try{
            return Util_Http_Command_Function.Register(PublicBeans.Companion.getAdmin(),password,PublicBeans.Companion.getSerial(),"Distributor","Distributor","spare","spare","spare","spare","spare","spare","spare","")==0;
        }catch (Exception e){e.printStackTrace();return false;}
    }
}
