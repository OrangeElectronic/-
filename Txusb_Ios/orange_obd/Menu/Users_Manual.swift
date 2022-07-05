//
//  Users_Manual.swift
//  txusb
//
//  Created by 王建智 on 2019/8/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Users_Manual: UIViewController {
    
   
    @IBOutlet var labletext: UILabel!
    @IBOutlet var label: UILabel!
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = SetLan.Setlan("ProvideUsbTpmsApp")
        labletext.text="USB PAD"
    }
    
    @IBAction func ProgramUsb(_ sender: Any) {
        let a=peacedefine().Menu_Detail
        act.ChangePage(to: a)
    }
    
    @IBAction func ProgramCopy(_ sender: Any) {
        let a=peacedefine().Menu_Detail
        a.place=1
        act.ChangePage(to: a)
    }
    override func viewWillAppear(_ animated: Bool) {
        act.tit.text="Users_manual"
    }
}
