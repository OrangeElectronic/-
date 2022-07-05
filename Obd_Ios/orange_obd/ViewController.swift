//
//  ViewController.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/4.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import Lottie
import CoreBluetooth
class ViewController: BleActivity{
    enum SendDataError: Error {
        case CharacteristicNotFound
    }
    @IBOutlet var tit: UILabel!
    @IBOutlet var rightop: UIButton!
    let delgate=UIApplication.shared.delegate as! AppDelegate
    let deledate = UIApplication.shared.delegate as! AppDelegate
    let command=Command()
    @IBOutlet var LoadingView: UIView!
    @IBOutlet var Connectlabel: UILabel!
    let animationView = AnimationView(name: "simple-loader2")
    @IBOutlet var back: UIButton!
    @IBOutlet var Container: UIView!
    var Selectmake=""
    var Selectmodel=""
    var Selectyear=""
    var directfit=""
    override func viewDidLoad() {
        super.viewDidLoad()
        let queue = DispatchQueue.global()
        centralManager = CBCentralManager(delegate: self, queue: queue)
        deledate.act=self
        rootView=Container
        if(ViewController.getShare("admin")=="nodata"){
            let a=peacedefine().Select_Area
            a.page=1
            ChangePage(to: a)
        }else{
            let a=peacedefine().HomePage
            ChangePage(to: a)
        }
        animationView.center = self.view.center
        animationView.frame = CGRect(x: animationView.frame.minX, y: animationView.frame.minY+10, width: 200, height: 200)
        animationView.contentMode = .scaleAspectFill
        animationView.loopMode=LottieLoopMode.loop
        view.addSubview(animationView)
        dowload_mmy()
        command.act=self
        Function.GetVersion()
      
    }
    func dowload_mmy(){
        self.LoadIng(SetLan.Setlan("Data_Loading"))
        DispatchQueue.global().async {
            let res=FtpManage().DowloadMmy(self.deledate)
            DispatchQueue.main.async {
                if(res){
                    self.LoadingSuccess()
                }else{self.dowload_mmy()}
            }
        }
    }
    @IBAction func GoBack(_ sender: Any) {
        if(isloading){return}
        if(back.image(for: .normal)==UIImage.init(named: "btn_Menu")){
            back.setImage(UIImage.init(named: "btn_back_normal"), for: .normal)
            let a=peacedefine().HomePage
            ChangePage(to: a)
            return
        }else{        GoBack()}
    }
    var isloading=false
    override func LoadIng(_ a:String) {
        Connectlabel.text=a
        animationView.center = self.view.center
        animationView.frame = CGRect(x: animationView.frame.minX, y: animationView.frame.minY+20, width: 200, height: 200)
        LoadingView.center = self.view.center
        isloading=true
        LoadingView.isHidden=false
        animationView.isHidden=false
        animationView.play()
    }
    override func LoadingSuccess() {
        isloading=false
        animationView.pause()
        LoadingView.isHidden=true
        animationView.isHidden=true
    }
    
    @IBAction func exit(_ sender: Any) {
        let a=peacedefine().Signout
        SwipePage(to: a)
    }
    override func PageChangeLinster(_ controler:UIViewController){
        let classname=NSStringFromClass(controler.classForCoder)
        back.setImage(UIImage.init(named: "UI_back"), for: .normal)
        print(classname)
        if(classname=="orange_obd.HomePage"){
            rightop.isHidden=false
        }else{
            rightop.isHidden=true
        }
        switch classname {
        case "orange_obd.HomePage":
            tit.text=SetLan.Setlan("OBD II DONGLE")
            break
        case "orange_obd.Selection":
            if(Selection.function==Selection.讀取){
                tit.text=SetLan.Setlan("app_sensor_info_read")
            }else{
                tit.text=SetLan.Setlan("OBDII relearn")
            }
            
            break
        default:
            
            break
        }
        if(Pagememory.count>=2){
            self.back.isHidden=false
        }else{   self.back.isHidden=true
            self.rightop.isHidden=false
            self.ISRUN=false
        }
    }
    override func Disconnect() {
        SetMenu(peacedefine().HomePage)
    }
    
}
