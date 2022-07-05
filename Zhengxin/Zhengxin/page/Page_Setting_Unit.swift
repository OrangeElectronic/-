//
//  Page_Setting_Unit.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit
import JzOsFrameWork
class Page_Setting_Unit: UIViewController {
    @IBOutlet var pressureUnit: UILabel!
    @IBOutlet var temUnit: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func topressure(_ sender: Any) {
        JzActivity.getControlInstance.changePage(Page_Setting_Change_Pressure_Unit(), "Page_Setting_Change_Pressure_Unit", true)
    }
    
    @IBAction func totem(_ sender: Any) {
        JzActivity.getControlInstance.changePage(Page_Setting_Unit_Tem(), "Page_Setting_Unit_Tem", true)
    }
    override func viewWillAppear(_ animated: Bool) {
        let userSetting=Md_User_Setting.getUserSetting()
        pressureUnit.text = ["kpa","psi","bar"][userSetting.punit]
        temUnit.text = ["C >","F >"][userSetting.tunit]
    }
}
