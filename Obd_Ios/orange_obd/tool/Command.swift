//
//  Command.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/8.
//  Copyright © 2019 王建智. All rights reserved.
//

import Foundation
class Command {
    var TimeOut=false
    var act:ViewController!
    var AppVersion=""
    static var Filedata=""
    func HandShake()->Bool{
        let pastTime = Date().timeIntervalSince1970
        act!.sendData(addcheckbyte("0A0000030000F5"), 14)
        while(true){
            let time=GetTime(pastTime)
            if(time>1){return false}
            if(act.Rx=="F500000301F70A"){return true}
        }
    }
    func GetId(_ count:String)->ID_Beans{
        let beans=ID_Beans()
        var pastTime = Date().timeIntervalSince1970
        let data="60BF0001\(count)FF0A"
        act!.sendData(XoR(data), 52)
        var fal=0
        while(true){
            let time=GetTime(pastTime)
            if(time>2){
                pastTime = Date().timeIntervalSince1970
                fal+=1
                if(fal==10){
                    beans.success=false
                    return beans
                }
                act!.sendData(XoR(data), 52)
            }
            if(act.Rx.count == 52){
                beans.success=true
                beans.LF=act.Rx.sub(8..<16)
                beans.RF=act.Rx.sub(16..<24)
                beans.RR=act.Rx.sub(24..<32)
                beans.LR=act.Rx.sub(32..<40)
                beans.SP=act.Rx.sub(40..<48)
                return beans}
        }
    }
    func AskVersion()->Bool{
        let pastTime = Date().timeIntervalSince1970
             act!.sendData(XoR("0ACF000100FFF5"), 54)
             while(true){
                 let time=GetTime(pastTime)
                 if(time>2){return false}
                if(act.Rx.count == 54){
                    AppVersion=act.Rx.sub(8..<50)
                    print("版本號:\(AppVersion)")
                    return true}
             }
    }
    func GoApp()->Bool{
        var pastTime = Date().timeIntervalSince1970
        act!.sendData(XoR("0ACD000100FFF5"), 14)
        var fal=0
        while(true){
            let time=GetTime(pastTime)
            if(time>1){
                pastTime = Date().timeIntervalSince1970
                fal+=1
                if(fal==3){return false}
                act!.sendData(XoR("0ACD000100FFF5"), 14)
            }
           if(act.Rx.count == 14){
               print("進入app")
               return true}
        }
    }
    func reboot()->Bool{
        act.sendData(addcheckbyte("0A0D00030000F5"), 14)
         let pastTime = Date().timeIntervalSince1970
        while(true){
             let time=GetTime(pastTime)
            if(time>8){return false}
            if(act.Rx=="F501000300F70A"){return true}
        }
    }
    
