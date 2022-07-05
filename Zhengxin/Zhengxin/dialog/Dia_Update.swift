//
//  Dia_Update.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/3/11.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzOsFrameWork
class Dia_Update: UIViewController {
    
    @IBOutlet var content: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func cancel(_ sender: Any) {
        JzActivity.getControlInstance.closeDialLog()
    }
    
    @IBAction func ok(_ sender: Any) {
        "資料更新中...".progress()
        DonloadFile().dataloading({
            a in
            DispatchQueue.main.async {
                JzActivity.getControlInstance.closeDialLog()
                if(!a){
                    JzActivity.getControlInstance.openDiaLog(Dia_Update(), false, "Dia_Update")
                    JzActivity.getControlInstance.toast("更新失敗!!")
                }
            }
        })
    }
    
}
