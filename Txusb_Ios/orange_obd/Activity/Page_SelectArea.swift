//
//  Page_SelectArea.swift
//  txusb
//
//  Created by Jianzhi.wang on 2020/2/25.
//  Copyright © 2020 王建智. All rights reserved.
//

import UIKit
import JzIos_Framework
//class Page_SelectArea: UIViewController {
//    var page=0
//    @IBOutlet var area: UILabel!
//    let act = (UIApplication.shared.delegate as! AppDelegate).act!
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        area.text=SetLan.Setlan("to_give")
//    }
//
//    @IBAction func eu(_ sender: Any) {
//
//          if(JzActivity.getControlInstance.getPro("Area", "US") != "EU"){JzActivity.getControlInstance.setPro("dataloading", "false")}
//        JzActivity.getControlInstance.setPro("Area", "EU")
//        let a=peacedefine().LanguageSetting
//        if(page != 1){a.page=1}
//        act.ChangePage(to: a)
//    }
//
//    @IBAction func us(_ sender: Any) {
//        if(JzActivity.getControlInstance.getPro("Area", "US") != "US"){JzActivity.getControlInstance.setPro("dataloading", "false")}
//         JzActivity.getControlInstance.setPro("Area", "US")
//        let a=peacedefine().LanguageSetting
//        if(page != 1){a.page=1}
//        act.ChangePage(to: a)
//    }
//
//
//}

import UIKit
import JzIos_Framework
class Page_SelectArea: UIViewController,UIPickerViewDelegate,UIPickerViewDataSource {
    @IBOutlet  var areabt: UIButton!
    @IBOutlet  var area: UILabel!
    @IBOutlet  var setupbt: UIButton!
    let act = (UIApplication.shared.delegate as! AppDelegate).act!
    var page=0
    var place=0
    var focus=0
    var Setting=false
    var item=["EU","North America","台灣","中國大陸"]
    var picker=UIPickerView()
    public func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    public func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return item.count
    }
    
    public func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        print("data")
        return item[row]
    }
    public func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        print(pickerView.selectedRow(inComponent: 0))
        areabt.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
        focus=pickerView.selectedRow(inComponent: 0)
        viewDidLoad()
        picker.removeFromSuperview()
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let pan = UITapGestureRecognizer(target:self,action:#selector(tap))
        view.addGestureRecognizer(pan)
        area.text=SetLan.Setlan("to_give")
    }
    @objc func tap(){
        print("click")
        picker.removeFromSuperview()
    }
    @IBAction func selectarea(_ sender: Any) {
        item=["EU","North America","台灣","中國大陸"]
        place=0
        picker.backgroundColor = UIColor(named: "gray")
        picker.frame=CGRect(x: 0,y: view.frame.maxY-200,width: view.frame.width,height: 200)
        view.addSubview(picker)
        picker.didMoveToSuperview()
        picker.delegate = self as UIPickerViewDelegate
        picker.dataSource = self as UIPickerViewDataSource
        picker.reloadAllComponents()
    }
    @IBAction func next(_ sender: Any) {
        let area=["EU","US","TW","TW"][focus]
        if(JzActivity.getControlInstance.getPro("Area", "EU") != area){
            FileJsonVersion().storeLocal()
        }
        JzActivity.getControlInstance.setPro("Area", area)
        let a=peacedefine().LanguageSetting
        if(page != 1){a.page=1}
        act.ChangePage(to: a)
        
    }
}

