//
//  Reloading.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/12.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Reloading: UIViewController {
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
     var position=0
    @IBOutlet var SetUpBt: UIButton!
    @IBOutlet var cancelbt: UIButton!
    @IBOutlet var errorlabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()

   errorlabel.text=SetLan.Setlan("error")
cancelbt.setTitle(SetLan.Setlan("cancel"), for: .normal)
    SetUpBt.setTitle(SetLan.Setlan("Yes"), for: .normal)
    }
    
    @IBAction func no(_ sender: Any) {
        act.back.isEnabled=true
        let a=peacedefine().HomePage
        act.ChangePage(to: a)
        self.willMove(toParent: nil)
        view.removeFromSuperview()
        self.removeFromParent()
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func yes(_ sender: Any) {
        act.back.isEnabled=true
        if(position==1){
            act.ChangePage(to: peacedefine().Show_Read)
        }else{
            act.ChangePage(to: peacedefine().Idcopy)
        }
        
        self.willMove(toParent: nil)
        view.removeFromSuperview()
        self.removeFromParent()
        self.dismiss(animated: true, completion: nil)
    }
}
