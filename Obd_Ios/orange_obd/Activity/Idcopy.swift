//
//  Idcopy.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
class Idcopy: UIViewController,UITextFieldDelegate {
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    let deledate = UIApplication.shared.delegate as! AppDelegate
    @IBOutlet var lft: UITextField!
    @IBOutlet var Rft: UITextField!
    var s19="TO001"
    var Scan_or_Key=1
    var tirecount=4
    @IBOutlet var spare: UIView!
    @IBOutlet var KeyInlabel: UILabel!
    @IBOutlet var ScanLabel: UILabel!
    @IBOutlet var SpareTire: UIView!
    @IBOutlet var SelectText: UILabel!
    @IBOutlet var condition: UILabel!
    @IBOutlet var Spt: UITextField!
    @IBOutlet var Rrt: UITextField!
    @IBOutlet var Lrt: UITextField!
    @IBOutlet var Rfbt: UIButton!
    @IBOutlet var Lfbt: UIButton!
    @IBOutlet var Rrbt: UIButton!
    @IBOutlet var Lrbt: UIButton!
    @IBOutlet var tit: UILabel!
    @IBOutlet var Rfb: UIButton!
    @IBOutlet var Lfb: UIButton!
    @IBOutlet var Lrb: UIButton!
    @IBOutlet var Rrb: UIButton!
    @IBOutlet var Spb: UIButton!
    @IBOutlet var SelectKey: UIView!
    @IBOutlet var menu: UIButton!
    var run=false
    let PROGRAM_SUCCESS=0
    let PROGRAM_WAIT=1
    let UN_LINK=4
    let PROGRAM_FAULSE=2
    let PROGRAMMING=3
    let LF=5
    let RF=6
    let LR=7
    let RR=8
    
