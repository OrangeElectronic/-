//
//  Page_Register.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/9.
//

import UIKit
import JzOsFrameWork
class Page_Register: UIViewController {
    
    @IBOutlet var company: UITextField!
    @IBOutlet var phone: UITextField!
    @IBOutlet var password: UITextField!
    @IBOutlet var firstname: UITextField!
    @IBOutlet var name: UITextField!
    @IBOutlet var email: UITextField!
    
    @IBOutlet var confirmpass: UITextField!
    @IBOutlet var policyBt: UIButton!
    @IBOutlet var manager: UIButton!
    @IBOutlet var driver: UIButton!
    var userData:Data_User_Setting{
        get{
            var userSetting=Md_User_Setting.getUserSetting()
            userSetting.admin=email.text!
            userSetting.name=name.text!
            userSetting.first_Name=firstname.text!
            userSetting.password=password.text!
            userSetting.phone=phone.text!
            userSetting.companyName=company.text!
            userSetting.driver = "\(drivert)"
            userSetting.manager = "\(mangert)"
            return userSetting
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        for i in JzActivity.getControlInstance.getAllSubViews(view){
            if(i is UITextField){
                
                (i as! UITextField).attributedPlaceholder=NSAttributedString(string:
                                                                                (i as! UITextField).placeholder!, attributes:
                                                                                    [NSAttributedString.Key.foregroundColor:UIColor.gray])
            }
            
        }
    }
    
    var polocy=false
    @IBAction func clickPolicy(_ sender: Any) {
        polocy.toggle()
        policyBt.setImage((polocy) ? UIImage(named: "Click"):UIImage(named: "Unchecked"), for: .normal)

    }
    var drivert=false
    @IBAction func driverToggle(_ sender: Any) {
        drivert.toggle()
        driver.setImage((drivert) ? UIImage(named: "Click"):UIImage(named: "Unchecked"), for: .normal)
    }
    var mangert=false
    @IBAction func managerToggle(_ sender: Any) {
        mangert.toggle()
        manager.setImage((mangert) ? UIImage(named: "Click"):UIImage(named: "Unchecked"), for: .normal)
    }
    @IBAction func signin(_ sender: Any) {
        if(!polocy){
            JzActivity.getControlInstance.toast("????????????????????????!!")
            return
        }
        print(userData)
        if(userData.admin.isEmpty){
            "???????????????".toast()
            return
        }else if(userData.password.isEmpty){
            "???????????????".toast()
            return
        }else if(userData.password != confirmpass.text){
            "?????????????????????".toast()
            return
        }else if(userData.phone.isEmpty){
            "?????????????????????".toast()
            return
        }else if(userData.companyName.isEmpty){
            "?????????????????????".toast()
            return
        }else if(!(mangert || drivert)){
            "???????????????????????????".toast()
            return
        }
        userData.register()
    }
    
}

extension String{
    func toast(){
        JzActivity.getControlInstance.toast(self)
    }
}
