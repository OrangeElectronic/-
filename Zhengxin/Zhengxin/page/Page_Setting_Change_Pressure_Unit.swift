//
//  Page_Setting_Change_Pressure_Unit.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit
import JzOsAdapter
class Page_Setting_Change_Pressure_Unit: UIViewController {
   
    @IBOutlet var tb: UITableView!
    var unit = ["kpa","psi","bar"]
    lazy var adapter=LinearAdapter(tb: tb, count: {
        return 3
    }, nib: ["Cell_Setting_Unit"], getcell: {
        a,b,c in
        let cell=a.dequeueReusableCell(withIdentifier: "Cell_Setting_Unit") as! Cell_Setting_Unit
        var userSetting=Md_User_Setting.getUserSetting()
        cell.clv.isHidden=userSetting.punit != c
        cell.tit.text = self.unit[c]
        return cell
    }, {
        a in
        print("click:\(a)")
        var userSetting=Md_User_Setting.getUserSetting()
        userSetting.punit=a
        userSetting.changeInfo(result: {
            self.notify()
            return 1
        })
    })
    override func viewDidLoad() {
        super.viewDidLoad()
        adapter.notifyDataSetChange()
    }
    
    func notify(){
        adapter.notifyDataSetChange()
    }



}
