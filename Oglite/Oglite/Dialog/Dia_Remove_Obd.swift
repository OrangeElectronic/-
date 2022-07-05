//
//  Dia_Remove_Obd.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2021/2/26.
//  Copyright © 2021 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzOsFrameWork
class Dia_Remove_Obd: UIViewController {
    public static var callback={
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        view.fixLanguage()
    }
    
    @IBAction func remove(_ sender: Any) {
        JzActivity.getControlInstance.closeDialLog("Dia_Remove_Obd")
        Dia_Remove_Obd.callback()
    }
}