    @IBOutlet var toptext: UILabel!
    @IBOutlet var spi: UIImageView!
    @IBOutlet var startbt: UIButton!
    @IBOutlet var Lri: UIImageView!
    @IBOutlet var Rri: UIImageView!
    @IBOutlet var Rfi: UIImageView!
    @IBOutlet var Lfi: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()
        toptext.text="\(act.Selectmake)/\(act.Selectmodel)/\(act.Selectyear)"
        let placeholserAttributes = [NSAttributedString.Key.foregroundColor : UIColor.white,NSAttributedString.Key.font : UIFont.systemFont(ofSize: 14)]
        lft.attributedPlaceholder = NSAttributedString(string: "LF ID Number",attributes: placeholserAttributes)
        Rft.attributedPlaceholder = NSAttributedString(string: "RF ID Number",attributes: placeholserAttributes)
        Lrt.attributedPlaceholder = NSAttributedString(string: "LR ID Number",attributes: placeholserAttributes)
        Rrt.attributedPlaceholder = NSAttributedString(string: "RR ID Number",attributes: placeholserAttributes)
        Spt.attributedPlaceholder = NSAttributedString(string: "SP ID Number",attributes: placeholserAttributes)
        lft.delegate = self
        Rft.delegate = self
        Lrt.delegate = self
        Rrt.delegate = self
        Spt.delegate=self
        tit.text="\(act.Selectmake)/\(act.Selectmodel)/\(act.Selectyear)"
        startbt.isEnabled=false
        query()
        UpdateUi(PROGRAM_WAIT)
        KeyInlabel.text=SetLan.Setlan("Key_in_the_original_sensor_ID_number")
        ScanLabel.text=SetLan.Setlan("Scan_Code")
        SelectText.text=SetLan.Setlan("Select")
        startbt.setTitle(SetLan.Setlan("START"), for: .normal)
        menu.setTitle(SetLan.Setlan("MENU"), for: .normal)
        
    }
    override func viewWillAppear(_ animated: Bool) {
        
    }
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        let aSet = NSCharacterSet(charactersIn:"0123456789abcdef").inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        if(string == numberFiltered&&(textField.text!+string).count<8){
            if((textField.text!+string).count>=6){
                textField.background=UIImage.init(named: "UI_img_input box_write")
            }else{
                textField.background=UIImage.init(named: "UI_img_input box_Locked")
            }
            return true
        }else{
            if((textField.text!+string).count>=6){
                textField.background=UIImage.init(named: "UI_img_input box_write")
            }else{
                textField.background=UIImage.init(named: "UI_img_input box_Locked")
            }
            return false
        }
    }
    override func viewDidDisappear(_ animated: Bool) {
        
    }
    func AddEmpty(_ id:String)->String{
        var a=id
        while(a.count<8){
            a="0\(a)"
        }
        return a
    }
    @IBAction func Setire(_ sender: Any) {
        var wlf=lft.text!
        var wrf=Rft.text!
        var wlr=Lrt.text!
        var wrr=Rrt.text!
        var spr=Spt.text!
        if(wlf.count < 6){
            return
        }
        if(wrf.count < 6){
            return
        }
        if(wlr.count < 6){
            return
        }
        if(wrr.count < 6){
            return
        }
         startbt.isEnabled=false
        self.act.LoadIng(SetLan.Setlan("Programming"))
        var a=[String]()
        wlf=AddEmpty(wlf)
        wrf=AddEmpty(wrf)
        wlr=AddEmpty(wlr)
        wrr=AddEmpty(wrr)
         a.append(wlf)
         a.append(wrf)
         a.append(wrr)
         a.append(wlr)
        if(!spr.isEmpty){
         a.append(AddEmpty(spr))
        }
        DispatchQueue.global().async {
            let issucess=self.act.command.SetireId(a)
            DispatchQueue.main.async {
                self.act.LoadingSuccess()
                self.startbt.isEnabled=true
                if(issucess){
                    
                    self.UpdateUi(self.PROGRAM_SUCCESS)
                }else{
                    
                    self.UpdateUi(self.PROGRAM_FAULSE)
                }
            }
        }
        
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
                            self.act.LoadingSuccess()
                            self.startbt.isEnabled=true
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
                        self.startbt.isEnabled=true
                        self.act.back.isEnabled=true
                    }else{
                        self.act.SwipePage(to: DialogPeace().Change_Car)
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
    func UpdateUi(_ situation:Int){
        switch situation {
        case PROGRAM_FAULSE:
            condition.text=SetLan.Setlan("Programming_failed")
            condition.textColor=UIColor.init(named: "colorPrimaryDark")
            Lrt.background=UIImage.init(named: "UI_img_input box_fail")
            Rrt.background=UIImage.init(named: "UI_img_input box_fail")
            lft.background=UIImage.init(named: "UI_img_input box_fail")
            Rft.background=UIImage.init(named: "UI_img_input box_fail")
            Spt.background=UIImage.init(named: "UI_img_input box_fail")
            Lfi.image=UIImage.init(named: "error")
            Rfi.image=UIImage.init(named: "error")
            Rri.image=UIImage.init(named: "error")
            Lri.image=UIImage.init(named: "error")
            spi.image=UIImage.init(named: "error")
            Lrt.isEnabled=false
            Rrt.isEnabled=false
            lft.isEnabled=false
            Rft.isEnabled=false
            Spt.isEnabled=false
            menu.isHidden=true
            startbt.isHidden=false
            startbt.setTitle("Re-Program", for: .normal)
            break
        case PROGRAM_SUCCESS:
            condition.textColor=UIColor.init(named: "Color")
            condition.text=SetLan.Setlan("Programming_completed")
            Lrt.background=UIImage.init(named: "UI_img_input box_OK")
            Rrt.background=UIImage.init(named: "UI_img_input box_OK")
            lft.background=UIImage.init(named: "UI_img_input box_OK")
            Rft.background=UIImage.init(named: "UI_img_input box_OK")
            Spt.background=UIImage.init(named: "UI_img_input box_OK")
            Lfi.image=UIImage.init(named: "correct")
            Rfi.image=UIImage.init(named: "correct")
            Rri.image=UIImage.init(named: "correct")
            Lri.image=UIImage.init(named: "correct")
            spi.image=UIImage.init(named: "correct")
            Lfi.isHidden=true
            Rfi.isHidden=true
            Rri.isHidden=true
            Lri.isHidden=true
            spi.isHidden=true
            Lrt.isEnabled=true
            Rrt.isEnabled=true
            lft.isEnabled=true
            Rft.isEnabled=true
            Spt.isEnabled=true
            menu.isHidden=false
            startbt.isHidden=true
            menu.setTitle(SetLan.Setlan("Relearn_Procedure"), for: .normal)
            break
        case PROGRAM_WAIT:
            condition.text=SetLan.Setlan("Key_in_the_original_sensor_ID_number")
            condition.textColor=UIColor.init(named: "Color")
            Lrt.background=UIImage.init(named: "UI_img_input box_Locked")
            Rrt.background=UIImage.init(named: "UI_img_input box_Locked")
            lft.background=UIImage.init(named: "UI_img_input box_Locked")
            Rft.background=UIImage.init(named: "UI_img_input box_Locked")
            Spt.background=UIImage.init(named: "UI_img_input box_Locked")
            Lfi.isHidden=true
            Rfi.isHidden=true
            Rri.isHidden=true
            Lri.isHidden=true
            spi.isHidden=true
            Lrt.isEnabled=true
            Rrt.isEnabled=true
            lft.isEnabled=true
            Rft.isEnabled=true
            Spt.isEnabled=true
            menu.isHidden=true
            startbt.isHidden=false
            break
        default:
            break
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
                    print("id\(iids)")
                    if(iids=="5"){
                        spare.isHidden=false
                    }else{spare.isHidden=true}
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
                    print("Id:\(iids)")
                } }  } }
    
    @IBAction func gohome(_ sender: Any) {
        Selection.function=Selection.完畢
        let a=peacedefine().Relarm
        act.ChangePage(to: a)
    }
    @IBAction func Goscanner(_ sender: Any) {
        DownS19()
        SelectKey.isHidden=true
        Scan_or_Key=0
    }
    
    @IBAction func KeyIn(_ sender: Any) {
        DownS19()
        Scan_or_Key=1
        Rrb.isHidden=true
        Lrb.isHidden=true
        Lfb.isHidden=true
        Rfb.isHidden=true
        Spb.isHidden=true
        SelectKey.isHidden=true
    }
    func toscanner(_ edit:UITextField) {
        let a=peacedefine().QrScanner
        a.idcopy=self
        a.VS_or_ID=1
        a.editext=edit
        self.act.ChangePage(to: a)
    }
    
    @IBAction func Spaction(_ sender: Any) {
        toscanner(Spt)
    }
    @IBAction func Rfaction(_ sender: Any) {
        toscanner(Rft)
    }
    @IBAction func Lfaction(_ sender: Any) {
        toscanner(lft)
    }
    @IBAction func Rraction(_ sender: Any) {
        toscanner(Rrt)
    }
    @IBAction func Lraction(_ sender: Any) {
        toscanner(Lrt)
    }
}
