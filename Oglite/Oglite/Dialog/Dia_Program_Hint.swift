//
//  Dia_Program_Hint.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2021/1/27.
//  Copyright © 2021 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzOsFrameWork
class Dia_Program_Hint: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        view.fixLanguage()
    }

    @IBAction func close(_ sender: Any) {
        JzActivity.getControlInstance.closeDialLog()
    }
} 
