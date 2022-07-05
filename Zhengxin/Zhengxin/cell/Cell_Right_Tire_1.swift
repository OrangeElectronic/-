//
//  Cell_Right_Tire_1.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/3.
//

import UIKit

class Cell_Right_Tire_1: UICollectionViewCell {

    @IBOutlet var t1: UIButton!
    @IBOutlet var t2: UIButton!
    var clickt1={}
    var clickt2={}
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    @IBAction func clickT1(_ sender: Any) {
        clickt1()
    }
    @IBAction func clickt2(_ sender: Any) {
        clickt2()
    }
}
