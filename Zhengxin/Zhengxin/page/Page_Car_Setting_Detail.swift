//
//  Page_Car_Setting_Detail.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit
import JzOsFrameWork
import FittedSheets
class Page_Car_Setting_Detail: UIViewController {
    var frag :Page_Car_Setting_Main! = nil
    @IBOutlet var container: UIView!
    @IBOutlet var tit: UILabel!
    //ID碼
    @IBOutlet var idFoeld: JzTextField!
    //Number號碼
    @IBOutlet var noT: UILabel!
    //Key in欄位
    @IBOutlet var keyinView: UIStackView!
    //右下按鈕
    @IBOutlet var rbt: UIButton!
    //雙按鈕
    @IBOutlet var twoView: UIView!
    //主選單按鈕
    @IBOutlet var menu: UIView!
    //輪胎條碼
    @IBOutlet var barCode: UITextField!
    @IBOutlet var rearBt: UIButton!
    @IBOutlet var frontBt: UIButton!
        @IBOutlet var selectView: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        PublicBeans.selectCar.isFront=true
        tit.text="\(PublicBeans.selectCar.make)/\(PublicBeans.selectCar.model)/\(PublicBeans.selectCar.year)/\(PublicBeans.selectCar.weight)"
        idFoeld.digits="0123456789abcdefABCDEF"
        idFoeld.textCount=6
        idFoeld.upcase=true
        idFoeld.textDidEdit={ [self] in
            for a in ["a","b","c","d","e","f"]{
                if(idFoeld.text!.contains(a)){
                    self.idFoeld.text=idFoeld.text!.uppercased()
                    break
                }
            }
            if(idFoeld.text!.count==6){
                storeSensorData()
            }
        }
        if(!PublicBeans.selectCar.carType.contains("+")){
            selectView.isHidden=true
        }
    }
    override func viewDidAppear(_ animated: Bool) {
        if(frag == nil){
            frag = Page_Car_Setting_Main()
            frag.fp=self
            JzActivity.getControlInstance.changeFrage(container,frag , self)
        }
    }
    
    @IBAction func scan(_ sender: Any) {
        let controller = Dia_Select_Scan()
        let sheetController = SheetViewController(controller: controller)
        self.present(sheetController, animated: true, completion: nil)
        controller.sensorCallBack={
            sheetController.dismiss(animated: true, completion: nil)
            PublicBeans.getQrCode({ [self]
                a in
                let string = a.replace("*", "")
                let aSet = NSCharacterSet(charactersIn:"0123456789ABCDEF").inverted
                let compSepByCharInSet = string.components(separatedBy: aSet)
                let numberFiltered = compSepByCharInSet.joined(separator: "")
                if(string != numberFiltered || string.count != 6){
                    let controller = UIAlertController(title: "訊息", message: "請掃描正確的QRCODE!", preferredStyle: .alert)
                    present(controller, animated: true, completion: nil)
                    let cancelAction = UIAlertAction(title: "確認", style: .cancel, handler: nil)
                    controller.addAction(cancelAction)
                }else{
                    idFoeld.text = string
                    storeSensorData()
                }
            })
        }
        controller.tireCallBack={
            sheetController.dismiss(animated: true, completion: nil)
            PublicBeans.getQrCode({ [self]
                a in
                barCode.text = a
            })
        }
    }
    
    @IBAction func storeUpload(_ sender: Any) {
        if(idFoeld.text!.isEmpty){
            let controller = UIAlertController(title: "訊息", message: "請輸入ID號碼!", preferredStyle: .alert)
            let cancelAction = UIAlertAction(title: "確認", style: .cancel, handler: nil)
            controller.addAction(cancelAction)
            present(controller, animated: true, completion: nil)
           
            return
        }
        let original=PublicBeans.selectCar.sensor.filter({ $0.wh != "\(frag.tireSelect)" })
        if(original.count>0  ){
            if(original[0].barCode != barCode.text && original[0].id != idFoeld.text){
                storeSensorData()
            }
        }else{
            storeSensorData()
        }
    }
    public func storeSensorData(){
        if(!idFoeld.text!.isEmpty){   
            PublicBeans.selectCar.sensor=PublicBeans.selectCar.sensor.filter({ $0.wh != "\(frag.tireSelect)" })
            PublicBeans.selectCar.sensor.append(SensorPosition(id: idFoeld.text! , wh: "\(frag.tireSelect)", barCode: barCode!.text!))
            Sensor_Data(data_name: PublicBeans.selectCar.carType, plateNumber: PublicBeans.selectCar.plateNumber, count: PublicBeans.selectCar.count, lan: "NA", lon: "NA", address: "NA", wh: "\(frag.tireSelect)", id: idFoeld.text!, pre: "NA", tem: "NA", time: "NA", data: "NA").storeSensorInfo()
            PublicBeans.selectCar.setSettingCar(callback: {
                self.frag.toUnset()
                self.frag.adapter?.notifyDataSetChange()
                self.idFoeld.text=""
                self.barCode.text=""
                return 1
            })
        }
    }
    public func storeSensor(){
        PublicBeans.selectCar.sensor=PublicBeans.selectCar.sensor.filter({ $0.wh != "\(frag.tireSelect)" })
        PublicBeans.selectCar.sensor.append(SensorPosition(id: idFoeld.text! , wh: "\(frag.tireSelect)", barCode: barCode!.text!))
        Sensor_Data(data_name: PublicBeans.selectCar.carType, plateNumber: PublicBeans.selectCar.plateNumber, count: PublicBeans.selectCar.count, lan: "NA", lon: "NA", address: "NA", wh: "\(frag.tireSelect)", id: idFoeld.text!, pre: "NA", tem: "NA", time: "NA", data: "NA").storeSensorInfo()
    }
    @IBAction func menu(_ sender: Any) {
        JzActivity.getControlInstance.goMenu()
    }
    
    @IBAction func clickRear(_ sender: Any) {
        PublicBeans.selectCar.isFront=false
        rearBt.setTitle("V 拖板車", for: .normal)
        frontBt.setTitle("拖車頭", for: .normal)
        rearBt.backgroundColor=UIColor(named: "E64E00")
        frontBt.backgroundColor=UIColor(named: "6Ac6D7")
        print("clickRear")
        frag.removeFromParent()
        frag.view.removeFromSuperview()
        frag = Page_Car_Setting_Main()
        frag.fp=self
        JzActivity.getControlInstance.changeFrage(container,frag , self)
    }
    
    @IBAction func clickFrom(_ sender: Any) {
        PublicBeans.selectCar.isFront=true
        rearBt.setTitle("拖板車", for: .normal)
        frontBt.setTitle("V 拖車頭", for: .normal)
        frontBt.backgroundColor=UIColor(named: "E64E00")
        rearBt.backgroundColor=UIColor(named: "6Ac6D7")
        print("clickFrom")
        frag.removeFromParent()
        frag.view.removeFromSuperview()
        frag = Page_Car_Setting_Main()
        frag.fp=self
        JzActivity.getControlInstance.changeFrage(container,frag , self)
    }
  
}