    func GetTime(timeStamp: Double)-> Double{
         let currentTime = Date().timeIntervalSince1970
         let reduceTime : TimeInterval = currentTime - timeStamp
         return reduceTime
    }
    func writeflush(_ ind:Int)->Bool{
        var Long=0
        if(Command.Filedata.count%ind == 0){
            Long=Command.Filedata.count/ind
        }else{ Long=Command.Filedata.count/ind+1}
        print("總行數\(Long)")
        for i in 0..<Long{
            var b=i
            if(b>=255){b=b-255}
                        var row=Int2strHex(b)
                        while(row.count<2){
                            row="0\(row)".uppercased()
                        }
            let cont=row.uppercased()
            if(i==Long-1){
                print("以跑完")
                let data=bytesToHex([UInt8](Command.Filedata.sub(i*ind..<Command.Filedata.count).data(using: String.Encoding.utf8)!))
                let length=(data.count/2)+3
               act.sendData(Convert(data,Int2strHex(length),cont),16)
                return true
            }else{
                let data=bytesToHex([UInt8](Command.Filedata.sub(i*ind..<i*ind+ind).data(using: String.Encoding.utf8)!))
                let length=(data.count/2)+3
               if(!check(Convert(data,Int2strHex(length),cont))
){return false}
                let iw:Float=Float(i)
                 let Lw:Float=Float(Long)
                DispatchQueue.main.async {
self.act.LoadIng("\(SetLan.Setlan("Programming"))...\(Int(iw/Lw*100))%")
                }
                              }
        }
        return false
    }
    func check(_ data:String)->Bool{
//        F50200043000C30A
        act.sendData(addcheckbyte(data), 0)
        var pastTime = Date().timeIntervalSince1970
        var fal=0
        while(true){
            let time=GetTime(pastTime)
            if(time>2){
                pastTime = Date().timeIntervalSince1970
                 act.sendData(addcheckbyte(data), 0)
                fal+=1
            }
            if(fal>20){return false}
            if(act.Rx.count>=16){return true}
        }
    }
    func sleepmill(_ t:Double){
        let pastTime = Date().timeIntervalSince1970
        while(true){
            let time=GetTime(pastTime)
            if(time>t){break}
        }
    }
    func AddEmpty(_ a:String)->String{
        var b=a
        while(a.count<8){
            b="0\(b)"
        }
        return b
    }
    func SetireId(_ Id:[String])->Bool{
        var i=0
        act.sendData("60A200FFFFFFFFC20A",18)
        sleepmill(0.05)
        var position=["4","1","2","3","5"]
        for var id in Id{
            id=AddEmpty(id)
act.sendData(addcheckbyte("60A20XidFF0A".replace("id",id).replace("X","\(position[i])")),0)
            i+=1
           sleepmill(0.05)
        }
         act.sendData("60A2FFFFFFFFFF3D0A",18)
          sleepmill(0.05)
         let pastTime = Date().timeIntervalSince1970
        while(true){
             let time=GetTime(pastTime)
            if(time>10){return false}
            if(act.Rx=="60B201FFFFFFFFD30A"){
                return true
            }
        }
    }
    
    func TimeOn(){
        DispatchQueue.global().async {
            sleep(1)
        }
    }
    func Convert(_ data:String,_ length:String,_ line:String)->String{
        let command="0A02LHX00F5"
        var long=length
        var Line=line
        while(long.count<4){
            long="0\(long)"
        }
        if(Line=="F5"){Line="00"}
        if(Line.count>2){Line="00"}
       return addcheckbyte(command.replace("L", long).replace("X", data).replace("H", line))
    }
    func addcheckbyte(_ hex:String)-> String{
        var bt=[UInt8](hex.HexToByte()!)
        var c1=UInt8(bt[0])
        for i in 1...bt.count-3{
            c1 ^= bt[i]
        }
        var re=bt
        re[re.count-2]=c1
        return bytesToHex(re)
    }
    func Int2strHex(_ int:Int)-> String
    {
        let str = String(int, radix: 16)
        return str
    }
    func bytesToHex(_ bt:[UInt8])->String{
        var re=""
        for i in 0..<bt.count{
              re=re.appending(String(format:"%02X",bt[i]))
        }
      return re
    }
    func GetTime(_ timeStamp: Double)-> Double{
        let currentTime = Date().timeIntervalSince1970
        let reduceTime : TimeInterval = currentTime - timeStamp
        return reduceTime
    }
    func WriteVersion()->Bool{
        var pastTime = Date().timeIntervalSince1970
        let data=XoR("0ACA0015DDFFF5".replace("DD", bytesToHex([UInt8](FtpManage.Downs19.data(using: .utf8)!))))
              act!.sendData(data, 14)
           var fal=0
              while(true){
                  let time=GetTime(pastTime)
                  if(time>1){
                    pastTime = Date().timeIntervalSince1970
                    if(fal==1){return false}
                    fal += 1
                    act!.sendData(data, 14)
                    }
                if(act.Rx.count==14){
                    print("寫入版本")
                    return true}
              }
    }
    func GoBootloader()->Bool{
        let pastTime = Date().timeIntervalSince1970
                    act!.sendData(addcheckbyte("0ACD010100C7F5"), 14)
                    while(true){
                        let time=GetTime(pastTime)
                        if(time>1){return false}
                        if(act.Rx=="F5CD010100CD0A"){
                            print("進入Bootloader")
                            return true}
                    }
    }
    func XoR(_ data:String)->String{
        var bytes=[UInt8](data.HexToByte()!)
        var xor=0
        for i in 0..<bytes.count-2{
            xor = xor ^ Int(bytes[i])
        }
        bytes[bytes.count-2] = UInt8(xor)
        return bytesToHex(bytes)
    }
}
