//
//  Command.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/8.
//  Copyright © 2019 王建智. All rights reserved.
//

import Foundation
import JzIos_Framework
import JzOsTool
class Command {
    var TimeOut=false
    var channel=0
    var Ic=0
    var mmydata=""
    var ID=""
    public static var rx=""
    var act:ViewController!
    public  func sendData(_ data:String){
        Command.rx=""
           let act=JzActivity.getControlInstance.getActivity() as! ViewController
           let spi=350
           var Tda=""
           if(data.count>spi){
               var long=0
               if(data.count%spi==0){long=data.count/spi}else{
                   long=data.count/spi+1
               }
               let a=JzOSTool.timer()
               for i in 0..<long{
                   a.zeroing()
                   while(a.stop()<0.1){
                       
                   }
                   if(i==long-1){
                       Tda="87"+data.sub(i*spi..<data.count)+"78"
                   }else{
                       if(i==0){
                           var slon=String(format:"%2X",long).replace(" ", "")
                           if(slon.count<2){slon="0"+slon}
                           Tda="87\(slon)"+data.sub(i*spi..<i*spi+spi)
                       }else{
                           Tda="87"+data.sub(i*spi..<i*spi+spi)
                       }
                   }
                   act.helper.sendData(Tda.HexToByte()!, "8D82", "8D81")
               }
           }else{
               act.helper.sendData(data.HexToByte()!, "8D82", "8D81")
           }
       }
    func ProgramAll(_ path:String,_ Id1:String,_ Id2:String,_ Id3:String,_ Id4:String,_ Lf:String)->Bool{
        print("03")
        if(!Command03()){return false}
        print("10")
        if(!Command10_FE()){return false}
        print("CheckS19")
        if(CheckS19(path)){
            print("25")
            return Command25(Id1, Id2, Id3, Id4, Lf)
        }else{
            if(!Command14()){return false}
            if(!LogData()){return false}
            if(!Command17()){return false}
            return Command25(Id1, Id2, Id3, Id4, Lf)
        }
    }
    func Command25(_ Id1:String,_ Id2:String,_ Id3:String,_ Id4:String,_ Lf:String)->Bool{
        FALSE_CHANNEL=[String]()
        CHANNEL_BLE=[String]()
        BLANK_CHANNEL=[String]()
        let check=72
        let command="0AFE250017LF1ID1LF2ID2LF3ID3LF4ID4XXXXF5".replace("LF",Lf ).replace("ID1", Id1).replace("ID2", Id2).replace("ID3", Id3).replace("ID4", Id4)
        sendData(CRC16().crc16(command))
        let pastTime = Date().timeIntervalSince1970
        while(true){
            if(GetTime(pastTime)>15){
                return false
            }
            if(Command.rx.count==check){
                var g=true
                for i in 0..<channel{
                    let tmp=Command.rx.sub(10..<((Command.rx.count)-6))
                    if(!CheckCommand(tmp.sub(i*14..<i*14+2))){
                        g=false
                        if(tmp.sub(i*14+4..<i*14+12)=="00018001"){
                            BLANK_CHANNEL.append(tmp.sub(i*14+2..<i*14+4))
                            print("空的channel"+tmp.sub(i*14+2..<i*14+4))
                        }else{
                            print("失敗的channel"+tmp.sub(i*14+2..<i*14+4))
                            FALSE_CHANNEL.append(tmp.sub(i*14+2..<i*14+4))
                        }
                    }else{
                        CHANNEL_BLE.append(tmp.sub(i*14+2..<i*14+4)+"."+tmp.sub(i*14+4..<i*14+12))
                    }
                }
                return g
            }
        }
    }
    func Command11(_ ic:Int,_ channel:Int,_ version:String)->SensorBean{
        let sensorBean=SensorBean()
        var CC=""
        if(version==SensorBean._315){
            CC="1\(channel)"
        }else if(version==SensorBean._433){
            CC="0\(channel)"
        }
        let command="0ASS110004CCXXXXF5".replace("SS",bytesToHex([UInt8]([UInt8(ic)]))).replace("CC",CC)
        sendData(CRC16().crc16(command))
        let pastTime = Date().timeIntervalSince1970
        ID=""
        while(true){
            if(GetTime(pastTime)>2){
                return sensorBean
            }
            if((Command.rx.count) >= 30){
                if(Command.rx.count == 32){
                    if(Command.rx.sub(24..<26)=="08"){
                        sensorBean.boot_var=SensorBean._433
                    }else if(Command.rx.sub(24..<26)=="18"){
                        sensorBean.boot_var=SensorBean._315
                    }else if(Command.rx.sub(24..<26)=="A8"){
                        sensorBean.boot_var=SensorBean._雙頻
                    }
                }
                sensorBean.id=Command.rx.sub(14..<22)
            sensorBean.canPr=(version==sensorBean.boot_var)||(sensorBean.boot_var==SensorBean._雙頻)
                sensorBean.result=sensorBean.id != "00018001"
                return sensorBean
            }
        }
    }
    var SensorModel=""
    var AppVersion=""
    var Lib=""
    func Command10_FE()->Bool{
        let check=(14*Ic+8)*2
        sendData("0AFE10000754504D537331F5")
        var pastTime = Date().timeIntervalSince1970
        var fal=0
        while(true){
            if(GetTime(pastTime)>5){
                sendData("0AFE10000754504D537331F5")
                pastTime = Date().timeIntervalSince1970
                fal+=1
            }
            if(fal==3){return false}
            if(Command.rx.count==check){
                A0X10(Command.rx)
                return true
            }
        }
    }
    var FALSE_CHANNEL=[String]()
    var CHANNEL_BLE=[String]()
    var BLANK_CHANNEL=[String]()
    func Command15(_ Lf:String)->Bool{
        FALSE_CHANNEL=[String]()
        CHANNEL_BLE=[String]()
        BLANK_CHANNEL=[String]()
        let check=(8+(7*2*Ic))*2
        sendData(CRC16().crc16("0AFE150008LF154504D53XXXXF5".replace("LF",Lf)))
        let pastTime = Date().timeIntervalSince1970
        while(true){
            if(GetTime(pastTime)>15){
                return false
            }
            if(Command.rx.count==check){
                var g=true
                for i in 0..<channel{
                    let tmp=Command.rx.sub(10..<((Command.rx.count)-6))
                    if(!CheckCommand(tmp.sub(i*14..<i*14+2))){
                        g=false
                        if(tmp.sub(i*14+4..<i*14+12)=="00018001"){
                            BLANK_CHANNEL.append(tmp.sub(i*14+2..<i*14+4))
                            print("空的channel"+tmp.sub(i*14+2..<i*14+4))
                        }else{
                            print("失敗的channel"+tmp.sub(i*14+2..<i*14+4))
                            FALSE_CHANNEL.append(tmp.sub(i*14+2..<i*14+4))
                        }
                    }else{
                        CHANNEL_BLE.append(tmp.sub(i*14+2..<i*14+4)+"."+tmp.sub(i*14+4..<i*14+12))
                    }
                }
                return g
            }
            
        }
    }
    func Command14()->Bool{
        let check=(8+6*Ic)*2
        sendData(CRC16().crc16("0AFE14000D4F52414E474554504D53XXXXF5"))
        var pastTime = Date().timeIntervalSince1970
        var fal=0
        while(true){
            if(GetTime(pastTime)>2){
                sendData(CRC16().crc16("0AFE14000D4F52414E474554504D53XXXXF5"))
                pastTime = Date().timeIntervalSince1970
                fal+=1
            }
            if(fal==2){return false}
            if(Command.rx.count==check){
                var g=true
                for i in 0..<Ic{
                    var tmp=Command.rx.sub(10..<(Command.rx.count-6))
                    if(CheckCommand(tmp.sub(i*12..<i*12+2))){g=false}
                }
                return g
            }
        }
        
    }

