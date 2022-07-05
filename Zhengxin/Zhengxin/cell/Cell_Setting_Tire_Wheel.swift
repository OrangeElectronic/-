//
//  Cell_Setting_Tire_Wheel.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit

class Cell_Setting_Tire_Wheel: UICollectionViewCell {

    @IBOutlet var stack: UIStackView!
    @IBOutlet var t2: UIButton!
    @IBOutlet var t1: UIButton!
    var clt1:()->Void={}
    var clt2:()->Void={}
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    @IBAction func clickt1(_ sender: Any) {
        clt1()
    }
    
    @IBAction func clickt2(_ sender: Any) {
        clt2()
    }
}
