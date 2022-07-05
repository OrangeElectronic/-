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

class ViewController: UIViewController {
    let glitterAct=GlitterActivity.getInstance()
    //var player: AVAudioPlayer?
    //let player = AVPlayer()
    var turnScreen = true
    var looper: AVPlayerLooper? = nil
    let player = AVQueuePlayer()
    var musicFile = "one_song_fix"
    var playMusic = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //print("viewDidLoad")
        Glitter_BLE.getInstance().create()
        Glitter_BLE.debugMode=true
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
        case .portraitUpsideDown:
            print("螢幕倒立")
            //if(turnString != "PORTRAIT"){
                glitterAct.webView.evaluateJavaScript("glitter.orientationPage('PORTRAIT')")
                turnString = "PORTRAIT"
//            }else{
//                return
//            }
            break
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
            
                        //glitterAct.webView.frame = self.view.frame
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
