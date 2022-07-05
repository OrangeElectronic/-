//
//  Dia_Select_Scan.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/9.
//

import UIKit
import JzOsFrameWork
class Dia_Select_Scan: UIViewController {
    
    var tireCallBack={}
    var sensorCallBack={}
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    @IBAction func tireAction(_ sender: Any) {
        tireCallBack()
        JzActivity.getControlInstance.closeDialLog("Dia_Select_Scan")
    }
    
    @IBAction func sensorAction(_ sender: Any) {
        sensorCallBack()
        JzActivity.getControlInstance.closeDialLog("Dia_Select_Scan")
    }
    @IBAction func clic(_ sender: Any) {
        print("click")
    }
    
}
