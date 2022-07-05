//
//  Make.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Make: UITableViewCell {
    @IBOutlet var b2: UIButton!
    @IBOutlet var b3: UIButton!
    @IBOutlet var b1: UIButton!
    var name1=""
    var name2=""
    var name3=""
    var act:ViewController? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)
    }

    @IBAction func B3(_ sender: Any) {
        act?.Selectmake=name3
      let a=peacedefine().SelectModel
        act?.ChangePage(to: a)
    }
    @IBAction func B2(_ sender: Any) {
        act?.Selectmake=name2
        let a=peacedefine().SelectModel
        act?.ChangePage(to: a)
    }
    @IBAction func B1(_ sender: Any) {
        act?.Selectmake=name1
        let a=peacedefine().SelectModel
        act?.ChangePage(to: a)
    }
}
