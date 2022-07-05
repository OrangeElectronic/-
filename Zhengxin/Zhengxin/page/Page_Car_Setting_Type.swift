//
//  Page_Car_Setting_Type.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit
import JzOsFrameWork
import JzOsAdapter
//首頁選項Struct
public struct SettingItemClass {
    var name:String
    var image:[String]
    var click:()->Void
}
class Page_Car_Setting_Type: UIViewController {
    
    @IBOutlet var tb: UITableView!
    
    var item=[SettingItemClass(name: "4輪",image: ["icon_4_wheels"],click: {
        
    }),SettingItemClass(name: "6輪",image: ["icon_6_wheels"],click: {
        
    }),SettingItemClass(name: "8輪(242)",image: ["icon_8wheels242"],click: {
        
    }),SettingItemClass(name: "8輪(224)",image: ["icon_8_wheels224"],click: {
        
    }),SettingItemClass(name: "10輪(244)",image: ["icon_10_wheels244"],click: {
        
    }),SettingItemClass(name: "10輪(2224)",image: ["icon_10_wheels_2224"],click: {
        
    }),SettingItemClass(name: "12輪",image: ["icon_12_wheels"],click: {
        
    }),SettingItemClass(name: "14輪",image: ["icon_14wheels"],click: {
        
    }),SettingItemClass(name: "6輪+4輪",image: ["icon_6_wheels","icon_pallet_truck_4wheel_rear"],click: {
        
    }),SettingItemClass(name: "6輪+8輪",image: ["icon_6_wheels","icon_pallet_truck_8wheel"],click: {
        
    }),SettingItemClass(name: "6輪+10輪",image: ["icon_6_wheels","icon_pallet_truck_10wheel"],click: {
        
    }),SettingItemClass(name: "6輪+12輪",image:
                            ["icon_6_wheels","icon_pallet_truck_12wheel"]                    ,click: {
                                
                            }),SettingItemClass(name: "6輪+16輪",image: ["icon_6_wheels","icon_pallet_truck_16wheel"],click: {
                                
                            }),SettingItemClass(name: "6輪+20輪",image: ["icon_6_wheels","icon_pallet_truck_20wheel"],click: {
                                
                            }),SettingItemClass(name: "10輪+8輪",image: ["icon_10_wheels_2224","icon_pallet_truck_8wheel"],click: {
                                
                            }),SettingItemClass(name: "10輪+10輪",image: ["icon_10_wheels_2224","icon_pallet_truck_10wheel"],click: {
                                
                            }),SettingItemClass(name: "10輪+12輪",image: ["icon_10_wheels_2224","icon_pallet_truck_12wheel"],click: {
                                
                            }),SettingItemClass(name: "10輪+16輪",image: ["icon_10_wheels_2224","icon_pallet_truck_16wheel"],click: {
                                
                            }),SettingItemClass(name: "10輪+20輪",image: ["icon_10_wheels_2224","icon_pallet_truck_20wheel"],click: {
                                
                            })]
    
    //選單Adapter
    lazy var adapter:LinearAdapter? = nil
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    override func viewDidAppear(_ animated: Bool) {
        if(adapter==nil){
            adapter = LinearAdapter(tb: tb, count: {
                return self.item.count
            }, nib: ["Cell_CarType_Select"], getcell: {
                a,b,c in
                let cell=a.dequeueReusableCell(withIdentifier: "Cell_CarType_Select", for: b) as! Cell_CarType_Select
                let it=self.item[c]
                cell.img.isHidden=it.image.count==1
                cell.plus.isHidden=it.image.count==1
                cell.front.image=UIImage(named: it.image[0])
                if(it.image.count==2){
                    cell.img.image=UIImage(named: it.image[1])
                }
                cell.tit.text=it.name
                let bv=UIView()
                bv.backgroundColor=#colorLiteral(red: 0.4156862745, green: 0.7764705882, blue: 0.8431372549, alpha: 1)
                cell.selectedBackgroundView =
                    bv
                return cell
            }, {a in
                PublicBeans.selectCar.carType=["4","6","8_242","8_224"
                ,"10_244","10_2224","12","14","6+4","6+8","6+10","6+12","6+16","6+20","10+8","10+10","10+12","10+16","10+20"][a]
                PublicBeans.selectCar.count=["4","6","8","8","10","10","12","14","10","14","16","18","22","26","18","20","22","26","30"][a]
                PublicBeans.selectCar.setSettingCar(callback: {
                    JzActivity.getControlInstance.changePage(Page_Car_Setting_Detail(), "Page_Car_Setting_Detail", true)
                    return 1
                })
              
                
            })
        }
        adapter?.notifyDataSetChange()
    }
    
    func toPage(){
        
    }
    
}
