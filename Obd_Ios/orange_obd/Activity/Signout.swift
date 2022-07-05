//
//  Signout.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/10.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Signout: UIViewController {

    @IBOutlet var sure: UIButton!
    @IBOutlet var no: UIButton!
    @IBOutlet var check: UILabel!
    @IBOutlet var logout: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        logout.text=SetLan.Setlan("Log_out")
        check.text=SetLan.Setlan("Are_you_sure_you_want_to_log_out")
        no.setTitle(SetLan.Setlan("cancel"), for: .normal)
        sure.setTitle(SetLan.Setlan("Yes"), for: .normal)
    }
    
    @IBAction func cancel(_ sender: Any) {
        self.willMove(toParent: nil)
        view.removeFromSuperview()
        self.removeFromParent()
        self.dismiss(animated: true, completion: nil)
    }
    @IBAction func yes(_ sender: Any) {
        ViewController.writeshare("nodata", "admin")
        exit(0)
    }
    
}
