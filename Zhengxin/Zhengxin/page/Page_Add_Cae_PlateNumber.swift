//
//  Page_Add_Cae_PlateNumber.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit
import JzOsFrameWork
class Page_Add_Cae_PlateNumber: UIViewController {

    @IBOutlet var plateField: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

      
    }


    @IBAction func next(_ sender: Any) {
        if(plateField.text!.isEmpty){
            JzActivity.getControlInstance.toast("請輸入車牌號碼")
            return
        }else{
            var car=Md_Setting_Car.getCarByPlate(plate: plateField.text!)
            if(car==nil){
                PublicBeans.selectCar=Data_Setting_Car(plateNumber: plateField.text!, carName: Md_User_Setting.getUserSetting().name, carType: "NA", count: "NA", make: "NA", model: "NA", year: "NA", weight: "NA", sensor: [])
                JzActivity.getControlInstance.changePage(Page_Select_Make(), "Page_Select_Make", true)
               
            }else{
                let controller = UIAlertController(title: "訊息", message: "此車輛已設定過,是否重新設定??", preferredStyle: .alert)
                let okAction = UIAlertAction(title: "確認", style: .default) { (_) in
                    PublicBeans.selectCar=car!
                    JzActivity.getControlInstance.changePage(Page_Car_Setting_Detail(), "Page_Car_Setting_Detail", true)
                }
                controller.addAction(okAction)
                let cancelAction = UIAlertAction(title: "取消", style: .cancel, handler: nil)
                controller.addAction(cancelAction)
                present(controller, animated: true, completion: nil)
            }
        
        }
       
    }
    
    @IBAction func clear(_ sender: Any) {
        plateField.text=""
    }
    
}
