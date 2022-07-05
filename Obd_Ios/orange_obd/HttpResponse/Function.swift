//
//  Function.swift
//  txusb
//
//  Created by 王建智 on 2019/8/23.
//  Copyright © 2019 王建智. All rights reserved.
//
import UIKit
import Foundation
class Function{
    static let link="https://bento2.orange-electronic.com/App_Asmx/ToolApp.asmx"
    static func GetVersion(){
        let act=(UIApplication.shared.delegate as! AppDelegate).act!
        let url = URL(string: "https://itunes.apple.com/lookup?bundleId=com.orange.obd")!
        let task = URLSession.shared.dataTask(with: url) {(data, response, error) in
            guard let data = data else { return }
            let DaS=String(data: data, encoding: .utf8)!
       let NewVersion=DaS.components(separatedBy: "\"version\"")[1].components(separatedBy:"\"")[1]
            if let currentUserInstallVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String {
                if(currentUserInstallVersion != NewVersion){
    print("current\(currentUserInstallVersion) new:\(NewVersion)")
                    DispatchQueue.main.async {
                        act.view.showToast(text: SetLan.Setlan("newversion"))
                    }
                }
            }
        }
        task.resume()
    }
    static func ResetPass(_ admin:String,_ act:ResetPassword){
        let url = URL(string:link)!
        var request = URLRequest(url: url)
        request.setValue("application/soap+xml; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "POST"
        var data="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "  <soap12:Body>\n" +
            "    <SysResetPwd xmlns=\"http://tempuri.org/\">\n" +
            "      <UserID>"+admin+"</UserID>\n" +
            "    </SysResetPwd>\n" +
            "  </soap12:Body>\n" +
        "</soap12:Envelope>"
        var res = -1
        request.httpBody = data.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
                let response = response as? HTTPURLResponse,
                error == nil else {
                    print("error", error ?? "Unknown error")
                    res = -1
                    DispatchQueue.main.async {
                        act.run=false
                        act.act.LoadingSuccess()
                        act.act.view.showToast(text: SetLan.Setlan("nointer"))
                    }
                    return
            }
            
            guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
                print("statusCode should be 2xx, but is \(response.statusCode)")
                print("response = \(response)")
                res = -1
                DispatchQueue.main.async {
                    act.run=false
                    act.act.LoadingSuccess()
                    act.act.view.showToast(text: SetLan.Setlan("nointer"))
                }
                return
            }
            
            let responseString = String(data: data, encoding: .utf8)
            print("responseString = \(responseString)")
            print(responseString!.components(separatedBy: "SysResetPwdResult").count)
            if(responseString!.components(separatedBy: "SysResetPwdResult").count != 3){
                res = -1
            }else{
                if(responseString!.components(separatedBy: "SysResetPwdResult")[1].replace(">", "").replace("<", "").replace("/", "").contains("true")){
                    res = 0
                }else{
                    res = 1
                }
            }
            DispatchQueue.main.async {
                act.run=false
                act.act.LoadingSuccess()
                switch (res){
                case 0:
                    act.act.GoBack(self)
                    break;
                case 1:
                    act.act.view.showToast(text: SetLan.Setlan("nointer"))
                    break;
                case -1:
                    act.act.view.showToast(text: SetLan.Setlan("nointer"))
                    break;
                default:
                    break
                }
            }
            
        }
        task.resume()
    }
    
    static func Register(_ admin:String,_ password:String,_ SerialNum:String,_ storetype:String,_ companyname:String,_ firstname:String,_ lastname:String,_ phone:String,_ State:String,_ city:String,_ streat:String,_ zp:String,_ act:Registration){
        let url = URL(string: link)!
        var request = URLRequest(url: url)
        request.setValue("application/soap+xml; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "POST"
        var data="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "  <soap12:Body>\n" +
            "    <Register xmlns=\"http://tempuri.org/\">\n" +
            "      <Reg_UserInfo>\n" +
            "        <UserID>"+admin+"</UserID>\n" +
            "        <UserPwd>"+password+"</UserPwd>\n" +
            "      </Reg_UserInfo>\n" +
            "      <Reg_StoreInfo>\n" +
            "        <StoreType>"+storetype+"</StoreType>\n" +
            "        <CompName>"+companyname+"</CompName>\n" +
            "        <FirstName>"+firstname+"</FirstName>\n" +
            "        <LastName>"+lastname+"</LastName>\n" +
            "        <Contact_Tel>"+phone+"</Contact_Tel>\n" +
            "        <Continent>"+State+"</Continent>\n" +
            "        <Country>"+State+"</Country>\n" +
            "        <State>"+city+"</State>\n" +
            "        <City>"+city+"</City>\n" +
            "        <Street>"+streat+"</Street>\n" +
            "        <CompTel>"+companyname+"</CompTel>\n" +
            "      </Reg_StoreInfo>\n" +
            "      <Reg_DeviceInfo>\n" +
            "        <SerialNum>"+SerialNum+"</SerialNum>\n" +
            "        <DeviceType>USBPad</DeviceType>\n" +
            "        <ModelNum>phone</ModelNum>\n" +
            "        <AreaNo>"+zp+"</AreaNo>\n" +
            "        <Firmware_1_Copy>EU-1.0</Firmware_1_Copy>\n" +
            "        <Firmware_2_Copy>EU-1.0</Firmware_2_Copy>\n" +
            "        <Firmware_3_Copy>EU-1.0</Firmware_3_Copy>\n" +
            "        <DB_Copy>EU-1.0 </DB_Copy>\n" +
            "        <MacAddress>00</MacAddress>\n" +
            "        <IpAddress>00</IpAddress>\n" +
            "      </Reg_DeviceInfo>\n" +
            "    </Register>\n" +
            "  </soap12:Body>\n" +
        "</soap12:Envelope>"
        var res = -1
        request.httpBody = data.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
                let response = response as? HTTPURLResponse,
                error == nil else {
                    print("error", error ?? "Unknown error")
                    res = -1
                    DispatchQueue.main.async {
                        act.run=false
                        act.act.LoadingSuccess()
                        act.act.view.showToast(text: SetLan.Setlan("nointer"))
                    }
                    return
            }
            
            guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
                print("statusCode should be 2xx, but is \(response.statusCode)")
                print("response = \(response)")
                res = -1
                DispatchQueue.main.async {
                    act.run=false
                    act.act.LoadingSuccess()
                    act.act.view.showToast(text: SetLan.Setlan("nointer"))
                }
                return
            }
            
            let responseString = String(data: data, encoding: .utf8)
            print("responseString = \(responseString)")
            print(responseString!.components(separatedBy: "RegisterResult").count)
            if(responseString!.components(separatedBy: "RegisterResult").count != 3){
                   res = -1
            }else{
                if(responseString!.components(separatedBy: "RegisterResult")[1].replace(">", "").replace("<", "").replace("/", "").contains("true")){
                    res = 0
                }else{
                    res = 1
                }
            }
            
            
            DispatchQueue.main.async {
                act.run=false
                act.act.LoadingSuccess()
                switch (res){
                case 0:
                    ViewController.writeshare("admin", admin)
                     ViewController.writeshare("password", password)
                    let a=peacedefine().HomePage
                    act.act.ChangePage(to: a)
                    break;
                case 1:
                    act.act.view.showToast(text: SetLan.Setlan("be_register"))
                    break;
                case -1:
                    act.act.view.showToast(text: SetLan.Setlan("nointer"))
                    break;
                default:
                    break
                }
            }
            
        }
        task.resume()
    }
    
    
    
    static func Signin(_ admin:String,_ password:String,_ act:Sign_in){
        let url = URL(string: link)!
        var request = URLRequest(url: url)
    request.setValue("application/soap+xml; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "POST"
        var data="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "  <soap12:Body>\n" +
            "    <ValidateUser xmlns=\"http://tempuri.org/\">\n" +
            "      <UserID>"+admin+"</UserID>\n" +
            "      <Pwd>"+password+"</Pwd>\n" +
            "    </ValidateUser>\n" +
            "  </soap12:Body>\n" +
        "</soap12:Envelope>"
         var res = -1
        request.httpBody = data.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
                let response = response as? HTTPURLResponse,
                error == nil else {
                    print("error", error ?? "Unknown error")
                 res = -1
                    DispatchQueue.main.async {
                        act.run=false
                        act.act.LoadingSuccess()
                        act.act.view.showToast(text: SetLan.Setlan("nointer"))
                    }
                    return
            }
            
            guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
                print("statusCode should be 2xx, but is \(response.statusCode)")
                print("response = \(response)")
               res = -1
                DispatchQueue.main.async {
                    act.run=false
                    act.act.LoadingSuccess()
                    act.act.view.showToast(text: SetLan.Setlan("nointer"))
                }
               return
            }
            
            let responseString = String(data: data, encoding: .utf8)
           print("responseString = \(responseString)")
            print(responseString!.components(separatedBy: "ValidateUserResult").count)
            if(responseString!.components(separatedBy: "ValidateUserResult").count != 3){
                print("noequal")
                res = -1
            }else{
                if(responseString!.components(separatedBy: "ValidateUserResult")[1].replace(">", "").replace("<", "").replace("/", "").contains("true")){
                    res = 0
                }else{
                    res = 1
                }
            }
           
           
            DispatchQueue.main.async {
                act.run=false
                act.act.LoadingSuccess()
                switch (res){
                case 0:
                    ViewController.writeshare(admin, "admin")
                    ViewController.writeshare( password,"password")
                    let a=peacedefine().HomePage
                    act.act.ChangePage(to: a)
                    break;
                case 1:
                    act.act.view.showToast(text: SetLan.Setlan("errorpass"))
                    break;
                case -1:
                    act.act.view.showToast(text: SetLan.Setlan("nointer"))
                    break;
                default:
                    break
                }
            }
            
        }
        task.resume()
    }
