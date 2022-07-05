//
//  LocationManager.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/25.
//

import Foundation
import CoreLocation
import MapKit
import JzOsFrameWork
class LocarionManager {
    static var manager=LocarionManager()
    private let locationManager = CLLocationManager()
    //判斷是否有定位
    func haveLocation(){
        
        "定位取得中".progress()
        guard CLLocationManager.locationServicesEnabled() else{
            "在\"設置\">\"隱私\"中打開\"定位服務\"來允許APP確認您的當前位置".showAlert()
           
            return
        }
        // 首次使用 向使用者詢問定位自身位置權限
        if CLLocationManager.authorizationStatus()
            == .notDetermined {
            // 取得定位服務授權
            locationManager.requestWhenInUseAuthorization()
            locationManager.startUpdatingLocation()
            if(locationManager.location?.coordinate.latitude == nil){
                DispatchQueue.global().async {
                    while( CLLocationManager.authorizationStatus() == .notDetermined){sleep(1)}
                    if(self.locationManager.location?.coordinate.latitude == nil){
                        DispatchQueue.main.async {
                            Progress.close()
                            "取得定位失敗".showAlert()
                        }
                    }else{
                        DispatchQueue.main.async { [self] in
                            Progress.close()
                            store()
                            "取得定位成功".showAlert()
                        }
                    }
                }
            }else{
                DispatchQueue.main.async { [self] in
                    Progress.close()
                    store()
                    "取得定位成功".showAlert()
                }
            }
        }
            // 使用者已經拒絕定位自身位置權限
        else if CLLocationManager.authorizationStatus()
            == .denied {
            // 提示可至[設定]中開啟權限
            "定位權限已關閉，如要變更權限，請至 設定 > 隱私權 > 定位服務 開啟".showAlert()
        }
            // 使用者已經同意定位自身位置權限
        else if CLLocationManager.authorizationStatus()
            == .authorizedWhenInUse {
            // 開始定位自身位置
            locationManager.startUpdatingLocation()
            if(locationManager.location?.coordinate.latitude == nil){
                DispatchQueue.global().async {
                    sleep(3)
                    if(self.locationManager.location?.coordinate.latitude == nil){
                        DispatchQueue.main.async {
                            Progress.close()
                            "取得定位失敗".showAlert()
                        }
                    }else{
                        DispatchQueue.main.async { [self] in
                            Progress.close()
                            store()
                            "取得定位成功".showAlert()
                          
                        }
                    }
                }
            }else{
                Progress.close()
                store()
                "取得定位成功".showAlert()
            }
        }else{
            Progress.close()
            store()
            "取得定位成功".showAlert()
        }
        
    }
     var lastKnownLocation=LocationData()
    func store(){
        //1
        let locale = Locale(identifier: "zh-tw")
        let loc: CLLocation = CLLocation(latitude: (self.locationManager.location!.coordinate.latitude), longitude: (self.locationManager.location!.coordinate.longitude))
        if #available(iOS 11.0, *) {
            CLGeocoder().reverseGeocodeLocation(loc, preferredLocale: locale) { placeMark, error in
                guard let placeMark = placeMark?.first, error == nil else {
                    UserDefaults.standard.removeObject(forKey: "AppleLanguages")
                   return
                }
                var b = self.lastKnownLocation
                b.address = "\(placeMark.postalCode ?? "") \(placeMark.country ?? "") \(placeMark.locality ?? "") \(placeMark.name ?? "")"
                self.lastKnownLocation=b
                let page=JzActivity.getControlInstance.getPageByTag("Page_Home") as! Page_Home
                page.location.text=b.address

            }
        }
        self.lastKnownLocation = LocationData(lat: String(self.locationManager.location!.coordinate.latitude), lon: String(self.locationManager.location!.coordinate.longitude))
       
    }
    
}

struct LocationData {
    var lat:String="NA"
    var lon:String="NA"
    var address:String="尚未取得位置"
}
