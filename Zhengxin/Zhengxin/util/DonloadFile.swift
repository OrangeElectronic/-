//
//  DonloadFile.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import Foundation
import JzOsFrameWork
import JzOsSqlHelper
import Foundation
import JzOsTaskHandler
import SwiftSoup
import JzOsHttpExtension
public class DonloadFile{
    public  static var bleUpdate = false
    public  var bleUpdateResult = false
    public  var webURL: String{
        get{
            if(PublicBeans.isBeta){
                return "https://bento2.orange-electronic.com/Orange%20Cloud/Beta"
            }else{
                return "https://bento2.orange-electronic.com/Orange%20Cloud"
            }
        }
    }
    public  var fileURL: String{
        get{
            if(PublicBeans.isBeta){
                return "/Orange%20Cloud/Beta"
            }else{
                return "/Orange%20Cloud"
            }
        }
    }
    //
    public  var arUrl: String{
        get{
            var Area=PublicBeans.area
            return Area
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
                "資料更新中...".progress()
            }
        }
    }
    
    //判斷是否需要初次加載
    public  func needInit()->Bool{
        let item=PublicBeans.localVersion
        return item.lanVersion == "no" || item.mmyVersion == "no" || item.s19List.isEmpty || item.obdList.isEmpty
    }
    //檢查新版本
    public  func checkNewVersion(){
        DispatchQueue.global().async {
            TaskHandler.newInstance().runTaskOnce("checkNewVersion", {
                let data=HttpCore.get("https://bento2.orange-electronic.com/getVersion?country=\(PublicBeans.area)&type=OG&beta=\(PublicBeans.isBeta)",10)
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
        let local=PublicBeans.localVersion
        let online=PublicBeans.nowVersion
        print("\(local.lanVersion) == \(online.lanVersion)")
        print("\(local.mmyVersion) == \(online.mmyVersion)")
        print("\(local.s19List) == \(online.s19List)")
        print("\(local.obdList) == \(online.obdList)")
        return  local.mmyVersion != online.mmyVersion
    }
    //下載更新
    public  func dataloading(_ finish:@escaping(_ a:Bool)->Void){
        DispatchQueue.global().async {
            TaskHandler.newInstance().runTaskOnce("dataloading", {
                self.progress=0
                let data=HttpCore.get("https://bento2.orange-electronic.com/getVersion?country=\(PublicBeans.area)&beta=\(PublicBeans.isBeta)&type=OG",10)
                print("rout:\("https://bento2.orange-electronic.com/getVersion?country=\(PublicBeans.area)&beta=\(PublicBeans.isBeta)")")
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
                self.progress+=1
                if(!self.donloadMmy()){
                    finish(false)
                    print("donloadMmyFalse")
                    return
                }
                var dd=PublicBeans.localVersion
                dd.mmyVersion=PublicBeans.nowVersion.mmyVersion
                dd.storeLocal()
                PublicBeans.資料庫.autoCreat()
                var aok=PublicBeans.localVersion
                aok=PublicBeans.nowVersion
                aok.storeLocal()
                finish(true)
            })
        }
    }

    
    public  func donloadMmy()->Bool{
        var local=PublicBeans.localVersion
        let online=PublicBeans.nowVersion
        if(local.mmyVersion == online.mmyVersion ){
            return true
        }
        print("\(webURL)/Database/MMY/\(arUrl)/\(online.mmyVersion)")
        if(PublicBeans.資料庫.initByUrl("\(webURL)/Database/MMY/\(arUrl)/\(online.mmyVersion)".replace(" ", "%20"))){
            local.mmyVersion=online.mmyVersion
            local.storeLocal()
            return true
        }else{
            return false
        }
    }
   
}
