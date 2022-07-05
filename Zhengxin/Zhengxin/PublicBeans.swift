//
//  PublicBeans.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/3.
//

import Foundation
import JzOsFrameWork
import UIKit
import JzOsSqlHelper
public class PublicBeans{
    //是否為Beta版本
    public static var isBeta:Bool{
        get{
            return JzActivity.getControlInstance.getPro("isBeta", true)
        }
        set(value){
            JzActivity.getControlInstance.setPro("isBeta", value)
        }
    }
    //mmy資料庫
    public static var 資料庫:SqlHelper! = SqlHelper("mmy.db")
    //地區資料
    public static var area:String{
        get{
            return JzActivity.getControlInstance.getPro("area", "TW%20Truck")
        }
        set(value){
            JzActivity.getControlInstance.setPro("area", value)
        }
    }
    //選擇的車種
   static var selectCar:Data_Setting_Car! = nil
    //取得ScanID
    public static func getQrCode(_ callback:@escaping(_ a:String)->Void){
        let scanner=Page_Scanner()
        scanner.tt="請掃描QRCODE"
        scanner.scanback={
            code in
            callback(code)
            return ()
        }
        JzActivity.getControlInstance.changePage(scanner, "Page_Scanner", true)
    }
    //產生Qrcode
    static func generateQRCode(from string: Data, imageView: UIImageView) -> UIImage? {
     let data = string

     if let filter = CIFilter(name: "CIQRCodeGenerator") {
     filter.setValue(data, forKey: "inputMessage")
     // L: 7%, M: 15%, Q: 25%, H: 30%
     filter.setValue("M", forKey: "inputCorrectionLevel")
     if let qrImage = filter.outputImage {
     let scaleX = imageView.frame.size.width / qrImage.extent.size.width
     let scaleY = imageView.frame.size.height / qrImage.extent.size.height
     let transform = CGAffineTransform(scaleX: scaleX, y: scaleY)
     let output = qrImage.transformed(by: transform)
     return UIImage(ciImage: output)
     }
     }
     return nil
     }
    //現在的版本
    static var localVersion:FileJsonVersion{
        get{
            let decoder = JSONDecoder()
            if(JzActivity.getControlInstance.getPro("localVersion", "").isEmpty){
                return FileJsonVersion()
            }else{
                do{
                    return  try decoder.decode(FileJsonVersion.self, from: JzActivity.getControlInstance.getPro("localVersion", "").data(using: String.Encoding.utf16)!)
                }catch{
                     print("取得失敗\(error)")
                    return FileJsonVersion()
                }
            }
        }
    }
    //最新的版本
    static var nowVersion:FileJsonVersion{
        get{
            let decoder = JSONDecoder()
            if(JzActivity.getControlInstance.getPro("nowVersion", "").isEmpty){
                return FileJsonVersion()
            }else{
                do{
                    return  try decoder.decode(FileJsonVersion.self, from: JzActivity.getControlInstance.getPro("nowVersion", "").data(using: String.Encoding.utf16)!)
                }catch{
                    print("取得失敗\(error)")
                    return FileJsonVersion()
                }
            }
        }
    }
    //Make選擇
    public static func getMake()->make{
        var sql=""
        let res=make()
            sql="select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA') and `Direct Fit` not in('SC2575','SC4379')  order by `Make` asc"
        
            PublicBeans.資料庫.query(sql, {
                result in
                res.make.append(result.getString(0))
                res.makeing.append(result.getString((1)))
            },{
                
            })
        print("make數量:\(res.make.count)")
        return res
    }
    //Model選擇
    public static func getModel()->model{
        let res=model()
        var sql=""
        
        sql = "select distinct `Model` from `Summary table` where `Make` = '\(PublicBeans.Make)' and `Direct Fit` not in('SC2575','SC4379') "
            PublicBeans.資料庫.query(sql, {
                result in
                res.model.append(result.getString(0))
            },{
                
            })
    
        return res
    }
    //Year選擇
    public static func getYear()->year{
        let res=year()
        var sql=""
            sql="select distinct `Year` from `Summary table` where `Make` = '\(PublicBeans.Make)' and `Model` = '\(PublicBeans.Model)' and `Direct Fit` not in('SC2575','SC4379') "
            
            PublicBeans.資料庫.query(sql, {
                result in
                res.year.append(result.getString(0))
            },{
                
            })
        
        
        return res
    }
    public static var Make=""
    public static var Model=""
    public static var Year=""
}
