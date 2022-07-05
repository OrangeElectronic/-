//
//  Relarm.swift
//  txusb
//
//  Created by 王建智 on 2019/8/7.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
import JzIos_Framework
class Relarm: UIViewController {
    let deledate = UIApplication.shared.delegate as! AppDelegate
    let activity=(UIApplication.shared.delegate as! AppDelegate).act!
    var position=0
    @IBOutlet var relarlmtext: UITextView!
    @IBOutlet var menu: UIButton!
    @IBOutlet var toper: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        toper.text="\(PublicBeans.Make)/\(PublicBeans.Model)/\(PublicBeans.Year)"
        if(position==0){    menu.setTitle(SetLan.Setlan("MENU"), for: .normal)}else{    menu.setTitle(SetLan.Setlan("Next"), for: .normal)}
        query()
    }
    
    @IBAction func Gomenu(_ sender: Any) {
        if(position==0){
            let a=peacedefine().HomePage
            activity.ChangePage(to: a)
        }else{
            if(PadSelect.Function==0){
                let a=peacedefine().Idcopy
                activity.ChangePage(to: a)
            }else{
                let a=peacedefine().Program
                activity.ChangePage(to: a)
            }
        }
    }
    
    func query(){
        let paraph = NSMutableParagraphStyle()
        paraph.lineSpacing = 10
        let attributes = [NSAttributedString.Key.font:UIFont.systemFont(ofSize: 18),
                          NSAttributedString.Key.paragraphStyle: paraph]
        if(PublicBeans.地區=="TW"){
            relarlmtext.attributedText = NSAttributedString(string: "OE Part# :\n\(PublicBeans.queryOE())\n\nFor OrangeSensor:\n\(PublicBeans.getSensorMode()) USB\n\nRelearn:\n\(PublicBeans.getRelearm())", attributes: attributes)
        }else{
            relarlmtext.attributedText = NSAttributedString(string: "OE Part# :\n\(PublicBeans.queryOE())\n\nFor OrangeSensor:\n\(PublicBeans.gets19())\((PublicBeans.地區=="EU" ? "":"/\(PublicBeans.getSensorMode()) USB"))\n\nRelearn:\n\(PublicBeans.getRelearm())", attributes: attributes)
        }
        
        relarlmtext.isEditable=false
        
    }
    
    
}
