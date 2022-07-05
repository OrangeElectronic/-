//
//  Signin.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/9.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Sign_in: UIViewController {
    var run=false
    @IBOutlet var password: UITextField!
    @IBOutlet var admin: UITextField!
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    
    @IBOutlet var forget: UIButton!
    @IBOutlet var sig: UIButton!
    @IBOutlet var reg: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        reg.setTitle(SetLan.Setlan("Registration"), for: .normal)
        sig.setTitle(SetLan.Setlan("Sign_in"), for: .normal)
        forget.setTitle(SetLan.Setlan("Forgot_password"), for: .normal)
    }
    
    @IBAction func Signin(_ sender: Any) {
        if(run){return}
        act.LoadIng("Sign in")
        let ad=admin.text
        let p=password.text
        run=true
        Function.Signin(ad!,p!,self)
    }
    
    @IBAction func Registration(_ sender: Any) {
        let a=peacedefine().Registration
        act.ChangePage(to: a)
    }
    
    @IBAction func goreset(_ sender: Any) {
        let a=peacedefine().ResetPassword
        act.ChangePage(to: a)
    }
}
