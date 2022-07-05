//
//  Cell_Setting_Car_List.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit

class Cell_Setting_Car_List: UITableViewCell {
    var deletec={}
    
    @IBOutlet var tit: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
       
    }
    @IBAction func deleteO(_ sender: Any) {
        deletec()
    }
    
}
