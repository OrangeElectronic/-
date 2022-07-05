//
//  ViewController.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/2.
//

import UIKit
import Glitter_IOS
import Glitter_BLE
class ViewController: UIViewController {
    let glitterAct=GlitterActivity.getInstance()
    @IBOutlet var container: UIView!
    override func viewDidLoad() {
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "openQrScanner", function: {
            a in
            let scanner=Page_Scanner()
            self.addChild(scanner)
            self.view.addSubview(scanner.view)
            scanner.view.frame=self.view.safeAreaLayoutGuide.layoutFrame
            self.glitterAct.didMove(toParent: self)
            scanner.scanback = {a in
                self.glitterAct.webView.evaluateJavaScript("glitter.qrScanBack('\(a)')")
            }
        }))
      

     
    }
    var pastime = Date().timeIntervalSince1970
    override func viewDidAppear(_ animated: Bool) {
        Glitter_BLE.getInstance().create()
        Glitter_BLE.debugMode=true
        addChild(glitterAct)
        view.addSubview(glitterAct.view)
        glitterAct.view.frame=view.safeAreaLayoutGuide.layoutFrame
        glitterAct.didMove(toParent: self)
        }
    func GetTime(_ timeStamp: Double)-> Double{
        let currentTime = Date().timeIntervalSince1970
        let reduceTime : TimeInterval = currentTime - timeStamp
        return reduceTime
    }
}
