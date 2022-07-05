//
//  QrScanner.swift
//  txusb
//
//  Created by 王建智 on 2019/7/18.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import JzIos_Framework
import AVFoundation
import UIKit
import SQLite3
class QrScanner: UIViewController, AVCaptureMetadataOutputObjectsDelegate {
    var captureSession: AVCaptureSession!
    var previewLayer: AVCaptureVideoPreviewLayer!
    var idcopy:Idcopy!=nil
    let deledate = UIApplication.shared.delegate as! AppDelegate
    var VS_or_ID=0
    var idcount=0
    var editext:UITextField!=nil
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    @IBOutlet var scantitle: UILabel!
    @IBOutlet var Qrplace: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        if(VS_or_ID==1){
            scantitle.text=SetLan.Setlan("Please_scan_the_QR_Code_on_the_catalog_or_poster")
        }else{
            scantitle.text=SetLan.Setlan("Scan_Two")
        }
        captureSession = AVCaptureSession()
        guard let videoCaptureDevice = AVCaptureDevice.default(for: .video) else { return }
        let videoInput: AVCaptureDeviceInput
        
        do {
            videoInput = try AVCaptureDeviceInput(device: videoCaptureDevice)
        } catch {
            return
        }
        
        if (captureSession.canAddInput(videoInput)) {
            captureSession.addInput(videoInput)
        } else {
            failed()
            return
        }
        
        let metadataOutput = AVCaptureMetadataOutput()
        
        if (captureSession.canAddOutput(metadataOutput)) {
            captureSession.addOutput(metadataOutput)
            
            metadataOutput.setMetadataObjectsDelegate(self, queue: DispatchQueue.main)
            metadataOutput.metadataObjectTypes = [.dataMatrix]
        } else {
            failed()
            return
        }
        
        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.frame = Qrplace.layer.bounds
        previewLayer.videoGravity = .resizeAspectFill
        Qrplace.layer.addSublayer(previewLayer)
        
        captureSession.startRunning()
    }
    
    func failed() {
        let ac = UIAlertController(title: "Scanning not supported", message: "Your device does not support scanning a code from an item. Please use a device with a camera.", preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: "OK", style: .default))
        present(ac, animated: true)
        captureSession = nil
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if (captureSession?.isRunning == false) {
            captureSession.startRunning()
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        if (captureSession?.isRunning == true) {
            captureSession.stopRunning()
        }
    }
    
    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
        captureSession.stopRunning()
        
        if let metadataObject = metadataObjects.first {
            guard let readableObject = metadataObject as? AVMetadataMachineReadableCodeObject else { return }
            guard let stringValue = readableObject.stringValue else { return }
            AudioServicesPlaySystemSound(SystemSoundID(kSystemSoundID_Vibrate))
            found(code: stringValue)
        }
        
        //        dismiss(animated: true)
    }
    
    func found(code: String) {
        var fullNameArr = code.components(separatedBy:"*")
        if(fullNameArr.count<3){fullNameArr=code.components(separatedBy:":")}
        print("\(code) \(fullNameArr)\t\(idcount) \(fullNameArr.count)")
        if(fullNameArr.count>=3){
            if(VS_or_ID==1){
                if(fullNameArr[1].count != idcount){
                    if (captureSession?.isRunning == false) {
                        captureSession.startRunning()
                    }
                    act.view.showToast(text:SetLan.Setlan("ID_code_should_be_8_characters").replace("8", "\(idcount)") )
                    captureSession.startRunning()
                    return }
                editext.text=fullNameArr[1]
                act.GoBack(self)
            }else{
                GoOk(code)
            }
            
        }else{
            if(VS_or_ID==1){
                view.showToast(text:SetLan.Setlan("Please_scan_the_QR_Code_on_the_catalog_or_poster"))
            }else{
                view.showToast(text:SetLan.Setlan("Scan_Two"))
                
            }
            captureSession.startRunning()
        }
        
    }
    func GoOk(_ code:String){
         if(code.components(separatedBy: "*").count>1){
                        print("init*\(code.components(separatedBy: "*")[0])")
                        PublicBeans.資料庫.query("select `Make`,`Model`,`Year`  from `Summary table` where `Direct Fit` not in('NA')  and `MMY number`='\(code.components(separatedBy: "*")[0])' limit 0,1", {
                            data in
                            PublicBeans.Make=data.getString(0)
                            PublicBeans.Model=data.getString(1)
                            PublicBeans.Year=data.getString(2)
                        print("init\(code.components(separatedBy: "*")[0])")
                            let a=peacedefine().Relarm
                                          a.position=1
        JzActivity.getControlInstance.changePage(a, "Page_Relearn", true)
                        }, {
                            print("resultsuccess")
                        })
                    }
    }
    override var prefersStatusBarHidden: Bool {
        return true
    }
    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        return .portrait
    }
    
}
