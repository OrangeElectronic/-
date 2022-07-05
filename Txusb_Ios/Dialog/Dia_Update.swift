//
//  Dia_Update.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/3/11.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzIos_Framework
class Dia_Update: UIViewController {

    @IBOutlet var tit: UILabel!
    
    @IBOutlet var yes: UIButton!
    @IBOutlet var cancel: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
//        intext.text=JzActivity.getControlInstance.getPro("update", "已有新版本!!")
        tit.text=SetLan.Setlan("Version_update")
      
        
    }
    override func viewDidAppear(_ animated: Bool) {
        yes.setTitle(SetLan.Setlan("Yes"), for: .normal)
        cancel.setTitle(SetLan.Setlan("cancel"), for: .normal)
    }
    
    @IBAction func cancel(_ sender: Any) {
        JzActivity.getControlInstance.closeDialLog()
    }
    
    @IBAction func ok(_ sender: Any) {
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
