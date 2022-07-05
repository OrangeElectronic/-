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
    
    @IBOutlet weak var stack: UIStackView!
    var name1=""
    var name2=""
    var name3=""
    var act:ViewController? = nil
    override func awakeFromNib() {
        
       
        // Initialization code
       
         super.awakeFromNib()
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)
    }

    @IBAction func B3(_ sender: Any) {
        PublicBeans.Make=name3
      let a=peacedefine().SelectModel
        a.act=act
        act?.ChangePage(to: a)
    }
    @IBAction func B2(_ sender: Any) {
        PublicBeans.Make=name2
        let a=peacedefine().SelectModel
        a.act=act
        act?.ChangePage(to: a)
    }
    @IBAction func B1(_ sender: Any) {
        PublicBeans.Make=name1
        let a=peacedefine().SelectModel
        a.act=act
        act?.ChangePage(to: a)
    }
}
