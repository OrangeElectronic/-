////
////  ViewController.swift
////  Oglite
////
////  Created by Jianzhi.wang on 2020/1/29.
////  Copyright © 2020 Jianzhi.wang. All rights reserved.
////
//
//  ViewController.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/2.
//

import UIKit
class ViewController: JzActivity,BleCallBack {
    var timer2: Timer?

    public static var device = [String:CBPeripheral]()

    @IBOutlet var tit: UILabel!
    func scanBack(_ device: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
            if(device.name != nil && device.name!.contains("BLERF")){
                let devic = device.name!.replace("BLERF-", "")
                ViewController.device[devic]=device
                print("有Sensor:\(devic)")
                let data=advertisementData["kCBAdvDataManufacturerData"]
                if(data is Data){
                    var tempstring = ""
                    for i in (data as! Data){
                        tempstring = tempstring+String(format:"%02X",i)
                    }
                    //BLE回傳資料解析
                    Md_Sensor_Info.ParsingData(tempstring: tempstring, device: device)
                }
            }
    }
    func onConnecting() {

    }

    func onConnectFalse() {

    }

    func onConnectSuccess() {
        DispatchQueue.global().async { [self] in
            usleep(1000*100)
            helper.writeHex("0AB70A","8D82", "8D81")
        }
    }
//    B726C44C94E548D7000150000491F4
//    B726C45C94E54AB700015100049139
    public static  var rx=""
    func rx(_ a: BleBinary) {
            ViewController.rx=a.readHEX()
            Md_Sensor_Info.ParsingData(tempstring: a.readHEX().replace("10FF", ""), device: nil)
            print("rx有資料:\(a.readHEX())")
    }

    func tx(_ b: BleBinary) {

    }



    func needOpen() {

    }
    lazy var helper=BleHelper(self)
    @IBOutlet var topbar: UIView!
    @IBOutlet var container: UIView!
    override func viewInit() {
        PublicBeans.資料庫.autoCreat()
        rootView=container
        topbar.isHidden=true
        JzActivity.getControlInstance.setHome(Page_Sign_In(), "Home")
        TestInit.initF()
        helper.startScan()
        "Sensor_Data".deleteSerialRout()
//        TaskHandler.newInstance().runTaskTimer("uploadSensorData", 3.0, {
//            TaskHandler.newInstance().runTaskOnce("uploadingData", {
//
//                for(i in  "uploadSensor".listObject()){}
//            })
//        })
    }

    override func changePageListener(_ controler: pagemenory) {
        print("Switch\(controler.tag)")
        switch controler.tag {
        case "Page_Select_Car","Page_Check_Sensor","Page_Car_Setting_Select","Page_Car_Setting_AddMenu","Page_Add_Cae_PlateNumber","Page_Car_Setting_Detail","Page_Car_Setting_Type","Page_Scanner","Page_Show_Crcode","Page_Setting_All","Page_Car_List","Page_Setting_Advice","Page_Select_Make","Page_Select_Model","Page_Select_Year","Page_Setting_Unit","Page_Setting_Change_Pressure_Unit"
             ,"Page_Setting_Unit_Tem","Page_Register","btn_export_data":
            topbar.isHidden=false
            switch(controler.tag){
            case "Page_Select_Car":
                self.tit.text="檢查輪胎壓力值"
            break
            case "Page_Car_Setting_Select":
                self.tit.text="車輛輪胎設定"
                break
            case "btn_export_data":
                self.tit.text="胎壓檢測紀錄"
                break
            case "Page_Setting_All":
                self.tit.text="系統設定"
                break
            default: break

            }
            break
        case "Page_Home":
            topbar.isHidden=true
            break

        default:
            topbar.isHidden=true
        }
    }

