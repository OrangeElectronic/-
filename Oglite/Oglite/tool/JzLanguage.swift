//
//  JzLanguage.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/9/23.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//
import Foundation
import UIKit
import JzOsSqlHelper
public class JzLanguage{
    init() {
        languageDB=SqlHelper("language")
        languageDB!.initByBundleMain("localLan", ".db")
     
    }
    public static var instance:JzLanguage?=nil
    public static func getInstance()->JzLanguage{
        if(instance==nil){
            instance=JzLanguage()
        }
        return instance!
    }
    func listallView(_ inview:[UIView])->[UIView]{
        var views=[UIView]()
        for view in inview {
            views.append(view)
            if(view.subviews.count>0){
                views.append(contentsOf: listallView(view.subviews))
            }
        }
        return views
    }
    public  func getPro(_ name: String, _ normal: Bool) -> Bool {
        let preferences = UserDefaults.standard
        let currentLevelKey = name
        if preferences.object(forKey: currentLevelKey) == nil {
            return normal
        } else {
            let currentLevel = preferences.bool(forKey: currentLevelKey)
            return currentLevel
        }
    }
    public  func setPro(_ name: String, _ key: Bool) {
        let preferences = UserDefaults.standard
        preferences.set(key,forKey: name)
        let didSave = preferences.synchronize()
        if !didSave {
            NSLog("saverror")
        }
    }
    public var languageDB:SqlHelper?=nil
    public var testLan=false
    public var lan="en"
}
public extension String{
    func getFix()->String{
        let db=JzLanguage.getInstance().languageDB
        if(JzLanguage.getInstance().testLan){
            return self
        }
        var b=self
        if(!b.contains("jz.")){return b}
        b = b.replace("jz.", "")
        var text=""
        db?.query("select `\(JzLanguage.getInstance().lan)` from language where id=\(b)", {
            a in
            text=a.getString(0)
        }, {})
        return (text.isEmpty) ? self:text
    }
    func replace(_ target: String, _ withString: String) -> String
      {
          return self.replacingOccurrences(of: target, with: withString, options: NSString.CompareOptions.literal, range: nil)
      }
}
public extension UIView{
    func fixLanguage(){
        for i in JzLanguage.getInstance().listallView(self.subviews){
            if(i is UIButton){
                let btn=i as! UIButton
                btn.setTitle(btn.titleLabel?.text?.getFix(), for: .normal)
            }else if(i is UITextField){
                let field=i as! UITextField
                field.placeholder=field.placeholder?.getFix()
            }else if(i is UILabel){
                let label=i as! UILabel
                label.text=label.text?.getFix()
            }
        }
    }
}
