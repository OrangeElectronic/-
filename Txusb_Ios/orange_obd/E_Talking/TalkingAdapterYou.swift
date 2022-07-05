//
//  TalkingAdapterYou.swift
//  txusb
//
//  Created by 王建智 on 2019/10/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import Kingfisher
class TalkingAdapterYou: UITableViewCell {
 var it=MessageItem()
    var position=0
    @IBOutlet var Time: UILabel!
    @IBOutlet var picture: UIImageView!
    @IBOutlet var reply: UILabel!
    @IBOutlet var name: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()

    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: true)
        name.text=SetLan.Setlan("Customer_service_specialist")
    }
  

}
