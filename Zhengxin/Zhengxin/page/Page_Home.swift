//
//  Page_Home.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/2.
//

import UIKit
import JzOsAdapter
import JzOsFrameWork

//首頁選項Struct
public struct ItemClass {
    var name:String
    var image:String
    var click:()->Void
}
class Page_Home: UIViewController {
    @IBOutlet var tb: UITableView!
    
    @IBOutlet var signUser: UILabel!
    @IBOutlet var location: UILabel!
    //所有首頁選項
    var item=[ItemClass(name: "檢測輪胎壓力值",image: "btn_check_tpms",click: {
        JzActivity.getControlInstance.changePage(Page_Select_Car(),"Page_Select_Car",true)
    }),
    ItemClass(name: "車輛輪胎設定",image: "btn_tire_setting",click: {
        JzActivity.getControlInstance.changePage(Page_Car_Setting_Select(),"Page_Car_Setting_Select",true)
    }),
    ItemClass(name: "胎壓檢測紀錄",image: "btn_export_data",click: {
        JzActivity.getControlInstance.changePage(WebViewController(), "btn_export_data", true)
    }),
    ItemClass(name: "系統設定",image: "btn_setting_system",click: {
        JzActivity.getControlInstance.changePage(Page_Setting_All(), "Page_Setting_All", true)
    })]
    //選單Adapter
    lazy var adapter:GridAdapter? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        LocarionManager.manager.haveLocation()
        signUser.text="登入者(駕駛)\(Md_User_Setting.getUserSetting().name.unicodeStr())"
    }
    
    override func viewWillDisappear(_ animated: Bool) {
       
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if(adapter==nil){
            adapter = GridAdapter(tb: tb, width:tb.bounds.width/2, height:tb.bounds.height/2, count: {return self.item.count}, spilt: 2, nib: ["Cell_Home_Item"], getcell: {
                a,b,c in
                let cell=a.dequeueReusableCell(withReuseIdentifier: "Cell_Home_Item", for: b) as! Cell_Home_Item
                cell.img.image=UIImage(named: self.item[c].image)
                cell.tit.text=self.item[c].name
                cell.clickVoid=self.item[c].click
                return cell
            })
        }
        adapter?.notifyDataSetChange()
       
    }
    override func viewWillAppear(_ animated: Bool) {
        location.text=LocarionManager.manager.lastKnownLocation.address
        checkUpdate()
    }
    //更新檢查
    func checkUpdate(){
        let cc = DonloadFile()
        if(cc.needInit()){
            "資料更新中...".progress()
            DonloadFile().dataloading({
                a in
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.closeDialLog()
                    if(!a){
                        JzActivity.getControlInstance.openDiaLog(Dia_Update(), false, "Dia_Update")
                    }
                }
            })
        }else if(cc.needUpdate()){
            JzActivity.getControlInstance.openDiaLog(Dia_Update(), false, "Dia_Update")
        }
    }
    
   
}
