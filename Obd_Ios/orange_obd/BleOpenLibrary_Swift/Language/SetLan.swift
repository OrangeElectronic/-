//
//  SetLan.swift
//  txusb
//
//  Created by 王建智 on 2019/8/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import Foundation
class SetLan{
    static func Setlan(_ lan:String)->String{
        var a=BleActivity.getShare("lan")
        if(a=="nodata"){a="English"}
        switch a {
        case "English":
            return English.dic[lan] ?? lan
        case "繁體中文":
            return ChineseTr.dic[lan] ?? lan
        case "简体中文":
            return ChineseSi.dic[lan] ?? lan
        case "Deutsch":
            return De.dic[lan] ?? lan
        case "Italiano":
            return it.dic[lan] ?? lan
        default:
            return lan
        }
    }
}
