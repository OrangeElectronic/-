//
//  VersionUpdate.swift
//  txusb
//
//  Created by 王建智 on 2019/8/9.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import JzIos_Framework
class VersionUpdate: UIViewController {

    @IBOutlet var versionupdate: UIButton!
    @IBOutlet var chechupdate: UILabel!
    @IBOutlet var fordeveloper: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
    chechupdate.text=SetLan.Setlan("Check for updates")
    fordeveloper.text=SetLan.Setlan("For developer")
    }
    
    @IBAction func checkVersion(_ sender: Any) {
        JzActivity.getControlInstance.closeDialLog()
        DonloadFile().dataloading({
            a in
            DispatchQueue.main.async {
                JzActivity.getControlInstance.closeDialLog()
                if(!a){
                    JzActivity.getControlInstance.toast(SetLan.Setlan("nointer"))
                }
            }
        })
    }
}
