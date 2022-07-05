//
//  Year.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Year: UITableViewCell {
    var name1=""
    var name2=""
    @IBOutlet var b2: UIButton!
    @IBOutlet var b1: UIButton!
    var act:ViewController?=nil
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)
        
    }

    @IBAction func B2(_ sender: Any) {
        act?.Selectyear=name2
        let a=peacedefine().Relarm
        act?.ChangePage(to: a)
        
    }
    @IBAction func B1(_ sender: Any) {
         act?.Selectyear=name1
        let a=peacedefine().Relarm
        act?.ChangePage(to: a)
    }
}
