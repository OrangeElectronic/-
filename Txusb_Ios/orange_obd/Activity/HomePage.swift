//
//  HomePage.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import JzIos_Framework
class HomePage: UIViewController {
    
    let activity=(UIApplication.shared.delegate as! AppDelegate).act!
    @IBOutlet var t6: UILabel!
    @IBOutlet var t5: UILabel!
    @IBOutlet var t4: UILabel!
    @IBOutlet var t3: UILabel!
    @IBOutlet var t2: UILabel!
    @IBOutlet var t1: UILabel!
    @IBOutlet var settingbt: UIButton!
    @IBOutlet var ShoppingBt: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        activity.checkUpdate()
        activity.back.isHidden=true
        t3.text=SetLan.Setlan("Cloud_information")
        t4.text=SetLan.Setlan("Online_shopping")
        t5.text=SetLan.Setlan("Users_manual")
        t6.text=SetLan.Setlan("Setting")
        if(JzActivity.getControlInstance.getPro("lan", "nodata")=="Italiano"){
            ShoppingBt.setImage(UIImage.init(named: "btn_setting_normal"), for: .normal)
            t4.text=SetLan.Setlan("Setting")
            t6.isHidden=true
            settingbt.isHidden=true
        }
        t2.text="ID COPY"
      
    }
    @IBAction func Shopping(_ sender: Any) {
        var ur=""
        
        var a=JzActivity.getControlInstance.getPro("lan", "nodata")
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
    @IBAction func tomake(_ sender: Any) {
        PadSelect.Function=1
        let a=peacedefine().PadSelect
        a.act=activity
        activity.ChangePage(to: a)
        //        let a=peacedefine().PadSelect
        //        a.act=activity
        //        activity?.ChangePage(to: a)
        
    }
    
    @IBAction func tocopy(_ sender: Any) {
        PadSelect.Function=0
        let a=peacedefine().PadSelect
        a.act=activity
        activity.ChangePage(to: a)
    }
    
    
    @IBAction func ToManual(_ sender: Any) {
        let a=peacedefine().Users_Manual
        activity.ChangePage(to: a)
    }
    override func viewWillAppear(_ animated: Bool) {
        activity.tit.text="Orange TPMS"
        //        activity.tlkingBt.isHidden=false
        if(JzActivity.getControlInstance.getPro("update", "nodata") != "nodata"){
                  JzActivity.getControlInstance.openDiaLog(Dia_Update(), false, "Dia_Update")
             }
    }
    @IBAction func ToSetting(_ sender: Any) {
        let a=peacedefine().Setting
        activity.ChangePage(to: a)
    }
}
