//
//  Page_Car_Setting_Main.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit
import JzOsAdapter
import JzOsFrameWork
class Page_Car_Setting_Main: UIViewController {
    //父頁面
    var fp:Page_Car_Setting_Detail! = nil
    //車輛數據
    var carItem:Data_Setting_Car{
        get{
            return PublicBeans.selectCar
        }
    }
    //Adapter View的資料
    var adapterView=PublicBeans.selectCar.getSettingAdapterViewInfo()
    //Sensor數據
    var sensorInfo = Md_Sensor_Info.getSensorData(data: PublicBeans.selectCar)
    @IBOutlet var tb: UITableView!
    @IBOutlet var carView: UIView!
    
    lazy var adapter:GridAdapter? = nil
    //選擇的輪位
    var selectIndex=1
    public var tireSelect:Int{
        get{
            return selectIndex
        }
        set(value){
            fp.storeSensorData()
            selectIndex=value
            adapter?.notifyDataSetChange()
            fp.noT.text="\(value)"
            let it=carItem.sensor.filter({$0.wh == "\(value)"})
            if(it.count==1){
                fp.idFoeld.text=it[0].id
                fp.barCode.text=it[0].barCode
            }else{
                fp.barCode.text=""
                fp.idFoeld.text=""
            }
            updateCar()
            fp.keyinView.isHidden=false
            fp.twoView.isHidden=false
            fp.menu.isHidden=true
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        JzActivity.getControlInstance.changeFrage(carView, adapterView.carView,self)
        tb.bounces=false
        updateCar()
        tb.isScrollEnabled=false
        tb.transform.ty=CGFloat(adapterView.translate)
    
    }
    func updateCar(){
        for i in adapterView.wheel{
            //判斷有無此輪位
            let sensor=carItem.sensor.filter({$0.wh == "\(i.tag)"})
            print("i.tag==\(i.tag)")
            if(i.tag==tireSelect){
                fp.noT.text="\(i.tag)"
                i.image=UIImage(named:  "icon_tire_select")
            }else{
                if(sensor.count == 0){
                    print("無輪位:\(i)")
                    i.image=UIImage(named:  "icon_wheel_normal")
                }else{
                    print("有輪位:\(i)")
                    i.image=UIImage(named:  "icon_wheel_ok")
                }
            }
        }
    }
    override func viewDidAppear(_ animated: Bool) {
        if(adapter == nil){
            adapter=GridAdapter(tb: tb, width:UIScreen.main.bounds.width/2, height: self.adapterView.height
                                , count: {return self.adapterView.adapterCount}, spilt: 2, nib: ["Cell_Setting_Tire_Wheel","Cell_Setting_Tire_Wheel_R"], getcell: {
                                    a,b,c in
                                    if(c%2==0){
                                        let cell=a.dequeueReusableCell(withReuseIdentifier: "Cell_Setting_Tire_Wheel", for: b) as! Cell_Setting_Tire_Wheel
                                        let wheelData=self.adapterView.wheelData[c]
                                     
                                        if(wheelData.count==2){
                                            cell.t2.setBackgroundImage( UIImage(named: ((wheelData[1]==self.tireSelect) ? "icon_tire_number_select":(
                                                (self.carItem.sensor.filter({$0.wh == "\(wheelData[1])"}).count==1) ? "icon_tire_number_ok" : "icon_tire_number_normal"
                                            ))), for: .normal)
                                            cell.t2.setTitle("\(wheelData[1])", for: .normal)
                                            cell.clt2={
                                                self.tireSelect=wheelData[1]
                                            }
                                            cell.t1.setBackgroundImage( UIImage(named: ((wheelData[0]==self.tireSelect) ? "icon_tire_number_select":(
                                                (self.carItem.sensor.filter({$0.wh == "\(wheelData[0])"}).count==1) ? "icon_tire_number_ok" : "icon_tire_number_normal"
                                            ))), for: .normal)
                                            cell.t1.setTitle("\(wheelData[0])", for: .normal)
                                            cell.clt1={
                                                self.tireSelect=wheelData[0]
                                            }
                                        }else{
                                            cell.t2.setBackgroundImage( UIImage(named: ((wheelData[0]==self.tireSelect) ? "icon_tire_number_select":(
                                                (self.carItem.sensor.filter({$0.wh == "\(wheelData[0])"}).count==1) ? "icon_tire_number_ok" : "icon_tire_number_normal"
                                            ))), for: .normal)
                                            cell.t2.setTitle("\(wheelData[0])", for: .normal)
                                            cell.clt2={
                                                self.tireSelect=wheelData[0]
                                            }
                                            cell.t1.setBackgroundImage(nil, for: .normal)
                                            cell.t1.setTitle("", for: .normal)
                                        }
                                       
                                        return cell
                                    }else{
                                        let cell=a.dequeueReusableCell(withReuseIdentifier: "Cell_Setting_Tire_Wheel_R", for: b) as! Cell_Setting_Tire_Wheel_R
                                        let wheelData=self.adapterView.wheelData[c]
                                        
                                        cell.t1.setBackgroundImage( UIImage(named: ((wheelData[0]==self.tireSelect) ? "icon_tire_number_select":(
                                            (self.carItem.sensor.filter({$0.wh == "\(wheelData[0])"}).count==1) ? "icon_tire_number_ok" : "icon_tire_number_normal"
                                        ))), for: .normal)
                                        cell.t1.setTitle("\(wheelData[0])", for: .normal)
                                        cell.clt1={
                                            self.tireSelect=wheelData[0]
                                        }
                                        if(wheelData.count==2){
                                            cell.t2.setBackgroundImage( UIImage(named: ((wheelData[1]==self.tireSelect) ? "icon_tire_number_select":(
                                                (self.carItem.sensor.filter({$0.wh == "\(wheelData[1])"}).count==1) ? "icon_tire_number_ok" : "icon_tire_number_normal"
                                            ))), for: .normal)
                                            cell.t2.setTitle("\(wheelData[1])", for: .normal)
                                            cell.clt2={
                                                self.tireSelect=wheelData[1]
                                            }
                                        }else{
                                            cell.t2.setBackgroundImage(nil, for: .normal)
                                            cell.t2.setTitle("", for: .normal)
                                        }
                                        return cell
                                    }
                                })
            tireSelect=adapterView.startIndext
        }
        adapter?.notifyDataSetChange()
    }

    public func toUnset(){
        for i in adapterView.startIndext...adapterView.sensorCount{
            if(carItem.sensor.filter({$0.wh == "\(i)"}).count==0){
                self.selectIndex=i
                updateCar()
                return
            }
        }
        selectIndex=adapterView.startIndext
        updateCar()
        fp.keyinView.isHidden=true
        fp.twoView.isHidden=true
        fp.menu.isHidden=false
    }
}
