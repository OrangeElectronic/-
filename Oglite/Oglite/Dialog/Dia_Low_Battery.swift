//
//  Dia_Low_Battery.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2021/1/15.
//  Copyright © 2021 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzOsFrameWork
class Dia_Low_Battery: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        view.fixLanguage()
        // Do any additional setup after loading the view.
    }

    @IBAction func close(_ sender: Any) {
        JzActivity.getControlInstance.closeDialLog()
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
