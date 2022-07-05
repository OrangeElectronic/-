//
//  Md_Setting_Car.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/3.
//

import Foundation
import JzOsSerialization
import UIKit
import JzOsFrameWork
import JzOsTaskHandler
//車輛資料
struct Data_Setting_Car :Codable{
    var plateNumber:String
    var carName:String
    var carType:String
    var count:String
    var make:String
    var model:String
    var year:String
    var weight:String
    var sensor:[SensorPosition]
    var isFront:Bool=true
}
//Sensor的放置位置和輪胎條碼
struct SensorPosition:Codable {
    var id:String
    var wh:String
    var barCode:String
}
//操作model
class Md_Setting_Car{
    //取得所有車輛
    public static func getSettingCar()->[Data_Setting_Car]{
        var data=[Data_Setting_Car]()
        for i in  "Data_Setting_Car".listObject(){
            let item:Data_Setting_Car? = i.getObject()
            if(item != nil){data.append(item!)}
        }
        return data
    }
    //取得此車牌的車輛
    public static func getCarByPlate(plate:String)->Data_Setting_Car?{
        return plate.getObject(rout: "Data_Setting_Car")
    }
}
//View的顯示Model
struct Data_View_Car {
    var adapterCount:Int
    var height:CGFloat
    var carView:UIViewController
    var wheelData:[[Int]]
    var wheel:[UIImageView]
    var sensorCount:Int
    var translate:Int=0
    var startIndext:Int=1
}

