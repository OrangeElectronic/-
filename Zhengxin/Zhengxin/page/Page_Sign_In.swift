//
//  Page_Sign_In.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/2.
//

import UIKit
import JzOsFrameWork
class Page_Sign_In: UIViewController {

    @IBOutlet var password: UITextField!
    @IBOutlet var admin: UITextField!
    @IBOutlet var AdminToggle: UIButton!
    var autologin:Bool{
        get{
            return JzActivity.getControlInstance.getPro("autologin",false)
        }
        set(value){
           JzActivity.getControlInstance.setPro("autologin", value)
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        self.admin.attributedPlaceholder = NSAttributedString(string:
            "E-mail", attributes:
                [NSAttributedString.Key.foregroundColor:UIColor.gray])
        self.password.attributedPlaceholder = NSAttributedString(string:
            "密碼", attributes:
                [NSAttributedString.Key.foregroundColor:UIColor.gray])
        AdminToggle.setImage((autologin) ? UIImage(named: "Click") : UIImage(named: "Unchecked"), for: .normal)
        if(autologin){
            let a=Md_User_Setting.getUserSetting()
            self.admin.text = a.admin
            self.password.text = a.password
        }
    }
     
    @IBAction func memoryAdmin(_ sender: Any) {
        autologin.toggle()
        AdminToggle.setImage((autologin) ? UIImage(named: "Click") : UIImage(named: "Unchecked"), for: .normal)
    }
    
    @IBAction func signIn(_ sender: Any) {
       var userSetting=Md_User_Setting.getUserSetting()
       userSetting.admin = admin.text!
       userSetting.password = password.text!
       userSetting.login()
//        JzActivity.getControlInstance.setHome(Page_Home(), "Page_Home")
    }
    
    @IBAction func register(_ sender: Any) {
        JzActivity.getControlInstance.changePage(Page_Register(),"Page_Register" , true)
    }
    
}
