//
//  ObdCommand.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/3/9.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//

import Foundation
import JzOsFrameWork
import JzOsTool
class ObdCommand{
    static var TimeOut=false
    static var AppVersion=""
    
    public static func bytesToHex(_ bt:[UInt8])->String{
        var re=""
        for i in 0..<bt.count{
            re=re.appending(String(format:"%02X",bt[i]))
        }
        return re
    }
    
    public static func addcheckbyte(_ hex:String)-> String{
        var bt=[UInt8](hex.HexToByte()!)
        var c1=UInt8(bt[0])
        for i in 1...bt.count-3{
            c1 ^= bt[i]
        }
        var re=bt
        re[re.count-2]=c1
        return bytesToHex(re)
    }
    public static func HandShake()->Bool{
        let pastTime = Date().timeIntervalSince1970
        Command.sendData(addcheckbyte("0A0000030000F5"))
        while(true){
            let time=GetTime(pastTime)
            if(time>1){return false}
            if(Command.rx=="F500000301F70A"){return true}
        }
    }
    public static func GetId(_ model:[Md_Idcopy])->Bool{
        let beans=ID_Beans()
        var pastTime = Date().timeIntervalSince1970
        let data="60BF00010\(PublicBeans.getWheelCount())FF0A"
        Command.sendData(XoR(data))
        var fal=0
        while(true){
            let time=GetTime(pastTime)
            if(time>2){
                pastTime = Date().timeIntervalSince1970
                fal+=1
                if(fal==10){
                    return false
                }
                Command.sendData(ObdCommand.XoR(data))
            }
            if(Command.rx.count == 52){
                beans.success=true
                let arraryPosition=["jz.312".getFix(),"jz.309".getFix(),"jz.310".getFix(),"jz.311".getFix(),"SP"]
                for i in 0..<5{
                    for md in model{
                        if(md.wh==arraryPosition[i]){
                            md.vid=Command.rx.sub((i+1)*8..<(i+2)*8)
                            md.condition=Md_Idcopy.尚未燒錄
                        }
                    }
                    
                }
                return true
            }
        }
    }
    public static func goPrObd()->Bool{
        let pastTime = Date().timeIntervalSince1970
        Command.sendData(XoR("0A8D00030100F5"))
        while(true){
            let time=GetTime(pastTime)
            if(time>3){return true}
            if(Command.rx.count == 14){
                return true}
            usleep(100*1000)
        }
    }
    public static func AskVersion()->Bool{
        let pastTime = Date().timeIntervalSince1970
        Command.sendData(XoR("0ACF000100FFF5"))
        while(true){
            let time=GetTime(pastTime)
            if(time>2){return false}
            if(Command.rx.count >= 56){
                AppVersion=Command.rx.sub(8..<52)
                NSLog("版本號:\(AppVersion)")
                return true}
        }
    }
    public static func GoApp()->Bool{
        var pastTime = Date().timeIntervalSince1970
        Command.sendData(XoR("0ACD000100FFF5"))
        var fal=0
        while(true){
            let time=GetTime(pastTime)
            if(time>1){
                pastTime = Date().timeIntervalSince1970
                fal+=1
                if(fal==3){return false}
                Command.sendData(XoR("0ACD000100FFF5"))
            }
            if(Command.rx.count == 14){
                NSLog("進入app")
                return true}
        }
    }
    public static  func reboot()->Bool{
        Command.sendData(addcheckbyte("0A0D00030000F5"))
        let pastTime = Date().timeIntervalSince1970
        while(true){
            let time=GetTime(pastTime)
            if(time>8){return false}
            if(Command.rx=="F501000300F70A"){return true}
        }
    }
    
