//
//  CRC16.swift
//  Peripheral
//
//  Created by 王建智 on 2019/7/29.
//  Copyright © 2019 KoKang Chu. All rights reserved.
//

import Foundation

class CRC16 {
    enum CRCType {
        case MODBUS
        case ARC
    }
    //hex範例為"0A00110099XXXXF5"
    func crc16(_ hex: String) -> String {
        print("crc\(hex)")
        let data=[UInt8](hex.sub(2..<(hex.count-6)).HexToByte()!)
        var crc=0x0000
        let poly=0x1021
        for i in 0..<data.count{
            let b=data[i]
            for i in 0..<8{
                let bit=((b >> (7-i) & 1) == 1)
                let c15=((crc >> 15 & 1) == 1)
                crc <<= 1
                if(c15 != bit){crc ^= poly}
            }
        }
        crc &= 0xffff
        var result=String(format:"%02X", crc)
        while (result.count<4){
            result="0"+result
        }
        return hex.replace("XXXX", result)
    }
    func bytesToHex(_ bt:[UInt8])->String{
        var re=""
        for i in 0..<bt.count{
            re=re.appending(String(format:"%02X",bt[i]))
        }
        return re
    }
}
