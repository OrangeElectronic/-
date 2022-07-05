//
//  Sqlhelper.swift
//  Peripheral
//
//  Created by 王建智 on 2019/7/29.
//  Copyright © 2019 KoKang Chu. All rights reserved.
//

import Foundation
import Cocoa
import SQLite3
class Sqlhelper {
     let deledate = NSApplication.shared.delegate as! AppDelegate
    func QueryMake(_ add:NSPopUpButton){
        add.removeAllItems()
                if deledate.db != nil {
            let sql="select distinct `Make`,`Make_img` from `Summary table` where `Direct Fit` not in('NA') and `Make` IS NOT NULL and `Make_img` not in('NA')  order by `Make` asc"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                      add.addItem(withTitle: iids)
                }
          
            }
          
        }
    }
    func QueryModel(_ add:NSPopUpButton,_ make:String){
        add.removeAllItems()
        if deledate.db != nil {
            let sql="select distinct model from `Summary table` where make='\(make)' and `Direct Fit` not in('NA') order by model asc"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    add.addItem(withTitle: iids)
                }
                
            }
            
        }
    }
    func QueryRelarm(_ rela:String,_ make:String,_ model:String,_ year:String)->String{
        if deledate.db != nil {
            let sql="select `\(rela)` from `Summary table` where make='\(make)' and model='\(model)' and year='\(year)' limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    if(iids.replace(" ", "").count==0){
                        switch (Language.getShare("language"))
                        {
                        case "繁體中文":
                           return "學碼流程尚未完成，請耐心等待，我們會儘快更新"
                        case "简体中文":
                            return "学码流程尚未完成，请耐心等待，我们会尽快更新"
                        case "Deutsche":
                            return  "Im Bau"
                        case "English":
                            return "Under construction"
                            
                        case "Italiano":
                            return "Apprendimento in fase di Controllo."
                        default:
                            return "Under construction"
                        }
                    }else{ return iids}
                  
                }
                
            }
        }
        return "學碼流程尚未完成，請耐心等待，我們會儘快更新"
    }
    func QueryLf(_ mmynum:String)->String{
        if deledate.db != nil {
            let sql="select `Lf` from `Summary table` where `Direct Fit`='\(mmynum)' limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    return iids
                } }  }
        return "nodata"
    }
    func QueryS19(_ make:String,_ model:String,_ year:String)->String{
        if deledate.db != nil {
            let sql="select `Direct Fit` from `Summary table` where Make='\(make)' and Model='\(model)' and year='\(year)' and `Direct Fit` not in('NA') limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    return iids
                } }
            
        }
        return "nodata"
    }
    func QueryYear(_ add:NSPopUpButton,_ make:String,_ model:String){
        add.removeAllItems()
        if deledate.db != nil {
            let sql="select distinct Year from `Summary table` where model='\(model)' and make='\(make)' and `Direct Fit` not in('INDIRECT') order by Year asc"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    add.addItem(withTitle: iids)
                }
                
            }
            
        }
    }
    func QueryId(_ mmynum:String)->Int{
        if deledate.db != nil {
            let sql="select `ID_Count` from `Summary table` where `Direct Fit`='\(mmynum)' and `make` not in('NA') limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    return Int(iids)!
                } }  }
        return 0
    }
    func SensorModel(_ s19:String)->String{
              if deledate.db != nil {
                  let sql="select `Sensor` from `Summary table` where `Direct Fit`='\(s19)'"
                  var statement:OpaquePointer? = nil
                  if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                      let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                      print(errmsg)
                  }
                  while sqlite3_step(statement)==SQLITE_ROW{
                      let iid = sqlite3_column_text(statement,0)
                      if iid != nil{
                          let iids = String(cString: iid!)
                          print("sensor:\(iids)")
                          return iids
                      } }  }
              return ""
          }
}
