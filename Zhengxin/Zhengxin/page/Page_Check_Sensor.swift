//
//  Page_Check_Sensor.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/3.
//

import UIKit
import JzOsFrameWork
import JzOsBleHelper
import JzOsTaskHandler
class Page_Check_Sensor: UIViewController {
    
    @IBOutlet var selectView: UIView!
    @IBOutlet var tit: UILabel!
    @IBOutlet var PlateView: UILabel!
    @IBOutlet var DriverName: UILabel!
    @IBOutlet var pv: UIView!
    @IBOutlet var tv: UIView!
    @IBOutlet var container: UIView!
    var frag:Page_Car_Main! = nil;
    
    @IBOutlet var rearBt: UIButton!
    @IBOutlet var frontBt: UIButton!
    @IBOutlet var tem: UILabel!
    @IBOutlet var psi: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        DriverName.text=PublicBeans.selectCar.carName.unicodeStr()
        PlateView.text=PublicBeans.selectCar.plateNumber
        tit.text="\(PublicBeans.selectCar.make)/\(PublicBeans.selectCar.model)/\(PublicBeans.selectCar.year)/\(PublicBeans.selectCar.weight)"
        psi.text=["Kpa","Psi","Bar"][Md_User_Setting.getUserSetting().punit]
        tem.text=["C","F"][Md_User_Setting.getUserSetting().tunit]
        if(!PublicBeans.selectCar.carType.contains("+")){
            selectView.isHidden=true
        }
    }
    override func viewDidAppear(_ animated: Bool) {
        if(frag == nil){
            frag = Page_Car_Main()
            JzActivity.getControlInstance.changeFrage(container,frag , self)
        }
        
    }
    
    @IBAction func upload(_ sender: Any) {
        "資料上傳中...".progress()
        
        Md_Sensor_Info.uploadSensorData(data: self.frag.sensorInfo)
       
    }
    @IBAction func actionPre(_ sender: Any) {
        frag.selectAction=0
        pv.alpha=1.0
        tv.alpha=0.5
    }
    @IBAction func trigger(_ sender: Any) {
        self.frag.showAlert=false
        let alltrigger=self.frag.carItem.sensor
        var reTrigger:[SensorPosition]=[SensorPosition]()
        if(alltrigger.count>0){  "胎壓檢測中...".progress()}
        DispatchQueue.global().async { [self] in
            var result=true
            alltrigger.map({
                it in
                if(it.id.getSensorInfo() == nil || it.id.getSensorInfo()!.pre == "NA"){
                    if(!self.triggerID(id: it.id) && (it.id.getSensorInfo() == nil || it.id.getSensorInfo()!.pre == "NA")){
                        reTrigger.append(it)
                    }
                }
            })
            reTrigger.map({
                it in
                if(!self.triggerID(id: it.id)){
                    result=false
                }
            })
            DispatchQueue.main.async {
                self.frag.canReader=true
                self.frag.showAlert=true
                if(result){
                    let controller = UIAlertController(title: "訊息", message: "胎壓檢測完畢!!", preferredStyle: .alert)
                    let okAction = UIAlertAction(title: "確認", style: .default) { (_) in
                        Progress.close()
                    }
                    controller.addAction(okAction)
                    self.present(controller, animated: true, completion: nil)

                }else{
                    let controller = UIAlertController(title: "訊息", message: "胎壓檢測失敗，請靠近輪胎再次按下檢測胎壓!!", preferredStyle: .alert)
                    let okAction = UIAlertAction(title: "確認", style: .default) { (_) in
                        Progress.close()
                    }
                    controller.addAction(okAction)
                    self.present(controller, animated: true, completion: nil)
                }
            }
        }
    }
    
    func triggerID(id:String)->Bool{
       
        if(ViewController.device[id] != nil){
            let helper = ViewController.getBleHelper()
            let clock=JzClock()
            let clock2=JzClock()
            helper.connect(ViewController.device[id]! , 3)
            while(!helper.isPaired()){
                if(!helper.isPaired() && clock2.stop()>5){
                    helper.connect(ViewController.device[id]! , 3)
                    clock2.zeroing()
                }
                if(helper.isPaired()){break}
                if(clock.stop()>15){
                    return false
                }
                usleep(1000*200)
            }
            usleep(1000*200)
            ViewController.rx=""
            if(helper.isPaired()){
                helper.writeHex("0AB70A","8D82", "8D81")
            }
            clock.zeroing()
            while(true){
                if(ViewController.rx != ""){
                    helper.disconnect()
                    return true
                }else if(clock.stop()>3.0){
                    helper.disconnect()
                    return false
                }
                sleep(1)
            }
        }else{
            print("writeHex NO")
            return false
        }
    }
    
    @IBAction func actionTem(_ sender: Any) {
        frag.selectAction=1
        pv.alpha=0.5
        tv.alpha=1.0
    }
    
    
    
    @IBAction func clickRear(_ sender: Any) {
        PublicBeans.selectCar.isFront=false
        rearBt.setTitle("V 拖板車", for: .normal)
        frontBt.setTitle("拖車頭", for: .normal)
        rearBt.backgroundColor=UIColor(named: "E64E00")
        frontBt.backgroundColor=UIColor(named: "6Ac6D7")
        print("clickRear")
        frag.view.removeFromSuperview()
        frag = Page_Car_Main()
        JzActivity.getControlInstance.changeFrage(container,frag , self)
        frag.timerAction()
    }
    
    @IBAction func clickFrom(_ sender: Any) {
        PublicBeans.selectCar.isFront=true
        rearBt.setTitle("拖板車", for: .normal)
        frontBt.setTitle("V 拖車頭", for: .normal)
        frontBt.backgroundColor=UIColor(named: "E64E00")
        rearBt.backgroundColor=UIColor(named: "6Ac6D7")
        print("clickFrom")
        frag.view.removeFromSuperview()
        frag = Page_Car_Main()
        JzActivity.getControlInstance.changeFrage(container,frag , self)
        frag.timerAction()
    }
}