    @IBAction func goBack(_ sender: Any) {
        JzActivity.getControlInstance.goBack()
    }
    @IBAction func goMenu(_ sender: Any) {
        JzActivity.getControlInstance.goMenu()
    }
    @objc func checkNewVersions(){
        DonloadFile().checkNewVersion()

    }
    override func viewWillAppear(_ animated: Bool) {
        self.timer2 = Timer.scheduledTimer(timeInterval: 10, target: self, selector: #selector(checkNewVersions), userInfo: nil, repeats: true)
    }
    override func viewDidDisappear(_ animated: Bool) {
        self.timer2?.invalidate()
    }
    override func openBottomSheetDialog(_ newViewController: UIViewController, _ swipe: Bool, _ tag: String) {
        let buttonSheet=BottomSheet()
        buttonSheet.dtag=tag
        for i in AllDialog{
            if(i.tag==tag){
                i.page.viewDidLoad()
                return
            }
        }
        buttonSheet.view.backgroundColor = .clear
        if(swipe){
            newViewController.view.backgroundColor = .clear
        }else{
            newViewController.view.backgroundColor = UIColor(red: 0, green: 0, blue: 0, alpha: 0.5)
        }
        buttonSheet.view.addSubview(newViewController.view)
        newViewController.view.frame = self.view.bounds
        newViewController.didMove(toParent: buttonSheet)
        addChild(buttonSheet)
        self.view.addSubview(buttonSheet.view)
        buttonSheet.view.frame = self.view.bounds
        buttonSheet.didMove(toParent: self)
        JzActivity.getControlInstance.openDiaLog(buttonSheet, swipe,tag)
    }

    public static func getBleHelper()->BleHelper{
        let controler = JzActivity.getControlInstance.getActivity() as! ViewController
        return controler.helper
    }
}


//
//import UIKit
//import JzOsFrameWork
//import JzBleHelper_os
//import IQKeyboardManagerSwift
//import CoreBluetooth
//import JzOsTool
//import Firebase
//import JzOsTaskHandler
//import JzOsHttpExtension
//import SwiftSoup
//import JzOsSerialization
//class ViewController:JzActivity,BleCallBack{
//    @IBOutlet var backbt: UIButton!
//    @IBOutlet  var container: UIView!
//    @IBOutlet var topright: UIButton!
//    @IBOutlet var tit: UILabel!
//
//    var timer: Timer?
//    var timer2: Timer?
//    lazy var helper=BleHelper(self)
//    var scanback:(()->Void?)? = nil
//    var menuToggle=false
//    var goMenu:Bool{
//        get {
//            return menuToggle
//        }
//        set {
//            menuToggle=newValue
//            if(menuToggle){
//                backbt.setImage(UIImage(named: "btn_Menu"), for: .normal)
//            }else{
//                backbt.setImage(UIImage(named: "btn_back"), for: .normal)
//            }
//        }
//    }
//    static var toggleReadSensor=true
//    static var updateBattery:((_ a:String)->Void?)? = nil
//    public static var tempb=""
//    public static var bleMemory:String
//    {
//        get{
//            return tempb
//        }
//        set(myValue) {
//            if(myValue.length>40000){
//                self.tempb=myValue.sub((myValue.length-40000)..<myValue.length)
//            }
//            self.tempb=myValue
//        }
//    }
//    @IBOutlet var iconarea: UIImageView!
//    override func viewInit() {
//
//        if #available(iOS 13.0, *) {
//            overrideUserInterfaceStyle = .light
//        } else {
//            // Fallback on earlier versions
//        }
//        AppDelegate.peripheralManager.startScan()
//        PublicBeans.txMemory.autoCreat()
//        JzLanguage.getInstance().testLan=SharePre.BetaLan
//        JzLanguage.getInstance().lan=SharePre.setLan
//        ViewController.updateBattery={
//            a in
//            switch a {
//            case "00":
//                self.topright.setImage(UIImage(named: "icon_replace"), for: .normal)
//                break
//            case "01":
//                self.topright.setImage(UIImage(named: "icon_low"), for: .normal)
//                break
//            case "02":
//                self.topright.setImage(UIImage(named: "icon_ok"), for: .normal)
//                break
//            case "03":
//                self.topright.setImage(UIImage(named: "icon_full"), for: .normal)
//                break
//            case "FF":
//                self.topright.setImage(UIImage(named: "replace_battery"), for: .normal)
//                JzActivity.getControlInstance.openDiaLog(Dia_Low_Battery(), false, "Dia_Low_Battery")
//                break
//            default:
//                break
//            }
//            return()
//        }
//        IQKeyboardManager.shared.enable=true
//        rootView=container
//        PublicBeans.OBD資料庫.autoCreat()
//        PublicBeans.資料庫.autoCreat()
//        if(SharePre.admin=="nodata"){
//            let area=JzActivity.getControlInstance.getNewController("Main", "Page_Area") as! Page_Area
//            JzActivity.getControlInstance.setHome(area, "area")
//        }else{
//            JzActivity.getControlInstance.setHome(Page_Home(), "Page_Home")
//        }
//        JzActivity.getControlInstance.openDiaLog(Dia_Logo(), false, "Dia_Logo")
//        //跑timer判斷有無資料需補上傳
//        TaskHandler.newInstance().runTaskTimer("checkUploader", 5.0, {
//            DispatchQueue.global().async {
//                TaskHandler.newInstance().runTaskOnce("updateing", {
//                    PublicBeans.txMemory.query("select * from `updateResult`", {
//                        it in
//                        if((HttpCore.post(MemeoryUploader.ip, 5.0,it.getString(1).hexToByte()!)) != nil){
//                            PublicBeans.txMemory.exSql("delete from `updateResult` where id=\(it.getString(0))")
//                        }
//                    }, {})
//
//                })
//            }
//        })
//        TaskHandler.newInstance().runTaskTimer("ErrorMessageJson", 5.0, {
//            DispatchQueue.global().async {
//                TaskHandler.newInstance().runTaskOnce("updateingErrorMessageJson", {
//                    let it = "ErrorMessageJson".listObject(limit: 10)
//                    for a in it{
//                        let decodedData = Data(base64Encoded: a.json.data(using: .utf8)!, options:NSData.Base64DecodingOptions.init(rawValue: 0))
//                        if( HttpCore.post("https://bento2.orange-electronic.com/sendSES", 15.0, decodedData!) != nil){
//                            a.name.deleteObject(rout: "ErrorMessageJson")
//                        }
//                    }
//                }
//                )
//            }
//        })
//        PublicBeans.OBD資料庫.query("select name from obd", {
//            result in
//            print(result.getString(0))
//        }, {})
//    }
//
//    override func viewWillAppear(_ animated: Bool) {
//        self.timer = Timer.scheduledTimer(timeInterval: 3, target: self, selector: #selector(askBle), userInfo: nil, repeats: true)
//        self.timer2 = Timer.scheduledTimer(timeInterval: 10, target: self, selector: #selector(checkNewVersions), userInfo: nil, repeats: true)
//    }
//
//    override func viewDidDisappear(_ animated: Bool) {
//        self.timer?.invalidate()
//        self.timer2?.invalidate()
//    }
//
//    @objc func checkNewVersions(){
//        if(helper.isPaired()){
//            DonloadFile().checkNewVersion()
//        }
//    }
//    @objc func askBle(){
//        if((!helper.isOpen() || !helper.isPaired()) && !DonloadFile.bleUpdate){
//            helper.startScan()
//            JzActivity.getControlInstance.openDiaLog(Select_Ble(), false, "Select_Ble")
//        }
//    }
//    @IBAction func signOut(_ sender: Any) {
//
//    }
//
//    override func changePageListener(_ controler: pagemenory) {
//        ViewController.toggleReadSensor=true
//        if(Pagememory.count<2){
//            backbt.isHidden=true
//        }else{
//            backbt.isHidden=false
//        }
//        controler.page.view.fixLanguage()
//        PublicBeans.refrsh()
//        if(PublicBeans.地區 == "EU"){
//            iconarea.image=UIImage(named: "icon_EU")
//        }else if(PublicBeans.地區 == "US"){
//            iconarea.image=UIImage(named: "icon_NA")
//        }else{
//            iconarea.image=UIImage(named: "icon_tw")
//        }
//        print("Switch\(controler.tag)")
//        goMenu=false
//        backbt.setImage(UIImage(named: "btn_back"), for: .normal)
//        if(controler.tag=="Page_Home"&&helper.isPaired()){
//            checkUpdate()
//        }
//        if(helper.isPaired()){
//            Command.getBattery()
//        }
//        switch controler.tag {
//        case "Page_Vehicle_Select":
//            tit.text=PublicBeans.選擇按鈕
//        case "area":
//            tit.text="jz.152".getFix()
//        case "Page_Policy":
//            tit.text="jz.63".getFix()
//        case "Page_SignIn":
//            tit.text="jz.5".getFix()
//        case "Page_Home":
//            tit.text="O-Genius Lite".getFix()
//        default:
//            break
//        }
//        if(SharePre.isBeta){
//            tit.text="我是BETA"
//        }
//    }
//
//    @IBAction func goback(_ sender: Any) {
//        if(goMenu){
//            JzActivity.getControlInstance.goMenu()
//        }else{
//            JzActivity.getControlInstance.goBack()
//        }
//    }
//    var bles:[CBPeripheral]=[CBPeripheral]()
//
//    //連線中的回調
//    func onConnecting() {
//
//        print("onConnecting")
//    }
//    //連線失敗時回調
//    func onConnectFalse() {
//        ViewController.bleMemory += "\n藍芽斷線"
//        NSLog("Ble->onConnectFalse")
//        if(Command.ogCommand.onProgram){
//            return
//        }
//        print("Ble->Disconnect")
//        if(!DonloadFile.bleUpdate){
//            JzActivity.getControlInstance.closeDialLog()
//            helper.startScan()
//            JzActivity.getControlInstance.openDiaLog(Select_Ble(), false, "Select_Ble")
//        }
//    }
//    var reClock=TaskHandler.newInstance().clock()
//    //連線成功時回調
//    func onConnectSuccess() {
//        if(helper.connectPeripheral.name=="ON FOTA RSL10"){
//            DonloadFile().downLoadBle()
//            JzActivity.getControlInstance.closeDialLog()
//            DispatchQueue.main.async {
//                JzActivity.getControlInstance.closeDialLog()
//            }
//            return
//        }
//        ViewController.bleMemory += "\n藍芽連線"
//        NSLog("Ble->onConnectSuccess")
//        helper.stopScan()
//        if(Command.ogCommand.onProgram ){
//            if(reClock.stop()>0.1){
//                Command.ogCommand.retry=true
//                reClock.zeroing()
//            }
//            return
//        }
//        SharePre.blememory="\(helper.connectPeripheral.identifier)"
//        let progress=Progress()
//        progress.label=progress.連線BLE
//        JzActivity.getControlInstance.openDiaLog(progress, false, "Progress")
//        DispatchQueue.global().async {
//            TaskHandler.newInstance().runTaskOnce("onConnectSuccess", {
//                sleep(4)
//                if(!Command.readBleVersion() || !Command.ogCommand.askVersion()){
//                    self.helper.disconnect()
//                    return
//                }
//                usleep(1000*200)
//                Command.readSN()
//                usleep(1000*200)
//                print("SN",SharePre.sn)
//                Command.setclose(SharePre.sleepTime)
//                usleep(1000*200)
//                DispatchQueue.main.async {
//                    JzActivity.getControlInstance.closeDialLog()
//                    if(JzActivity.getControlInstance.getNowPageTag() == "Page_Home"){
//                        self.checkUpdate()
//                    }
//                }
//            })
//        }
//
//    }
//    //更新檢查
//    func checkUpdate(){
//        let cc = DonloadFile()
//        if(cc.needInit()){
//            Command.cangetBattery=false
//            let a=DataLoading()
//            a.label=a.檢查更新
//            JzActivity.getControlInstance.openDiaLog(a, false, "DataLoading")
//            DonloadFile().dataloading({
//                a in
//                Command.cangetBattery=true
//                DispatchQueue.main.async {
//                    JzActivity.getControlInstance.closeDialLog()
//                    if(!a){
//                        self.checkUpdate()
//                    }
//                }
//            })
//        }else if(cc.needUpdate()){
//            JzActivity.getControlInstance.openDiaLog(Dia_Update(), false, "Dia_Update")
//        }
//    }
//    //    F520000E0E 005A9C 08 32 4F00 0200 B000D40A
//    //    F520000E00 91C648 06 32 FF00 0000 30003F0A
//    //三種方式返回接收到的藍芽訊息
//    func rx(_ a: BleBinary) {
//        NSLog("Ble->rx:\(a.readHEX())")
//        Command.rx=Command.rx+a.readHEX()
//        if(Command.rx.contains("F5EE0000FF")){
//            Command.ogCommand.cancel=true
//            ViewController.updateBattery!("FF")
//            return
//        }
//        ViewController.bleMemory += "RX \(Date().date2String("HH:mm:ss:SSS")):\(a.readHEX())\n"
//
//        if(a.readHEX().contains("F500000304F20A") || a.readHEX().contains("0AF700030000F5") ){
//            if(!ViewController.toggleReadSensor){return}
//            if(JzActivity.getControlInstance.getNowPage() is Page_Idcopy_obd  ){
//                (JzActivity.getControlInstance.getNowPage() as! Page_Idcopy_obd).readsensor("")
//            }else if(JzActivity.getControlInstance.getNowPage() is Page_CheckSesor_Detail){
//                (JzActivity.getControlInstance.getNowPage() as! Page_CheckSesor_Detail).readsensor(self)
//            }else if(JzActivity.getControlInstance.getNowPage() is Page_Program_Detail){
//                (JzActivity.getControlInstance.getNowPage() as! Page_Program_Detail).read(self)
//            }
//        }
//    }
//
//    //三種方式返回傳送的藍芽訊息
//    func tx(_ b: BleBinary) {
//        ViewController.bleMemory += "TX: \(Date().date2String("HH:mm:ss:SSS")):\(b.readHEX())\n"
//        NSLog("Ble->tx:\(b.readHEX())")
//    }
//    //返回搜尋到的藍芽,可將搜尋到的藍芽儲存於陣列中，可用於之後的連線
//    func scanBack(_ device: CBPeripheral) {
//        if(Command.ogCommand.onProgram){
//            return
//        }
//        if(DonloadFile.bleUpdate){return}
//        if(!helper.isPaired() && (("\(device.identifier)" == SharePre.blememory)||device.name=="ON FOTA RSL10")){
//            let progress=Progress()
//            progress.label=progress.連線BLE
//            JzActivity.getControlInstance.openDiaLog(progress, false, "Progress")
//            helper.connect(device, 5)
//        }
//        if(!bles.contains(device)){
//            if(device.name != nil){
//                if(device.name!.uppercased().contains("OG_LITE")){
//                    bles.append(device)
//                }
//            }
//        }
//        //        if(device.name=="ON FOTA RSL10"){
//        //            AppDelegate.peripheralManager.selected = device
//        //        }
//        if(scanback != nil){scanback!()}
//    }
//
//    //藍芽未打開，經聽到此function可提醒使用者打開藍芽
//    func needOpen() {
//        print("noble")
//    }
//}
//
