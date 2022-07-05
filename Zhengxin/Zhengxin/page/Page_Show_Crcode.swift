//
//  Page_Show_Crcode.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/6.
//

import UIKit

class Page_Show_Crcode: UIViewController {
    @IBOutlet var img: UIImageView!
    //選擇的車
    var selectCar=PublicBeans.selectCar
    
    @IBOutlet var tit: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        tit.text=selectCar?.plateNumber
    }
    override func viewDidAppear(_ animated: Bool) {
        let encoder: JSONEncoder = JSONEncoder()
        let encoded = try! encoder.encode(selectCar)
        img.image=PublicBeans.generateQRCode(from: encoded, imageView: img)
    }
}
