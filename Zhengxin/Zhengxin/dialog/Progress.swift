//
//  Progress.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/1/30.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//

import UIKit
import Lottie
import JzOsFrameWork
class Progress: UIViewController {
    var label=""
    var program=false
    @IBOutlet var spilView: UIView!
    @IBOutlet var cancelBt: UIButton!
    @IBOutlet weak var tit: UILabel!
    let animationView = AnimationView(name: "simple-loader2")
    override func viewDidLoad() {
        super.viewDidLoad()
        print("label\(label)")
        tit.text=label
        if(label.isEmpty){
            tit.isHidden=true
        }
    }
    override func viewDidAppear(_ animated: Bool) {
        animationView.frame.size = CGSize(width: 200, height: 200)
        animationView.center = CGPoint(x: self.view.center.x, y: (label.isEmpty) ? self.view.center.y:self.view.center.y+10)
        if(program){
            animationView.center = CGPoint(x: self.view.center.x, y: self.view.center.y-10)
        }
        animationView.contentMode = .scaleAspectFit
        animationView.loopMode=LottieLoopMode.loop
        view.addSubview(animationView)
        animationView.play()
        animationView.isUserInteractionEnabled=false
      
    }
    
    @IBAction func cancel(_ sender: Any) {
        print("cancel")
    }
    public static func close(){
        JzActivity.getControlInstance.closeDialLog("Progress")
    }
}
extension String{
    func progress(){
        var progress=Progress()
        progress.label=self
        JzActivity.getControlInstance.openDiaLog(progress, true, "Progress")
    }
}
