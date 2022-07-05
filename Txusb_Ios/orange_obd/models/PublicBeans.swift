//
//  PublicBeans.swift
//  txusb
//
//  Created by Jianzhi.wang on 2020/3/16.
//  Copyright © 2020 王建智. All rights reserved.
//

import Foundation
import JzIos_Framework
import JzOsSqlHelper
public class PublicBeans{
    
    public static var 地區:String
    {
        get{
            return JzActivity.getControlInstance.getPro("Area","EU")
        }
    }
    public static var 語言=""
    public static var Make=""
    public static var Model=""
    public static var Year=""
    public static var 資料庫版本=""
    public static var 燒錄數量=4
    public static var 選擇按鈕=""
    public static var 資料庫:SqlHelper! = SqlHelper("mmy.db")
    public static var OBD資料庫:SqlHelper! = SqlHelper("obd.db")
    public static var Scan=0
    public static var KetIn=1
    public static var Trigger=2
    public static var selectway=0
    public static func refrsh(){
        PublicBeans.語言=JzActivity.getControlInstance.getPro("lan","English")
        PublicBeans.資料庫版本=JzActivity.getControlInstance.getPro("mmy","nodata")
    }
    public static func gets19()->String{
        var s19=""
        PublicBeans.資料庫.query("select `Direct Fit` from `Summary table` where Make='\(Make)' and Model='\(Model)' and year='\(Year)' and `Direct Fit` not in('NA') limit 0,1", {
            result in
            s19=(result.getString(0))
        }, {})
        return s19
    }
    
    public static func getidcount()->Int{
        var idcount=0
        PublicBeans.資料庫.query("select `ID_Count` from `Summary table` where Make='\(Make)' and Model='\(Model)' and year='\(Year)' and `Direct Fit` not in('NA') limit 0,1", {
            result in
            idcount=(Int(result.getString(0))!)
        }, {})
        return idcount
    }
    public static func getWheelCount()->Int{
        var idcount=0
        PublicBeans.資料庫.query("select `Wheel_Count` from `Summary table` where `OBD1`='\(getObd1())' and `Wheel_Count` not in('NA') limit 0,1", {
            result in
            idcount=(Int(result.getString(0))!)
        }, {})
        return idcount
    }
    public static func getObd1()->String{
        var obd=""
        PublicBeans.資料庫.query("select `OBD1` from `Summary table` where Make='\(Make)' and Model='\(Model)' and year='\(Year)' and `OBD1` not in('NA')   limit 0,1", {
            result in
            obd=String(result.getString(0))
        }, {})
        return obd
    }
    public static func getOBDFile()->String{
        var obd=""
        PublicBeans.OBD資料庫.query("select data from obd where name='\(getObd1())'", {
            result in
            obd=result.getString(0)
        }, {})
        return obd
    }
    public static func getS19File()->String{
        var obd=""
        PublicBeans.OBD資料庫.query("select data from `s19` where name='\(gets19())'", {
            result in
            obd=result.getString(0)
        }, {})
        return obd
    }
    
    public static func getObdVersion()->String{
        return JzActivity.getControlInstance.getPro(getObd1(),"nodata").replace(".srec", "")
    }
    
    public static func getMake()->[model]{
        var sql=""
        sql="select distinct `Make`,`Make_img` from `Summary table` where `Direct Fit` not in('NA') and `Make` IS NOT NULL and `Make_img` not in('NA')  and SIII != 'X' order by `Make` asc"
        var res=[model]()
        PublicBeans.資料庫.query(sql, {
            result in
            let mod=model()
            mod.make=(result.getString(0))
            mod.image=(result.getString((1)))
            res.append(mod)
        },{
            
        })
        return res
    }
    public static func getModel()->[model]{
        var sql=""
        sql="select distinct model from `Summary table` where make='\(PublicBeans.Make)' and `Direct Fit` not in('NA') and SIII != 'X' order by model asc"
        var res=[model]()
        PublicBeans.資料庫.query(sql, {
            result in
            let mod=model()
            mod.modele=(result.getString(0))
            res.append(mod)
        },{
            
        })
        return res
    }
    public static func getYear()->[model]{
        var res=[model]()
        var sql=""
        sql="select distinct Year from `Summary table` where model='\(PublicBeans.Model)' and make='\(PublicBeans.Make)' and `Direct Fit` not in('INDIRECT')  and SIII != 'X' order by Year asc"
        PublicBeans.資料庫.query(sql, {
            result in
            var mod=model()
            mod.year=(result.getString(0))
            res.append(mod)
        },{
            
        })
        return res
    }
    
    public static func getLf()->String{
        var res=""
        var sql=""
        sql="select `Lf` from `Summary table` where `Direct Fit`='\(gets19())'"
        PublicBeans.資料庫.query(sql, {
            result in
            res=result.getString(0)
        },{
            
        })
        return res
    }
    public static func getSensorMode() -> String{
        var res=""
        var sql=""
        sql="select `Sensor` from `Summary table` where `Direct Fit`='\(gets19())'"
        PublicBeans.資料庫.query(sql, {
            result in
            res=result.getString(0)
        },{
            
        })
        return res
    }
    public static func queryOE() -> String{
        var res=""
        var sql=""
        sql="select `OE Part Num` from `Summary table` where `Direct Fit`='\(gets19())' limit 0,1"
        PublicBeans.資料庫.query(sql, {
            result in
            res=result.getString(0)
        },{
            
        })
        return res
    }
    public static func getRelearm()->String{
        var result=""
        let a=JzActivity.getControlInstance.getPro("lan", "English")
        print(a)
        
        var colume="English"
        switch(a){
        case "English":
            colume="English"
            break
        case "繁體中文":
            colume="Traditional Chinese"
            break
        case "简体中文":
            colume="Jane"
            break
        case "Deutsch":
            colume="German"
            break
        case "Italiano":
            colume="Italian"
            break
        default:
            break;
        }
        let sql="select `Relearn code` from `Summary table` where `Make`='\(PublicBeans.Make)' and `Model`='\(PublicBeans.Model)' and `Year`='\(PublicBeans.Year)' limit 0,1"
        PublicBeans.資料庫.query(sql,{
            a in
            PublicBeans.資料庫.query("select `\(colume)` from `RelearnProcedureText` where `RelearnNumber`='\(a.getString(0))' limit 0,1",{
                b in
                result=b.getString(0).replace("\\n", "")
            },{})
        } , {})
        return result
    }
    
    
}


