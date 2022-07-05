//
//  Dia_Insert_Obd.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2021/2/26.
//  Copyright © 2021 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzOsFrameWork
class Dia_Insert_Obd: UIViewController {
    public static var callback={
    
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        view.fixLanguage()
        // Do any additional setup after loading the view.
    }
    @IBAction func close(_ sender: Any) {
        JzActivity.getControlInstance.closeDialLog("Dia_Insert_Obd")
        Dia_Insert_Obd.callback()
    }
    
}
