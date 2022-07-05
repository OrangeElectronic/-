//
//  Page_Car_List.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit
import JzOsAdapter
class Page_Car_List: UIViewController {
    @IBOutlet var tb: UITableView!
    
    @IBOutlet var tit: UILabel!
    var car=Md_Setting_Car.getSettingCar()
    lazy var adapter=LinearAdapter(tb: self.tb, count: {return self.car.count}, nib: ["Cell_Setting_Car_List"], getcell: {
        a,b,c in
        var cell=a.dequeueReusableCell(withIdentifier: "Cell_Setting_Car_List") as! Cell_Setting_Car_List
        cell.tit.text=self.car[c].plateNumber
        cell.deletec={
            self.car[c].deleteCar(callback: {
                self.car=Md_Setting_Car.getSettingCar()
                self.notify()
                self.tit.text="記錄數量\(self.car.count)台"
                return 1
            })
          
            print("delete")
        }
        return cell
    }, {
        a in
    })
    override func viewDidLoad() {
        
        super.viewDidLoad()
        adapter.notifyDataSetChange()
        tit.text="記錄數量\(car.count)台"
    }
    func notify(){
        adapter.notifyDataSetChange()
    }


}
