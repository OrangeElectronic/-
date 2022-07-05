//
//  Md_Sensor_Info.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/4.
//

import Foundation
import CoreBluetooth
import JzOsTaskHandler
//讀取到的Sensor資訊
//pre and tem 當沒溫度和壓力時必須帶空值
struct Sensor_Data:Codable{
    var data_name:String
    var plateNumber:String
    var count:String
    var lan:String
    var lon:String
    var address:String
    var wh:String
    var id:String
    var pre:String
    var tem:String
    var time:String
    var data:String
}
//
class Md_Sensor_Info{
    //上傳此車牌的資料
    public static func uploadSensorData(data: [Sensor_Data]){
        "資料上傳中".progress()
        var map:Dictionary<String,Any>=[:]
        map["request"] = "uploadSensor"
        print(data.toJsonString2())
        map["data"] = data.toJsonString2()
        map["userInfo"] = Md_User_Setting.getUserSetting().toJsonString2()
        print("passregister")
        HttpProtocal.startRequest(map: map, callback: {
            a,b in
            Progress.close()
            if(a == .success){
                if(b!["result"] == "true" ){
                    "資料上傳成功".showAlert()
                }else{
                    "上傳失敗".showAlert()
                }
            }else{
                "請檢查網路連線!!".showAlert()
            }
        })
    }
    
    //取得此車牌下所有Sensor資料
    public static func getSensorData(data:Data_Setting_Car) -> [Sensor_Data] {
        var sensor=[Sensor_Data]()
        for i in data.sensor{
            var sen:Sensor_Data? = i.id.getObject(rout: "Sensor_Data")
            if(sen != nil){
                print("senesorInfo\(sen!)")
                sen?.wh=i.wh
                sen?.plateNumber=data.plateNumber
                sen?.data_name=data.carType
                sen?.count=data.count
                sen?.clalauteUnit()
                if(sen!.time != "NA"){
                    sensor.append(sen!)
                }else{
                    sen=Sensor_Data(data_name: data.carType, plateNumber: data.plateNumber, count: data.count, lan: "NA", lon: "NA", address: "NA", wh: "\(i.wh)", id: i.id, pre: "NA", tem: "NA", time: "NA", data: "NA")
                    sensor.append(sen!)
                }
            }else{
                let sen=Sensor_Data(data_name: data.carType, plateNumber: data.plateNumber, count: data.count, lan: "NA", lon: "NA", address: "NA", wh: "\(i.wh)", id: i.id, pre: "NA", tem: "NA", time: "NA", data: "NA")
                sen.storeSensorInfo()
                sensor.append(sen )
            }
        }
        return sensor
    }
    //解析BLEDATA
    public static func ParsingData(tempstring:String,device:CBPeripheral?){
        if(tempstring.count > 8){
            print("data解析\(tempstring)")
            let sensorId = tempstring.sub(2..<8)
            if(device != nil){  ViewController.device[sensorId]=device}
            if(tempstring.count <= 12){
                return
            }
            var sensorData=sensorId.getSensorInfo()
            let pre = tempstring.sub(16..<20)
            print("pre=\(pre)----\(byte2Int(pre.HexToByte()!))")
            let tem = tempstring.sub(20..<22)
            if(sensorData != nil){
                sensorData!.pre = "\(byte2Int(pre.HexToByte()!))"
                sensorData!.tem = "\(byte2Int(tem.HexToByte()!)-55)"
                sensorData!.lan = "\(LocarionManager.manager.lastKnownLocation.lat)"
                sensorData!.lon = "\(LocarionManager.manager.lastKnownLocation.lon)"
                sensorData!.address = "\(LocarionManager.manager.lastKnownLocation.address.stringToUnicode())"
                sensorData!.data=tempstring
                let dataFormatter=DateFormatter()
                dataFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                sensorData!.time=dataFormatter.string(from: Date())
                sensorData!.storeSensorInfo()
                sensorData!.uploadSensor()
            }
        }
    }
    //INt轉換
    public static func byte2Int(_ array:[UInt8]) -> Int {
        let height:Int=Int(array[0])
        let low:Int=Int(array[1])
        return (height << 8 & 0xFF00) | (low & 0xFF)
    }
    public static func byte2Int(_ data:Data) -> Int {
        let array = [UInt8](data)
        let height:Int=Int(array[0])
        if(array.count>1){
            let low:Int=Int(array[1])
            return (height << 8 & 0xFF00) | (low & 0xFF)
        }else{
            return (height & 0xFF)
        }
    }
    
    
}
extension Sensor_Data{
    func uploadSensor(){
        DispatchQueue.global().async {
            TaskHandler.newInstance().runTaskDelay(self.id, 1.0, {
                var da=self
                da.data_name=self.data_name.stringToUnicode()
                var map:Dictionary<String,Any>=[:]
                map["request"] = "storeData"
                map["plate-number"] = self.plateNumber
                map["data"] = da.toJsonString2().stringToUnicode()
                HttpProtocal.startRequest(map: map, callback: {
                    a,b in
                    if(a != .success){
                        self.storeObject(name: "\(self.id)_\(self.time)" , rout: "uploadSensor")
                    }
                })
            })
            
        }
    }
    func storeSensorInfo()->Bool{
        self.storeObject(name: self.id, rout: "Sensor_Data")
    }
    //單位計算
    mutating func clalauteUnit(){
        let userSetting=Md_User_Setting.getUserSetting()
        if((!self.pre.isEmpty) && (self.pre != "NA")){
            switch userSetting.punit {
            case 0:
                break
            case 1:
                self.pre = "\(Int(Double(self.pre)!*0.145))"
                break
            case 2:
                self.pre = "\(Int(Double(self.pre)!*0.01))"
                break
            default:
                break
            }
        }
        if((!self.tem.isEmpty) && (self.tem != "NA")){
            switch userSetting.tunit {
            case 0:
                break
            case 1:
                self.tem = "\(Int(Double(self.tem)!*9/5+32))"
                break
            default:
                break
            }
        }
    }
}
extension String{
    func getSensorInfo()->Sensor_Data?{
        return self.getObject(rout: "Sensor_Data")
    }
}

class JsonConverter {
    public static   func tojson(from object:Any) -> String? {
        guard let data = try? JSONSerialization.data(withJSONObject: object, options: []) else {
            return nil
        }
        return String(data: data, encoding: String.Encoding.utf8)
    }
    
}
extension Int{
    func toKpa()->Int{
        let userSetting=Md_User_Setting.getUserSetting()
        switch userSetting.punit {
        case 0:
            return self
        case 1:
            return  Int(Double(self)/0.145)
        case 2:
            return Int(Double(self)/0.01)
        default:
            return self
        }
    }
    func toTem()->Int{
        let userSetting=Md_User_Setting.getUserSetting()
        switch userSetting.tunit {
        case 0:
            return self
        case 1:
            return Int(Double(self)/9*5-32)
        default:
            return self
        }
    }
    func toSettingPre()->Int{
        let userSetting=Md_User_Setting.getUserSetting()
        switch userSetting.punit {
        case 0:
            return self
        case 1:
            return Int(Double(self)*0.145)
        case 2:
            return Int(Double(self)*0.01)
        default:
            return self
        }
    }
    func toSettingTem()->Int{
        let userSetting=Md_User_Setting.getUserSetting()
        switch userSetting.tunit {
        case 0:
            return self
        case 1:
            return Int(Double(self)*9/5+32)
        default:
            return self
        }
    }
}
