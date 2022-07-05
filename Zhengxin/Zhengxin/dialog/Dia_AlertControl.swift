//
//  Dia_AlertControl.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/9.
//

import Foundation
import UIKit
import JzOsFrameWork
extension String{
    func showAlert(){
        let ac = UIAlertController(title: "訊息", message: self, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: "OK", style: .default))
        JzActivity.getControlInstance.getActivity().present(ac, animated: true)
    }
}
