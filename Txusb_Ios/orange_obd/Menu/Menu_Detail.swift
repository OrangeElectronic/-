//
//  Menu_Detail.swift
//  txusb
//
//  Created by 王建智 on 2019/8/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import ViewPager_Swift
class Menu_Detail: UIViewController,ViewPagerDataSource,ViewPagerDelegate{
    let tabs1 = [
        ViewPagerTab(title: "1", image: nil),
        ViewPagerTab(title: "2", image: nil),
        ViewPagerTab(title: "3", image: nil),
        ViewPagerTab(title: "4", image: nil),
        ViewPagerTab(title: "5", image: nil),
        ViewPagerTab(title: "6", image: nil),
        ViewPagerTab(title: "7", image: nil),
        ViewPagerTab(title: "8", image: nil),
        ViewPagerTab(title: "9", image: nil),
        ViewPagerTab(title: "10", image: nil),
        ViewPagerTab(title: "11", image: nil)
    ]
    func tabsForPages() -> [ViewPagerTab] {
        return tabs1
    }
    
    func willMoveToControllerAtIndex(index: Int) {
        print("移動\(index)")
        Clear()
        switch index {
        case 0:
            c1.backgroundColor=UIColor.init(named: "btncolor")
        case 1:
            c2.backgroundColor=UIColor.init(named: "btncolor")
        case 2:
            c3.backgroundColor=UIColor.init(named: "btncolor")
        case 3:
            c4.backgroundColor=UIColor.init(named: "btncolor")
        case 4:
            c5.backgroundColor=UIColor.init(named: "btncolor")
        case 5:
            c6.backgroundColor=UIColor.init(named: "btncolor")
        case 6:
            c7.backgroundColor=UIColor.init(named: "btncolor")
        case 7:
            c8.backgroundColor=UIColor.init(named: "btncolor")
        case 8:
            c9.backgroundColor=UIColor.init(named: "btncolor")
        case 9:
            c10.backgroundColor=UIColor.init(named: "btncolor")
        case 10:
            c11.backgroundColor=UIColor.init(named: "btncolor")
        default:
            break
        }
        return
    }
    
    func didMoveToControllerAtIndex(index: Int) {
     
        return
    }
    
    func numberOfPages() -> Int {
        if(place==0){return 9}else{return 11}
    }
    
    func viewControllerAtPosition(position: Int) -> UIViewController {
        if(place==0){
            switch position {
            case 0:
                let a=peacedefine().P1
                return a
            case 1:
                let a=peacedefine().P2
                return a
            case 2:
                let a=peacedefine().P3
                return a
            case 3:
                let a=peacedefine().P4
                return a
            case 4:
                let a=peacedefine().P5
                return a
            case 5:
                let a=peacedefine().P6
                return a
            case 6:
                let a=peacedefine().P7
                return a
            case 7:
                let a=peacedefine().P8
                return a
            case 8:
                let a=peacedefine().P9
                return a
            default:
                break
            }
        }else{
            switch position {
            case 0:
                let a=peacedefine().I1
                return a
            case 1:
                let a=peacedefine().I2
                return a
            case 2:
                let a=peacedefine().I3
                return a
            case 3:
                let a=peacedefine().I4
                return a
            case 4:
                let a=peacedefine().I5
                return a
            case 5:
                let a=peacedefine().I6
                return a
            case 6:
                let a=peacedefine().I7
                return a
            case 7:
                let a=peacedefine().I8
                return a
            case 8:
                let a=peacedefine().I9
                return a
            case 9:
                let a=peacedefine().I10
                return a
            case 10:
                let a=peacedefine().I11
                return a
            default:
                break
            }
        }
        let a=peacedefine().P1
        return a
    }
    func Clear(){
        c1.backgroundColor=UIColor.init(named: "Circle")
        c2.backgroundColor=UIColor.init(named: "Circle")
        c3.backgroundColor=UIColor.init(named: "Circle")
        c4.backgroundColor=UIColor.init(named: "Circle")
        c5.backgroundColor=UIColor.init(named: "Circle")
        c6.backgroundColor=UIColor.init(named: "Circle")
        c7.backgroundColor=UIColor.init(named: "Circle")
        c8.backgroundColor=UIColor.init(named: "Circle")
        c9.backgroundColor=UIColor.init(named: "Circle")
        c10.backgroundColor=UIColor.init(named: "Circle")
        c11.backgroundColor=UIColor.init(named: "Circle")
    }
   
    
    func startViewPagerAtIndex() -> Int {
        return 0
    }
    override func viewWillDisappear(_ animated: Bool) {
        loop=false
    }
    var loop=true
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    @IBOutlet var Display: UIView!
    var viewPager:ViewPager!
    @IBOutlet var c1: UIView!
    @IBOutlet var c2: UIView!
    @IBOutlet var c11: UIView!
    @IBOutlet var c10: UIView!
    @IBOutlet var c9: UIView!
    @IBOutlet var c8: UIView!
    @IBOutlet var c7: UIView!
    @IBOutlet var c6: UIView!
    @IBOutlet var c5: UIView!
    @IBOutlet var c4: UIView!
    @IBOutlet var c3: UIView!
    @IBOutlet var goback: UIButton!
    var place=0
    override func viewDidLoad() {
        super.viewDidLoad()
        if(place==1){c10.isHidden=false
            c11.isHidden=false}
        let myOptions = ViewPagerOptions()
        myOptions.viewPagerTransitionStyle = .scroll
        myOptions.isTabIndicatorAvailable=false
        myOptions.tabViewHeight=0
        viewPager = ViewPager(viewController: self, containerView: Display)
        viewPager.setOptions(options: myOptions)
        viewPager.setDataSource(dataSource: self)
        viewPager.setDelegate(delegate: self)
        viewPager.build()
        DispatchQueue.global().async {
            self.Changer()
        }
        goback.setTitle(SetLan.Setlan("Users_manual"), for: .normal)
    }
    func Changer(){
     
    }
    override func viewWillAppear(_ animated: Bool) {
        if(place==0){
             act.tit.text="Program"
        }else{
             act.tit.text="ID COPY"
        }
    }
    
    @IBAction func Tomenu(_ sender: Any) {
        act.GoBack(self)
    }
}
