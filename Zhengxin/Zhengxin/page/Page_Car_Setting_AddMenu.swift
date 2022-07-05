//
//  Page_Car_Setting_AddMenu.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/5.
//

import UIKit
import JzOsFrameWork
import JzOsAdapter
class Page_Car_Setting_AddMenu: UIViewController {
    
    @IBOutlet var tb: UITableView!
    var item=[ItemClass(name: "手動輸入資料",image: "btn_enter_data",click: {
        JzActivity.getControlInstance.changePage(Page_Add_Cae_PlateNumber(),"Page_Add_Cae_PlateNumber",true)
    }),
    ItemClass(name: "匯入資料",image: "btn_import_data",click: {
        PublicBeans.getQrCode({
            a in
            let decoder: JSONDecoder = JSONDecoder()
            let decoded = try? decoder.decode(Data_Setting_Car.self, from: a.data(using: .utf16)!)
            print("data==\(a)")
            if(decoded == nil){
                JzActivity.getControlInstance.toast("請掃描正確的QRCODE!!")
            }else{
                decoded!.setSettingCar(callback: {
                    JzActivity.getControlInstance.toast("資料匯入成功!!")
                    JzActivity.getControlInstance.goMenu()
                    return 1
                })
            }
        })
    })]
    
    //選單Adapter
    lazy var adapter:GridAdapter? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        if(adapter==nil){
            adapter = GridAdapter(tb: tb, width:tb.bounds.width, height:tb.bounds.height/2, count: {return self.item.count}, spilt: 1, nib: ["Cell_Home_Item"], getcell: {
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
    
}
