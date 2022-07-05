//
//  Cell_Home_Item.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/2.
//

import UIKit

class Cell_Home_Item: UICollectionViewCell {
    var clickVoid={}
    @IBOutlet var tit: UILabel!
    @IBOutlet var img: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    @IBAction func click(_ sender: Any) {
        print("click")
        clickVoid()
    }
    
}
