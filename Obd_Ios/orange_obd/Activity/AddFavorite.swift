//
//  AddFavorite.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/14.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
class AddFavorite: UIViewController,UIPickerViewDelegate,UIPickerViewDataSource {
     var item=[String]()
    var modle=[String]()
    var place=0
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
        if(item.count==0){return }
        switch place {
        case 0:
            Makebt.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
            break
        case 1:
            Modelbt.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
            break
        case 2:
            YearBt.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
            break
        default:
            break
        }
        pick.isHidden=true
        closebt.isHidden=true
    }
    @IBOutlet var closebt: UIButton!

    @IBOutlet var cancelbt: UIButton!
    
    @IBOutlet var SetUpBt: UIButton!
    @IBOutlet var YeatLabel: UILabel!
    @IBOutlet var ModelLabe: UILabel!
    @IBOutlet var Makelabel: UILabel!
    @IBOutlet var YearBt: UIButton!
    @IBOutlet var Modelbt: UIButton!
    @IBOutlet var Makebt: UIButton!
    @IBOutlet var pick: UIPickerView!
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    let deledate = UIApplication.shared.delegate as! AppDelegate
    override func viewDidLoad() {
        super.viewDidLoad()
Makelabel.text=SetLan.Setlan("Select_CAR_Make")
        ModelLabe.text=SetLan.Setlan("Select_CAR_Model")
        YeatLabel.text=SetLan.Setlan("Select_Year")
SetUpBt.setTitle(SetLan.Setlan("Set_up"), for: .normal)
        cancelbt.setTitle(SetLan.Setlan("cancel"), for: .normal)
        Makebt.setTitle(SetLan.Setlan("Select"), for: .normal)
        Modelbt.setTitle(SetLan.Setlan("Select"), for: .normal)
        YearBt.setTitle(SetLan.Setlan("Select"), for: .normal)
    }
    
    @IBAction func cancel(_ sender: Any) {
        self.act.GoBack(self)
    }
    
    func addmodle(){
        ViewController.writeshare("\(modle.count)", "Favcount")
        for i in 0..<modle.count{
            ViewController.writeshare(modle[i], "fav\(i)")
        }
    }
    @IBAction func yes(_ sender: Any) {
        if(Makebt.titleLabel?.text==SetLan.Setlan("Select")){return}
        if(Modelbt.titleLabel?.text==SetLan.Setlan("Select")){return}
        if(YearBt.titleLabel?.text==SetLan.Setlan("Select")){return}
modle.append("\(Makebt.titleLabel!.text!)/\(Modelbt.titleLabel!.text!)/\(YearBt.titleLabel!.text!)")
        addmodle()
        act.GoBack(self)
    }
    
    @IBAction func selectmake(_ sender: Any) {
        Modelbt.setTitle(SetLan.Setlan("Select"), for: .normal)
        YearBt.setTitle(SetLan.Setlan("Select"), for: .normal)
        place=0
        item=[String]()
         pick.reloadAllComponents()
        querymake()
        pick.reloadAllComponents()
        pick.isHidden=false
        closebt.isHidden=false
        Makebt.setTitle(item[0], for: .normal)
    }
    @IBAction func selectmodel(_ sender: Any) {
        if(Makebt.titleLabel?.text==SetLan.Setlan("Select")){return}
        YearBt.setTitle(SetLan.Setlan("Select"), for: .normal)
        place=1
        item=[String]()
         pick.reloadAllComponents()
        querymodel()
        pick.reloadAllComponents()
        pick.isHidden=false
        closebt.isHidden=false
        Modelbt.setTitle(item[0], for: .normal)
    }
    
    @IBAction func selectyear(_ sender: Any) {
        if(Makebt.titleLabel?.text==SetLan.Setlan("Select")){return}
        if(Modelbt.titleLabel?.text==SetLan.Setlan("Select")){return}
        place=2
        item=[String]()
        pick.reloadAllComponents()
        queryear()
        pick.reloadAllComponents()
        pick.isHidden=false
        closebt.isHidden=false
        YearBt.setTitle(item[0], for: .normal)
    }
    @IBAction func close(_ sender: Any) {
        pick.isHidden=true
        closebt.isHidden=true
    }
    func querymake(){
        if deledate.db != nil {
            let sql="select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA') and `OBD1` not in('NA') order by `Make` asc"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let cname = String(cString: sqlite3_column_text(statement,0))
                item.append(cname)
            }
            
        }
    }
    func querymodel(){
        if deledate.db != nil {
            let sql="select distinct model from `Summary table` where make='\(Makebt.titleLabel!.text!)' and `OBD1` not in('NA') order by model asc"
            print(sql)
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                item.append(String(cString: iid!))
//                print(iid)
            }
        }
    }
    func queryear(){
        if deledate.db != nil {
            let sql="select distinct Year from `Summary table` where model='\(Modelbt.titleLabel!.text!)' and make='\(Makebt.titleLabel!.text!)' and `OBD1` not in('NA') order by Year asc"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    item.append(String(cString: iid!))
                }
            }
          
        }
    }

}
