//
//  TalkingAdapter.swift
//  txusb
//
//  Created by 王建智 on 2019/10/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import Kingfisher
class TalkingAdapter: UITableViewCell {
 var it=MessageItem()
    
    var position=0
    @IBOutlet var time: UILabel!
    @IBOutlet var reply: UILabel!
    
    @IBOutlet var showim: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
   
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: true)
      
     
      
    }

}
