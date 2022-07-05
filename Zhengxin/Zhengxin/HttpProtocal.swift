//
//  HttpProtocal.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/11.
//

import Foundation
import JzOsHttpExtension
class HttpProtocal {
    //請求結果
    enum result {
        case success
        case interNetError
    }
    //伺服器路徑
   
    //public static var rout = "http://192.168.3.219/Zhexing"
    
  public static var rout = "https://bento2.orange-electronic.com/Zhexing"
    
    public static func startRequest(map:Dictionary<String,Any>, callback:@escaping(_ result:result,_ data:Dictionary<String,String>?)->Void){
        DispatchQueue.global().async {
            let data:Dictionary<String,String>? = HttpCore.post(rout, 10.0,map.toJsonData())?.jsonToObject()
            print("請求內容:\(map)")
            DispatchQueue.main.async {
                if(data == nil){
                    callback(.interNetError,nil)
                }else{
                    print("請求成功:\(data!)")
                    callback(.success,data)
                }
            }
        }
    }
}
