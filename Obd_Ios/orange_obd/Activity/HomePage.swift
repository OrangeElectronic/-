//
//  HomePage.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class HomePage: UIViewController {
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    @IBOutlet var readsensor: UILabel!
    @IBOutlet var shopping: UILabel!
    @IBOutlet var Manual: UILabel!
    @IBOutlet var Setting: UILabel!
    @IBOutlet var Favorite: UILabel!
    @IBOutlet var Obd: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        act.Pagememory.removeAll()
        act.Pagememory.append(self)
        act.back.isHidden=true
        act.ISRUN=false
        Obd.text=SetLan.Setlan("OBDII relearn")
        Setting.text=SetLan.Setlan("Setting")
        Favorite.text="USB PAD (ID Copy by OBD)"
        Manual.text=SetLan.Setlan("Users_manual")
        shopping.text=SetLan.Setlan("Online_shopping")
        readsensor.text=SetLan.Setlan("app_sensor_info_read")
    }
    override func viewWillAppear(_ animated: Bool) {
     act.rightop.isHidden=false
    }
    
    @IBAction func towrite(_ sender: Any) {
        Selection.function=Selection.寫入
        if(!act.haveble){
                   act.view.showToast(text: SetLan.Setlan("openble"))
                   return
               }
               if(act.IsConnect){
                   let a=peacedefine().Selection
                   self.act.ChangePage(to: a)
               }else{
                   let a=peacedefine().Selection
                   self.act.GoScanner(a)
               }
    }
    @IBAction func toread(_ sender: Any) {
        Selection.function=Selection.讀取
        if(!act.haveble){
            act.view.showToast(text: SetLan.Setlan("openble"))
            return
        }

        if(act.IsConnect){
            let a=peacedefine().Selection
            self.act.ChangePage(to: a)
        }else{
            let a=peacedefine().Selection
            self.act.GoScanner(a)
        }
    }
    
    @IBAction func ToSetting(_ sender: Any) {
        let a=peacedefine().Setting
        act.ChangePage(to: a)
    }
    @IBAction func Tofavorite(_ sender: Any) {
       let a=peacedefine().MyFavorite
        act.ChangePage(to: a)
    }
    
    @IBAction func ToManual(_ sender: Any) {
        let a=peacedefine().UserManual
        act.ChangePage(to: a)
    }
    
    @IBAction func ToShopping(_ sender: Any) {
        var ur=""
             var a=ViewController.getShare("lan")
             if(a=="nodata"){a="English"}
             switch a {
             case "English":
                 ur="http://simple-sensor.com"
                 break
             case "繁體中文":
                    ur="http://simple-sensor.com"
                break
             case "简体中文":
                    ur="http://simple-sensor.com"
                break
             case "Deutsch":
                    ur="http://orange-rdks.de"
                break
             case "Italiano":
                    ur="http://orange-like.it"
                    ToSetting(self)
                    return
             default:
                break
             }
             if let url = URL(string: ur)
             {
                 if #available(iOS 10.0, *)
                 {
                     UIApplication.shared.open(url, options: [:])
                 }
                 else
                 {
                     UIApplication.shared.openURL(url)
                 }
             }
    }
}
