//
//  Setting.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/16.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Setting: UIViewController {
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    @IBOutlet var policy: UILabel!
    @IBOutlet var version: UILabel!
    @IBOutlet var area: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        policy.text=SetLan.Setlan("Privacy_Policy")
        version.text=SetLan.Setlan("Version_update")
        area.text=SetLan.Setlan("AreaLanguage")
    }
    @IBAction func ToLan(_ sender: Any) {
        let a=peacedefine().Select_Area
        act.ChangePage(to: a)
    }
    
    @IBAction func ToPolicy(_ sender: Any) {
        let a=peacedefine().Privacy
        act.ChangePage(to: a)
    }
    
    @IBAction func ToUpdate(_ sender: Any) {
        let a=peacedefine().Update
        act.ChangePage(to: a)
    }
}
