//
//  MainPrace.swift
//  Peripheral
//
//  Created by 王建智 on 2019/7/25.
//  Copyright © 2019 KoKang Chu. All rights reserved.
//

import Cocoa
import Lottie
import CoreBluetooth
class MainPrace: NSViewController,NSTextFieldDelegate,CBCentralManagerDelegate, CBPeripheralDelegate,NSWindowDelegate{
    var SensorMode=""
    var spversion=""
    
    var mmynum:String!=nil
    var connectblename="USB_PROG.PAD"
    var ISRUN=false
    var BleIsConnect=false
    var UsbIsConnect=false
    var bles:[String]=[String]()
    var centralManager: CBCentralManager!
    // 儲存連上的 peripheral，此變數一定要宣告為全域
    var connectPeripheral: CBPeripheral!
    // 記錄所有的 characteristic
    var charDictionary = [String: CBCharacteristic]()
    let C001_CHARACTERISTIC = "8D81"
    
    func isPaired() -> Bool {
        let user = UserDefaults.standard
        if let uuidString = user.string(forKey: "KEY_PERIPHERAL_UUID")
        {
            print("uuid是\(uuidString)")
            let uuid = UUID(uuidString: uuidString)
            let list = centralManager.retrievePeripherals(withIdentifiers: [uuid!])
            if list.count > 0 {
                connectPeripheral = list.first!
                connectPeripheral.delegate = self
                return true
            }
        }
        return false
    }
    
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        // 先判斷藍牙是否開啟，如果不是藍牙4.x ，也會傳回電源未開啟
        guard central.state == .poweredOn else {
            // iOS 會出現對話框提醒使用者
            return
        }
        if(isPaired()){
            centralManager.connect(connectPeripheral, options: nil)
        }else{
            centralManager.scanForPeripherals(withServices: nil, options: nil)}
        
        
        
    }
    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
        
        guard let deviceName = peripheral.name else {
            return
        }
        print("找到藍牙裝置:\(deviceName)")
        if !(bles.contains(deviceName)){bles.append(deviceName)
        }
        //        guard deviceName.range(of: connectblename) != nil
        //            else {
        //                return
        //        }
        if(!deviceName.contains(connectblename)){return}
        central.stopScan()
        let user = UserDefaults.standard
        user.set(peripheral.identifier.uuidString, forKey: "KEY_PERIPHERAL_UUID")
        user.synchronize()
        connectPeripheral = peripheral
        connectPeripheral.delegate = self
        centralManager.connect(connectPeripheral, options: nil)
        print("最大傳輸單元\( connectPeripheral.maximumWriteValueLength(for: .withResponse))")
    }
    /* 3號method */
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        // 清除上一次儲存的 characteristic 資料
        charDictionary = [:]
        // 將觸發 4號method
        peripheral.discoverServices(nil)
    }
    /* 4號method */
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        guard error == nil else {
            print(error!.localizedDescription)
            return
        }
        
        for service in peripheral.services! {
            // 將觸發 5號method
            connectPeripheral.discoverCharacteristics(nil, for: service)
        }
    }
    /* 5號method */
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        guard error == nil else {
            print(error!.localizedDescription)
            return
        }
        for characteristic in service.characteristics! {
            let uuidString = characteristic.uuid.uuidString
            charDictionary[uuidString] = characteristic
            if(uuidString=="8D81"){
                NoConnect.isHidden=true
                BleIsConnect=true
                print("yes")
                connectPeripheral.setNotifyValue(true, for: characteristic)
            }
            print(uuidString)
            peripheral.discoverDescriptors(for: characteristic)
        }
    }
    /* 將資料傳送到 peripheral */
    func sendData(_ data:String,_ len:Int)  {
        CheckLen=len
        TmpData=""
        Rx=""
        var Tda=""
        if(UsbIsConnect){
            print("傳送\(data)\n資料長度\(data.count)/\(data.HexToByte())")
            serport.serialPort.send(data.HexToByte()!)
        }else{
            guard let characteristic = charDictionary["8D82"] else {
                print("寫入失敗")
                return
            }
            let spi=198
            if(data.count>spi){
                var long=0
                if(data.count%spi==0){long=data.count/spi}else{
                    long=data.count/spi+1
                }
                for i in 0..<long{
                    let pastTime = Date().timeIntervalSince1970
                    while(GetTime(pastTime)<0.1){
                        
                    }
                    if(i==long-1){
                        Tda="87"+data.sub(i*spi..<data.count)+"78"
                        //      Tda="8702bb78"
                    }else{
                        if(i==0){
                            var slon=String(format:"%2X",long).replace(" ", "")
                            if(slon.count<2){slon="0"+slon}
                            Tda="87\(slon)"+data.sub(i*spi..<i*spi+spi)
                            //                        Tda="870\(long)aa"
                        }else{
                            Tda="87"+data.sub(i*spi..<i*spi+spi)
                            //                         Tda="87cc"
                        }
                    }
                    connectPeripheral.setNotifyValue(true, for: charDictionary[C001_CHARACTERISTIC]!)
                    connectPeripheral.writeValue(
                        Tda.HexToByte()!,
                        for: characteristic,
                        type: .withResponse
                    )
                    print("傳送\(Tda)\n資料長度\(Tda.count)/\(Tda.HexToByte())")
                }
            }else{
                print("傳送\(data)\n資料長度\(data.count)/\(data.HexToByte())")
                connectPeripheral.writeValue(
                    data.HexToByte()!,
                    for: characteristic,
                    type: .withResponse
                )
            }
        }
    }
    func GetTime(_ timeStamp: Double)-> Double{
        let currentTime = Date().timeIntervalSince1970
        let reduceTime : TimeInterval = currentTime - timeStamp
        return reduceTime
    }
    /* 將資料傳送到 peripheral 時如果遇到錯誤會呼叫 */
    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        if error != nil {
            print("寫入資料錯誤: \(error!)")
        }else{
            
        }
    }
    var CheckLen=0
    var TmpData=""
    var Rx=""
    /* 取得 peripheral 送過來的資料 */
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        guard error == nil else {
            print(error!)
            return
        }
        let data = characteristic.value
        //            let string = "> " + String(data: data as Data, encoding: .utf8)!
        for i in 0...data!.count-1{
            TmpData=TmpData+String(format:"%02X",data![i])
        }
        print("---------收到數據ㄦ---------")
        print(TmpData)
        if(TmpData.count==CheckLen||CheckLen==0){
            Rx=TmpData
            //            print("---------收到數據ㄦ---------")
            //            print(Rx)
        }
    }
    /* 斷線處理 */
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        print("連線中斷")
        NoConnect.isHidden=false
        BleIsConnect=false
        if isPaired() {
            centralManager.connect(connectPeripheral, options: nil)
        }
    }
    /* 解配對 */
    func unpair() {
        let user = UserDefaults.standard
        user.removeObject(forKey: "KEY_PERIPHERAL_UUID")
        user.synchronize()
        guard connectPeripheral != nil else {
            return
        }
        centralManager.cancelPeripheralConnection(connectPeripheral)
    }
    func peripheral(_ peripheral: CBPeripheral, didDiscoverDescriptorsFor characteristic: CBCharacteristic, error: Error?) {
        print("*******************************************************")
        
        if error != nil {
            print("\(error.debugDescription)")
            return
        }
        if ((characteristic.descriptors) != nil) {
            
            for x in characteristic.descriptors!{
                let descript = x as CBDescriptor?
                print("function name: DidDiscoverDescriptorForChar \(String(describing: descript?.description))")
            }
            
        }
    }
    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor descriptor: CBDescriptor, error: Error?) {
        print("--------send-------")
        print()
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //////////////
    let serport:SerialPortDemoController=SerialPortDemoController()
    let PROGRAM_FAULSE=2
    let PROGRAMMING=3
    let PROGRAM_SUCCESS=0
    let PROGRAM_WAIT=1
    let PROGRAN_INSERT=9
    var admin=""
    var password=""
    let UN_LINK=4
    let LF=5
    let RF=6
    let LR=7
    let RR=8
    @IBOutlet var relarmtext: NSTextView!
    @IBOutlet var ii: NSButton!
    @IBOutlet var pi: NSButton!
    @IBOutlet var CnbK: NSColorWell!
    @IBOutlet var NoConnect: NSView!
    @IBOutlet var Cw: NSColorWell!
    @IBOutlet var numbertires: NSButton!
    var PRORID="ID"
    var ISPROGRAMMING=false
    var Lf="0"
    var Lfid="ABCDEF"
    var Rfid="ABCDEF"
    var Lrid="ABCDEF"
    var Rrid="ABCDEF"
    var WriteLf=""
    var WriteLr=""
    var WriteRR=""
    var WriteRf=""
    var idcount=8
    var command=Command()
    let animationView = AnimationView(name: "simple-loader2")
    let deledate = NSApplication.shared.delegate as! AppDelegate
    @IBOutlet var RFC: NSImageView!
    @IBOutlet var RRC: NSImageView!
    @IBOutlet var LFC: NSImageView!
    @IBOutlet var LRC: NSImageView!
    @IBOutlet var ColorTop: NSColorWell!
    @IBOutlet var YearPop: NSPopUpButton!
    @IBOutlet var ModelPop: NSPopUpButton!
    @IBOutlet var YearBt: NSButton!
    @IBOutlet var ModelBt: NSButton!
    @IBOutlet var MakeBt: NSButton!
    @IBOutlet var TopText: NSTextField!
    @IBOutlet var LoadingText: NSTextField!
    @IBOutlet var tset: NSColorWell!
    @IBOutlet var RRT: NSTextField!
    @IBOutlet var RFT: NSTextField!
    @IBOutlet var LRT: NSTextField!
    @IBOutlet var LFT: NSTextField!
    @IBOutlet var LRL2: NSTextField!
    @IBOutlet var LRL1: NSTextField!
    @IBOutlet var LFL2: NSTextField!
    @IBOutlet var LFL1: NSTextField!
    @IBOutlet var RRL2: NSTextField!
    @IBOutlet var RRL1: NSTextField!
    @IBOutlet var RFL2: NSTextField!
    @IBOutlet var RFL1: NSTextField!
    @IBOutlet var LRI2: NSImageView!
    @IBOutlet var LRI1: NSImageView!
    @IBOutlet var LFI2: NSImageView!
    @IBOutlet var LFI1: NSImageView!
    @IBOutlet var RRI2: NSImageView!
    @IBOutlet var RRI1: NSImageView!
    @IBOutlet var RFI1: NSImageView!
    @IBOutlet var RFI2: NSImageView!
    @IBOutlet var tit: NSTextField!
    @IBOutlet var reoghtview: NSView!
    @IBOutlet var SelectAction: NSButton!
    @IBOutlet var selectpad: NSPopUpButton!
    @IBOutlet var label2: NSTextField!
    
    @IBOutlet var prback: NSImageView!
    
    @IBOutlet var label11: NSTextField!
    @IBOutlet var PRT: NSTextField!
    
    @IBOutlet var relarmt: NSTextField!
    var OriginView:NSView! = nil
    var timer: Timer?
    var db=Sqlhelper()
    @IBOutlet var MakePop: NSPopUpButton!
    override func viewDidLayout() {
        tit.stringValue=Language.SetLanguAge(1)
        LFT.stringValue=Language.SetLanguAge(15)
        LRT.stringValue=Language.SetLanguAge(14)
        RFT.stringValue=Language.SetLanguAge(12)
        RRT.stringValue=Language.SetLanguAge(13)
        label2.stringValue=Language.SetLanguAge(6)
        relarmt.stringValue=Language.SetLanguAge(44)
        PRT.stringValue=Language.SetLanguAge(45)
        label11.stringValue=Language.SetLanguAge(31)
        UpdateUiCondition(PROGRAM_WAIT)
        print("重繪\(view.bounds.size.width)")
        let screenSize = NSScreen.main!.frame.width
        print("螢幕寬度\(screenSize)")
        if(PRORID == "PR"){
            LFI1.isHidden=true
            LFL1.isHidden=true
            RFI1.isHidden=true
            RFL1.isHidden=true
            RRI2.isHidden=true
            RRL2.isHidden=true
            LRI2.isHidden=true
            LRL2.isHidden=true
        }else{
            LFI1.isHidden=false
            LFL1.isHidden=false
            RFI1.isHidden=false
            RFL1.isHidden=false
            RRI2.isHidden=false
            RRL2.isHidden=false
            LRI2.isHidden=false
            LRL2.isHidden=false
        }
        let refont=RFI1.frame.width/168
        resize(LRL1,refont,14)
        resize(LRL2,refont,12)
        resize(RRL1,refont,14)
        resize(RRL2,refont,12)
        resize(RFL1,refont,12)
        resize(RFL2,refont,14)
        resize(LFL1,refont,12)
        resize(LFL2,refont,14)
        resize(RRT,refont,14)
        resize(RFT,refont,14)
        resize(LFT,refont,14)
        resize(LRT,refont,14)
        //        RFL1.bounds.size=NSSize(width: RFL1.frame.width, height: 18*refont)
        let anw=refont*200
        let anh=refont*200
        let x=(view.bounds.size.width/2)-(anw/2)
        let y=(view.bounds.size.height/2)-(anh/2)-(15*refont)
        animationView.frame = CGRect(x: x, y: y, width: anw, height: anh)
        let low=refont*88
        let loh=refont*17
        let lx=(view.bounds.size.width/2)-(low/2)
        let ly=(view.bounds.size.height/2)-(loh/2)+(15*refont)
        LoadingText.frame=CGRect(x: lx, y: ly, width: low, height: loh)
        LoadingText.font=NSFont.systemFont(ofSize: 13*refont)
        TopText.font=NSFont.systemFont(ofSize: 13*refont)
    }
    func play(_ text:String) {
        LoadingText.stringValue=text
        animationView.play()
        animationView.isHidden=false
        LoadingText.isHidden=false
        tset.isHidden=false
    }
    func pause() {
        animationView.pause()
        animationView.isHidden=true
        LoadingText.isHidden=true
        tset.isHidden=true
    }
    override func viewDidAppear() {
        self.view.window?.delegate = self
    }
    
    func windowShouldClose(_ sender: NSWindow) -> Bool {
        print("close")
        NSApplication.shared.terminate(self)
        return true
    }
    func dowload_mmy(){
        self.play("Data Loading")
        DispatchQueue.global().async {
            let res=FtpManage().DowloadMmy(self.deledate)
            DispatchQueue.main.async {
                if(res){
                    self.pause()
                    self.db.QueryMake(self.MakePop)
                }else{self.dowload_mmy()}
            }
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        command.act=self
        if(admin==""){
            //           enroll.
            
            OriginView = self.view
            let enroll =  self.storyboard!.instantiateController(withIdentifier: NSStoryboard.Name("Enroll")) as! Enroll
            self.addChild(enroll)
            enroll.main=self
            enroll.view.frame = self.view.bounds
            self.view=enroll.view
            
        }
        LFI1.isEditable=false
        LFL1.isEditable=false
        RFI1.isEditable=false
        RFL1.isEditable=false
        RRI2.isEditable=false
        RRL2.isEditable=false
        LRI2.isEditable=false
        LRL2.isEditable=false
        let queue = DispatchQueue.main
        centralManager = CBCentralManager(delegate: self, queue: queue)
        animationView.contentMode = .scaleAspectFill
        animationView.loopMode=LottieLoopMode.loop
        OriginView.addSubview(animationView)
        dowload_mmy()
        selectpad.addItem(withTitle: "USB_PAD")
        tset.wantsLayer=true
        tset.layer?.cornerRadius=15
        tset.alphaValue=0.8
        CnbK.wantsLayer=true
        CnbK.alphaValue=0.8
        LFL1.delegate=self
        RFL1.delegate=self
        RRL2.delegate=self
        LRL2.delegate=self
        reoghtview.wantsLayer=true
        reoghtview.layer?.backgroundColor = .white
        Cw.wantsLayer=true
        Cw.layer?.cornerRadius=15
        selectpad.bezelColor = .gray
        SetBtText("Make",MakeBt,false)
        SetBtText("Model",ModelBt,false)
        SetBtText("Year",YearBt,false)
        SetBtText("USB_PAD",SelectAction,false)
        SetBtText("Number of tires",numbertires,false)
        UdCondition()
    }
    @ objc func UpdateMessage(){
        if(!BleIsConnect && !UsbIsConnect){
            centralManager.scanForPeripherals(withServices: nil, options: nil)
            serport.SeeAvailable(self)
        }
    }
    override func  viewWillAppear() {
        self.timer = Timer.scheduledTimer(timeInterval: 3, target: self, selector: #selector(UpdateMessage), userInfo: nil, repeats: true)
    }
    override func viewWillDisappear() {
        timer?.invalidate()
    }
    @IBAction func openaction(_ sender: Any) {
        selectpad.performClick(self)
    }
    
    @IBAction func change(_ sender: Any) {
        
    }
    func resize(_ la:NSTextField,_ refont:CGFloat,_ size:CGFloat){
        if(la.placeholderString != nil || la.placeholderAttributedString != nil){
            la.placeholderAttributedString = nil
            if(la.isEditable){
                la.SetPlaceText(size*refont, .black, "Original sensor ID")
            }else{la.SetPlaceText(size*refont, .white, "Original sensor ID")}
            
        }
        la.font = NSFont.systemFont(ofSize: size*refont)
        la.sizeToFit()
    }
    
    func controlTextDidChange(_ notification : Notification) {
        if let textField = notification.object as? NSTextField {
            let string=textField.stringValue
            let aSet = NSCharacterSet(charactersIn:"0123456789abcdefABCDEF").inverted
            let compSepByCharInSet = string.components(separatedBy: aSet)
            let numberFiltered = compSepByCharInSet.joined(separator: "")
            if(string != numberFiltered||string.count>idcount){
                textField.stringValue=String(string.prefix((string.count-1)))
            }
            print(textField.stringValue)
            //do what you need here
        }
    }
    
    @IBAction func Select(_ sender: Any) { SetBtText(selectpad.selectedItem!.title,SelectAction,false)
    }
    
    @IBAction func MakeAction(_ sender: Any) {
        SetBtText(MakePop.selectedItem!.title,MakeBt,false)
        prback.image=NSImage.init(named:NSImage.Name.Prbackg)
    }
    
    @IBAction func reselect(_ sender: Any) {
        OriginView = self.view
        let enroll =  self.storyboard!.instantiateController(withIdentifier: NSStoryboard.Name("Enroll")) as! Enroll
        
        self.addChild(enroll)
        enroll.main=self
        enroll.view.frame = self.view.bounds
        self.view=enroll.view
    }
    @IBAction func SelectMake(_ sender: Any) {
        MakePop.performClick(self)
        first=false
        self.numbertires.image=NSImage.init(named:NSImage.Name.img_Dropdownmenu_gray)
        SetBtText("Number of tires",numbertires,false)
        SetBtText("Model",ModelBt,false)
        SetBtText("Year",YearBt,false)
        
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
    
    @IBAction func SelectModel(_ sender: Any) {
        if(MakeBt.title != "Make"){
            first=false
            SetBtText("Number of tires",numbertires,false)
            self.numbertires.image=NSImage.init(named:NSImage.Name.img_Dropdownmenu_gray)
            SetBtText("Year",YearBt,false)
            self.db.QueryModel(ModelPop,MakePop.selectedItem!.title)
            ModelPop.performClick(self)
        }
    }
    
    @IBAction func ModelAction(_ sender: Any) {
        SetBtText(ModelPop.selectedItem!.title,ModelBt,false)
        prback.image=NSImage.init(named:NSImage.Name.Prbackg)
    }
    @IBAction func SelectYeay(_ sender: Any) {
        if(ModelBt.title != "Model"){
            SetBtText("Number of tires",numbertires,false)
            self.numbertires.image=NSImage.init(named:NSImage.Name.img_Dropdownmenu_gray)
            self.db.QueryYear(YearPop,MakePop.selectedItem!.title,ModelPop.selectedItem!.title)
            YearPop.performClick(self)
        }
    }
    
    @IBAction func YearAction(_ sender: Any) {
        SetBtText(YearPop.selectedItem!.title,YearBt,false)
        prback.image=NSImage.init(named:NSImage.Name.Prbackb)
        first=true
        mmynum=self.db.QueryS19(MakePop.selectedItem!.title,ModelPop.selectedItem!.title,YearPop.selectedItem!.title)
        Lf=db.QueryLf(mmynum)
        idcount=db.QueryId(mmynum)
        UpdateUiCondition(PROGRAN_INSERT)
        dowloadmmy()
        pr(self)
        relarmtext.string=self.db.QueryRelarm("Relearn Procedure (English)", MakePop.selectedItem!.title,ModelPop.selectedItem!.title,YearPop.selectedItem!.title)
        spversion=db.SensorModel(mmynum)
        if(spversion=="SP201"){
            SensorMode=SensorBean._433
        }
        if(spversion=="SP202"){
            SensorMode=SensorBean._315
        }
    }
    
    var first=true
    var beans=[SensorBean(),SensorBean(),SensorBean(),SensorBean()]
    func UdCondition(){
        var tt="Year"
        DispatchQueue.global().async {
            while(tt == "Year"){
                DispatchQueue.main.async {
                    tt=self.YearBt.title
                }
                sleep(1)
            }
            while(self.first==false){sleep(1)
            }
            for i in 0...1{
                let CH1=self.command.Command11(i, 1,self.SensorMode)
                var Id1=CH1.id
                let CH2=self.command.Command11(i, 2,self.SensorMode)
                var Id2=CH2.id
                if(i==0){
                    self.beans[0]=CH1
                    self.beans[1]=CH2
                }else{
                    self.beans[2]=CH1
                    self.beans[3]=CH2
                }
                DispatchQueue.main.async {
                    if(CH1.result){
                        if(self.mmynum=="RN1628"||self.mmynum=="SI2048"){
                            let Writetmp=Id1.sub(0..<2)+"XX"+Id1.sub(4..<6)+"YY"
                            Id1=Writetmp.replace("XX", Id1.sub(6..<8)).replace("YY", Id1.sub(2..<4))
                        }
                        Id1=String(Id1.suffix(self.idcount))
                        if(i==0){
                            self.Lfid=Id1
                            if(self.first){
                                self.UpdateUI(self.LF, self.PROGRAM_WAIT,CH1.canPr)}
                            self.LFL2.stringValue=self.Lfid
                        }else{
                            self.Rfid=Id1
                            if(self.first){
                                self.UpdateUI(self.RF, self.PROGRAM_WAIT,CH1.canPr)}
                            self.RFL2.stringValue=self.Rfid
                        }
                    }else{
                        if(i==0){
                            self.Lfid="Unlinked"
                            if(self.first){self.UpdateUI(self.LF, self.UN_LINK,CH1.canPr)}
                            self.LFL2.stringValue=self.Lfid
                        }else{
                            self.Rfid="Unlinked"
                            if(self.first){self.UpdateUI(self.RF, self.UN_LINK,CH1.canPr)}
                            self.RFL2.stringValue=self.Rfid
                        }
                    }
                    if(CH2.result){
                        if(self.mmynum=="RN1628"||self.mmynum=="SI2048"){
                            let Writetmp=Id2.sub(0..<2)+"XX"+Id2.sub(4..<6)+"YY"
                            Id2=Writetmp.replace("XX", Id2.sub(6..<8)).replace("YY", Id2.sub(2..<4))
                        }
                        Id2=String(Id2.suffix(self.idcount))
                        if(i==0){
                            self.Lrid=Id2
                            if(self.first){self.UpdateUI(self.LR, self.PROGRAM_WAIT,CH2.canPr)}
                            self.LRL1.stringValue=self.Lrid
                        }else{
                            self.Rrid=Id2
                            if(self.first){self.UpdateUI(self.RR, self.PROGRAM_WAIT,CH2.canPr)}
                            self.RRL1.stringValue=self.Rrid
                        }
                    }else{
                        if(i==0){
                            self.Lrid="Unlinked"
                            if(self.first){self.UpdateUI(self.LR, self.UN_LINK,true)}
                            self.LRL1.stringValue=self.Lrid
                        }else{
                            self.Rrid="Unlinked"
                            if(self.first){self.UpdateUI(self.RR, self.UN_LINK,true)}
                            self.RRL1.stringValue=self.Rrid
                        }
                    }
                    var tires=0
                    if(self.RFL2.stringValue != "Unlinked"){
                        tires+=1
                    }
                    if(self.RRL1.stringValue != "Unlinked"){
                        tires+=1
                    }
                    if(self.LRL1.stringValue != "Unlinked"){
                        tires+=1
                    }
                    if(self.LFL2.stringValue != "Unlinked"){
                        tires+=1
                    }
                    self.SetBtText("\(tires)",self.numbertires,false)
                    self.numbertires.image=NSImage.init(named:NSImage.Name.img_Dropdownmenu_Blue)
                }
            }
            sleep(4)
            self.UdCondition()
        }
    }
    func dowloadmmy(){
        self.play("Data Loading")
        ISPROGRAMMING=true
        DispatchQueue.global().async {
            let a=FtpManage().DowloadS19(self.mmynum!)
            DispatchQueue.main.async {
                if(a=="false"){
                    self.dowloadmmy()
                }else{
                    self.pause()
                    self.command.mmydata=a
                    self.ISPROGRAMMING=false
                }
            }
            
        }
    }
    func UpdateUI(_ position:Int,_ situation:Int,_ canPr:Bool){
        switch position {
        case LF:
            LFC.image=NSImage.init(named:NSImage.Name.icon_Round_normal)
            switch(situation){
            case UN_LINK:
                LFL2.stringValue="Unlinked"
                LFL1.stringValue=""
                LFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                LFI1.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                LFL1.isEditable=false
                break
            case PROGRAM_SUCCESS:
                LFL2.stringValue=Lfid
                LFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_Green)
                LFC.image=NSImage.init(named:NSImage.Name.icon_Round_OK)
                break
            case PROGRAM_FAULSE:
                LFL2.stringValue=Lfid
                LFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                LFC.image=NSImage.init(named:NSImage.Name.icon_Round_error)
                break
            case PROGRAM_WAIT:
                LFL2.stringValue=Lfid
                
                LFI1.image=NSImage.init(named:NSImage.Name.img_rectangle_white)
                LFL1.isEditable=true
                if(canPr){
                    LFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_Blue)
                }else{
                    LFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                }
                break
            default:
                break
            }
            let refont=RFI1.frame.width/168
            resize(LFL1, refont, 12)
            break
        case RF:
            RFC.image=NSImage.init(named:NSImage.Name.icon_Round_normal)
            switch(situation){
            case UN_LINK:
                RFL2.stringValue="Unlinked"
                RFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                RFI1.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                RFL1.isEditable=false
                RFL1.stringValue=""
                break
            case PROGRAM_SUCCESS:
                RFL2.stringValue=Rfid
                RFC.image=NSImage.init(named:NSImage.Name.icon_Round_OK)
                RFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_Green)
                break
            case PROGRAM_FAULSE:
                RFL2.stringValue=Rfid
                RFC.image=NSImage.init(named:NSImage.Name.icon_Round_error)
                RFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                break
            case PROGRAM_WAIT:
                RFL2.stringValue=Rfid
                RFI1.image=NSImage.init(named:NSImage.Name.img_rectangle_white)
                RFL1.isEditable=true
                if(canPr){
                    RFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_Blue)
                }else{
                    RFI2.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                }
                break
            default:
                break
            }
            let refont=RFI1.frame.width/168
            resize(RFL1, refont, 12)
            break
        case LR:
            LRC.image=NSImage.init(named:NSImage.Name.icon_Round_normal)
            switch(situation){
            case UN_LINK:
                LRL2.stringValue=""
                LRL1.stringValue="Unlinked"
                LRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                LRI2.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                LRL2.isEditable=false
                break
            case PROGRAM_SUCCESS:
                LRL1.stringValue=Lrid
                LRC.image=NSImage.init(named:NSImage.Name.icon_Round_OK)
                LRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_Green)
                break
            case PROGRAM_FAULSE:
                LRL1.stringValue=Lrid
                LRC.image=NSImage.init(named:NSImage.Name.icon_Round_error)
                LRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                break
            case PROGRAM_WAIT:
                LRL1.stringValue=Lrid
                LRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_Blue)
                LRI2.image=NSImage.init(named:NSImage.Name.img_rectangle_white)
                LRL2.isEditable=true
                if(canPr){
                    LRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_Blue)
                }else{
                    LRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                }
                break
            default:
                break
            }
            let refont=RFI1.frame.width/168
            resize(LRL2, refont, 12)
            break
        case RR:
            RRC.image=NSImage.init(named:NSImage.Name.icon_Round_normal)
            switch(situation){
            case UN_LINK:
                RRL2.stringValue=""
                RRL1.stringValue="Unlinked"
                RRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                RRI2.image=NSImage.init(named:NSImage.Name.img_rectangle_gray)
                RRL2.isEditable=false
                break
            case PROGRAM_SUCCESS:
                RRL1.stringValue=Rrid
                RRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_Green)
                RRC.image=NSImage.init(named:NSImage.Name.icon_Round_OK)
                break
            case PROGRAM_FAULSE:
                RRL1.stringValue=Rrid
                RRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                RRC.image=NSImage.init(named:NSImage.Name.icon_Round_error)
                break
            case PROGRAM_WAIT:
                RRL1.stringValue=Rrid
                RRI2.image=NSImage.init(named:NSImage.Name.img_rectangle_white)
                RRL2.isEditable=true
                if(canPr){
                    RRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_Blue)
                }else{
                    RRI1.image=NSImage.init(named:NSImage.Name.img_rectangle_error)
                }
                break
            default:
                break
            }
            let refont=RFI1.frame.width/168
            resize(RRL2, refont, 12)
            break
        default:
            break
        }
    }
    func insertz(_ text:String)->String{
        var a=text
        while(a.count<8){
            a="0"+a;
        }
        return a
    }
    @IBAction func program(_ sender: Any) {
        if(YearBt.title != "Year"){ Program()}
        
    }
    
    func Program(){
         for i in beans{
                       if(!i.canPr){
                        let answer = dialogOKCancel(question: "error", text: Language.SetLanguAge(84).replace("SP201", spversion))
                           return
                       }
                   }
        if(PRORID=="ID"){
            if(RFL1.stringValue.count != idcount && RFL1.isEditable){return}
            if(RRL2.stringValue.count != idcount && RRL2.isEditable){return}
            if(LFL1.stringValue.count != idcount && LFL1.isEditable){return}
            if(LRL2.stringValue.count != idcount && LRL2.isEditable){return}
            WriteLf=insertz(LFL1.stringValue)
            WriteLr=insertz(LRL2.stringValue)
            WriteRf=insertz(RFL1.stringValue)
            WriteRR=insertz(RRL2.stringValue)
        }
        if(!ISPROGRAMMING){
            first=false
            ISPROGRAMMING=true
            UpdateUiCondition(PROGRAMMING)
            DispatchQueue.global().async {
                var condition=false
                if(self.PRORID=="ID"){
                    condition=self.command.ProgramAll(self.mmynum!,self.WriteLf,self.WriteLr,self.WriteRf,self.WriteRR,self.Lf)
                }else{
                    condition=self.command.ProgramAll(self.mmynum!, self.Lf)
                }
                DispatchQueue.main.async {
                    self.ISPROGRAMMING=false
                    //                    self.Lfid="Unlinked"
                    //                    self.Rfid="Unlinked"
                    //                    self.Lrid="Unlinked"
                    //                    self.Rrid="Unlinked"
                    for i in 0..<self.command.CHANNEL_BLE.count{
                        let a=self.command.CHANNEL_BLE[i]
                        if(a.sub(0..<2)=="04"){
                            self.Rrid=a.sub(3..<a.count)
                            if(self.mmynum=="RN1628"||self.mmynum=="SI2048"){
                                let WriteTmp=self.Rrid.sub(0..<2)+"XX"+self.Rrid.sub(4..<6)+"YY"
                                self.Rrid=WriteTmp.replace("XX", self.Rrid.sub(6..<8)).replace("YY", self.Rrid.sub(2..<4))
                            }
                            self.Rrid=String(self.Rrid.suffix(self.idcount))
                            self.UpdateUI(self.RR, self.PROGRAM_SUCCESS,true)
                        }
                        if(a.sub(0..<2)=="03"){
                            self.Rfid=a.sub(3..<a.count)
                            if(self.mmynum=="RN1628"||self.mmynum=="SI2048"){
                                let WriteTmp=self.Rfid.sub(0..<2)+"XX"+self.Rfid.sub(4..<6)+"YY"
                                self.Rfid=WriteTmp.replace("XX", self.Rfid.sub(6..<8)).replace("YY", self.Rfid.sub(2..<4))
                            }
                            self.Rfid=String(self.Rfid.suffix(self.idcount))
                            self.UpdateUI(self.RF, self.PROGRAM_SUCCESS,true)
                        }
                        if(a.sub(0..<2)=="02"){
                            self.Lrid=a.sub(3..<a.count)
                            if(self.mmynum=="RN1628"||self.mmynum=="SI2048"){
                                let WriteTmp=self.Lrid.sub(0..<2)+"XX"+self.Lrid.sub(4..<6)+"YY"
                                self.Lrid=WriteTmp.replace("XX", self.Lrid.sub(6..<8)).replace("YY", self.Lrid.sub(2..<4))
                            }
                            self.Lrid=String(self.Lrid.suffix(self.idcount))
                            self.UpdateUI(self.LR, self.PROGRAM_SUCCESS,true)
                        }
                        if(a.sub(0..<2)=="01"){
                            self.Lfid=a.sub(3..<a.count)
                            if(self.mmynum=="RN1628"||self.mmynum=="SI2048"){
                                let WriteTmp=self.Lfid.sub(0..<2)+"XX"+self.Lfid.sub(4..<6)+"YY"
                                self.Lfid=WriteTmp.replace("XX", self.Lfid.sub(6..<8)).replace("YY", self.Lfid.sub(2..<4))
                            }
                            self.Lfid=String(self.Lfid.suffix(self.idcount))
                            self.UpdateUI(self.LF, self.PROGRAM_SUCCESS,true)}}
                    self.UpdateUiCondition(self.PROGRAM_SUCCESS)
                    if(!condition){
                        for i in self.command.FALSE_CHANNEL{
                            self.UpdateUiCondition(self.PROGRAM_FAULSE)
                            switch(i){
                            case "04":
                                self.Rrid="error"
                                self.UpdateUI(self.RR, self.PROGRAM_FAULSE,true)
                                break
                            case "03":
                                self.Rfid="error"
                                self.UpdateUI(self.RF, self.PROGRAM_FAULSE,true)
                                break
                            case "02":
                                self.Lrid="error"
                                self.UpdateUI(self.LR, self.PROGRAM_FAULSE,true)
                                break
                            case "01":
                                self.Lfid="error"
                                self.UpdateUI(self.LF, self.PROGRAM_FAULSE,true)
                                break
                            default:
                                break
                            } }
                        if(self.command.FALSE_CHANNEL.count==0&&self.command.BLANK_CHANNEL.count==0){
                            self.UpdateUiCondition(self.PROGRAM_FAULSE)
                            self.UpdateUI(self.LF, self.PROGRAM_FAULSE,true)
                            self.UpdateUI(self.LR, self.PROGRAM_FAULSE,true)
                            self.UpdateUI(self.RF, self.PROGRAM_FAULSE,true)
                            self.UpdateUI(self.RR, self.PROGRAM_FAULSE,true)
                        }
                    }
                    for a in self.command.BLANK_CHANNEL{
                        switch(a){
                        case "04":
                            self.UpdateUI(self.RR, self.UN_LINK,true)
                            break
                        case "03":
                            self.UpdateUI(self.RF, self.UN_LINK,true)
                            break
                        case "02":
                            self.UpdateUI(self.LR, self.UN_LINK,true)
                            break
                        case "01":
                            self.UpdateUI(self.LF, self.UN_LINK,true)
                            break
                        default:
                            break
                        }  }
                    
                }
            }
        }
    }
    func UpdateUiCondition(_ position:Int){
        pause()
        switch position {
        case PROGRAN_INSERT:
            TopText.stringValue=Language.SetLanguAge(46)
            ColorTop.color = .init(red: 31/255, green: 73/255, blue: 106/255, alpha: 1)
            TopText.textColor = .init(red: 255/255, green: 222/255, blue: 0/255, alpha: 1)
            break
        case PROGRAM_SUCCESS:
            ColorTop.color = .init(red: 37/255, green: 155/255, blue: 36/255, alpha: 1)
            TopText.textColor = .white
            TopText.stringValue=Language.SetLanguAge(26)
            break
        case PROGRAM_WAIT:
            TopText.stringValue=Language.SetLanguAge(22)
            ColorTop.color = .init(red: 31/255, green: 73/255, blue: 106/255, alpha: 1)
            TopText.textColor = .init(red: 255/255, green: 222/255, blue: 0/255, alpha: 1)
            break
        case PROGRAM_FAULSE:
            TopText.stringValue=Language.SetLanguAge(27)
            TopText.textColor = .white
            ColorTop.color = .init(red: 229/255, green: 28/255, blue: 35/255, alpha: 1)
            break
        case PROGRAMMING:
            TopText.stringValue=Language.SetLanguAge(25)
            ColorTop.color = .init(red: 31/255, green: 73/255, blue: 106/255, alpha: 1)
            TopText.textColor = .init(red: 255/255, green: 222/255, blue: 0/255, alpha: 1)
            self.play("Programming")
            break
        default:
            break
        }
    }
    
    @IBAction func pr(_ sender: Any) {
        ii.image=NSImage.init(named:NSImage.Name.id1)
        pi.image=NSImage.init(named:NSImage.Name.PR2)
        PRORID="PR"
        viewDidLayout()
    }
    
    @IBAction func id(_ sender: Any) {
        ii.image=NSImage.init(named:NSImage.Name.id2)
        pi.image=NSImage.init(named:NSImage.Name.pr1)
        PRORID="ID"
        viewDidLayout()
    }
    func dialogOKCancel(question: String, text: String) -> Bool {
        let alert = NSAlert()
        alert.messageText = question
        alert.informativeText = text
        alert.alertStyle = .warning
        alert.addButton(withTitle: "OK")
        return alert.runModal() == .alertFirstButtonReturn
    }
}
