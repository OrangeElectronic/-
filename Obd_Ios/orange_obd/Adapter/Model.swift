//
//  Model.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Model: UITableViewCell {
var n1=""
var n2=""
     var act:ViewController?=nil
    @IBOutlet var b2: UIButton!
    @IBOutlet var b1: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)
    }

    @IBAction func B2(_ sender: Any) {
        act?.Selectmodel=n2
        let a=peacedefine().SelectYear
        act?.ChangePage(to: a)
    }
    @IBAction func B1(_ sender: Any) {
        act?.Selectmodel=n1
        let a=peacedefine().SelectYear
        act?.ChangePage(to: a)
    }
}