extension Data_Setting_Car{
    //設定車輛
     func setSettingCar(callback:@escaping()->Int){
        var dataMap=self
        dataMap.carName=Md_User_Setting.getUserSetting().name.stringToUnicode()
        dataMap.sensor.sort{ (wh1, wh2) -> Bool in
            return wh1.wh.compare(wh2.wh) == ComparisonResult.orderedAscending
        }
        var dataMap2=Md_Setting_Car.getCarByPlate(plate: self.plateNumber)
        if(dataMap2 != nil){
            dataMap2!.sensor.sort{ (wh1, wh2) -> Bool in
                return wh1.wh.compare(wh2.wh) == ComparisonResult.orderedAscending
            }
            if(dataMap.toJsonString2() == dataMap2.toJsonString2()){
                callback()
                return
            }
        }
       
        "車輛設定中....".progress()
        var map:Dictionary<String,Any>=[:]
        map["request"] = "settingCar"
        map["data"] = dataMap.toJsonString2().stringToUnicode()
        map["admin"] = Md_User_Setting.getUserSetting().admin
        map["plate"] =  self.plateNumber
        HttpProtocal.startRequest(map: map, callback: {
            a,b in
            if(a == .success){
                if(b!["result"] == "true"){
                    dataMap.storeObject(name: self.plateNumber,rout:"Data_Setting_Car")
                    DispatchQueue.main.async {
                        "車輛設定成功".showAlert()
                        callback()
                        Progress.close()
                    }
                }else{
                    Progress.close()
                    "車輛設定失敗".showAlert()
                }
            }else{
                DispatchQueue.main.async {
                    "請檢查網路連線...".showAlert()
                    Progress.close()
                }
            }
        })
    }
    //刪除車輛
    func deleteCar(callback:@escaping()->Int){
           "車輛刪除中....".progress()
           var map:Dictionary<String,Any>=[:]
           map["request"] = "deleteCar"
           map["plate"] = self.plateNumber
           HttpProtocal.startRequest(map: map, callback: {
               a,b in
               if(a == .success){
                   if(b!["result"] == "true"){
                    self.plateNumber.deleteObject(rout: "Data_Setting_Car")
                       DispatchQueue.main.async {
                           "車輛刪除成功".showAlert()
                           callback()
                           Progress.close()
                       }
                   }else{
                       Progress.close()
                       "車輛刪除失敗".showAlert()
                   }
               }else{
                   DispatchQueue.main.async {
                       "請檢查網路連線...".showAlert()
                       Progress.close()
                   }
               }
           })
       
    }
    //取得View的顯示資料
    func getAdapterViewInfo()->Data_View_Car{
        var data=Data_View_Car(adapterCount: 0, height: 0,carView: Car_4(),wheelData: [], wheel: [], sensorCount: 4)
        print("CarType=\(self.carType)")
//        let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(ViewController.cellTappedMethod(_:)))
        switch self.carType {
        case "4":
            data.height=400/2-25
            data.adapterCount=4
            data.carView=Car_4()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([4])
            data.wheelData.append([3])
            data.sensorCount=4
            break
        case "6":
            data.height=400/2-25
            data.adapterCount=4
            data.carView=Car_6()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([5,6])
            data.wheelData.append([4,3])
            data.sensorCount=6
            break
        case "8_242":
            data.adapterCount=6
            data.height=400/3
            data.carView=Car_8_242()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([7,8])
            data.wheelData.append([4,3])
            data.wheelData.append([6])
            data.wheelData.append([5])
            data.sensorCount=8
            break
        case "8_224":
            data.adapterCount=6
            data.height=400/3
            data.carView=Car_8_224()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([8])
            data.wheelData.append([3])
            data.wheelData.append([6,7])
            data.wheelData.append([5,4])
            data.sensorCount=8
            break
        case "10_244":
            data.adapterCount=6
            data.height=400/3
            data.carView=Car_10_244()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([9,10])
            data.wheelData.append([4,3])
            data.wheelData.append([7,8])
            data.wheelData.append([6,5])
            data.sensorCount=10
            break
        case "10_2224":
            data.adapterCount=8
            data.height=400/4
            data.carView=Car_10_2224()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([10])
            data.wheelData.append([3])
            data.wheelData.append([9])
            data.wheelData.append([4])
            data.wheelData.append([7,8])
            data.wheelData.append([6,5])
            data.sensorCount=10
            break
        case "12":
            data.adapterCount=8
            data.height=400/4
            data.carView=Car_12_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([12])
            data.wheelData.append([3])
            data.wheelData.append([10,11])
            data.wheelData.append([5,4])
            data.wheelData.append([8,9])
            data.wheelData.append([7,6])
            data.sensorCount=12
            break
        case "14":
            break
        case "6+8":
            if(self.isFront){
                data.adapterCount=4
                data.height=400/2-30
                data.carView=Car_Front_6()
                data.wheelData.append([1])
                data.wheelData.append([2])
                data.wheelData.append([5,6])
                data.wheelData.append([4,3])
                data.sensorCount=6
            }else{
                data.adapterCount=4
                data.height=400/2-30
                data.carView=Car_Rear_8()
                data.wheelData.append([7,8])
                data.wheelData.append([10,9])
                data.wheelData.append([13,14])
                data.wheelData.append([12,11])
                data.sensorCount=8
                for i in data.carView.view.listallView(){
                    if(i.tag>0){
                        i.tag=6+i.tag
                    }
                }
            }
            break
        case "6+10":
            if(self.isFront){
                data.adapterCount=4
                data.height=400/2-30
                data.carView=Car_Front_6()
                data.wheelData.append([1])
                data.wheelData.append([2])
                data.wheelData.append([5,6])
                data.wheelData.append([4,3])
                data.sensorCount=6
            }else{
                data.adapterCount=6
                data.height=400/3-30
                data.translate=30
                data.carView=Car_Rear_10()
                data.wheelData.append([7])
                data.wheelData.append([8])
                data.wheelData.append([15,16])
                data.wheelData.append([10,9])
                data.wheelData.append([13,14])
                data.wheelData.append([12,11])
                data.sensorCount=16
                for i in data.carView.view.listallView(){
                    if(i.tag>0){
                        i.tag=6+i.tag
                    }
                }
            }
            break
        case "6+12":
            break
        case "6+4":
            if(self.isFront){
                data.adapterCount=4
                data.height=400/2-30
                data.carView=Car_Front_6()
                data.wheelData.append([1])
                data.wheelData.append([2])
                data.wheelData.append([5,6])
                data.wheelData.append([4,3])
                data.sensorCount=6
            }else{
                data.adapterCount=4
                data.height=400/3-30
                data.translate=100
                data.carView=Car_Rear_4()
                data.wheelData.append([7])
                data.wheelData.append([8])
                data.wheelData.append([10])
                data.wheelData.append([9])
                data.sensorCount=10
                data.startIndext=7
                for i in data.carView.view.listallView(){
                    if(i.tag>0){
                        i.tag=6+i.tag
                    }
                }
            }
            break
        default: break
        }
        for i in data.carView.view.listallView(){
            print("imageTag:\(i.tag)")
            if(i.tag > 0){
                var imagev = i as! UIImageView
                data.wheel.append(imagev)
            }
        }
        return data
    }
    //取得設定View的顯示資料
    func getSettingAdapterViewInfo()->Data_View_Car{
       
        var data=Data_View_Car(adapterCount: 0, height: 0,carView: Car_4(),wheelData: [], wheel: [], sensorCount: 4)
        switch self.carType {
        case "4":
            data.height=(300/2)-20
            data.adapterCount=4
            data.carView=Car_4_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([4])
            data.wheelData.append([3])
            data.sensorCount=4
            break
        case "6":
            data.height=(300/2)-20
            data.adapterCount=4
            data.carView=Car_6_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([5,6])
            data.wheelData.append([4,3])
            data.sensorCount=6
            break
        case "8_242":
            data.adapterCount=6
            data.height=(300/3)
            data.carView=Car_8_242_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([7,8])
            data.wheelData.append([4,3])
            data.wheelData.append([6])
            data.wheelData.append([5])
            data.sensorCount=8
            break
        case "8_224":
            data.adapterCount=6
            data.height=300/3
            data.carView=Car_8_224_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([8])
            data.wheelData.append([3])
            data.wheelData.append([6,7])
            data.wheelData.append([5,4])
            data.sensorCount=8
            break
        case "10_244":
            data.adapterCount=6
            data.height=300/3
            data.carView=Car_10_244_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([9,10])
            data.wheelData.append([4,3])
            data.wheelData.append([7,8])
            data.wheelData.append([6,5])
            data.sensorCount=10
            break
        case "10_2224":
            data.adapterCount=8
            data.height=300/4
            data.carView=Car_10_2224_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([10])
            data.wheelData.append([3])
            data.wheelData.append([9])
            data.wheelData.append([4])
            data.wheelData.append([7,8])
            data.wheelData.append([6,5])
            data.sensorCount=10
            break
        case "12":
            data.adapterCount=8
            data.height=300/4
            data.carView=Car_12_Smail()
            data.wheelData.append([1])
            data.wheelData.append([2])
            data.wheelData.append([12])
            data.wheelData.append([3])
            data.wheelData.append([10,11])
            data.wheelData.append([5,4])
            data.wheelData.append([8,9])
            data.wheelData.append([7,6])
            data.sensorCount=12
            break
        case "14":
            break
        case "6+8":
            if(self.isFront){
                data.adapterCount=4
                data.height=300/2-30
                data.translate=30
                data.carView=Car_Front_6_Small()
                data.carView.view.transform.ty = -20
                data.wheelData.append([1])
                data.wheelData.append([2])
                data.wheelData.append([5,6])
                data.wheelData.append([4,3])
                data.sensorCount=6
            }else{
                data.adapterCount=4
                data.height=300/2
                data.carView=Car_Rear_8_Small()
                data.wheelData.append([7,8])
                data.wheelData.append([10,9])
                data.wheelData.append([13,14])
                data.wheelData.append([12,11])
                data.sensorCount=14
                data.startIndext=7
                print("changeTag")
                for i in data.carView.view.listallView(){
                    if(i.tag>0){
                        print("changeTag")
                        i.tag=6+i.tag
                    }
                }
            }
            break
        case "6+10":
            if(self.isFront){
                data.adapterCount=4
                data.height=300/2-30
                data.translate=30
                data.carView=Car_Front_6_Small()
                data.carView.view.transform.ty = -20
                data.wheelData.append([1])
                data.wheelData.append([2])
                data.wheelData.append([5,6])
                data.wheelData.append([4,3])
                data.sensorCount=6
            }else{
                data.adapterCount=6
                data.height=300/3-20
                data.translate=50
                data.carView=Car_Rear_10_Small()
                data.wheelData.append([7])
                data.wheelData.append([8])
                data.wheelData.append([15,16])
                data.wheelData.append([10,9])
                data.wheelData.append([13,14])
                data.wheelData.append([12,11])
                data.sensorCount=16
                data.startIndext=7
                for i in data.carView.view.listallView(){
                    if(i.tag>0){
                        i.tag=6+i.tag
                    }
                }
            }
            break
        case "6+12":
            break
        case "6+4":
            if(self.isFront){
                data.adapterCount=4
                data.height=300/2-30
                data.translate=30
                data.carView=Car_Front_6_Small()
                data.carView.view.transform.ty = -20
                data.wheelData.append([1])
                data.wheelData.append([2])
                data.wheelData.append([5,6])
                data.wheelData.append([4,3])
                data.sensorCount=6
            }else{
                data.adapterCount=4
                data.height=300/3-20
                data.translate=100
                data.carView=Car_Rear_4_Small()
                data.wheelData.append([7])
                data.wheelData.append([8])
                data.wheelData.append([10])
                data.wheelData.append([9])
                data.sensorCount=10
                data.startIndext=7
                for i in data.carView.view.listallView(){
                    if(i.tag>0){
                        i.tag=6+i.tag
                    }
                }
            }
            break
        default: break
        }
        for i in data.carView.view.listallView(){
            print("imageTag:\(i.tag)")
            if(i.tag > 0){
                let imagev = i as! UIImageView
                data.wheel.append(imagev)
            }
        }
        return data
    }
   
 
}
