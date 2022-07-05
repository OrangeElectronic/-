//
//  FtpManager.swift
//  txusb
//
//  Created by 王建智 on 2019/7/15.
//  Copyright © 2019 王建智. All rights reserved.
//
//
import UIKit

import SQLite3
class FtpManage{
    static var Downs19="nodata"
    func DowloadMmy(_ deledate:AppDelegate)->Bool{
           let mmyan=mmyname()
//           if(mmyan.count>19){SersorRecord.DB_Version=mmyan.sub(16..<mmyan.count)}else{SersorRecord.DB_Version=mmyan}
           if(mmyan==ViewController.getShare("mmy")){
                let dst=NSHomeDirectory()+"/Documents/mmytb.db"
               if sqlite3_open(dst, &deledate.db) == SQLITE_OK{
                   print("資料庫開啟成功")
                   ViewController.writeshare(mmyan,"mmy")
                   return true
               }else{
                   print("資料庫開啟失敗")
                   deledate.db=nil
                   
               }
               }
           print("donload")
           let url = URL(string: "http://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/\(mmyan)")
           var data: Data? = nil
           if let anUrl = url {
               do{
                   try data = Data(contentsOf: anUrl)
                   let dst=NSHomeDirectory()+"/Documents/mmytb.db"
                   let urlfrompath = URL(fileURLWithPath: dst)
                   try data?.write(to: urlfrompath)
                   if sqlite3_open(dst, &deledate.db) == SQLITE_OK{
                       print("資料庫開啟成功")
                       ViewController.writeshare(mmyan,"mmy")
                     return true
                   }else{
                       print("資料庫開啟失敗")
                       deledate.db=nil
                         return false
                   }
               }catch{print(error)
                   return false
               }
               
           }
           return false
       }
       func mmyname()->String {
           let url = URL(string: "http://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/EU/")
           var data: Data? = nil
           if let anUrl = url {
               do{
                   try data = Data(contentsOf: anUrl)
                   let ds=String(decoding: data!, as: UTF8.self).components(separatedBy: "HREF=")
                   let filename=ds[2].components(separatedBy: ">")[1].components(separatedBy: "<")[0]
                   print(filename)
                   return filename
               }catch{print(error)
                   return "false"
               }
           }
           return "false"
       }
    func GetS19name(_ name:String) -> String {
           let url = URL(string: "http://bento2.orange-electronic.com/Orange%20Cloud/Drive/OBD%20DONGLE/\(name)/")
            var data: Data? = nil
                 if let anUrl = url {
                     do{
                         try data = Data(contentsOf: anUrl)
                       let ds=String(decoding: data!, as: UTF8.self).components(separatedBy: "HREF=")
                         let filename=ds[2].components(separatedBy: ">")[1].components(separatedBy: "<")[0]
                         print(filename)
                         return filename
                     }catch{print(error)
                         return "false"
                     }
                 }
                 return "false"
       }
    func DownS19(_ name:String)->Bool{
        var s19name=GetS19name(name)
        let url = URL(string: "http://bento2.orange-electronic.com/Orange%20Cloud/Drive/OBD%20DONGLE/\(name)/\(s19name)")
        var data: Data? = nil
        if let anUrl = url {
            do{
                try data = Data(contentsOf: anUrl)
                let ds=String(decoding: data!, as: UTF8.self).replace("\r", "").replace("\n", "")
//                print(ds)
                Command.Filedata=ds
                FtpManage.Downs19=s19name.replace(".srec", "")
                return true
            }catch{print(error)
                return false
            }
        }
        print("空")
        return false
    }
    func bytesToHex(_ bt:[UInt8])->String{
        var re=""
        for i in 0..<bt.count{
            re=re.appending(String(format:"%02X",bt[i]))
        }
        return re
    }
}
