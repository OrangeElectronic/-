//
//  Selection.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/12.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class Selection: UIViewController {
 let act=(UIApplication.shared.delegate as! AppDelegate).act!
    public static var function=1
    public static let 讀取=1
    public static let 寫入=2
    public static let 完畢=3
    @IBOutlet var Favorite: UILabel!
    @IBOutlet var SelectLabel: UILabel!
    @IBOutlet var ScanLabel: UILabel!
    @IBOutlet var ThreeWaysLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
ThreeWaysLabel.text=SetLan.Setlan("Methods_of_vehicle_data_selection")
   Favorite.text=SetLan.Setlan("My favorite")
   ScanLabel.text=SetLan.Setlan("Scan_Code")
    SelectLabel.text=SetLan.Setlan("Vehicle_data_selection")
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(false)
        let home=act.Pagememory[0]
        act.Pagememory.removeAll()
        act.Pagememory.append(home)
        act.Pagememory.append(self)
    }
    @IBAction func SelectMake(_ sender: Any) {
        let a=peacedefine().SelectMake
        act.ChangePage(to: a)
    }
    
    @IBAction func scancode(_ sender: Any) {
        let a=peacedefine().QrScanner
        act.ChangePage(to: a)
    }
    
    @IBAction func myfavroite(_ sender: Any) {
        let a=peacedefine().MyFavorite
        act.ChangePage(to: a)
    }
    
}