//    static func Upload_ProgramRecord(_ make:String,_ model:String, _ year:String, _ startime:String, _ Endtime:String, _ SreialNum:String, _ Devicetype:String, _ Mode:String, _ SensorCount:Int, _ position:String
//        ,_ idrecord:[SersorRecord]){
//        let url = URL(string: link)!
//        var request = URLRequest(url: url)
//        request.setValue("application/soap+xml; charset=utf-8", forHTTPHeaderField: "Content-Type")
//        request.httpMethod = "POST"
//        var data="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
//            "  <soap12:Body>\n" +
//            "    <Upload_VersionUpdateRecord xmlns=\"http://tempuri.org/\">\n" +
//            "      <SerialNum>\(SreialNum)</SerialNum>\n" +
//            "      <data>\n" +
//            "        <Device_BurnVersionUpdate>\n" +
//            "          <DeviceInfo>\n" +
//            "            <enum_DeviceType>\(Devicetype)</enum_DeviceType>\n" +
//            "            <SerialNum>\(SreialNum)</SerialNum>\n" +
//            "            <enum_SensorMode>\(Mode)</enum_SensorMode>\n" +
//            "            <DateTime_Start>\(startime)</DateTime_Start>\n" +
//            "            <DateTime_End>\(Endtime)</DateTime_End>\n" +
//            "            <SensorCount>\(SensorCount)</SensorCount>\n" +
//            "            <enum_BurnPosition>\(position)</enum_BurnPosition>\n" +
//            "          </DeviceInfo>\n" +
//            "          <CarInfo>\n" +
//            "            <Type>\(make)</Type>\n" +
//            "            <Model>\(model)</Model>\n" +
//            "            <Year>\(year)</Year>\n" +
//            "            <CarNum>nodata</CarNum>\n" +
//            "          </CarInfo>\n" +
//            "          <TireInfo>\n" +
//            "            <TireBrand>nodata</TireBrand>\n" +
//            "            <TireModel>nodata</TireModel>\n" +
//            "            <TireProcDate>nodata</TireProcDate>\n" +
//            "          </TireInfo>\n" +
//            "          <ConsumerInfo>\n" +
//            "            <Name>nodata</Name>\n" +
//            "            <Age>0</Age>\n" +
//            "            <Sex>男</Sex>\n" +
//            "            <Tel>nodata</Tel>\n" +
//            "            <Email>nodata</Email>\n" +
//            "            <Continent>nodata</Continent>\n" +
//            "            <Country>nodata</Country>\n" +
//            "            <State>nodata</State>\n" +
//            "            <City>nodata</City>\n" +
//            "            <Street>nodata</Street>\n" +
//            "          </ConsumerInfo>\n" +
//        "          <Record>\n"
//        for  record in idrecord{
//            data="\(data)<VersionUpdate_Record>\n" +
//                " <SensorID>\(record.SerSorId)</SensorID>\n" +
//                "<IsSuccess>\(record.Issucesss)</IsSuccess>\n" +
//                "<ModelNo>\(record.ModelNo)</ModelNo>\n" +
//                "<enum_BurnResult>\(record.enum_burnResult)</enum_BurnResult>\n" +
//                "<DB_Version>\(SersorRecord.DB_Version)</DB_Version>\n" +
//                "<SensorCode_Version>\(SersorRecord.SersorCode_Sersion)</SensorCode_Version>\n" +
//            "</VersionUpdate_Record>\n"
//        }
//        data="\(data)</Record>\n" +
//            "</Device_BurnVersionUpdate>\n" +
//            "      </data>\n" +
//            "    </Upload_VersionUpdateRecord>\n" +
//            "  </soap12:Body>\n" +
//        "</soap12:Envelope>"
//        print(data)
//        request.httpBody = data.data(using: .utf8)
//        let task = URLSession.shared.dataTask(with: request) { data, response, error in
//            guard let data = data,
//                let response = response as? HTTPURLResponse,
//                error == nil else {
//                    print("error", error ?? "Unknown error")
//
//                    return
//            }
//
//            guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
//                print("statusCode should be 2xx, but is \(response.statusCode)")
//                print("response = \(response)")
//
//                return
//            }
//
//            let responseString = String(data: data, encoding: .utf8)
//            print("responseString = \(responseString)")
//            print(responseString!.components(separatedBy: "ValidateUserResult").count)
//
//        }
//        task.resume()
//    }
    static func Upload_IDCopyRecord(_ make:String,_ model:String, _ year:String, _ startime:String, _ Endtime:String, _ SreialNum:String, _ Devicetype:String, _ Mode:String, _ SensorCount:Int, _ position:String
        ,_ idrecord:[SersorRecord]){
        let url = URL(string: link)!
        var request = URLRequest(url: url)
        request.setValue("application/soap+xml; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "POST"
        var data="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "  <soap12:Body>\n" +
            "    <Upload_IDCopyRecord xmlns=\"http://tempuri.org/\">\n" +
            "      <SerialNum>\(SreialNum)</SerialNum>\n" +
            "      <data>\n" +
            "        <Device_BurnIDCopy>\n" +
            "          <DeviceInfo>\n" +
            "            <enum_DeviceType>\(Devicetype)</enum_DeviceType>\n" +
            "            <SerialNum>\(SreialNum)</SerialNum>\n" +
            "            <enum_SensorMode>\(Mode)</enum_SensorMode>\n" +
            "            <DateTime_Start>\(startime)</DateTime_Start>\n" +
            "            <DateTime_End>\(Endtime)</DateTime_End>\n" +
            "            <SensorCount>\(SensorCount)</SensorCount>\n" +
            "            <enum_BurnPosition>\(position)</enum_BurnPosition>\n" +
            "          </DeviceInfo>\n" +
            "          <CarInfo>\n" +
            "            <Type>\(make)</Type>\n" +
            "            <Model>\(model)</Model>\n" +
            "            <Year>\(year)</Year>\n" +
            "            <CarNum>nodata</CarNum>\n" +
            "          </CarInfo>\n" +
            "          <TireInfo>\n" +
            "            <TireBrand>nodata</TireBrand>\n" +
            "            <TireModel>nodata</TireModel>\n" +
            "            <TireProcDate>nodata</TireProcDate>\n" +
            "          </TireInfo>\n" +
            "          <ConsumerInfo>\n" +
            "            <Name>nodata</Name>\n" +
            "            <Age>0</Age>\n" +
            "            <Sex>男</Sex>\n" +
            "            <Tel>nodata</Tel>\n" +
            "            <Email>nodata</Email>\n" +
            "            <Continent>nodata</Continent>\n" +
            "            <Country>nodata</Country>\n" +
            "            <State>nodata</State>\n" +
            "            <City>nodata</City>\n" +
            "            <Street>nodata</Street>\n" +
            "          </ConsumerInfo>\n" +
        "          <Record>\n"
        for  record in idrecord{
            data="\(data)<IDCopy_Record>\n" +
                " <SensorID>\(record.SerSorId)</SensorID>\n" +
                " <Car_SensorID>\(record.Car_SersorId)</Car_SensorID>\n" +
                "<IsSuccess>\(record.Issucesss)</IsSuccess>\n" +
                "<ModelNo>\(record.ModelNo)</ModelNo>\n" +
                "<enum_BurnResult>\(record.enum_burnResult)</enum_BurnResult>\n" +
            "</IDCopy_Record>\n"
        }
        data="\(data)</Record>\n" +
            "</Device_BurnIDCopy>\n" +
            "      </data>\n" +
            "    </Upload_IDCopyRecord>\n" +
            "  </soap12:Body>\n" +
        "</soap12:Envelope>"
        request.httpBody = data.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
                let response = response as? HTTPURLResponse,
                error == nil else {
                    print("error", error ?? "Unknown error")
                    
                    return
            }
            
            guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
                print("statusCode should be 2xx, but is \(response.statusCode)")
                print("response = \(response)")
                
                return
            }
            
            let responseString = String(data: data, encoding: .utf8)
            print("responseString = \(responseString)")
            print(responseString!.components(separatedBy: "ValidateUserResult").count)
            
        }
        task.resume()
    }
    static func AddIfNotValid(_ serial:String,_ admin:String,_ pass:String){
            let url = URL(string: link)!
            var request = URLRequest(url: url)
            request.setValue("application/soap+xml; charset=utf-8", forHTTPHeaderField: "Content-Type")
            request.httpMethod = "POST"
            var data="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "    <soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "      <soap12:Body>\n" +
            "        <Register xmlns=\"http://tempuri.org/\">\n" +
            "          <Reg_UserInfo>\n" +
            "            <UserID>\(admin)</UserID>\n" +
            "            <UserPwd>\(pass)</UserPwd>\n" +
            "          </Reg_UserInfo>\n" +
            "          <Reg_StoreInfo>\n" +
            "            <StoreType>Distributor</StoreType>\n" +
            "            <CompName>spare</CompName>\n" +
            "            <FirstName>spare</FirstName>\n" +
            "            <LastName>spare</LastName>\n" +
            "            <Contact_Tel>spare</Contact_Tel>\n" +
            "            <Continent>spare</Continent>\n" +
            "            <Country>spare</Country>\n" +
            "            <State>spare</State>\n" +
            "            <City>spare</City>\n" +
            "            <Street>spare</Street>\n" +
            "            <CompTel>spare</CompTel>\n" +
            "          </Reg_StoreInfo>\n" +
            "          <Reg_DeviceInfo>\n" +
            "            <SerialNum>\(serial)</SerialNum>\n" +
            "            <DeviceType>USBPad</DeviceType>\n" +
            "            <ModelNum>PA001</ModelNum>\n" +
            "            <AreaNo></AreaNo>\n" +
            "            <Firmware_1_Copy>EU-1.0</Firmware_1_Copy>\n" +
            "            <Firmware_2_Copy>EU-1.0</Firmware_2_Copy>\n" +
            "            <Firmware_3_Copy>EU-1.0</Firmware_3_Copy>\n" +
            "            <DB_Copy>EU-1.0 </DB_Copy>\n" +
            "            <MacAddress>00</MacAddress>\n" +
            "            <IpAddress>00</IpAddress>\n" +
            "          </Reg_DeviceInfo>\n" +
            "        </Register>\n" +
            "      </soap12:Body>\n" +
            "    </soap12:Envelope>"
            request.httpBody = data.data(using: .utf8)
            let task = URLSession.shared.dataTask(with: request) { data, response, error in
                guard let data = data,
                    let response = response as? HTTPURLResponse,
                    error == nil else {
                        print("error", error ?? "Unknown error")
                        
                        return
                }
                
                guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
                    print("statusCode should be 2xx, but is \(response.statusCode)")
                    print("response = \(response)")
                    
                    return
                }
                let responseString = String(data: data, encoding: .utf8)
                print("responseString = \(responseString)")
            }
            task.resume()
        }
    }

extension Dictionary {
    func percentEscaped() -> String {
        return map { (key, value) in
            let escapedKey = "\(key)".addingPercentEncoding(withAllowedCharacters: .urlQueryValueAllowed) ?? ""
            let escapedValue = "\(value)".addingPercentEncoding(withAllowedCharacters: .urlQueryValueAllowed) ?? ""
            return escapedKey + "=" + escapedValue
            }
            .joined(separator: "&")
    }
}

extension CharacterSet {
    static let urlQueryValueAllowed: CharacterSet = {
        let generalDelimitersToEncode = ":#[]@" // does not include "?" or "/" due to RFC 3986 - Section 3.4
        let subDelimitersToEncode = "!$&'()*+,;="
        
        var allowed = CharacterSet.urlQueryAllowed
        allowed.remove(charactersIn: "\(generalDelimitersToEncode)\(subDelimitersToEncode)")
        return allowed
    }()
}
