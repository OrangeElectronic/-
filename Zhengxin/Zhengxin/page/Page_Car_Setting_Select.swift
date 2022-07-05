//
//  Page_Car_Setting_Select.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit
import JzOsFrameWork
import JzOsAdapter
class Page_Car_Setting_Select: UIViewController {
    @IBOutlet var tb: UITableView!
    var data=Md_Setting_Car.getSettingCar()
    lazy var adapter=LinearAdapter(tb: tb, count: {
        //return your item count
        return self.data.count
    }, nib: ["Cell_SettingCar_List"], getcell: {
        tableview,indexpath,position in
        let cell=tableview.dequeueReusableCell(withIdentifier: "Cell_SettingCar_List") as! Cell_SettingCar_List
        let bv=UIView()
        bv.backgroundColor=#colorLiteral(red: 0, green: 0, blue: 0, alpha: 0)
        cell.selectedBackgroundView = nil
        cell.exportC={
            PublicBeans.selectCar=self.data[position]
            JzActivity.getControlInstance.changePage(Page_Show_Crcode(), "Page_Show_Crcode", true)
        }
        cell.resetC={
            PublicBeans.selectCar=self.data[position]
            JzActivity.getControlInstance.changePage(Page_Car_Setting_Detail(), "Page_Car_Setting_Detail", true)
        }
        cell.tit.text = self.data[position].plateNumber
        return cell
    }, {
        clickPosition in
    })
    override func viewDidLoad() {
        super.viewDidLoad()
        adapter.notifyDataSetChange()
        // Do any additional setup after loading the view.
    }
    
    @IBAction func addCar(_ sender: Any) {
        JzActivity.getControlInstance.changePage(Page_Car_Setting_AddMenu(), "Page_Car_Setting_AddMenu", true)
    }
    override func viewDidAppear(_ animated: Bool) {
        data=Md_Setting_Car.getSettingCar()
        adapter.notifyDataSetChange()
    }
    
}
