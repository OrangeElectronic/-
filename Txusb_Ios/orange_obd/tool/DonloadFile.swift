//
//  DonloadFile.swift
//  txusb
//
//  Created by Jianzhi.wang on 2020/3/16.
//  Copyright © 2020 王建智. All rights reserved.
//

import Foundation
import JzIos_Framework
import JzOsSqlHelper
import Foundation
import JzOsTool
import JzOsTaskHandler
import JzOsHttpExtension
import Alamofire
//public class DonloadFile{
//
//    public static  var url="http://bento2.orange-electronic.com/Orange%20Cloud/Beta"
//    public static func dataloading(){
//        if(JzActivity.getControlInstance.getPro("Beta","false")=="true"){
//            print("為beta")
//            DonloadFile.url="http://bento2.orange-electronic.com/Orange%20Cloud/Beta"
//        }else{
//            print("佈為beta")
//            DonloadFile.url="http://bento2.orange-electronic.com/Orange%20Cloud"
//        }
//        let a=Progress()
//        a.label=a.資料載入
//        JzActivity.getControlInstance.openDiaLog(a,true,"Progress")
//        let sql=SqlHelper("mmy.db")
//        if(JzActivity.getControlInstance.getPro("dataloading", "false") != "false"){
//            sql.autoCreat()
//            PublicBeans.資料庫=sql
//            JzActivity.getControlInstance.closeDialLog()
//            return
//        }
//        DispatchQueue.global().async {
//            //            if(!downAllobd()){dataloading()}
//            if(!downAllS19()){DispatchQueue.main.async {dataloading()
//                }
//                return
//            }
//            print("下載s19成功")
//            let mmy=mmyname()
//            if(mmy==nil){
//                DispatchQueue.main.async {dataloading()
//                }
//                return
//            }else{
//                sql.initByUrl(mmy!,{
//                    result in
//                    if(result){
//                        print("資料庫開啟成功")
//                        PublicBeans.資料庫=sql
//                        JzActivity.getControlInstance.setPro("mmy", mmy!)
//                        JzActivity.getControlInstance.setPro("dataloading", "true")
//                        DispatchQueue.main.async {
//                            JzActivity.getControlInstance.setPro("update", "nodata")
//                            JzActivity.getControlInstance.closeDialLog()
//                        }
//                    }else{
//                        DispatchQueue.main.async {
//                            print("資料庫開啟失敗")
//                            dataloading()
//                            return
//                        }
//                    }
//                })
//            }
//        }
//    }
//    public static func mmyname()->String?{
//        let Area=JzActivity.getControlInstance.getPro("Area", "EU")
//        let url = URL(string: "\(DonloadFile.url)/Database/MMY/\(Area)/")
//        var data: Data? = nil
//        if let anUrl = url {
//            do{
//                try data = Data(contentsOf: anUrl)
//                let ds=String(decoding: data!, as: UTF8.self).components(separatedBy: "HREF=")
//                let filename=ds[2].components(separatedBy: ">")[1].components(separatedBy: "<")[0]
//                print(filename)
//                return "\(DonloadFile.url)/Database/MMY/\(Area)/\(filename)"
//
//            }catch{print(error)
//                return nil
//            }
//        }
//        return nil
//    }
//public static func progress(_ pro:Int){
//    DispatchQueue.main.async {
//        JzActivity.getControlInstance.closeDialLog()
//        let a=Progress()
//        a.label="\(a.資料載入)...\(pro)%"
//        JzActivity.getControlInstance.openDiaLog(a,true,"Progress")
//    }
//}
//    public static func downAllS19()->Bool{
//        PublicBeans.OBD資料庫.exSql("CREATE TABLE if not exists `s19` ( name VARCHAR, data TEXT);")
//        let a = JzOSTool.http().listHttpFile("http://35.240.51.141:8077", "/Database/SensorCode/SIII/")
//        if a != nil{
//            print("s19數量:\(a!.count)")
//            for i in 0..<a!.count{
//                print("下載中:\(a![i])")
//                if(!self.downs19(a![i])){
//                    return false}
//                progress(i*100/a!.count)
//            }
//        }else{
//            print("失敗")
//            return false
//        }
//        return true
//    }
//    public static func downs19(_ name:String)->Bool{
//        if(name.isEmpty){return true}
//        print("下載file:\(name)")
//        let a=JzOSTool.http().listHttpFile("http://35.240.51.141:8077", "/Database/SensorCode/SIII/\(name)/")
//        if let dir=a{
//            print("dir.count:\(dir.count)")
//            if(dir.count>0){
//                if(JzActivity.getControlInstance.getPro(name, "nodata")==dir[1]){return true}
//                let file=JzOSTool.http().getFileText("http://35.240.51.141:8077/Database/SensorCode/SIII/\(name)/\(dir[1])")
//                print("data:\(file)")
//                if file != nil{
//                    PublicBeans.OBD資料庫.exSql("delete from `s19` where name='\(name)'")
//                    PublicBeans.OBD資料庫.exSql("insert into `s19` (name,data) values ('\(name)','\(file!.replace("\r\n", ""))')")
//                    JzActivity.getControlInstance.setPro(name,dir[1])
//                    print("下載檔案","\(file!.replace("\r\n", ""))")
//                    return true
//                }else{
//                    print("失敗")
//                }
//            }
//        }
//        return false
//    }
//
//
//}
public class DonloadFile{
    public  static var bleUpdate = false
    public  var bleUpdateResult = false
    public  var webURL: String{
        get{
            if(JzActivity.getControlInstance.getPro("Beta","false")=="true"){
                return "https://bento2.orange-electronic.com/Orange%20Cloud/Beta"
            }else{
                return "https://bento2.orange-electronic.com/Orange%20Cloud"
            }
        }
    }
    public  var fileURL: String{
        get{
            if(JzActivity.getControlInstance.getPro("Beta","false")=="true"){
                return "/Orange%20Cloud/Beta"
            }else{
                return "/Orange%20Cloud"
            }
        }
    }
    //
    public  var arUrl: String{
        get{
            var Area=PublicBeans.地區
            return Area
        }
    }
    public static func progress(_ pro:Int){
        DispatchQueue.main.async {
            JzActivity.getControlInstance.closeDialLog()
            let a=Progress()
            a.label="\(a.資料載入)...\(pro)%"
            JzActivity.getControlInstance.openDiaLog(a,true,"Progress")
        }
    }
    //
    public  var progressValue=0
    public  var progress:Int{
        get{
            return progressValue
        }
        set{
            progressValue=newValue
            DispatchQueue.main.async {
                JzActivity.getControlInstance.closeDialLog()
                DonloadFile.progress(Int(Float(newValue)*(Float(100)/Float(125))))
            }
        }
    }
    
