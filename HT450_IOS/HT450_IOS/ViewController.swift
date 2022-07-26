//
//  ViewController.swift
//  HT450_IOS
//
//  Created by Orangerd on 2022/1/25.
//

import UIKit
import Glitter_IOS
import Glitter_BLE
import AVFoundation
import JzOsHttpExtension
import NordicDFU

class ViewController: UIViewController, LoggerDelegate, DFUProgressDelegate, DFUServiceDelegate{
    func dfuProgressDidChange(for part: Int, outOf totalParts: Int, to progress: Int, currentSpeedBytesPerSecond: Double, avgSpeedBytesPerSecond: Double) {
        print("dfuProgress_part:"+String(progress))
        glitterAct.webView.evaluateJavaScript("glitter.deflationTime('<br>"+String(progress)+"%');")
    }
    
    func logWith(_ level: LogLevel, message: String) {
        print("dfuProgress_level:"+String(message))
    }
    
    func dfuStateDidChange(to state: DFUState) {
        print("dfuProgress_state:"+state.description)
        if(state.description == "Completed"){
            glitterAct.webView.evaluateJavaScript("glitter.deflationTime('success');")
        }
    }
    
    func dfuError(_ error: DFUError, didOccurWithMessage message: String) {
        print("dfuProgress_error:"+message)
        glitterAct.webView.evaluateJavaScript("glitter.deflationTime('error');")
    }
    
    
    let glitterAct=GlitterActivity.getInstance()
    //var player: AVAudioPlayer?
    //let player = AVPlayer()
    var turnScreen = true
    var looper: AVPlayerLooper? = nil
    let player = AVQueuePlayer()
    var musicFile = "one_song_fix"
    var playMusic = false
    
    let glitterBle = Glitter_BLE.self
    override func viewDidLoad() {
        super.viewDidLoad()
        //print("viewDidLoad")
//        Glitter_BLE.getInstance().create()
//        Glitter_BLE.debugMode=true
        glitterBle.getInstance().create()
        glitterBle.debugMode=true
        
        
        // Do any additional setup after loading the view.
        
        registerNotification()
        
        //try? AVAudioSession.sharedInstance().setCategory(.playback)
        
        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default, options: [.mixWithOthers, .allowAirPlay])
            print("Sound2_Playback OK")
            try AVAudioSession.sharedInstance().setActive(true)
            print("Sound2_Session is Active")
        } catch {
            print("Sound2_",error)
        }
        
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "turnScreen", function: {
                request in
        
            let open=request.receiveValue["open"] as! Bool
            self.turnScreen=open
            request.responseValue["turn"] = self.turnScreen
            if(!self.turnScreen){
                print("noturnScreen1:true")
                self.forceOrientationPortrait()
//                self.glitterAct.webView.evaluateJavaScript("glitter.orientationPage('PORTRAIT')")
            }

                    request.finish()
        
        }))
        //playSound()
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "playSound", function: {
                request in
            let play=request.receiveValue["play"] as! String
                        if(play=="true"){
                            self.playMusic=true
                            if self.looper==nil{
                                self.playSound()
                            }else{
                                //self.playSound()
                                self.player.play()
                            }
                            
                            //mediaPlayer.isLooping = true
                        }else{
                            self.playMusic=false
                            self.player.pause()
                            //pauseMusic()
                            //mediaPlayer.isLooping = false
                        }
            print("Soundplay:",play)

                        request.finish()
                    }))
        
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "changeSound", function: {
                request in
            let music=request.receiveValue["music"] as! String
                            //let play = self.looper
            let status = self.player.rate;
            print("status1:",status)
            //**
//                            if(status != 0.0){
//                                self.player.pause()
//                            }
            if(self.playMusic){
                self.player.pause()
            }
                            switch music {
                                case "mute":
                                self.musicFile = "zero_song_fix";
                                    break
                                
                                case "01":
                                self.musicFile = "one_song_fix";
                                    break
                                
                                case "02":
                                self.musicFile = "two_song_fix";
                                    break
                                
                                case "03":
                                self.musicFile = "three_song_fix";
                                    break
                            default:
                                break
                                
                            }
                            print("Soundchange:",self.musicFile)
                            self.playSound()
                            
            print("status2:",status)
                        if(!self.playMusic){
                            //**
                            //if(status == 0.0){
                                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.5) {
                                    self.player.pause()
                                }
                            
                            }

                        request.finish()
                    }))
        
        //取得系統版本資訊
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "keepScreen", function: {
                    request in
            let keep=request.receiveValue["keep"] as! String
            if(keep == "true"){
                UIApplication.shared.isIdleTimerDisabled = true
            }else{
                UIApplication.shared.isIdleTimerDisabled = false
            }
//
                    request.finish()
                }))
        
