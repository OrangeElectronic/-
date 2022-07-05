//
//  Page_Car_Main.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/3.
//

import UIKit
import JzOsAdapter
import JzOsFrameWork
import JzOsTaskHandler
class Page_Car_Main: UIViewController {
    //車輛數據
    var carItem:Data_Setting_Car{
        get{
            return PublicBeans.selectCar
        }
    }
    //Adapter View的資料
    lazy var adapterView:Data_View_Car = PublicBeans.selectCar.getAdapterViewInfo()
    //Sensor數據
    var sensorInfo :[Sensor_Data] = Md_Sensor_Info.getSensorData(data: PublicBeans.selectCar)
    //用戶設定
    lazy var userSetting = Md_User_Setting.getUserSetting()
    //胎壓溫度選擇
    lazy var action=0
    var selectAction:Int
    {
        get{
            return action
        }
        set(value){
            action = value
            adapter?.notifyDataSetChange()
        }
    }
    
    //是否檢測完畢的View
    @IBOutlet var triggerFinish: UIView!
    @IBOutlet var tb: UITableView!
    @IBOutlet var carView: UIView!
    lazy var adapter:GridAdapter? = nil
    var timer=Timer()
    var canReader=true
    override func viewDidLoad() {
        super.viewDidLoad()
        timer = Timer.scheduledTimer(timeInterval: 3.0, target: self, selector: #selector(timerAction), userInfo: nil, repeats: true)
        JzActivity.getControlInstance.changeFrage(carView, adapterView.carView,self)
        tb.bounces=false
        triggerFinish.isHidden=sensorInfo.filter({ $0.pre == "NA" }).count==0
        tb.isScrollEnabled=false
        tb.transform.ty=CGFloat(adapterView.translate)
        triggerFinish.isHidden=true
        for a in adapterView.wheel{
            let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(imageTapped(tapGestureRecognizer:)))
               a.isUserInteractionEnabled = true
               a.addGestureRecognizer(tapGestureRecognizer)
        }
    }
    override func viewWillAppear(_ animated: Bool) {
        
    }
    @objc func timerAction() {
        if(!canReader){
            return
        }
        self.sensorInfo = Md_Sensor_Info.getSensorData(data: PublicBeans.selectCar)
        if(self.adapter != nil){
            self.adapter!.notifyDataSetChange()
        }
        self.updateCar()
    }
    var focus = -1
    //更新車的圖片
    func updateCar(){
        for i in adapterView.wheel{
            //判斷有無此輪位
            let sensor=carItem.sensor.filter({($0.wh == "\(i.tag)")})
            if(sensor.count == 0){
                print("無輪位:\(i)")
                i.image=UIImage(named:  "icon_wheel_normal")
            }else{
                print("有輪位:\(i)")
                var sen:Sensor_Data? = sensor[0].id.getObject(rout: "Sensor_Data")
                if(sen == nil){
                    i.image=UIImage(named: (focus == i.tag) ? "icon_tire_ok": "icon_tire_select" )
                }else{
                    print("異常==最大壓力->\(self.userSetting.maxPre)==最小壓力->\(self.userSetting.minPre)==現在壓力->\(sen!.pre)")
                    if(sen!.pre == "NA"){
                        i.image=UIImage(named:  (focus == i.tag) ? "icon_tire_ok" : "icon_tire_select")
                    }else{
                        if((Int(sen!.pre)! < self.userSetting.minPre)){
                            i.image=UIImage(named:  (focus == i.tag) ? "icon_tire_ok" : "icon_wheel_fail")
                            print("Int(sen!.pre)!==\(Int(sen!.pre)!)")
                            print("self.userSetting.minPre==\(self.userSetting.minPre)")
                            checkAdvice()
                        }else{
                            i.image=UIImage(named:  (focus == i.tag) ? "icon_tire_ok" : "icon_wheel_ok")
                        }
                    }
                }
            }
        }
    }
    var showAlert=true
    //胎溫胎壓異常提醒
    func checkAdvice(){
        if(!showAlert){return}
        TaskHandler.newInstance().runTaskDelay("checkAdvice", 5.0, {
            Util_Play_Sound.getInstance.playSound()
            let controller = UIAlertController(title: "訊息", message: "胎溫胎壓異常!!", preferredStyle: .alert)
            let cancelAction = UIAlertAction(title: "關閉提醒", style: .default){ (_) in
                self.showAlert=false
            }
            controller.addAction(cancelAction)
            let okAction = UIAlertAction(title: "確認", style: .default) { (_) in
            }
            controller.addAction(okAction)
            self.present(controller, animated: true, completion: nil)
        })
    }
    override func viewDidAppear(_ animated: Bool) {
        if(adapter == nil){
            reFreshAdapter()
        }
        adapter?.notifyDataSetChange()
        
    }
    override func viewWillDisappear(_ animated: Bool) {
        timer.invalidate()
    }
    func reFreshAdapter(){
        adapter=GridAdapter(tb: tb, width:UIScreen.main.bounds.width/2, height: self.adapterView.height
                            , count: {return self.adapterView.adapterCount}, spilt: 2, nib: ["Cell_Left_Tire_1","Cell_Right_Tire_1"], getcell: {
                                a,b,c in
                                if(c%2 == 0){
                                    let cell=a.dequeueReusableCell(withReuseIdentifier: "Cell_Left_Tire_1", for: b) as! Cell_Left_Tire_1
                                    cell.clickt2={}
                                    cell.clickt1={}
                                    cell.t2.isHidden=self.adapterView.wheelData[c].count==1
                                    let data = self.sensorInfo.filter({ $0.wh == String(self.adapterView.wheelData[c][0])  })
                                    
                                    if(data.count>0){
                                        let text=(self.selectAction==0) ? data[0].pre : data[0].tem
                                
                                            cell.t1.setTitleColor((text.isEmpty) ? UIColor(named: "6B7B85") : UIColor.white, for: .normal)
                                        cell.clickt1={
                                            self.focus=Int(data[0].wh)!
                                            self.updateCar()
                                        }
                                        if(self.selectAction==0){
                                            if((Int(text) != nil) && ((Int(text)! < self.userSetting.minPre.toSettingPre()))){
                                                cell.t1.setTitleColor(UIColor.red
                                                                      , for: .normal)
                                            }
                                        }else{
                                            if((Int(text) != nil) && ((Int(text)! > self.userSetting.temAdvice.toSettingTem()) )){
                                                cell.t1.setTitleColor(UIColor.red
                                                                      , for: .normal)
                                            }
                                        }
                                        cell.t1.setTitle(((Int(text) != nil)) ? text : "---", for: .normal)
                                    }else{
                                        cell.t1.setTitle("---",for: .normal)
                                        cell.t1.setTitleColor(UIColor(named: "6B7B85")
                                                              , for: .normal)
                                    }
                                    if(self.adapterView.wheelData[c].count==2){
                                        let data = self.sensorInfo.filter({ $0.wh == String(self.adapterView.wheelData[c][1])  })
                                        if(data.count>0){
                                            cell.clickt2={
                                                self.focus=Int(data[0].wh)!
                                                self.updateCar()
                                            }
                                            let text=(self.selectAction==0) ? data[0].pre : data[0].tem
                                            cell.t2.setTitleColor((text.isEmpty) ? UIColor(named: "6B7B85") : UIColor.white, for: .normal)
                                            if(self.selectAction==0){
                                                if((Int(text) != nil) && ((Int(text)! < self.userSetting.minPre.toSettingPre()))){
                                                    cell.t2.setTitleColor(UIColor.red
                                                                          , for: .normal)
                                                }
                                            }else{
                                                if((Int(text) != nil) && ((Int(text)! > self.userSetting.temAdvice.toSettingTem()) )){
                                                    cell.t2.setTitleColor(UIColor.red
                                                                          , for: .normal)
                                                }
                                            }
                                            cell.t2.setTitle(((Int(text) != nil)) ? text : "---", for: .normal)
                                        }else{
                                            cell.t2.setTitle("---",for: .normal)
                                            cell.t2.setTitleColor(UIColor(named: "6B7B85")
                                                                  , for: .normal)
                                        }
                                    }
                                    if(!self.canReader){
                                        cell.t1.setTitle("---", for: .normal)
                                        cell.t2.setTitle("---", for: .normal)
                                    }
                                    return cell
                                }else{
                                    let cell=a.dequeueReusableCell(withReuseIdentifier: "Cell_Right_Tire_1", for: b) as! Cell_Right_Tire_1
                                    cell.clickt2={}
                                    cell.clickt1={}
                                    cell.t2.isHidden=self.adapterView.wheelData[c].count==1
                                    let data = self.sensorInfo.filter({ $0.wh == String(self.adapterView.wheelData[c][0])  })
                                    
                                    if(data.count>0){
                                        let text=(self.selectAction==0) ? data[0].pre : data[0].tem
                                        cell.clickt1={
                                            self.focus=Int(data[0].wh)!
                                            self.updateCar()
                                        }
                                        cell.t1.setTitleColor((text.isEmpty) ? UIColor(named: "6B7B85") : UIColor.white, for: .normal)
                                        if(self.selectAction==0){
                                            if((Int(text) != nil) && ((Int(text)! < self.userSetting.minPre.toSettingPre()))){
                                                cell.t1.setTitleColor(UIColor.red
                                                                      , for: .normal)
                                            }
                                        }else{
                                            if((Int(text) != nil) && ((Int(text)! > self.userSetting.temAdvice.toSettingTem()) )){
                                                cell.t1.setTitleColor(UIColor.red
                                                                      , for: .normal)
                                            }
                                        }
                                        cell.t1.setTitle(((Int(text) != nil)) ? text : "---", for: .normal)
                                    }else{
                                        cell.t1.setTitle("---",for: .normal)
                                        cell.t1.setTitleColor(UIColor(named: "6B7B85")
                                                              , for: .normal)
                                    }
                                    if(self.adapterView.wheelData[c].count==2){
                                        let data = self.sensorInfo.filter({ $0.wh == String(self.adapterView.wheelData[c][1])  })
                                        if(data.count>0){
                                            cell.clickt2={
                                                self.focus=Int(data[0].wh)!
                                                self.updateCar()
                                            }
                                            let text=(self.selectAction==0) ? data[0].pre : data[0].tem
                                            cell.t2.setTitleColor((text.isEmpty) ? UIColor(named: "6B7B85") : UIColor.white, for: .normal)
                                            if(self.selectAction==0){
                                                if((Int(text) != nil) && ((Int(text)! < self.userSetting.minPre.toSettingPre()))){
                                                    cell.t2.setTitleColor(UIColor.red
                                                                          , for: .normal)
                                                }
                                            }else{
                                                if((Int(text) != nil) && ((Int(text)! > self.userSetting.temAdvice.toSettingTem()) )){
                                                    cell.t2.setTitleColor(UIColor.red
                                                                          , for: .normal)
                                                }
                                            }
                                            cell.t2.setTitle(((Int(text) != nil)) ? text : "---", for: .normal)
                                        }else{
                                            cell.t2.setTitle("---",for: .normal)
                                            cell.t2.setTitleColor(UIColor(named: "6B7B85")
                                                                  , for: .normal)
                                        }
                                    }
                                    if(!self.canReader){
                                        cell.t1.setTitle("---", for: .normal)
                                        cell.t2.setTitle("---", for: .normal)
                                    }
                                    return cell
                                }
                            })
    }
    @objc func imageTapped(tapGestureRecognizer: UITapGestureRecognizer)
    {
//        let tappedImage = tapGestureRecognizer.view as! UIImageView
//        let sensor=carItem.sensor.filter({($0.wh == "\(tappedImage.tag)")})
//        if(sensor.count != 0){
//            focus=tappedImage.tag
//            updateCar()
//        }
        // Your action
    }
}
