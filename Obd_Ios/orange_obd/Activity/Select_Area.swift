//
//  Select_Area.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/9.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
class Select_Area: UIViewController,UIPickerViewDelegate,UIPickerViewDataSource{
    var place=0
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return item.count
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return item[row]
    }
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        print(pickerView.selectedRow(inComponent: 0))
        if(place==0){
            SelectAr.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
        }else{ SelectLa.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
        }
        picker.isHidden=true
        closebt.isHidden=true
    }
    
    @IBOutlet var SelectLa: UIButton!
    @IBOutlet var SelectAr: UIButton!
    @IBOutlet var closebt: UIButton!
    var item=["EU","North America","台灣","中國大陸"]
    @IBOutlet var picker: UIPickerView!
    @IBOutlet var info: UILabel!
    @IBOutlet var lan: UILabel!
    @IBOutlet var area: UILabel!
    @IBOutlet var togive: UILabel!
    
    @IBOutlet var setup: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        area.text=SetLan.Setlan("Area")
        togive.text=SetLan.Setlan("to_give")
        info.text=SetLan.Setlan("Languages_info")
        lan.text=SetLan.Setlan("Languages")
        setup.setTitle(SetLan.Setlan("Set_up"), for: .normal)
    }
    
    @IBAction func SelectLan(_ sender: Any) {
        place=1
        item=["繁體中文","简体中文","Deutsch","English","Italiano"]
        picker.reloadAllComponents()
        picker.isHidden=false
        closebt.isHidden=false
    }
    @IBAction func SelectArea(_ sender: Any) {
        place=0
        item=["EU","North America","台灣","中國大陸"]
        picker.reloadAllComponents()
        picker.isHidden=false
        closebt.isHidden=false
    }
    @IBAction func close(_ sender: Any) {
        picker.isHidden=true
        closebt.isHidden=true
    }
    var page=0
    @IBAction func Set(_ sender: Any) {
        if(SelectLa.titleLabel?.text=="Select"){
            return
        }
        if(SelectAr.titleLabel?.text=="Select"){
            return
        }
        ViewController.writeshare(SelectAr.titleLabel!.text!, "Area")
        ViewController.writeshare(SelectLa.titleLabel!.text!, "lan")
        if(page==0){
            let a=peacedefine().HomePage
            act.ChangePage(to: a)
        }else{
            let a=peacedefine().Privacy
            a.position=1
            act.ChangePage(to: a)
        }
        
    }
}
