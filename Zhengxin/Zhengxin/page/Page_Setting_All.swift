//
//  Page_Setting_All.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit
import JzOsAdapter
import JzOsFrameWork
class Page_Setting_All: UIViewController {
    @IBOutlet var tb: UITableView!
    struct settingItem {
        var name:String
        var callback:()->Void
    }
    
    var tit=[settingItem(name: "車輛清單", callback:{
        JzActivity.getControlInstance.changePage(Page_Car_List(), "Page_Car_List", true)
    }),settingItem(name: "警報設定(胎壓/胎溫)", callback:{
        JzActivity.getControlInstance.changePage(Page_Setting_Advice(), "Page_Setting_Advice",true)
    }),settingItem(name: "單位", callback:{
        JzActivity.getControlInstance.changePage(Page_Setting_Unit(), "Page_Setting_Unit", true)
    }),settingItem(name: "登出", callback:{
        let controller = UIAlertController(title: "訊息", message: "確認登出??", preferredStyle: .alert)
        let cancelAction = UIAlertAction(title: "取消", style: .default){ (_) in
        }
        controller.addAction(cancelAction)
        let okAction = UIAlertAction(title: "確認", style: .default) { (_) in
            JzActivity.getControlInstance.closeApp()
        }
        controller.addAction(okAction)
        JzActivity.getControlInstance.getActivity().present(controller, animated: true, completion: nil)
    })]
    //後續添加
    //    settingItem(name: "語言", callback:{}),settingItem(name: "關於", callback:{}),settingItem(name: "修改密碼", callback:{})
    
    lazy var adapter=LinearAdapter(tb: tb, count: {
        //return your item count
        return self.tit.count
    }, nib: ["Cell_Setting_Item"], getcell: {
        tableview,indexpath,position in
        let cell=tableview.dequeueReusableCell(withIdentifier: "Cell_Setting_Item") as! Cell_Setting_Item
        cell.tit.text=self.tit[position].name
        let bv=UIView()
        bv.backgroundColor=#colorLiteral(red: 0.4156862745, green: 0.7764705882, blue: 0.8431372549, alpha: 1)
        cell.contentView.addSubview(bv)
        return cell
    }, {
        clickPosition in
        self.tit[clickPosition].callback()
    })
    override func viewDidLoad() {
        super.viewDidLoad()
        adapter.notifyDataSetChange()
    }
}
