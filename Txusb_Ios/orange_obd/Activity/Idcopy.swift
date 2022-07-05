//
//  Idcopy.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
import JzIos_Framework
class Idcopy: UIViewController,UITextFieldDelegate {
    var Scan_or_Key=0
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    @IBOutlet var lft: UITextField!
    @IBOutlet var Rft: UITextField!
    let deledate = UIApplication.shared.delegate as! AppDelegate
    @IBOutlet var Rrt: UITextField!
    @IBOutlet var Lrt: UITextField!
    @IBOutlet var OgSensorText: UILabel!
    @IBOutlet var IDtext: UIButton!
    @IBOutlet var keyin: UILabel!
    @IBOutlet var Scancode: UILabel!
    @IBOutlet var Rfbt: UIButton!
    @IBOutlet var Lfbt: UIButton!
    @IBOutlet var Rrbt: UIButton!
    @IBOutlet var Lrbt: UIButton!
    @IBOutlet var SelectKey: UIView!
    var spversion=""
    var mmynum:String!=nil
    let placeholserAttributes = [NSAttributedString.Key.foregroundColor : UIColor.white,NSAttributedString.Key.font : UIFont.systemFont(ofSize: 14)]
    @IBOutlet var Next: UIButton!
    @IBOutlet var tit: UILabel!
    @IBOutlet var toptitle: UILabel!
    var tt:String!=nil
    var first=true
    var idcount=8
    var SensorMode=""
    override func viewDidDisappear(_ animated: Bool) {
        first=false
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        IDtext.setTitle("ID COPY", for: .normal)
        OgSensorText.text=SetLan.Setlan("Key_in_the_original_sensor_ID_number")
        Scancode.text=SetLan.Setlan("Scan_Code")+"\n(For Orange Sensor)"
        keyin.text=SetLan.Setlan("keyin")
        first=true
        mmynum=PublicBeans.gets19()
        idcount=PublicBeans.getidcount()
      toptitle.text="\(PublicBeans.Make)/\(PublicBeans.Model)/\(PublicBeans.Year)"
        tit.text=SetLan.Setlan("Key_in_the_original_sensor_ID_number")
        lft.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: placeholserAttributes)
        Rft.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: placeholserAttributes)
        Lrt.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: placeholserAttributes)
        Rrt.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: placeholserAttributes)
        lft.delegate = self
        Rft.delegate = self
        Lrt.delegate = self
        Rrt.delegate = self
        dowloadmmy()
        Next.setTitle(SetLan.Setlan("Next"), for: .normal)
        spversion=PublicBeans.getSensorMode()
        if(spversion=="SP201"){
            SensorMode=SensorBean._433
        }
        if(spversion=="SP202"){
            SensorMode=SensorBean._315
        }
        
    }
    override func viewWillAppear(_ animated: Bool) {
        first=true
        UdCondition()
    }
    override func viewDidAppear(_ animated: Bool) {
        var string=lft.text
        let aSet = NSCharacterSet(charactersIn:"0123456789abcdef").inverted
        let compSepByCharInSet = string!.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        for i in [lft,Rft,Lrt,Rrt]{
            if(i?.text?.count ?? "".count>0){
                i!.background=UIImage.init(named: "icon_input_box_write")
            }else{
                i!.background=UIImage.init(named: "icon_input_box_locked")
            }
        }
    }
    func dowloadmmy(){
        SersorRecord.SersorCode_Sersion=JzActivity.getControlInstance.getPro(PublicBeans.gets19(), "nodata")
        self.act.command.mmydata=PublicBeans.getS19File()
    }
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        print(string.count)
        let aSet = NSCharacterSet(charactersIn:"0123456789abcdef").inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        if(string == numberFiltered&&(textField.text!+string).count<=idcount){
            if((textField.text!+string).count==idcount){
                textField.background=UIImage.init(named: "icon_input_box_write")
            }else{
                textField.background=UIImage.init(named: "icon_input_box_locked")
            }
            return true
        }else{
            if((textField.text!+string).count>=idcount){
                textField.background=UIImage.init(named: "icon_input_box_write")
            }else{
                textField.background=UIImage.init(named: "icon_input_box_locked")
            }
            return false
        }
    }
    
    @IBAction func next(_ sender: Any) {
        var lf=lft.text!
        var Rf=Rft.text!
        var Rr=Rrt.text!
        var lr=Lrt.text!
        if(!lft.isEnabled){lf="00000000"}
        if(!Rft.isEnabled){Rf="00000000"}
        if(!Rrt.isEnabled){Rr="00000000"}
        if(!Lrt.isEnabled){lr="00000000"}
        if(lf.count<idcount){
            view.showToast(text: SetLan.Setlan("ID_code_should_be_8_characters").replace("8", "\(idcount)"))
            return
        }
        if(Rf.count<idcount){
            view.showToast(text: SetLan.Setlan("ID_code_should_be_8_characters").replace("8", "\(idcount)"))
            return
        }
        if(lr.count<idcount){
            view.showToast(text: SetLan.Setlan("ID_code_should_be_8_characters").replace("8", "\(idcount)"))
            return
        }
        if(Rr.count<idcount){
            view.showToast(text: SetLan.Setlan("ID_code_should_be_8_characters").replace("8", "\(idcount)"))
            return
        }
        let a=peacedefine().Program
        a.WriteRR=insert(Rrt.text!)
        a.WriteLf=insert(lft.text!)
        a.WriteLr=insert(Lrt.text!)
        a.WriteRf=insert(Rft.text!)
        act.ChangePage(to: a)
    }
   
 
    
    func insert(_ a:String)->String{
        var tmp=a
        while(tmp.count<8){
            tmp="0"+tmp
        }
        if(mmynum=="RN1628"||mmynum=="SI2048"){
            let Writetmp=tmp.sub(0..<2)+"XX"+tmp.sub(4..<6)+"YY"
            return Writetmp.replace("XX", tmp.sub(6..<8)).replace("YY",tmp.sub(2..<4))
        }
        
        return tmp.uppercased()
    }
    
    @IBAction func toprogram(_ sender: Any) {
        PadSelect.Function=1
        JzActivity.getControlInstance.goBack("PadSelect")
    }
    var run=false
    func UdCondition(){
        if(run){return}
        run=true
        DispatchQueue.global().async {
            for i in 0...1{
                let CH1=self.act.command.Command11(i, 1,self.SensorMode)
                let CH2=self.act.command.Command11(i, 2,self.SensorMode)
                DispatchQueue.main.async {
                    if(CH1.result){
                        if(i==0){
                            self.lft.isEnabled=true
                            if(self.Scan_or_Key==0){self.Lfbt.isHidden=false}
                            self.lft.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: self.placeholserAttributes)
                        }else{
                            self.Rft.isEnabled=true
                            if(self.Scan_or_Key==0){self.Rfbt.isHidden=false}
                            self.Rft.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: self.placeholserAttributes)
                        }
                    }else{
                        if(i==0){
                            self.lft.isEnabled=false
                            self.lft.text=""
                            if(self.Scan_or_Key==0){self.Lfbt.isHidden=true}
                            self.lft.attributedPlaceholder = NSAttributedString(string: SetLan.Setlan("Unlinked"),attributes: self.placeholserAttributes)
                        }else{
                            self.Rft.isEnabled=false
                            self.Rft.text=""
                            if(self.Scan_or_Key==0){self.Rfbt.isHidden=true}
                            self.Rft.attributedPlaceholder = NSAttributedString(string: SetLan.Setlan("Unlinked"),attributes: self.placeholserAttributes)
                        }
                    }
                    if(CH2.result){
                        if(i==0){
                            self.Lrt.isEnabled=true
                            if(self.Scan_or_Key==0){self.Lrbt.isHidden=false}
                            self.Lrt.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: self.placeholserAttributes)
                        }else{
                            self.Rrt.isEnabled=true
                            if(self.Scan_or_Key==0){self.Rrbt.isHidden=false}
                            self.Rrt.attributedPlaceholder = NSAttributedString(string: "Original sensor ID",attributes: self.placeholserAttributes)
                        }
                    }else{
                        if(i==0){
                            self.Lrt.isEnabled=false
                            if(self.Scan_or_Key==0){self.Lrbt.isHidden=true}
                            self.Lrt.text=""
                            self.Lrt.attributedPlaceholder = NSAttributedString(string: SetLan.Setlan("Unlinked"),attributes: self.placeholserAttributes)
                        }else{
                            self.Rrt.isEnabled=false
                            self.Rrt.text=""
                            if(self.Scan_or_Key==0){self.Rrbt.isHidden=true}
                            self.Rrt.attributedPlaceholder = NSAttributedString(string: SetLan.Setlan("Unlinked"),attributes: self.placeholserAttributes)
                        }
                    }
                }
            }
            sleep(4)
            self.run=false
            if(self.first){
                self.UdCondition()}
        }
    }
    
    @IBAction func Scanner(_ sender: Any) {
        SelectKey.isHidden=true
        tit.text=SetLan.Setlan("checkscan")
        Scan_or_Key=0
    }
    
    @IBAction func Keyin(_ sender: Any) {
        SelectKey.isHidden=true
        Scan_or_Key=1
        Lrbt.isHidden=true
        Rrbt.isHidden=true
        Lfbt.isHidden=true
        Rfbt.isHidden=true
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
    func toscanner(_ edit:UITextField) {
        let a=peacedefine().QrScanner
        a.idcopy=self
        a.VS_or_ID=1
        a.idcount=idcount
        a.editext=edit
        self.act.ChangePage(to: a)
    }
}
