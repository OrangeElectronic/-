//
//  myExtension.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/10.
//

import Foundation
import UIKit

public extension UIView{
    public func listallView()->[UIView]{
        var views=[UIView]()
        for view in self.subviews {
            views.append(view)
            if(view.subviews.count>0){
                views.append(contentsOf: view.listallView())
            }
        }
        return views
    }
}
public extension String{
    func getDateStamp()->TimeInterval{
        let dfmatter = DateFormatter()
        dfmatter.dateFormat="yyyy-MM-dd HH:mm:ss"
        let date = dfmatter.date(from: self)
        let dateStamp:TimeInterval = date!.timeIntervalSince1970
        return dateStamp
    }
    func caluateWithNowTime()->Double{
        let dfmatter = DateFormatter()
        dfmatter.dateFormat="yyyy-MM-dd HH:mm:ss"
        let date = dfmatter.date(from: self)
        let dateStamp:TimeInterval = date!.timeIntervalSince1970
        return Date().timeIntervalSince1970-dateStamp
    }
}
public extension Data{
    func jsonToObject<T>()->T?{
        do {
            //create json object from data
            if let json = try JSONSerialization.jsonObject(with: self, options: .mutableContainers) as? T {
                // print(json)
                return json
            }
        } catch let error {
            print("JSON to Dictionary error: \(error.localizedDescription)")
        }
        return nil
    }
}
public extension String{
    func jsonToObject<T:Codable>()->T?{
        let decoder: JSONDecoder = JSONDecoder()
        return try? decoder.decode(T.self, from: self.data(using: .utf8)!)
    }
}
public extension Collection{
    func toJsonData()->Data{
        try! JSONSerialization.data(withJSONObject: self,options: .prettyPrinted)
    }
    func toJsonString()->String{
        String(data: try! JSONSerialization.data(withJSONObject: self,options: .prettyPrinted), encoding: .utf8)!
    }
}
public extension Encodable{
    func toJsonData()->Data{
        let encoder: JSONEncoder = JSONEncoder()
        return try! encoder.encode(self)
    }
    func toJsonString()->String{
        let encoder: JSONEncoder = JSONEncoder()
        return try! String(data: encoder.encode(self), encoding: .utf8)!
    }
    func toJsonString2()->String{
        let encoder: JSONEncoder = JSONEncoder()
        return try! String(data: encoder.encode(self), encoding: .utf8)!
    }
}
extension String {
    func unicodeStr()->String {
        let tempStr1 = self.replacingOccurrences(of: "\\u", with: "\\U")
        let tempStr2 = tempStr1.replacingOccurrences(of: "\"", with: "\\\"")
        let tempStr3 = "\"".appending(tempStr2).appending("\"")
        let tempData = tempStr3.data(using: String.Encoding.utf8)
        var returnStr:String = ""
        do {
            returnStr = try PropertyListSerialization.propertyList(from: tempData!, options: [.mutableContainers], format: nil) as! String
        } catch {
            print(error)
        }
        return returnStr.replacingOccurrences(of: "\\r\\n", with: "\n")
    }
    func stringToUnicode()->String{
        //String to Unicode
        let dataenc = self.data(using: String.Encoding.nonLossyASCII)
        let encodevalue = String(data: dataenc!, encoding: String.Encoding.utf8)
        return encodevalue!
    }
}
