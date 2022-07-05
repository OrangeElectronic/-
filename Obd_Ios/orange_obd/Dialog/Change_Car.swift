//
//  Change_Car.swift
//  orange_obd
//
//  Created by 王建智 on 2019/11/1.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Change_Car: UIViewController {
let act=(UIApplication.shared.delegate as! AppDelegate).act!
    override func viewDidLoad() {
        super.viewDidLoad()

    }
    @IBAction func close(_ sender: Any) {
        act.CloseSwip()
        act.unpair()
        act.Pagememory.removeAll()
        act.ChangePage(to: peacedefine().HomePage)
    }
}
