//
//  P3.swift
//  txusb
//
//  Created by 王建智 on 2019/8/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class P3: UIViewController {

    @IBOutlet var label: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
label.text = SetLan.Setlan("step3_3")
        // Do any additional setup after loading the view.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
