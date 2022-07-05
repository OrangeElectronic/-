//
//  sheet.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/9.
//

import Foundation
import JzOsFrameWork
import UIKit

class BottomSheet: UIViewController {
    var tableView: UITableView? = nil
    let fullView: CGFloat = 0
    var controler:UIViewController! = nil
    var partialView: CGFloat {
        return UIScreen.main.bounds.height - 50
    }
    var dtag=""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("viewDidLoad")
    }
    func listallView(_ inview:[UIView])->[UIView]{
        var views=[UIView]()
        for view in inview {
            views.append(view)
            if(view.subviews.count>0){
               views.append(contentsOf: listallView(view.subviews))
            }
        }
      return views
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        for i in listallView(self.view.subviews){
                   print(i)
                   if(i is UITableView){
                    self.tableView=(i as! UITableView)
                   }
               }
               
               if(tableView != nil){
                   tableView!.bounces=false
               }
               let gesture = UIPanGestureRecognizer.init(target: self, action: #selector(BottomSheet.panGesture))
               gesture.delegate = self
               view.addGestureRecognizer(gesture)
//        prepareBackgroundView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        UIView.animate(withDuration: 0.6, animations: { [weak self] in
            let frame = self?.view.frame
            let yComponent = self?.fullView
            self?.view.frame = CGRect(x: 0, y: yComponent!, width: frame!.width, height: frame!.height )
        })
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func panGesture(_ recognizer: UIPanGestureRecognizer) {
        
        let translation = recognizer.translation(in: self.view)
        let velocity = recognizer.velocity(in: self.view)
        
        let y = self.view.frame.minY
        if (y + translation.y >= fullView) && (y + translation.y <= partialView) {
            self.view.frame = CGRect(x: 0, y: y + translation.y, width: view.frame.width, height: view.frame.height)
            recognizer.setTranslation(CGPoint.zero, in: self.view)
        }
        
        if recognizer.state == .ended {
            var duration =  velocity.y < 0 ? Double((y - fullView) / -velocity.y) : Double((partialView - y) / velocity.y )
            var close=false
            duration = duration > 1.3 ? 1 : duration
            UIView.animate(withDuration: duration, delay: 0.0, options: [.allowUserInteraction], animations: {
                if  velocity.y >= 0 {
                    print("UIView_close")
                    close=true
                    self.view.frame = CGRect(x: 0, y: self.partialView, width: self.view.frame.width, height: self.view.frame.height)
                } else {
                    print("UIView_open")
                    close=false
                    self.view.frame = CGRect(x: 0, y: self.fullView, width: self.view.frame.width, height: self.view.frame.height)
                }
                
            }, completion: { [weak self] _ in
                if(close){
                    JzActivity.getControlInstance.closeDialLog(self!.dtag)
                }
                if ( velocity.y < 0 ) {
                    if(self?.tableView != nil){
                        self?.tableView!.isScrollEnabled = true
                    }
                }
            })
        }
    }
    
    func open(){
        
    }
//    func prepareBackgroundView(){
//        let blurEffect = UIBlurEffect.init(style: .dark)
//        let visualEffect = UIVisualEffectView.init(effect: blurEffect)
//        let bluredView = UIVisualEffectView.init(effect: blurEffect)
//        bluredView.contentView.addSubview(visualEffect)
//        visualEffect.frame = UIScreen.main.bounds
//        bluredView.frame = UIScreen.main.bounds
//        view.insertSubview(bluredView, at: 0)
//    }
    
}



extension BottomSheet: UIGestureRecognizerDelegate {
    
    // Solution
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        let gesture = (gestureRecognizer as! UIPanGestureRecognizer)
        let direction = gesture.velocity(in: view).y
        
        let y = view.frame.minY
        if(tableView != nil){
            if (y == fullView && tableView!.contentOffset.y == 0 && direction > 0) || (y == partialView) {
                tableView!.isScrollEnabled = false
            } else {
                tableView!.isScrollEnabled = true
            }
        }
        return false
    }
}