    //判斷是否需要初次加載
    public  func needInit()->Bool{
        let item=SharePre.localVersion
        return item.lanVersion == "no" || item.mmyVersion == "no" || item.s19List.isEmpty || item.obdList.isEmpty
    }
    //檢查新版本
    public  func checkNewVersion(){
        DispatchQueue.global().async {
            TaskHandler.newInstance().runTaskOnce("checkNewVersion", {
                let data=HttpCore.get("https://bento2.orange-electronic.com/getVersion?country=\(PublicBeans.地區)&type=OG_lite&beta=\(SharePre.isBeta)&isComic=true",10)
                if(data != nil){
                    do{
                        let decoder = JSONDecoder()
                        let file = try decoder.decode(FileJsonVersion.self, from: data!)
                        file.storeOnline()
                    }catch{
                        
                    }
                }
            })
        }
    }
    //清除資料加載
    public  func clearInit(){
        FileJsonVersion().storeLocal()
    }
    //判斷是否需要更新
    public  func needUpdate()->Bool{
        let local=SharePre.localVersion
        let online=SharePre.nowVersion
        print("\(local.lanVersion) == \(online.lanVersion)")
        print("\(local.mmyVersion) == \(online.mmyVersion)")
        print("\(local.s19List) == \(online.s19List)")
        print("\(local.obdList) == \(online.obdList)")
        print("\(SharePre.ogversion) == \(online.mcuVerion)")
        return  local.mmyVersion != online.mmyVersion || local.s19List != online.s19List 
    }
    //下載更新
    public  func dataloading(_ finish:@escaping(_ a:Bool)->Void){
        DispatchQueue.global().async {
            TaskHandler.newInstance().runTaskOnce("dataloading", {
                self.progress=0
                let data=HttpCore.get("https://bento2.orange-electronic.com/getVersion?country=\(PublicBeans.地區)&type=OG_lite&isComic=true&beta=\(SharePre.isBeta)",10)
                if(data != nil){
                    do{
                        let decoder = JSONDecoder()
                        let file = try decoder.decode(FileJsonVersion.self, from: data!)
                        file.storeOnline()
                        print("下載成功:data->\(file.obdList.count)")
                    }catch{
                        finish(false)
                        return
                    }
                }else{
                    finish(false)
                    return
                }
                if(!self.downAllS19()){
                    finish(false)
                    print("downAllS19False")
                    return
                }
                self.progress+=1
                if(!self.donloadMmy()){
                    finish(false)
                    print("donloadMmyFalse")
                    return
                }
                var dd=SharePre.localVersion
                dd.mmyVersion=SharePre.nowVersion.mmyVersion
                dd.storeLocal()
                PublicBeans.資料庫.autoCreat()
                var aok=SharePre.localVersion
                aok=SharePre.nowVersion
                aok.storeLocal()
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.closeDialLog()
                }
                finish(true)
            })
        }
    }
  
    
    public  func donloadMmy()->Bool{
        var local=SharePre.localVersion
        let online=SharePre.nowVersion
        if(local.mmyVersion == online.mmyVersion ){
            return true
        }
        if(PublicBeans.資料庫.initByUrl("\(webURL)/Database/MMY/Comic%20version/\(arUrl)/\(online.mmyVersion)")){
            local.mmyVersion=online.mmyVersion
            local.storeLocal()
            return true
        }else{
            return false
        }
    }
  
    public  func downAllS19()->Bool{
        PublicBeans.OBD資料庫.exSql("CREATE TABLE if not exists `s19` ( name VARCHAR, data TEXT);")
        for i in SharePre.nowVersion.s19List{
            let res=downs19(i.key)
            progress+=1
            if(!res){
                return false
            }
        }
        return true
    }
    
    public func downs19(_ name:String)->Bool{
        let nowVersion=SharePre.nowVersion
        var local=SharePre.localVersion
        if(local.s19List[name] == nowVersion.s19List[name]){return true}
        print("rout:\(webURL)/Database/SensorCode/SIII/\(name)/\(nowVersion.s19List[name] ?? "")")
        let result=HttpCore.get("\(webURL)/Database/SensorCode/SIII/\(name)/\(nowVersion.s19List[name] ?? "")",10)
        if(result != nil){
            local.s19List[name]=nowVersion.s19List[name]
            local.storeLocal()
            PublicBeans.OBD資料庫.exSql("delete from `s19` where name='\(name)'")
            PublicBeans.OBD資料庫.exSql("insert into `s19` (name,data) values ('\(name)','\(String(data: result!, encoding: String.Encoding.utf8)!.replace("\r\n", ""))')")
            return true
        }else{
            return false
        }
    }
    

    
    
    
    

    
}
public class SharePre {
    
