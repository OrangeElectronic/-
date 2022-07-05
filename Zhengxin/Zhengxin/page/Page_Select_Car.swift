//
//  Md_Select_Car.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/3.
//

import UIKit
import JzOsAdapter
import JzOsFrameWork
class Page_Select_Car: UIViewController {
    
    @IBOutlet var tb: UITableView!
    var data=Md_Setting_Car.getSettingCar()
    lazy var adapter=LinearAdapter(tb: tb, count: {
        //return your item count
        return self.data.count
    }, nib: ["Cell_Select_Car"], getcell: {
        tableview,indexpath,position in
        let cell=tableview.dequeueReusableCell(withIdentifier: "Cell_Select_Car") as! Cell_Select_Car
        cell.tit.text=self.data[position].plateNumber
        let bv=UIView()
        bv.backgroundColor=#colorLiteral(red: 0.4156862745, green: 0.7764705882, blue: 0.8431372549, alpha: 1)
        cell.selectedBackgroundView = bv
        return cell
    }, {
        clickPosition in
        PublicBeans.selectCar=self.data[clickPosition]
        PublicBeans.selectCar.isFront=true
        JzActivity.getControlInstance.changePage(Page_Check_Sensor(), "Page_Check_Sensor", true)
    })
    override func viewDidLoad() {
        super.viewDidLoad()
        adapter.notifyDataSetChange()
    }
    
    @IBAction func goMenu(_ sender: Any) {
        JzActivity.getControlInstance.goMenu()
    }
    @IBAction func addCar(_ sender: Any) {
        JzActivity.getControlInstance.changePage(Page_Car_Setting_AddMenu(), "Page_Car_Setting_AddMenu", true)
    }
    
}
