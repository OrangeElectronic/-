//
//  Registration.swift
//  txusb
//
//  Created by 王建智 on 2019/8/23.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Registration: UIViewController,UIPickerViewDelegate,UIPickerViewDataSource {
     let act=(UIApplication.shared.delegate as! AppDelegate).act!
    var run=false
    var place=0
    @IBOutlet var picker: UIPickerView!
    var item=["EU","North America","台灣","中國大陸"]
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
        if(place==0){
            country.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
            picker.isHidden=true
        }else{
Distribut.setTitle(item[pickerView.selectedRow(inComponent: 0)], for: .normal)
            picker.isHidden=true
        }
        print(pickerView.selectedRow(inComponent: 0))


    }
    
    @IBOutlet var StoreL: UILabel!
    @IBOutlet var Distribut: UIButton!
    @IBOutlet var admin: UITextField!
    @IBOutlet var content: UIView!
    @IBOutlet var zpcode: UITextField!
    @IBOutlet var state: UITextField!
    @IBOutlet var city: UITextField!
    @IBOutlet var streat: UITextField!
    @IBOutlet var country: UIButton!
    @IBOutlet var phonenumber: UITextField!
    @IBOutlet var company: UITextField!
    @IBOutlet var lastname: UITextField!
    @IBOutlet var firstname: UITextField!
    @IBOutlet var SN: UITextField!
    @IBOutlet var repeatpassword: UITextField!
    @IBOutlet var password: UITextField!
    @IBOutlet var scroll: UIScrollView!
    @IBOutlet var l2: UILabel!
    @IBOutlet var l17: UILabel!
    @IBOutlet var l16: UILabel!
    @IBOutlet var l15: UILabel!
    @IBOutlet var l14: UILabel!
    @IBOutlet var l13: UILabel!
    @IBOutlet var l12: UILabel!
    @IBOutlet var l11: UILabel!
    @IBOutlet var l10: UILabel!
    @IBOutlet var l9: UILabel!
    @IBOutlet var l8: UILabel!
    @IBOutlet var l7: UILabel!
    @IBOutlet var l6: UILabel!
    @IBOutlet var l5: UILabel!
    @IBOutlet var l4: UILabel!
    @IBOutlet var l3: UILabel!
    @IBOutlet var l1: UILabel!
    
    @IBOutlet var sin: UIButton!
    @IBOutlet var can: UIButton!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
       StoreL.text=SetLan.Setlan("Store_type")
        l1.text=SetLan.Setlan("Create_your_email_account")
        l2.text=SetLan.Setlan("E_mail")
        l3.text=SetLan.Setlan("Password")
        l4.text=SetLan.Setlan("Choose_a_password")
        l5.text=SetLan.Setlan("Repeat_password")
        l6.text=SetLan.Setlan("Product_serial_number")
        l7.text=SetLan.Setlan("personal_details")
        l8.text=SetLan.Setlan("First_Name")
        l9.text=SetLan.Setlan("Last_Name")
        l10.text=SetLan.Setlan("Company")
        l11.text=SetLan.Setlan("Contact_Phone_Number")
        l12.text=SetLan.Setlan("Physical_address_Mailing_address")
        l13.text=SetLan.Setlan("Country")
        l14.text=SetLan.Setlan("Street")
        l15.text=SetLan.Setlan("City")
        l16.text=SetLan.Setlan("State")
        l17.text=SetLan.Setlan("ZP_Code")
      
    }
    override func viewDidAppear(_ animated: Bool) {
        can.setTitle(SetLan.Setlan("cancel"), for: .normal)
        sin.setTitle(SetLan.Setlan("Sign_in"), for: .normal)
    }

    @IBAction func selectcon(_ sender: Any) {
        place=0
        item=["EU","North America","台灣","中國大陸"]
        picker.reloadAllComponents()
        picker.isHidden=false
    }
    
    @IBAction func close(_ sender: Any) {
   act.GoBack(self)
    }
    
    @IBAction func next(_ sender: Any) {
        if(run){return}
        let email=admin.text!
        let pass=password.text!
        let repeatpass=repeatpassword.text!
        let sn=SN.text!
        let fname=firstname.text!
        let lname=lastname.text!
        let com=company.text!
        let ph=phonenumber.text!
        let str=streat.text!
        let ct=city.text!
        let state=country.titleLabel!.text!
        let zpcod=zpcode.text!
        if(email.count==0){return}
                if(sn.count==0){return}
                if(fname.count==0){return}
                if(lname.count==0){return}
                if(com.count==0){return}
                if(ph.count==0){return}
                if(str.count==0){return}
                if(ct.count==0){return}
          if(state.count==0){return}
          if(zpcod.count==0){return}
         if(pass.count<8){
            act.view.showToast(text: SetLan.Setlan("Password")+SetLan.Setlan("At_least_8_characters"))
            return
        }
        if(pass != repeatpass){
            act.view.showToast(text: "confirm_password")
            return
        }
        act.LoadIng(SetLan.Setlan("Data_Loading"))
        run=true
        if(Distribut.titleLabel!.text==SetLan.Setlan("Distributor")){
            Function.Register(email,pass,sn,"Distributor",com,fname,lname,ph,state,ct,str,zpcod, self)
        }else{
            Function.Register(email,pass,sn,"Retailer",com,fname,lname,ph,state,ct,str,zpcod, self)
        }

    }
    
    @IBAction func SelectStore(_ sender: Any) {
        place=1
        item=[SetLan.Setlan("Distributor"),SetLan.Setlan("Retailer")]
        picker.reloadAllComponents()
        picker.isHidden=false
    }
}
