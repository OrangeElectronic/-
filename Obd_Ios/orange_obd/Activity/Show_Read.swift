//
//  Show_Read.swift
//  orange_obd
//
//  Created by 王建智 on 2019/10/31.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3

class Show_Read: UIViewController {
    let deledate = UIApplication.shared.delegate as! AppDelegate
    @IBOutlet var SPID: UITextField!
    @IBOutlet var RRID: UITextField!
    @IBOutlet var LRID: UITextField!
    @IBOutlet var RFID: UITextField!
    @IBOutlet var LFID: UITextField!
    
    @IBOutlet var toptext: UILabel!
    @IBOutlet var Menu: UIButton!
    var idcount=8
    var s19="TO001"
    var tire="04"
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    override func viewDidLoad() {
        super.viewDidLoad()
        Menu.setTitle(SetLan.Setlan("MENU"), for: .normal)
        toptext.text="\(act.Selectmake)/\(act.Selectmodel)/\(act.Selectyear)"

        query()
        DownS19()
    }
    func DownS19(){
        act.LoadIng(SetLan.Setlan("Data_Loading"))
        self.act.back.isEnabled=false
        DispatchQueue.global().async {
            if(!self.act.command.HandShake()){self.act.command.reboot()}
            if(FtpManage().DownS19(self.s19)){
                if(self.act.command.AskVersion()&&self.act.command.AppVersion == self.act.command.bytesToHex([UInt8](FtpManage.Downs19.data(using: .utf8)!))){
                    if(self.act.command.GoApp()){
                        DispatchQueue.main.async {
                            self.SetId()
                            self.act.back.isEnabled=true
                        }
                        return
                    }
                }
                if(!self.act.command.WriteVersion() || !self.act.command.GoBootloader()){
                    DispatchQueue.main.async {
                        self.act.unpair()
                        self.act.back.isEnabled=true
                        self.act.SwipePage(to: DialogPeace().Change_Car)
                        self.act.LoadingSuccess()
                    }
                    return
                }
                sleep(2)
                let Pro=self.act.command.writeflush(296)
                DispatchQueue.main.async{
                    self.act.back.isEnabled=true
                    if(Pro){
                        self.SetId()
                        self.act.back.isEnabled=true
                    }else{
                        self.act.SwipePage(to: DialogPeace().Change_Car)
                        self.act.LoadingSuccess()
                    }
                    self.act.LoadingSuccess()
                }
            }else{
                DispatchQueue.main.async {
                    self.act.LoadingSuccess()
                    let a=peacedefine().Reloading
                    a.position=1
                    self.act.GoScanner(a)
                }}
            sleep(1)
        }
    }
    func query(){
        if deledate.db != nil {
            let sql="select `OBD1` from `Summary table` where Make='\(act.Selectmake)' and Model='\(act.Selectmodel)' and year='\(act.Selectyear)' and `OBD1` not in('NA')   limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    s19=iids
                    print("s19:\(iids)")
                    queryid()
                    isfive()
                } }  } }
    func isfive(){
        if deledate.db != nil {
            let sql="select `Wheel_Count` from `Summary table` where `OBD1`='\(s19)' and `Wheel_Count` not in('NA') limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    if(iids=="5"){
                        SPID.isHidden=false
                        
                        tire="05"
                    }else{SPID.isHidden=true
                        tire="04"
                    }
                } }  }
    }
    func queryid(){
        if deledate.db != nil {
            let sql="select `ID_Count` from `Summary table` where `OBD1`='\(s19)' and `Product Number` not in('SP201') limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    idcount=Int(iids)!
                    print("Id:\(iids)")
                } }  } }
    func SetId(){
        act.LoadIng("讀取中")
        DispatchQueue.global().async {
            let a=self.act.command.GetId(self.tire)
            DispatchQueue.main.async {
                self.act.LoadingSuccess()
                if(a.success){
                    self.LFID.text=a.LF
                    self.RFID.text=a.RF
                    self.LRID.text=a.LR
                    self.RRID.text=a.RR
                    self.SPID.text=a.SP
                }else{
                    self.act.SwipePage(to: DialogPeace().Change_Car)
                }
            }
        }
    }
    
    @IBAction func Gomenu(_ sender: Any) {
        act.GoMenu()
    }
}