    //ble連線紀錄
    static var blememory: String {
        get {
            return JzActivity.getControlInstance.getPro("blememory", "nodata")
        }
        set {
            JzActivity.getControlInstance.setPro("blememory", newValue)
        }
    }
    //SN
    static var sn:String{
        get {
            return JzActivity.getControlInstance.getPro("sn", "nodata")
        }
        set {
            JzActivity.getControlInstance.setPro("sn", newValue)
        }
    }
    //Ble版本號
    static var bleVersion:String{
        get {
            return JzActivity.getControlInstance.getPro("bleVersion", "nodata")
        }
        set {
            JzActivity.getControlInstance.setPro("bleVersion", newValue)
        }
    }
    //版本號
    static var ogversion:String{
        get {
            let data=JzActivity.getControlInstance.getPro("ogversion", "nodata").HexToByte()
            if(data==nil){
                return "nodata"
            }else{
                return String(decoding: data!,as: UTF8.self).replace(".srec", "")
            }
        }
        set {
            JzActivity.getControlInstance.setPro("ogversion", newValue)
        }
    }
    //資料庫版本
    static var mmyVersion:String{
        get {
            return JzActivity.getControlInstance.getPro("mmyVersion", "nodata")
        }
        set {
            JzActivity.getControlInstance.setPro("mmyVersion", newValue)
        }
    }
    //語言版本
    static var languageVersion:String{
        get {
            return JzActivity.getControlInstance.getPro("languageVersion", "nodata")
        }
        set {
            JzActivity.getControlInstance.setPro("languageVersion", newValue)
        }
    }
    //最新版本mmy
    static var mmyinit:String{
        get {
            return JzActivity.getControlInstance.getPro("mmyinit", "no").replace(".db", "")
        }
        set {
            JzActivity.getControlInstance.setPro("mmyinit", newValue)
        }
    }
    //最新版本mcu
    static var mcuinit:String{
        get {
            return JzActivity.getControlInstance.getPro("mcuinit", "nodata").replace(".srec", "")
        }
        set {
            JzActivity.getControlInstance.setPro("mcuinit", newValue)
        }
    }
    //最新版本Lan
    static var laninit:String{
        get {
            return JzActivity.getControlInstance.getPro("laninit", "nodata")
        }
        set {
            JzActivity.getControlInstance.setPro("laninit", newValue)
        }
    }
    //s19是否加載過
    static var s19init:Bool{
        get {
            return JzActivity.getControlInstance.getPro("s19init", false)
        }
        set {
            JzActivity.getControlInstance.setPro("s19init", newValue)
        }
    }
    //s19是否加載過
    static var obdinit:Bool{
        get {
            return JzActivity.getControlInstance.getPro("obdinit", false)
        }
        set {
            JzActivity.getControlInstance.setPro("obdinit", newValue)
        }
    }
    //是否為Beta
    static var isBeta:Bool{
        get {
            return JzActivity.getControlInstance.getPro("isBeta", false)
        }
        set {
            JzActivity.getControlInstance.setPro("isBeta", newValue)
        }
    }
    //溫度單位
    static var tem:Int{
        get {
            return JzActivity.getControlInstance.getPro("Tem", 0)
        }
        set {
            JzActivity.getControlInstance.setPro("Tem", newValue)
        }
    }
    //胎壓單位
    static var pre:Int{
        get {
            return JzActivity.getControlInstance.getPro("Pre", 2)
        }
        set {
            JzActivity.getControlInstance.setPro("Pre", newValue)
        }
    }
    //登入的帳號
    static var admin:String{
        get{
            return JzActivity.getControlInstance.getPro("admin", "nodata")
        }
        set{
            JzActivity.getControlInstance.setPro("admin", newValue)
        }
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
                     NSLog("取得失敗\(error)")
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
                    NSLog("取得失敗\(error)")
                    return FileJsonVersion()
                }
            }
        }
    }
 
}
struct  FileJsonVersion:Codable {
    var mmyVersion = "no"
    var lanVersion = "no"
    var mcuVerion  = "no"
    var bleVersion = "no"
    var apkVersion = "no"
    var s19List = Dictionary<String,String>()
    var s18List = Dictionary<String,String>()
    var obdList = Dictionary<String,String>()
    
}
extension FileJsonVersion{
    func storeLocal(){
        do{
            let encodedData = try JSONEncoder().encode(self)
            let jsonString = String(data:encodedData,encoding: .utf8)
            JzActivity.getControlInstance.setPro("localVersion", jsonString!)
        }catch{
              NSLog("儲存失敗")
        }
    }
    
    func storeOnline(){
        do{
            let encodedData = try JSONEncoder().encode(self)
            let jsonString = String(data:encodedData,encoding: .utf8)
            JzActivity.getControlInstance.setPro("nowVersion", jsonString!)
        }catch{
            NSLog("儲存失敗")
        }
    }
}