    func Command17()->Bool{
        var check=(8+10*(Ic-1))*2
        sendData("0AFE1700094F52414E4745A7C4F5")
        var pastTime = Date().timeIntervalSince1970
        var fal=0
        while(true){
            if(GetTime(pastTime)>10){
                sendData("0AFE1700094F52414E4745A7C4F5")
                pastTime = Date().timeIntervalSince1970
                fal+=1
            }
            if(fal==2){return false}
            if(Command.rx.count==check){
                var g=true
                for i in 0..<(Ic-1){
                    var tmp=Command.rx.sub(10..<Command.rx.count-6)
                    if(!CheckCommand(tmp.sub(i*14..<i*14+2))){
                        g=false
                    }
                }
                return g
            }
        }
    }
    func ProgramAll(_ filename:String,_ Lf:String)->Bool{
        if(!Command03()){return false}
        if(!Command10_FE()){return false}
        if(CheckS19(filename)){
            if(Command15(Lf)){return true}else{return false}
        }else{
            if(!Command14()){return false}
            if(!LogData()){return false}
            if(!Command17()){return false}
            if(Command15(Lf)){return true}else{return false}
        }
    }
    func LogData()->Bool{
        var Long=0
        let ln=512
        if(mmydata.count%ln == 0){
            Long=mmydata.count/ln
        }else{
            Long=mmydata.count/ln+1
        }
        for i in 0..<Long{
            if(i==Long-1){
                let a=mmydata.sub(i*ln..<mmydata.count)
                sendData(AddCommand(a,i))
            }else{
                let a=mmydata.sub(i*ln..<i*ln+ln)
                sendData(AddCommand(a,i))
            }
            let pastTime = Date().timeIntervalSince1970
            while(true){
                if(Command.rx.count>12&&CheckCommand(Command.rx.sub(10..<12))){
                    break
                }
                if(GetTime(pastTime)>3){return false}
            }
            
        }
        return true
    }
    func AddCommand(_ data:String,_ long:Int)->String{
        var length=String(format:"%2X", data.count/2+5).replace(" ", "")
        print("轉換\(length) size:\(length.count)")
        while(length.count<4){
            length="0"+length
        }
        var row=String(format:"%2X",long).replace(" ", "")
        print("轉換\(row) size:\(row.count)")
        while(row.count<2){row="0"+row}
        var TmpCommand="0A0013"+length+row+data+data.sub(18..<20)+"XXXXF5"
        return CRC16().crc16(TmpCommand)
    }
    func CheckS19(_ filename:String)->Bool{
        print("mmydata:\(mmydata)")
        if(SensorModel==mmydata.sub(4..<8)&&AppVersion==mmydata.sub(8..<10)&&Lib==mmydata.sub(20..<22)){return true}else{return false}
    }
    func A0X10(_ data:String){
        let a=data.sub(10..<(data.count-6))
        var re=[String]()
        var tmpmodel=""
        var tmpversion=""
        var tmplib=""
        for i in 0..<(a.count/28){
            if(i==0){
                tmpmodel=a.sub(i*28+2..<i*28+6)
                tmpversion=a.sub(i*28+6..<i*28+8)
                tmplib=a.sub(i*28+12..<i*28+14)
            }else{
                if(tmpmodel != a.sub(i*28+2..<i*28+6)){tmpmodel="noequal"}
                if(tmpversion != a.sub(i*28+6..<i*28+8)){tmpversion="noequal"}
                if(tmplib != a.sub(i*28+12..<i*28+14)){tmplib="noequal"}
            }
        }
        SensorModel=tmpmodel
        AppVersion=tmpversion
        Lib=tmplib
        print("SensorModel\(SensorModel):AppVersion\(AppVersion):Lib\(tmplib)")
    }
    func Command03()->Bool{
        let command="0AFE03000754504D539CC8F5"
        //        print("Crc\(CRC16.crc16Ccitt(command,0x1021,0))")
        sendData(command)
        let pastTime = Date().timeIntervalSince1970
        while(true){
            if(Command.rx.count==22){
                //  if(CheckCommand(Command.rx.sub(10..<12))){  break}
                channel=Int(Command.rx.sub(12..<14).HexToByte()![0])
                Ic=channel/2
                print("channel數量\(channel)")
                break
                
            }
            if(GetTime(pastTime)>5){return false}
        }
        return true
    }
    func CheckCommand(_ check:String)->Bool{
        let byte: UInt8 = [UInt8](check.HexToByte()!)[0]
        let ch=getBit(byte)
        return ch.sub(7..<8)=="0"
    }
    func getBit(_ by:UInt8)->String{
        var a=""
        a.append(String((by>>7)&0x1))
        a.append(String((by>>6)&0x1))
        a.append(String((by>>5)&0x1))
        a.append(String((by>>4)&0x1))
        a.append(String((by>>3)&0x1))
        a.append(String((by>>2)&0x1))
        a.append(String((by>>1)&0x1))
        a.append(String((by>>0)&0x1))
        return a
    }
    func GetTime(_ timeStamp: Double)-> Double{
        let currentTime = Date().timeIntervalSince1970
        let reduceTime : TimeInterval = currentTime - timeStamp
        return reduceTime
    }
    func bytesToHex(_ bt:[UInt8])->String{
        var re=""
        for i in 0..<bt.count{
            re=re.appending(String(format:"%02X",bt[i]))
        }
        return re
    }
    func Setserial()->Bool{
        let command="0A0004000754504D535610F5"
        //        print("Crc\(CRC16.crc16Ccitt(command,0x1021,0))")
        sendData(command)
        let pastTime = Date().timeIntervalSince1970
        while(true){
            if(Command.rx.count==32){
                act!.serialnum=Command.rx.sub(14..<26)
                print("serial:\(act!.serialnum)")
                break
            }
            if(GetTime(pastTime)>5){return false}
        }
        return true
    }
}
