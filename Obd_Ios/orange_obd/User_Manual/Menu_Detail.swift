//
//  Menu_Detail.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/19.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import ViewPager_Swift
class Menu_Detail: UIViewController ,ViewPagerDataSource,ViewPagerDelegate{
     let act=(UIApplication.shared.delegate as! AppDelegate).act!
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
        ViewPagerTab(title: "11", image: nil),
         ViewPagerTab(title: "12", image: nil)
    ]
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
        c12.backgroundColor=UIColor.init(named: "Circle")
    }
    
    func willMoveToControllerAtIndex(index: Int) {
        print("移動\(index)")
        Clear()
        switch index {
        case 0:
            c1.backgroundColor=UIColor.init(named: "Color")
        case 1:
            c2.backgroundColor=UIColor.init(named: "Color")
        case 2:
            c3.backgroundColor=UIColor.init(named: "Color")
        case 3:
            c4.backgroundColor=UIColor.init(named: "Color")
        case 4:
            c5.backgroundColor=UIColor.init(named: "Color")
        case 5:
            c6.backgroundColor=UIColor.init(named: "Color")
        case 6:
            c7.backgroundColor=UIColor.init(named: "Color")
        case 7:
            c8.backgroundColor=UIColor.init(named: "Color")
        case 8:
            c9.backgroundColor=UIColor.init(named: "Color")
        case 9:
            c10.backgroundColor=UIColor.init(named: "Color")
        case 10:
            c11.backgroundColor=UIColor.init(named: "Color")
        case 11:
            c12.backgroundColor=UIColor.init(named: "Color")
        default:
            break
        }
        return
    }
    
    func didMoveToControllerAtIndex(index: Int) {
         return
    }
    
    func numberOfPages() -> Int {
        return 12
    }
    
    func viewControllerAtPosition(position: Int) -> UIViewController {
        switch position {
        case 0:
            let a=peacedefine().M1
            return a
        case 1:
            let a=peacedefine().M2
            return a
        case 2:
            let a=peacedefine().M3
            return a
        case 3:
            let a=peacedefine().M4
            return a
        case 4:
            let a=peacedefine().M5
            return a
        case 5:
            let a=peacedefine().M6
            return a
        case 6:
            let a=peacedefine().M7
            return a
        case 7:
            let a=peacedefine().M8
            return a
        case 8:
            let a=peacedefine().M9
            return a
        case 9:
            let a=peacedefine().M10
            return a
        case 10:
            let a=peacedefine().M11
            return a
        case 11:
            let a=peacedefine().M12
            return a
        default:
            let a=peacedefine().M1
            return a
        }
    }
    
    func tabsForPages() -> [ViewPagerTab] {
         return tabs1
    }
    
    func startViewPagerAtIndex() -> Int {
         return 0
    }
    
    @IBOutlet var goback: UIButton!
    @IBOutlet var Display: UIView!
    var viewPager:ViewPager!
    @IBOutlet var c1: UIView!
    @IBOutlet var c2: UIView!
    @IBOutlet var c11: UIView!
    @IBOutlet var c12: UIView!
    @IBOutlet var c10: UIView!
    @IBOutlet var c9: UIView!
    @IBOutlet var c8: UIView!
    @IBOutlet var c7: UIView!
    @IBOutlet var c6: UIView!
    @IBOutlet var c5: UIView!
    @IBOutlet var c4: UIView!
    @IBOutlet var c3: UIView!

    override func viewDidLoad() {
        super.viewDidLoad()
        let myOptions = ViewPagerOptions()
        myOptions.viewPagerTransitionStyle = .scroll
        myOptions.isTabIndicatorAvailable=false
        myOptions.tabViewHeight=0
        viewPager = ViewPager(viewController: self, containerView: Display)
        viewPager.setOptions(options: myOptions)
        viewPager.setDataSource(dataSource: self)
        viewPager.setDelegate(delegate: self)
        viewPager.build()
        goback.setTitle(SetLan.Setlan("Users_manual"), for: .normal)
    }
    
    @IBAction func back(_ sender: Any) {
        act.GoBack(self)
    }
    
}