    public static func GetTime(timeStamp: Double)-> Double{
        let currentTime = Date().timeIntervalSince1970
        let reduceTime : TimeInterval = currentTime - timeStamp
        return reduceTime
    }
    public static func writeflush(_ ind:Int)->Bool{
        var Long=0
        let data=PublicBeans.getOBDFile()
        if(data.count%ind == 0){
            Long=data.count/ind
        }else{Long=data.count/ind+1}
        NSLog("總行數\(Long)")
        for i in 0..<Long{
            var b=i
            if(b>=255){b=b-255}
            var row=Int2strHex(b)
            while(row.count<2){
                row="0\(row)".uppercased()
            }
            let cont=row.uppercased()
            if(i==Long-1){
                NSLog("以跑完")
                let data=bytesToHex([UInt8](data.sub(i*ind..<data.count).data(using: String.Encoding.utf8)!))
                let length=(data.count/2)+3
                Command.sendData(Convert(data,Int2strHex(length),cont))
                return true
            }else{
                let data=bytesToHex([UInt8](data.sub(i*ind..<i*ind+ind).data(using: String.Encoding.utf8)!))
                let length=(data.count/2)+3
                if(!check(Convert(data,Int2strHex(length),cont))
                ){return false}
                let iw:Float=Float(i)
                let Lw:Float=Float(Long)
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.closeDialLog()
                    var a=Progress()
                    a.label="\("Programming".getFix())...\(Int(iw/Lw*100))%"
                    JzActivity.getControlInstance.openDiaLog(a, false, "Progress")
                }
            }
        }
        return false
    }
    public static  func check(_ data:String)->Bool{
        //        F50200043000C30A
        Command.sendData(addcheckbyte(data))
        var pastTime = Date().timeIntervalSince1970
        var fal=0
        while(true){
            let time=GetTime(pastTime)
            if(time>2){
                pastTime = Date().timeIntervalSince1970
                Command.sendData(addcheckbyte(data))
                fal+=1
            }
            if(fal>20){return false}
            if(Command.rx.count>=14){return true}
        }
    }
    public static func sleepmill(_ t:Double){
        let pastTime = Date().timeIntervalSince1970
        while(true){
            let time=GetTime(pastTime)
            if(time>t){break}
        }
    }
    public static func AddEmpty(_ a:String)->String{
        var b=a
        while(b.count<8){
            b="0\(b)"
        }
        return b
    }
    public static  func SetireId(_ model:[Md_Idcopy])->Bool{
        var i=0
        
        for i in model{
            print("newid==\(i.newid)")
            if i.newid.isEmpty{
                i.newid=i.vid
            }
        }
        Command.sendData("60A200FFFFFFFFC20A")
        sleepmill(0.05)
        let position=["4","1","2","3","5"]
        for  id in model{
            id.newid=AddEmpty(id.newid)
            Command.sendData(addcheckbyte("60A20XidFF0A".replace("id",id.newid).replace("X","\(position[i])")))
            i+=1
            usleep(50*1000)
        }
        Command.sendData("60A2FFFFFFFFFF3D0A")
        sleepmill(0.05)
        let pastTime = Date().timeIntervalSince1970
        while(true){
            let time=GetTime(pastTime)
            if(time>10){
                for  id in model{
                    id.condition=Md_Idcopy.燒錄失敗
                }
                return false}
            if(Command.rx=="60B201FFFFFFFFD30A"){
                for  id in model{
                    id.condition=Md_Idcopy.燒錄成功
                    id.vid=id.newid
                }
                return true
            }
        }
    }
    
    public static  func TimeOn(){
        DispatchQueue.global().async {
            sleep(1)
        }
    }
    public static  func Convert(_ data:String,_ length:String,_ line:String)->String{
        let command="0A02LHX00F5"
        var long=length
        var Line=line
        while(long.count<4){
            long="0\(long)"
        }
        if(Line=="F5"){Line="00"}
        if(Line.count>2){Line="00"}
        return addcheckbyte(command.replace("L", long).replace("X", data).replace("H", Line))
    }
    
    public static   func Int2strHex(_ int:Int)-> String
    {
        let str = String(int, radix: 16)
        return str
    }
    
    public static  func GetTime(_ timeStamp: Double)-> Double{
        let currentTime = Date().timeIntervalSince1970
        let reduceTime : TimeInterval = currentTime - timeStamp
        return reduceTime
    }
    public static func WriteVersion()->Bool{
        var pastTime = Date().timeIntervalSince1970
        let data=XoR("0ACA0015DDFFF5".replace("DD", bytesToHex([UInt8](PublicBeans.getObdVersion().data(using: .utf8)!))))
        Command.sendData(data)
        var fal=0
        while(true){
            let time=GetTime(pastTime)
            if(time>1){
                pastTime = Date().timeIntervalSince1970
                if(fal==1){return false}
                fal += 1
                Command.sendData(data)
            }
            if(Command.rx.count==14){
                NSLog("寫入版本")
                return true}
        }
    }
    public static  func GoBootloader()->Bool{
        let pastTime = Date().timeIntervalSince1970
        Command.sendData(addcheckbyte("0ACD010100FFF5"))
        while(true){
            let time=GetTime(pastTime)
            if(time>10){return false}
            if(Command.rx.contains("F5CD010100380AF501000300F70A")){
                NSLog("進入Bootloader")
                return true}
        }
    }
    public static    func XoR(_ data:String)->String{
        var bytes=[UInt8](data.HexToByte()!)
        var xor=0
        for i in 0..<bytes.count-2{
            xor = xor ^ Int(bytes[i])
        }
        bytes[bytes.count-2] = UInt8(xor)
        return bytesToHex(bytes)
    }
    public static func laodingBootloader()->Bool{
        Command.goState(BootloaderState.Bootloader)
        ObdCommand.AskVersion()
        usleep(1000*500)
        print("OBD版本比對\(ObdCommand.AppVersion )-----\(ObdCommand.bytesToHex([UInt8](PublicBeans.getObdVersion().data(using: .utf8)!)))")
        if(ObdCommand.bytesToHex([UInt8](PublicBeans.getObdVersion().data(using: .utf8)!)).contains(ObdCommand.AppVersion ) ){
            return Command.goState(BootloaderState.Obd_App)
        }else{
            NSLog("nowversion\(ObdCommand.AppVersion)")
            NSLog("interversion\(ObdCommand.bytesToHex([UInt8](PublicBeans.getObdVersion().data(using: .utf8)!)))")
            if(ObdCommand.goPrObd()&&ObdCommand.GoBootloader()){
                let a=writeflush(298)
                if(a){
                    sleep(1)
                    let b = ObdCommand.WriteVersion()
                    return a&&b
                }else{
                    return false
                }
            }else{
                return false
            }
        }
    }
}
