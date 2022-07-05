//
//  ViewController.swift
//  ZhenZhan_ios
//
//  Created by Jianzhi.wang on 2021/2/20.
//
import UIKit
import Glitter_IOS
import Glitter_BLE
import JzOsHttpExtension
class ViewController: UIViewController {
    let glitterAct=GlitterActivity.getInstance()
    @IBOutlet var container: UIView!
    
    override func viewDidLoad() {
    }
    
    override func viewDidAppear(_ animated: Bool) {
        //Glitter跨平台開發框架
        Glitter_BLE.getInstance().create()
        addChild(glitterAct)
        view.addSubview(glitterAct.view)
        glitterAct.view.frame=view.safeAreaLayoutGuide.layoutFrame
        glitterAct.didMove(toParent: self)
    }
    
}
public extension String{
    func replace(_ target: String, _ withString: String) -> String
    {
        return self.replacingOccurrences(of: target, with: withString, options: NSString.CompareOptions.literal, range: nil)
    }
}
