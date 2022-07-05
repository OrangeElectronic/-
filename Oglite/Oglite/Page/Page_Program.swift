//
//  Page_Program.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/2/24.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzOsTool
import JzOsFrameWork
class Page_Program: UIViewController,UITextFieldDelegate {
    
    @IBOutlet var nextbt: UIButton!
    @IBOutlet var quality: UILabel!
    @IBOutlet var tit: UILabel!
    @IBOutlet var numbercount: JzTextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        numbercount.digits="1234"
        numbercount.textCount=1
        tit.text="\(PublicBeans.Make)/\(PublicBeans.Model)/\(PublicBeans.Year)"
        numbercount.delegate=self
        numbercount.textWillChange = { [self]
            a in
            if(a.isEmpty){
                self.nextbt.layer.opacity=0.8
            }else{
                self.nextbt.layer.opacity=1.0
            }
        }
    }
    override func viewWillAppear(_ animated: Bool) {
        nextbt.setTitle("jz.145".getFix(), for: .normal)
        quality.text="jz.24".getFix()
    }
    func textFieldDidEndEditing(_ textField: UITextField) {
        NSLog("textFieldDidEndEditing")
        if(numbercount.text!.isEmpty){
            nextbt.layer.opacity=0.8
        }else{
            nextbt.layer.opacity=1.0
        }
    
    }
    func textFieldShouldEndEditing(_ textField: UITextField) {
        NSLog("textFieldDidEndEditing")
        if(numbercount.text!.isEmpty){
            nextbt.layer.opacity=0.8
        }else{
            nextbt.layer.opacity=1.0
        }
    
    }
    @IBAction func next(_ sender: Any) {
        if(numbercount.text!.isEmpty){
            return
        }else{
            PublicBeans.燒錄數量=Int(numbercount.text!)!
        }
        JzActivity.getControlInstance.changePage(Page_Program_Detail(), "Page_Program_Detail", true)
    }
    
    @IBAction func goMenu(_ sender: Any) {
    JzActivity.getControlInstance.goMenu()
    }
}
