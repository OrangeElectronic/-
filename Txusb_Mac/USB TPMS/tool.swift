//
//  tool.swift
//  hh
//
//  Created by 王建智 on 2019/4/26.
//  Copyright © 2019 王建智. All rights reserved.
//

import Foundation

public class mytool{
   
    func addcheckbyte(_ bt:[UInt8])-> UInt8{
        var c1=bt[0]
        for i in 1..<7{
            c1^=bt[i]
        }
        
        return c1
    }
    func info(){
        print("請輸入6到8位數")
    }
    func WriteError(){
        print("寫入資料錯誤")
    }
    func DataString(_ bt:[UInt8],_ end:Int)->String{
        var tx="TX:"
        for i in 0...8{
            tx="\(tx)\(String(format: "%02X",bt[i]))  "
        }
        return tx
    }
    func Setire(_ position:UInt8,_ id:String)->[UInt8]{
        var Id=id
        var allpt:[UInt8]=[0x60,0xA2,0x00,0x00,0xFF,0xFF,0xFF,0xFF,0x0A]
        allpt[2]=position
        
        switch id.count{
        case 6:
          Id="00\(Id)"
            break
        case 7:
          Id="0\(Id)"
            break
        case 8:
            break
        default:
            break
        }
        let bytes:[UInt8] = [UInt8](Id.dataFromHexadecimalString()!)
        for i in 0..<bytes.count{
            allpt[i+3]=bytes[i]
        }
        allpt[7]=mytool().addcheckbyte(allpt)
        return allpt
    }
}
