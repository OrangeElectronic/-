//
//  Cell_SettingCar_List.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit

class Cell_SettingCar_List: UITableViewCell {
    var resetC={}
    var exportC={}
    @IBOutlet var tit: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(false, animated: false)
    }
    
    @IBAction func export(_ sender: Any) {
        exportC()
    }
    @IBAction func reset(_ sender: Any) {
        resetC()
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
      tableView.deselectRow(at: indexPath, animated: false)
    }
}
