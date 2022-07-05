//
//  Page_Setting_Advice.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit
import JzOsFrameWork
class Page_Setting_Advice: UIViewController {
    
    @IBOutlet var cunit: UILabel!
    @IBOutlet var punit: UILabel!
    @IBOutlet var pressure: UITextField!
    @IBOutlet var heightc: UITextField!
    @IBOutlet var minpressure: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let userSetting=Md_User_Setting.getUserSetting()
        pressure.text="\(userSetting.maxPre.toSettingPre())"
        heightc.text = "\(userSetting.temAdvice.toSettingTem())"
        minpressure.text=userSetting.preAdviceRatio
        punit.text=["Kpa","Psi","Bar"][Md_User_Setting.getUserSetting().punit]
        cunit.text=["C","F"][Md_User_Setting.getUserSetting().tunit]
    }
    
    
    
    @IBAction func reset(_ sender: Any) {
        var userSetting=Md_User_Setting.getUserSetting()
        userSetting.maxPre = 800
        userSetting.minPre = Int(800*0.75)
        userSetting.temAdvice = 80
        userSetting.preAdviceRatio = "25"
        userSetting.changeInfo(result: {
            JzActivity.getControlInstance.toast("已恢復預設值")
            let userSetting2=Md_User_Setting.getUserSetting()
            self.pressure.text="\(userSetting2.maxPre.toSettingPre())"
            self.heightc.text="\(userSetting2.temAdvice.toSettingTem())"
            self.minpressure.text=userSetting2.preAdviceRatio
            return 1
        })
        
    }
    
    @IBAction func set(_ sender: Any) {
        var userSetting=Md_User_Setting.getUserSetting()
        userSetting.maxPre = ((pressure.text!.isEmpty) ? 800 : Int (pressure.text!)!.toKpa())
        userSetting.minPre = ((minpressure.text!.isEmpty) ? Int(800*0.75) : Int( Float(userSetting.maxPre) * (1-(Float("0.\(minpressure.text!)")!)) ))
        userSetting.temAdvice = ((heightc.text!.isEmpty) ? 80 :Int(heightc.text!)!).toTem()
        userSetting.preAdviceRatio = (minpressure.text!.isEmpty) ? "25" : minpressure.text!
        userSetting.changeInfo(result: {
           
            return 1
           
        })
    }
}
