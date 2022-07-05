//
//  UserManual.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/19.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class UserManual: UIViewController {
 let act=(UIApplication.shared.delegate as! AppDelegate).act!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func SeeMNaual(_ sender: Any) {
        let a=peacedefine().Menu_Detail
        act.ChangePage(to: a)
    }
}
