//
//  Md_FileJsonVersion.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import Foundation
import JzOsFrameWork
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
              print("儲存失敗")
        }
    }
    
    func storeOnline(){
        do{
            let encodedData = try JSONEncoder().encode(self)
            let jsonString = String(data:encodedData,encoding: .utf8)
            JzActivity.getControlInstance.setPro("nowVersion", jsonString!)
        }catch{
            print("儲存失敗")
        }
    }
}
