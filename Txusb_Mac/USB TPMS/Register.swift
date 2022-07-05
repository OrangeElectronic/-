//
//  Register.swift
//  USB TPMS
//
//  Created by 王建智 on 2019/11/19.
//  Copyright © 2019 王建智. All rights reserved.
//

import Cocoa

class Register: NSViewController {
    
    @IBOutlet var email: NSTextField!
    @IBOutlet var l5: NSTextField!
    @IBOutlet var l18: NSTextField!
    @IBOutlet var l17: NSTextField!
    @IBOutlet var l16: NSTextField!
    @IBOutlet var l15: NSTextField!
    @IBOutlet var l14: NSTextField!
    @IBOutlet var l13: NSTextField!
    @IBOutlet var l12: NSTextField!
    @IBOutlet var l11: NSTextField!
    @IBOutlet var l10: NSTextField!
    @IBOutlet var l9: NSTextField!
    @IBOutlet var l8: NSTextField!
    @IBOutlet var l7: NSTextField!
    @IBOutlet var l6: NSTextField!
    @IBOutlet var l4: NSTextField!
    @IBOutlet var l3: NSTextField!
    @IBOutlet var l2: NSTextField!
    @IBOutlet var l1: NSTextField!
    @IBOutlet var state: NSTextField!
    @IBOutlet var city: NSTextField!
    @IBOutlet var streat: NSTextField!
    @IBOutlet var zpcode: NSTextField!
    @IBOutlet var serialnumber: NSTextField!
    @IBOutlet var telephone: NSTextField!
    @IBOutlet var company: NSTextField!
    @IBOutlet var lastname: NSTextField!
    @IBOutlet var firstname: NSTextField!
    @IBOutlet var passwordconfirm: NSSecureTextField!
    @IBOutlet var passwoed: NSSecureTextField!
    @IBOutlet var account: NSTextFieldCell!
    var enroll:Enroll!
    @IBOutlet var enterbt: NSButton!
    @IBOutlet var cancelbt: NSButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        refresh()
    }
    func SetBtText(_ text:String,_ bt:NSButton,_ center:Bool){
        let pstyle = NSMutableParagraphStyle()
        if(center){
            pstyle.alignment = .center
        }else{
            pstyle.alignment = .left
            pstyle.firstLineHeadIndent = 20
        }
        bt.attributedTitle = NSAttributedString(string: text, attributes: [ NSAttributedString.Key.foregroundColor : NSColor.white, NSAttributedString.Key.paragraphStyle : pstyle ])
    }
    func refresh(){
        SetBtText(Language.SetLanguAge(77),enterbt,true)
        SetBtText(Language.SetLanguAge(78),cancelbt,true)
        l1.stringValue=Language.SetLanguAge(57)
        l2.stringValue=Language.SetLanguAge(58)
        l3.stringValue=Language.SetLanguAge(60)
        l4.stringValue=Language.SetLanguAge(61)
        l5.stringValue=Language.SetLanguAge(62)
        l6.stringValue=Language.SetLanguAge(63)
        l7.stringValue=Language.SetLanguAge(64)
        l8.stringValue=Language.SetLanguAge(65)
        l9.stringValue=Language.SetLanguAge(66)
        l10.stringValue=Language.SetLanguAge(67)
        l11.stringValue=Language.SetLanguAge(68)
        l13.stringValue=Language.SetLanguAge(69)
        l14.stringValue=Language.SetLanguAge(70)
        l15.stringValue=Language.SetLanguAge(71)
        l16.stringValue=Language.SetLanguAge(72)
        l17.stringValue=Language.SetLanguAge(73)
        l18.stringValue=Language.SetLanguAge(74)
    }
    
    @IBAction func pk(_ sender: Any) {
        Function.Register(email.stringValue, passwoed.stringValue, serialnumber.stringValue, "Distributor", company.stringValue, firstname.stringValue, lastname.stringValue, telephone.stringValue, state.stringValue, city.stringValue, streat.stringValue, zpcode.stringValue, self)
    }
    @IBAction func cancel(_ sender: Any) {
        enroll.main.view=enroll.view
        
    }
}