//        if let currentUserInstallVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String {
//                    print("currentUserInstallVersion:\(currentUserInstallVersion)")
//
//                }
        let currentUserInstallVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"]
        
        //取得系統版本資訊
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "getSystem_Version", function: {
                    request in
            print("getSystemVersion:\(currentUserInstallVersion)")
            
                    request.responseValue["version"]=UIDevice.current.systemVersion
                    request.responseValue["model"]=UIDevice.modelName
                    request.responseValue["make"]="Apple"
                    //request.responseValue["appVersion"]=Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
            
                    request.responseValue["app_version"]=currentUserInstallVersion
            //Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
                    request.finish()
                }))
        
        
        var DFUFirmwareFile = URL(string: "String")
        var DFUDatFile = URL(string: "String")
        //檔案下載並解壓縮
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "getDownloadFile", function: {
            request in
            DispatchQueue.global().async {
                let appName = request.receiveValue["app_type"] as! String
                
                let download_data = "https://bento3.orange-electronic.com/getVersion?country=JP%20Truck&type="+appName+"&beta=true"
                let fileData=HttpCore.get(download_data,TimeInterval(30*1000))
                var fileDataName = ""
                do {
                    let jsonResult = try JSONSerialization.jsonObject(with: fileData!, options: JSONSerialization.ReadingOptions.mutableContainers) as? NSDictionary
            
                        // 解析 JSON 資料 mcuVerion mmyVersion
                    let jsonLoans = jsonResult?["mcuVerion"] as! String
//                        let jsonLoans = jsonResult?["loans"] as! [AnyObject]
//                        for jsonLoan in jsonLoans {
//                            var loan = Loan()
//                            loan.name = jsonLoan["name"] as! String
//                            loan.amount = jsonLoan["loan_amount"] as! Int
//                            loans.append(loan)
//                        }
                    
                    print("fileData:"+jsonLoans)
                    fileDataName = jsonLoans

                    } catch {
                        print(error)
                    }
//                let decoder = JSONDecoder()
//                let createResponse = try! decoder.decode(_, from: fileData!)
//                print("fileData:"+fileData!.description)
                if(fileData == nil){
                    request.responseValue["result"]="false"
                    request.finish()
                    return
                }
                
                let fileHex = fileDataName
                //let fileHex = "HT450_ble_app_template.hex"
                //let fileHex = "ble_app_template_20171121.hex"
                let download_url = "https://bento3.orange-electronic.com/Orange%20Cloud/Beta/Drive/"+appName+"/Firmware/"
                let rout=download_url+fileHex
                print("result->rout->\(rout)")
                let timeOut=1000*10
                
                let fileName="fileName.hex"
                //let datName="fileName.dat"
                let file=HttpCore.get(rout,TimeInterval(30*1000))
                let dst =  NSHomeDirectory() + "/Documents/\(fileName)"
                //let dat =  NSHomeDirectory() + "/Documents/\(datName)"
                
                let routArray=dst.split(separator: "/")
                print("result->startCreateRout:\(routArray.count)")
                print("result->createRout:\(dst.sub(0..<(dst.count-routArray[routArray.count-1].count)))")
                let fm = FileManager.default
                if !fm.fileExists(atPath: dst) {
                    try! fm.createDirectory(atPath: dst.sub(0..<(dst.count-routArray[routArray.count-1].count-1)), withIntermediateDirectories: true, attributes: nil)
                    try! fm.createFile(atPath: dst, contents: nil, attributes: nil)
                }
                
//                let datArray=dat.split(separator: "/")
//                print("result->startCreateRout:\(datArray.count)")
//                print("result->createRout:\(dat.sub(0..<(dat.count-datArray[datArray.count-1].count)))")
//                let fmdat = FileManager.default
//                if !fmdat.fileExists(atPath: dat) {
//                    try! fmdat.createDirectory(atPath: dat.sub(0..<(dst.count-datArray[datArray.count-1].count-1)), withIntermediateDirectories: true, attributes: nil)
//                    try! fmdat.createFile(atPath: dat, contents: nil, attributes: nil)
//                }
                
                let urlfrompath = URL(fileURLWithPath: dst)
                //let urldatpath = URL(fileURLWithPath: dat)
                
                DFUFirmwareFile = urlfrompath
                //DFUDatFile = urldatpath
                
                print("加載路徑:\(urlfrompath)")
                if(file==nil){
                    print("下載失敗")
                    request.responseValue["result"]="false"
                    request.finish()
                }else{
                    do{
                        try file?.write(to: urlfrompath)
                        do {
                            let date=Date().timeIntervalSince1970
                            //let unzipPath=(NSHomeDirectory() + "/Documents/\(date)/")
                            let unzipPath=(NSHomeDirectory() + "/Documents/")
                            let fm = FileManager.default
                            print("result->date->\(date)")
                            try? fm.createDirectory(atPath: unzipPath, withIntermediateDirectories: true, attributes: nil)
//                            SSZipArchive.unzipFile(atPath: urlfrompath.path, toDestination: unzipPath)
                            let fileList = try FileManager.default.contentsOfDirectory(atPath:unzipPath)
                            print("檔案->\(fileList.count),\(fileList)")
                            
                            for item in fileList {
                                if(item == fileName){
                                    request.responseValue["result"]="true"
                                    break
                                }
                               }
                            
                            request.finish()
//                            if(fileList.count==1){
//                                let loading =  try Data(contentsOf: URL(fileURLWithPath: "\(unzipPath)\(fileList[0])"))
//                                print("檔案解壓->\(fileName)-\(loading.count)")
//                                var isDirectory:ObjCBool = false
//                                let path=NSHomeDirectory() + "/Documents/\(fileName)"
//                                let isExist = FileManager.default.fileExists(atPath: path, isDirectory: &isDirectory)
//                                if(isExist){
//                                    try FileManager.default.removeItem(atPath:NSHomeDirectory() + "/Documents/\(fileName)")
//                                }
//                                try FileManager.default.copyItem(atPath:  "\(unzipPath)\(fileList[0])", toPath:   NSHomeDirectory() + "/Documents/\(fileName)")
//                                request.responseValue["result"]=true
//                                request.finish()
//                            }else{
//                                request.responseValue["result"]=false
//                                request.finish()
//                            }
                        }
                        catch {
                            print("error:\(error)")
                            print("Something went wrong")
                            request.responseValue["result"]="false"
                            request.finish()
                        }
                        
                    }catch{
                        print("error:\(error)")
                        request.responseValue["result"]="false"
                        request.finish()
                    }
                }
                print("result->\(request.responseValue["result"])")
            }
        }))
        
        //檔案下載並解壓縮
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "DFU", function: {
            request in
           
            let start_DFU = request.receiveValue["start_DFU"] as! String

            print("Dfu:"+start_DFU)
            //let selectedFirmware = try? DFUFirmware(urlToBinOrHexFile: DFUFirmwareFile!, urlToDatFile: DFUFirmwareFile, type: .application)
            let selectedFirmware = try! DFUFirmware(urlToBinOrHexFile: DFUFirmwareFile!, urlToDatFile: nil, type: .application)
            //let selectedFirmware = try! DFUFirmware(urlToBinOrHexFile: DFUFirmwareFile!, type: .application)
            let initiator = DFUServiceInitiator().with(firmware: selectedFirmware)
            // Optional:
            // initiator.forceDfu = true/false // default false
            // initiator.packetReceiptNotificationParameter = N // default is 12
            initiator.logger = self // - to get log info
            initiator.delegate = self // - to be informed about current state and errors
            initiator.progressDelegate = self // - to show progress bar
            // initiator.peripheralSelector = ... // the default selector is used
            //let controller = initiator.start(target: peripheral)
            //let controller = initiator.start(target: )
            //let controller = initiator.start(target: glitterBle.getInstance().deviceList[0])
//            if self.glitterBle.instance!.deviceList.contains(where: { $0.name == "DfuTarg" }) {
//                return
//            }
            let peripheral = self.glitterBle.instance!.deviceList.filter { CBPeripheral in
                CBPeripheral.name == "DfuTarg"
            }
            if(peripheral.count > 0 ){
                print("dfuProgress_name:"+peripheral[0].name!)
                let controller = initiator.start(target:peripheral[0])
            }
        
            if(start_DFU=="true"){
                
            }
            
            request.finish()
            
        }))
        
    }
    
    func playSound() {
        print("func:playSound")

        //**
        let fileUrl = Bundle.main.url(forResource: musicFile, withExtension: "mp3")!
        
        do {
        
        let item = AVPlayerItem(url: fileUrl)
        looper = AVPlayerLooper(player: player, templateItem: item)
        player.play()
        }catch let error {
            print("playSound_mp3:"+error.localizedDescription)
        }
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        addChild(glitterAct)
        view.addSubview(glitterAct.view)
        glitterAct.view.frame=view.safeAreaLayoutGuide.layoutFrame
        glitterAct.didMove(toParent: self)
        //print("viewDidAppear")
    }
    
    var turnString = ""
    // 强制旋转竖屏
        func forceOrientationPortrait() {
            
            let oriention = UIInterfaceOrientation.portrait // 设置屏幕为竖屏
            UIDevice.current.setValue(oriention.rawValue, forKey: "orientation")
            //UIViewController.attemptRotationToDeviceOrientation()
            if(turnString=="LANDSCAPE"){
                glitterAct.view.frame=view.safeAreaLayoutGuide.layoutFrame
                if(glitterAct.webView != nil){
                    glitterAct.webView.frame=glitterAct.view.bounds
                }
            }
            
        }
    
    @objc func receiverNotification(){
        
        let orient = UIDevice.current.orientation
        if(glitterAct.webView == nil){
            return
        }
        if(!turnScreen){
            glitterAct.webView.evaluateJavaScript("glitter.orientationPage('PORTRAIT')")
            forceOrientationPortrait()
            turnString = "PORTRAIT"
            return
        }
        print("noturnScreen2:",turnString)
        
        switch orient {
        case .portrait :
            print("螢幕正常豎向")
            //if(turnString != "PORTRAIT"){
                glitterAct.webView.evaluateJavaScript("glitter.orientationPage('PORTRAIT')")
                 turnString = "PORTRAIT"
//            }else{
//                return
//            }
            
            break
            //此方面似乎要看手機是否有可倒立轉動
//        case .portraitUpsideDown:
//            print("螢幕倒立")
//            //if(turnString != "PORTRAIT"){
//                glitterAct.webView.evaluateJavaScript("glitter.orientationPage('PORTRAIT')")
//                turnString = "PORTRAIT"
////            }else{
////                return
////            }
//            break
        case .landscapeLeft:
            print("螢幕左旋轉")
            if(turnScreen){
                //f(turnString != "LANDSCAPE"){
                    glitterAct.webView.evaluateJavaScript("glitter.orientationPage('LANDSCAPE')")
                    turnString = "LANDSCAPE"
//                }else{
//                    return
//                }
            }
            break
        case .landscapeRight:
            print("螢幕右旋轉")
            if(turnScreen){
                //if(turnString != "LANDSCAPE"){
                    glitterAct.webView.evaluateJavaScript("glitter.orientationPage('LANDSCAPE')")
                    turnString = "LANDSCAPE"
//                }else{
//                    return
//                }
            }
            break
        default:
            break }
        
        print("noturnScreen3:",turnString)
       
        //glitterAct.view.frame=view.safeAreaLayoutGuide.layoutFrame
        if(glitterAct.webView != nil){
            self.glitterAct.webView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
//            self.glitterAct.webView.translatesAutoresizingMaskIntoConstraints = false
            
//                        glitterAct.webView.frame = self.view.frame
            self.glitterAct.webView.frame=self.glitterAct.view.bounds
        
//            if(turnScreen){
//            switch orient {
//            case .portraitUpsideDown,.portrait:
//                let height = self.glitterAct.webView.scrollView.contentSize.height
//                //let height = self.glitterAct.webView.frame.height
//                self.glitterAct.webView.bounds = CGRect(x: 0, y: 0, width: self.view.frame.width, height: Double(height))
//                break
//            default:
////                let width = self.glitterAct.webView.scrollView.contentSize.width
////                //let height = self.glitterAct.webView.frame.height
////                self.glitterAct.webView.bounds = CGRect(x: 0, y: 0, width: Double(width), height:self.view.frame.height )
//                break
//            }
//            }

        }
     
        glitterAct.view.backgroundColor = .black
    }
    
    func registerNotification() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.receiverNotification), name: UIDevice.orientationDidChangeNotification, object: nil)
        
        if #available(iOS 13.0, *) {
            NotificationCenter.default.addObserver(self, selector: #selector(willResignActive), name: UIScene.willDeactivateNotification, object: nil)
        } else {
            NotificationCenter.default.addObserver(self, selector: #selector(willResignActive), name: UIApplication.willResignActiveNotification, object: nil)
        }
        NotificationCenter.default.addObserver(self, selector: #selector(willEnterForeground), name: UIScene.willEnterForegroundNotification, object: nil)
        
//        registerforDeviceLockNotification
        //Screen lock notifications
        CFNotificationCenterAddObserver(CFNotificationCenterGetDarwinNotifyCenter(),     //center
            Unmanaged.passUnretained(self).toOpaque(),     // observer
            displayStatusChangedCallback,     // callback
            "com.apple.springboard.lockcomplete" as CFString,     // event name
            nil,     // object
            .deliverImmediately)
        
        CFNotificationCenterAddObserver(CFNotificationCenterGetDarwinNotifyCenter(),     //center
            Unmanaged.passUnretained(self).toOpaque(),     // observer
            displayStatusChangedCallback,     // callback
            "com.apple.springboard.lockstate" as CFString,    // event name
            nil,     // object
            .deliverImmediately)
    }

    private let displayStatusChangedCallback: CFNotificationCallback = { _, cfObserver, cfName, _, _ in
        guard let lockState = cfName?.rawValue as? String else {
            return
        }

        let catcher = Unmanaged<ViewController>.fromOpaque(UnsafeRawPointer(OpaquePointer(cfObserver)!)).takeUnretainedValue()
        catcher.displayStatusChanged(lockState)
    }

    private func displayStatusChanged(_ lockState: String) {
        // the "com.apple.springboard.lockcomplete" notification will always come after the "com.apple.springboard.lockstate" notification
        print("Darwin notification NAME = \(lockState)")
        if (lockState == "com.apple.springboard.lockcomplete") {
            print("Screen DEVICE LOCKED")
        } else {
            print("Screen LOCK STATUS CHANGED")
        }
        
        if(self.playMusic){
            let status = self.player.rate;
            print("Screen_LOCKED:",status)
            
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1) {
                if(self.playMusic){
                    self.playSound()
                }
            }
        }
    }
    
    @objc func willResignActive(_ notification: Notification) {
        // code to execute
        print("Screen Background")
        
        if(self.playMusic){
            let status = self.player.rate;
            print("Screen_Background:",status)
            
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1) {
                if(self.playMusic){
                    self.playSound()
                }
            }
        }
    }
    
    @objc func willEnterForeground(_ notification: Notification) {
        print("Screen Foreground")
        
        if(self.playMusic){
            let status = self.player.rate;
            print("Screen_Foreground:",status)
            
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1) {
                if(self.playMusic){
                    self.playSound()
                }
            }
        }
    }
    
}
extension String{
    func  sub(_ range: CountableRange<Int>) -> String {
        let idx1 = index(startIndex, offsetBy: max(0, range.lowerBound))
        let idx2 = index(startIndex, offsetBy: min(self.count, range.upperBound))
        return String(self[idx1..<idx2])
    }
}
