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
import JzBleHelper_os
import JzIos_Framework
import Firebase
import JzOsTaskHandler
import Alamofire
class ViewController: JzActivity,BleCallBack{
    @IBOutlet var rightop: UIButton!
    @IBOutlet var padicon: UIImageView!
    @IBOutlet var tlkingBt: UIButton!
    lazy var helper=BleHelper(self)
    @IBOutlet var areaimage: UIButton!
    @IBOutlet var tit: UILabel!
    @IBOutlet var LoadingView: UIView!
    @IBOutlet var Connectlabel: UILabel!
    @IBOutlet var back: UIButton!
    @IBOutlet var Container: UIView!
    @IBOutlet var topv: UIView!
    
    let animationView = AnimationView(name: "simple-loader2")
    var command=Command()
    let delgate = UIApplication.shared.delegate as! AppDelegate
    var timer: Timer?
    var etalk=E_Command()
    var serialnum="99"
    var scanback:(()->Void?)? = nil
    var connectBack:(()->Void?)? = nil
    override func viewInit()  {
        if #available(iOS 13.0, *) {
            overrideUserInterfaceStyle = .light
        } else {
            // Fallback on earlier versions
        }
        if(JzActivity.getControlInstance.getPro("Beta", "false")=="true"){
            topv.backgroundColor=UIColor.green
        }
        Messaging.messaging().subscribe(toTopic: "txusbupdate") { error in
            print("Subscribed to txusbupdate topic")
        }
        print("id:\(Bundle.main.bundleIdentifier!)")
        PublicBeans.OBD資料庫.autoCreat()
        rootView=Container
        command.act=self
        delgate.act=self
        if(JzActivity.getControlInstance.getPro("admin","nodata")=="nodata"){
            JzActivity.getControlInstance.setHome(Page_SelectArea(), "area")
        }else{
            JzActivity.getControlInstance.setHome(peacedefine().HomePage, "Page_SelectArea")
        }
        Function.GetVersion()
        //檢查版本
        TaskHandler.newInstance().runTaskTimer("checkNewVersion", 30.0, { [self] in
            DonloadFile().checkNewVersion()
        })
        PublicBeans.資料庫.autoCreat()
    }
    //更新檢查
    func checkUpdate(){
        
        let cc = DonloadFile()
        if(cc.needInit()){
            DonloadFile().dataloading({
                a in
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.closeDialLog()
                    if(!a){
                        self.checkUpdate()
                    }
                }
            })
        }else if(cc.needUpdate()){
            JzActivity.getControlInstance.openDiaLog(Dia_Update(), false, "Dia_Update")
        }
    }
    var isloading=false
    
    func ChangePage(to:UIViewController){
        JzActivity.getControlInstance.changePage(to,  String(describing: type(of: to)), true)
    }
    
    @IBAction func GoBack(_ sender: Any) {
        if(isloading){return}
        if(back.image(for: .normal)==UIImage.init(named: "btn_Menu")){
            back.setImage(UIImage.init(named: "btn_back_normal"), for: .normal)
            JzActivity.getControlInstance.goMenu()
            return
        }else{
            JzActivity.getControlInstance.goBack()
        }
    }
    
    @IBAction func Signout(_ sender: Any) {
        let a=peacedefine().Signout
        a.act=self
        JzActivity.getControlInstance.openDiaLog(a, false, "Signout")
    }
    func DataLoading(){
        let a=Progress()
        a.label=SetLan.Setlan("Data_Loading")
        JzActivity.getControlInstance.openDiaLog(a,true,"Progress")
    }
    
    func LoadingSuccess(){
        JzActivity.getControlInstance.closeDialLog()
    }
    override func changePageListener(_ controler: pagemenory) {
        if(Pagememory.count<2){
            back.isHidden=true
        }else{
            back.isHidden=false
        }
        PublicBeans.refrsh()
        print("Switch\(controler.tag)")
        if(controler.page is Page_SelectArea){
            areaimage.isHidden=true
        }else{
            if(JzActivity.getControlInstance.getPro("Area", "EU")=="EU"){
                areaimage.setImage(UIImage(named: "icon_EU"), for: .normal)
            }else if(JzActivity.getControlInstance.getPro("Area", "TW")=="TW"){
                areaimage.setImage(UIImage(named: "icon_tw"), for: .normal)
            }else{
                areaimage.setImage(UIImage(named: "icon_NA"), for: .normal)
            }
            areaimage.isHidden=false
        }
        if(Pagememory.count>=2){
            self.back.isHidden=false
        }else{   self.back.isHidden=true
            self.rightop.isHidden=false
            self.padicon.isHidden=true}
    }
    @IBAction func ToTalking(_ sender: Any) {
        
    }
    
    var count=0
    @IBAction func selectArea(_ sender: Any) {
        count+=1
        if(count==10){
            if(JzActivity.getControlInstance.getPro("Beta", "false")=="true"){
                JzActivity.getControlInstance.setPro("Beta", "false")
            }else{
                JzActivity.getControlInstance.setPro("Beta", "true")
            }
            JzActivity.getControlInstance.setPro("dataloading", "false")
            checkUpdate()
        }
        if(count>10){count=0}
    }
    
    var bles:[CBPeripheral]=[CBPeripheral]()
    
    //連線中的回調
    func onConnecting() {
        print("onConnecting")
    }
    //連線失敗時回調
    func onConnectFalse() {
        print("onConnectFalse")
        JzActivity.getControlInstance.closeDialLog()
        helper.startScan()
        self.padicon.isHidden=true
    }
    //連線成功時回調
    func onConnectSuccess() {
        print("onConnectSuccess")
        JzActivity.getControlInstance.closeDialLog()
        DispatchQueue.global().async { [self] in
            sleep(3)
            if(!self.command.Setserial()){
                helper.disconnect()
            }
            Function.AddIfNotValid(self.serialnum,JzActivity.getControlInstance.getPro("admin", "nodata"),JzActivity.getControlInstance.getPro("password", "nodata"))
        }
        self.padicon.isHidden=false
        if(connectBack != nil){
            connectBack!()
        }
    }
    
    //三種方式返回接收到的藍芽訊息
    func rx(_ a: BleBinary) {
        Command.rx=Command.rx+a.readHEX()
        print("rx:\(a.readHEX())")
    }
    
    //三種方式返回傳送的藍芽訊息
    func tx(_ b: BleBinary) {
        print("tx:\(b.readHEX())")
    }
    
    //返回搜尋到的藍芽,可將搜尋到的藍芽儲存於陣列中，可用於之後的連線
    func scanBack(_ device: CBPeripheral) {
        if(!bles.contains(device) && device.name != nil && (device.name!.contains("PAD"))){
            bles.append(device)
        }
        print(device.name)
        if(scanback != nil){scanback!()}
        
    }
    
    //藍芽未打開，經聽到此function可提醒使用者打開藍芽
    func needOpen() {
        print("noble")
    }
}
