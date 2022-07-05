//
//  BleAdapter.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/8.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class BleAdapter: UITableViewCell {

    @IBOutlet var blename: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: animated)
    }

}
