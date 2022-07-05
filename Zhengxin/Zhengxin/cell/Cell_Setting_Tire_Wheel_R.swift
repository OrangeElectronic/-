//
//  Cell_Setting_Tire_Wheel_R.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit

class Cell_Setting_Tire_Wheel_R: UICollectionViewCell {

    @IBOutlet var t2: UIButton!
    @IBOutlet var t1: UIButton!
    var clt2:()->Void={}
    var clt1:()->Void={}
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    @IBAction func clickt2(_ sender: Any) {
        clt2()
    }
    
    @IBAction func clickt1(_ sender: Any) {
        clt1()
    }
}
