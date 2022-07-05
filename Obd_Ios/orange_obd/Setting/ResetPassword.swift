//
//  ResetPassword.swift
//  txusb
//
//  Created by 王建智 on 2019/8/26.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class ResetPassword: UIViewController {
let act=(UIApplication.shared.delegate as! AppDelegate).act!
    @IBOutlet var mail: UITextField!
    
    @IBOutlet var sub: UIButton!
    @IBOutlet var l2: UILabel!
    @IBOutlet var l1: UILabel!
    var run=false
    override func viewDidLoad() {
        super.viewDidLoad()
        l1.text=SetLan.Setlan("Og_email")
        l2.text=SetLan.Setlan("Go_email")
        sub.setTitle("Submit", for: .normal)
    }
    

    @IBAction func enter(_ sender: Any) {
        if(run){return}
        act.LoadIng("Data loading")
        run=true
        Function.ResetPass(mail.text!,self)
    }
    
}
