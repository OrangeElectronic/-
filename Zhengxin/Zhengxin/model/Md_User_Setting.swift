//
//  Md_User_Setting.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/4.
//

import Foundation
import JzOsFrameWork
import JzOsSerialization
//用戶設定
struct Data_User_Setting :Codable{
    //最小壓力值
    var minPre:Int=672
    //最大壓力值
    var maxPre:Int=896
    //0為KPA 1為PSI 2為BAR
    var punit:Int=1
    //0為C 1為F
    var tunit:Int=0
    //用戶帳號
    var admin:String=""
    //用戶密碼
    var password:String=""
    //用戶語言
    var lan:String="CN"
    //用戶名稱
    var name:String="NA"
    //標準壓力值
    var genalPre:Int=896
    //溫度警報
    var temAdvice:Int=80
    //標準呀百分比
    var preAdviceRatio:String="25"
    //用戶姓氏
    var first_Name:String="NA"
    //用戶電話
    var phone:String="NA"
    //用戶公司
    var companyName:String="NA"
    //是否為管理者
    var manager:String="NA"
    //是否為駕駛員
    var driver:String="NA"
}
class Md_User_Setting {
    //取得用戶設定
    public static func getUserSetting()->Data_User_Setting{
        let data:Data_User_Setting? = "Data_User_Setting".getObject()
        return (data == nil) ? Data_User_Setting(minPre: 540, maxPre: 770, punit: 0,tunit: 0, admin: "NA", password: "NA", lan: "TW",name: "orangeRd", genalPre: 800,temAdvice: 80, preAdviceRatio: "25") : data!
    }
    //取得用戶設定(tem和pre做單位換算)
//    public static func getUserSettingWithUnit()->Data_User_Setting{
//        var data:Data_User_Setting? = "Data_User_Setting".getObject()
//        if((data == nil)){
//            data=Data_User_Setting(minPre: 540, maxPre: 770, punit: 0,tunit: 0, admin: "NA", password: "NA", lan: "TW",name: "orangeRd", genalPre: 800,temAdvice: 80, preAdviceRatio: "25")
//        }else{
//            switch data!.punit {
//            case 0:
//                break
//            case 1:
//                data!.maxPre = Int(Double(data!.maxPre)*0.145)
//                data!.minPre = Int(Double(data!.minPre)*0.145)
//                data!.genalPre = Int(Double(data!.genalPre)*0.145)
//                break
//            case 2:
//                data!.maxPre = Int(Double(data!.maxPre)*0.01)
//                data!.minPre = Int(Double(data!.minPre)*0.01)
//                data!.genalPre = Int(Double(data!.genalPre)*0.01)
//                break
//            default:
//                break
//            }
//            switch data!.tunit {
//            case 0:
//                break
//            case 1:
//                data!.temAdvice = Int(Double(data!.temAdvice)*9/5)+32
//                break
//            default:
//                break
//            }
//        }
//        return data!
//    }
   
}

extension Data_User_Setting{
    //儲存Kpa單位和C單位
    mutating func storeWithOriginalUnit(){
        switch self.punit {
        case 0:
            break
        case 1:
            self.maxPre = Int(Double(self.maxPre)/0.145)
            self.minPre = Int(Double(self.minPre)/0.145)
            self.genalPre = Int(Double(self.genalPre)/0.145)
            break
        case 2:
            self.maxPre = Int(Double(self.maxPre)*0.01)
            self.minPre = Int(Double(self.minPre)*0.01)
            self.genalPre = Int(Double(self.genalPre)*0.01)
            break
        default:
            break
        }
        switch self.tunit {
        case 0:
            break
        case 1:
            print("tunitOriginal:\(self.temAdvice)")
            self.temAdvice = Int(Double(self.temAdvice-32)/9*5)
            print("tunitConvert:\(self.temAdvice)")
            break
        default:
            break
        }
        self.changeInfo(result: {
           
            return 1
           
        })
       
    }
    //註冊
    public  func register(){
        "註冊中".progress()
        var map:Dictionary<String,Any>=[:]
        map["request"] = "register"
        map["data"] = self.toJsonString().stringToUnicode()
        print("passregister")
        HttpProtocal.startRequest(map: map, callback:{
            a,b in
            Progress.close()
            if(a == .success){
                if(b!["result"] == "true"){
                    "註冊成功".showAlert()
                    JzActivity.getControlInstance.setHome(Page_Home(), "Page_Home")
                    self.setUserInfoMation()
                }else{
                    "此帳號已註冊".showAlert()
                }
            }else{
                "請檢查網路連線!!".showAlert()
            }
        })
    }
    //登入
    public  func login(){
        "登入中".progress()
        var map:Dictionary<String,Any>=[:]
        map["request"] = "login"
        map["data"] = self.toJsonString().stringToUnicode()
        print("passregister")
        HttpProtocal.startRequest(map: map, callback: {
            a,b in
            Progress.close()
            if(a == .success){
                "Data_Setting_Car".deleteSerialRout()
                let data:Data_User_Setting? = b!["data"]?.jsonToObject()
                let carData:[Data_Setting_Car]? = b!["carData"]?.jsonToObject()
                if(b!["result"] == "true" && data != nil && carData != nil){
                    if(data != nil){
                        data!.setUserInfoMation()
                        JzActivity.getControlInstance.toast("登入成功")
                        JzActivity.getControlInstance.setHome(Page_Home(), "Page_Home")
                    }
                    for i in carData!{
                        i.storeObject(name: i.plateNumber,rout:"Data_Setting_Car")
                    }
                }else{
                    "登入失敗".showAlert()
                }
            }else{
                "請檢查網路連線!!".showAlert()
            }
        })
    }
    //存紀錄
    public func setUserInfoMation(){
        self.storeObject(name: "Data_User_Setting")
    }
    //更改紀錄
    public func changeInfo(result:@escaping()->Int){
        "設定中".progress()
        var map:Dictionary<String,Any>=[:]
        map["request"] = "updateSetting"
        map["data"] = self.toJsonString().stringToUnicode()
        HttpProtocal.startRequest(map: map, callback: {
            a,b in
            Progress.close()
            if(a == .success){
                if(b!["result"] == "true" ){
                    "設定成功!!".showAlert()
                    result()
                    self.storeObject(name: "Data_User_Setting")
                }else{
                    "設定失敗!!".showAlert()
                }
            }else{
                "請檢查網路連線!!".showAlert()
            }
        })
    }
}

