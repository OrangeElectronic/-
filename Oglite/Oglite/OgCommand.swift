//
//  OgCommand.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/3/18.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//

import Foundation
import JzOsTool
import JzOsFrameWork
class OgCommand {
    var Program_Progress:((_ a:Int)->Void)?=nil
    //是否正在燒錄
    var onProgram=false
    //斷線retry
    var retry=false
    //按下取消鍵
    var cancel=false
    var ScanCount = 0
    var Rx:String{
        get{
            return Command.rx
        }
    }
    
    func Program(
        count: String,
        caller: @escaping ((_ a:Int)->Void),
        sensor: [String],programFinish:(_ a:Bool)->Void
    ) {
        Program_Progress = caller
        if (SendTrigerInfo(sensor: sensor) && ProgramFirst(Lf: PublicBeans.getLfPower(), Hex: PublicBeans.getHEX(), count: count, data: PublicBeans.getS19File())) {
            programFinish(ProgramCheck(data: spilt))
        } else {
            programFinish(false)
        }
    }
    
    func StringHexToByte(_ a:String)->Data{
        return a.HexToByte()!
    }
    
    public  func GetCrcString(_ hex:String)-> String{
        var bt=[UInt8](hex.HexToByte()!)
        var c1=UInt8(bt[0])
        for i in 1...bt.count-3{
            c1 ^= bt[i]
        }
        var re=bt
        re[re.count-2]=c1
        return OgCommand.bytesToHex(re)
    }
    
    public static func bytesToHex(_ bt:[UInt8])->String{
        var re=""
        for i in 0..<bt.count{
            re=re.appending(String(format:"%02X",bt[i]))
        }
        return re
    }
    
    func getBit(_ by:[UInt8])->String{
        var a=""
        for i in 0..<by.count{
            a.append(String((by[i]>>7)&0x1))
            a.append(String((by[i]>>6)&0x1))
            a.append(String((by[i]>>5)&0x1))
            a.append(String((by[i]>>4)&0x1))
            a.append(String((by[i]>>3)&0x1))
            a.append(String((by[i]>>2)&0x1))
            a.append(String((by[i]>>1)&0x1))
            a.append(String((by[i]>>0)&0x1))
        }
        
        return a
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
    

    func byte2Int(_ array:[UInt8]) -> Int {
        NSLog(OgCommand.bytesToHex(array))
        var height:Int=Int(array[0])
        var low:Int=Int(array[1])
        return (height << 8 & 0xFF00) | (low & 0xFF)
    }
    
    func  GetPrId()->[SensorData] {
        var array = [SensorData]()
        let replace =
            "0A 10 000E 01 02 LF HEX 00 00 00 00 00 00 00 00 39 F5".replace("HEX", PublicBeans.getHEX())
            .replace(" ", "").replace("LF", PublicBeans.getLfPower())
        Command.sendData(GetCrcString(replace))
        let a=Command.timer.zeroing()
        var fal = 0
        while (true) {
            if (Command.rx == GetCrcString("F51C000301000A") ||  Command.rx == GetCrcString("F51C000302000A") || Command.timer.stop() > 20 ) {
                fal += 1
                if (fal == 3) {
                    return array
                }
                Command.sendData(GetCrcString(replace))
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.toast("第幾次\(fal)")
                }
            }
            if(retry){
                fal += 1
                if (fal == 3) {
                    return array
                }
                Command.sendData(GetCrcString(replace))
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.toast("第幾次\(fal)")
                }
            }
            if (cancel) {
                return array
            }
            
            if ( Command.rx.count >= 36) {
                let data = SensorData()
                let idcount = Int( Command.rx.sub(17..<18)) ?? 0
                data.id=( Command.rx.sub(8..<16))
                data.idcount=idcount
                data.bat=getBit( Command.rx.sub(28..<30).HexToByte()![0]).sub(3..<4)
                data.kpa=Float(byte2Int([UInt8](Command.rx.sub(22..<26).HexToByte()!)))
                let bytes = Command.rx.substring(18, 22).HexToByte()
                data.c=Float((Int(bytes![1]) - Int(bytes![0])))
                data.vol=Int((22 + ( Command.rx.substring(26, 28).HexToByte()![0] & 0x0F)))
                data.success=(true)
                array.add(data)
                if (array.size == ScanCount) {
                    return array
                } else {
                    if ( Command.rx.length > 36) {
                        Command.rx =  Command.rx.substring(36)
                    } else {
                        Command.rx = ""
                    }
                }
            }
            usleep(100*1000)
        }
    }
    func 叫一下(){
        Command.sendData(GetCrcString("0AE200030000F5"))
        usleep(1000*200)
        Command.sendData(GetCrcString("0AE000030000F5"))
    }
    func GetPr()-> [SensorData] {
        var response = [SensorData]()
        var co = PublicBeans.燒錄數量.toHexString()
        while (co.length < 2) {
            co = "0\(co)"
        }
        var data=GetCrcString(
            "0A 10 000E 01 00 LF hex 00 00 00 00 count 00 00 00 39 F5".replace(
                " ",
                ""
            ).replace("LF", "00").replace("count", co).replace("hex", PublicBeans.getHEX())
        )
        Command.sendData(data)
        while (true) {
            if(retry){
                return response
            }
            if (Command.timer.stop() > 20 || Command.rx == GetCrcString("F51C000301000A") ||  Command.rx == GetCrcString("F51C000302000A")) {
                return response
            }
            if(cancel){
                return response
                }
            if (Command.rx.length >= 36) {
                let data = SensorData()
                let idcount = Int(Command.rx.substring(17, 18))
                data.idcount=(idcount ?? 8)
                data.id=(Command.rx.substring(8, 16))
                data.bat=(getBit(Rx.substring(28, 30).HexToByte()![0]).substring(3, 4))
                data.kpa = Float(byte2Int([UInt8](Rx.substring(22, 26).HexToByte()!)))
                let bytes = StringHexToByte(Rx.substring(18, 22))
                data.c=Float((bytes[1] - bytes[0]))
                data.vol=Int((22 + (StringHexToByte(Rx.substring(26, 28))[0] & 0x0F)))
                data.success=(true)
                response.add(data)
                Command.rx = Command.rx.substring(36)
                if (response.size == PublicBeans.燒錄數量) {
                    return response
                }
            }
            usleep(1000*100)
        }
    }
    //    public static Read
    func GetId()-> SensorData {
        let data = SensorData()
        var ddata=GetCrcString(
            "0A 10 000E 01 00 LF HEX 00 00 00 00 00 00 00 00 39 F5".replace(
                "HEX",
                PublicBeans.getHEX()
            ).replace(" ", "").replace("LF", PublicBeans.getLfPower())
        )
        Command.sendData(ddata)
        var fal=0
        while (true) {
            if (Command.timer.stop() > 15 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A")  ) {
                fal+=1
                if(fal==4){
                    data.success=(false)
                    return data
                }
                Command.sendData(ddata)
            }
            if(retry){
                fal+=1
                if(fal==4){
                    data.success=(false)
                    return data
                }
                Command.sendData(ddata)
            }
            if(cancel){
                data.success=(false)
                return data
            }
            if (Rx.length >= 36) {
                let idcount = Int(Rx.substring(17, 18))
                data.idcount=(idcount ?? 8)
                data.id=(Rx.substring(16 - data.idcount, 16))
                //data.id=Rx.substring(8, 16);
                data.bat=(getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(3, 4))
                data.kpa=Float(byte2Int([UInt8](StringHexToByte(Rx.substring(22, 26)))))
                let bytess = Command.rx.substring(18, 22).HexToByte()
                if(bytess == nil){return data}
                data.c=Float(((bytess![1] & 0xFF) - (bytess![0] & 0xFF)))
                data.vol=Int((22 + (StringHexToByte(Rx.substring(26, 28))[0] & 0x0F)))
                data.有無胎溫=(
                    getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(
                        0,
                        1
                    ) == "1"
                )
                data.有無電壓=(
                    getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(
                        1,
                        2
                    ) == "1"
                )
                data.有無電池=(
                    getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(
                        2,
                        3
                    ) == "1"
                )
                data.success=(true)
                return data
            }
            usleep(100*1000)
        }
    }
    
    func SendTrigerInfo(sensor: [String])->Bool {
        for i in 0..<sensor.count {
            var position = (i + 1).toHexString()
            while (position.length < 2) {
                position = "0\(position)"
            }
            var id = sensor[i]
            while (id.length < 8) {
                id = "0\(id)"
            }
            var data=GetCrcString(
                "0A 15 00 0E position ID 00 00 00 00 00 00 00 18 F5".replace(
                    "position",
                    position
                ).replace("ID", id).replace(" ", "")
            )
            Command.sendData(data)
            Command.timer.zeroing()
            var fal=0
            while (Rx.length != 36) {
                if (Command.timer.stop() > 0.5 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A") ) {
                    fal+=1
                    if(fal==4){
                        MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "noResponse")
                        return false
                    }
                    Command.sendData(data)
                }
                if(retry){
                    fal+=1
                    if(fal==4){
                        MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "noResponse")
                        return false
                    }
                    Command.sendData(data)
                }
                if(cancel){return false}
                usleep(100*1000)
                NSLog("Ble->\(Command.timer.stop())")
            }
        }
        return true
    }
    var spilt: String=""
    func ProgramFirst(Lf: String, Hex: String, count: String, data: String) -> Bool {
        var Hex = Hex
        var count = count
        
        while (count.length < 2) {
            count = "0\(count)"
        }
        while (Hex.length < 2) {
            Hex = "0\(Hex)"
        }
        NSLog("data\(data)")
        let B8 = data.substring(14, 16)
        let B9 = data.substring(16, 18)
        let B12 = data.substring(22, 24)
        let B13 = data.substring(24, 26)
        let Data =
            "0A 10 00 0E  02 CT  Lf Hex 8b 9b 12b 13b 00 00 00 00 ff f5".replace("CT", count)
            .replace("Lf", Lf).replace("Hex", Hex)
            .replace("8b", B8).replace("9b", B9).replace("12b", B12).replace("13b", B13)
            .replace(" ", "")
        Command.sendData(GetCrcString(Data))
        var fal = 0
        while (true) {
            if (Command.timer.stop() > 20  || Rx.contains(GetCrcString("F51C000301000A"))  || Rx.contains(GetCrcString("F51C000302000A"))) {
                fal += 1
                if (fal == 3 ) {
                    MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "2")
                    return false
                }
                Command.sendData(GetCrcString(Data))
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.toast("第幾次\(fal)")
                }
            }
            if ( retry) {
                fal += 1
                if (fal == 3 || cancel) {
                    MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "2")
                    return false
                }
                Command.sendData(GetCrcString(Data))
                DispatchQueue.main.async {
                    JzActivity.getControlInstance.toast("第幾次\(fal)")
                }
            }
            if(cancel){
                return false
            }
            if (Rx.length >= 36) {
                let t=Int(Rx.substring(9, 10))
                if(t==nil){
                    return false
                }else{
                    ScanCount=t!
                }
                if (Rx.substring(10, 12) == "04"){
                    spilt=data.substring(
                        0,
                        2048 * 2)
                }else{
                    spilt=data.substring(0, 6144 * 2)
                }
                
                return WriteFlash(spilt)
            }
            usleep(100*1000)
        }
        
    }
    
    func WriteFlash(_ data: String)-> Bool {
        var count=0
        if (data.length % 400 == 0) {
            count=data.length / 400
        }else{
            count=data.length / 400 + 1
        }
        for i in 0..<count {
            if (i == count - 1) {
                Program_Progress!(100)
                if (!CheckData(data.substring(400 * i), (i + 1).toHexString())) {
                    return false
                }
            } else {
                Program_Progress!(i * 100 / count)
                if (!CheckData(
                    data.substring(400 * i, 400 * i + 400),
                    (i + 1).toHexString()
                )
                ) {
                    return false
                }
            }
        }
        return true
        
    }
    func CheckData(_ data: String,_ place: String)-> Bool {
        var place = place
        while (place.length < 2) {
            place = "0\(place)"
        }
        var Long :String{
            get{
                if (data.length == 400) {return "00CB"} else {
                    return  "00" + (data.length / 2 + 3).toHexString()
                }
            }
        }
        let command = "0A 13 LONG DATA PLACE FF F5".replace(" ", "").replace("LONG", Long)
            .replace("DATA", data).replace("PLACE", place)
        Command.sendData(GetCrcString(command))
        var fal = 0
        while (true) {
            if(retry){
            
                fal+=1
                if(fal==4){
                    MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "3")
                    return false
                }else{
                    Command.sendData(GetCrcString(command))
                }
            }
            if (Command.timer.stop() > 6 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A") ) {
                fal+=1
                if(fal==4){
                    MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "3")
                    return false
                }else{
                    Command.sendData(GetCrcString(command))
                }
            }
            if(cancel){
                return false
            }
            if (Rx.length >= 36) { return true}
            usleep(100*1000)
        }
    }
    func ProgramCheck(data: String)->Bool {
        let command=GetCrcString("0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(" ", ""))
        Command.sendData(command)
        var fal = 0
        while (true) {
            if(retry){
                fal+=1
                if(fal==3){
                    MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "4")
                    return false
                }else{
                    Command.sendData(GetCrcString(command))
                }
            }
            if (Command.timer.stop() > 15 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A") ) {
                fal+=1
                if(fal==3){
                    MemeoryUploader.InsertMemory(memory: ViewController.bleMemory, errorType: "4")
                    return false
                }else{
                    Command.sendData(GetCrcString(command))
                }
            }
            if(cancel){
                return false
            }
            if (Rx.length >= 36 && Rx.contains("F513000E00")) {
                let check = Rx.substring(12, 20)
                if (check == "7FFFFFFF" || check == "000007FF") {
                    return true
                } else {
                    Program_Progress = {
                        a in 
                    }
                    fal += 1
                    if(fal==3){return false}
                    if (!RePr(getBit([UInt8](check.HexToByte()!)).substring(1), data)) {
                        return false
                    }
                    Command.timer.zeroing()
                }
            }
            usleep(100*1000)
        }
    }
    func RePr(_ b: String,_ data: String)->Bool {
        var b :String = String(b.reversed())
        NSLog("DATA:: 失敗" + b)
        var count:Int{
            get{
                if (data.length % 400 == 0){
                    return data.length / 400
                }else{
                    return data.length / 400 + 1
                }
            }
        }
        for i in 0..<count {
            Program_Progress!(i * 100 / count)
            if (b.substring(i,i+1) != "1") {
                if (i == count - 1) {
                    if (!CheckData(data.substring(400 * i), (i + 1).toHexString())) {
                        return false
                    }
                } else {
                    if (!CheckData(
                        data.substring(400 * i, 400 * i + 400),
                        (i + 1).toHexString()
                    )
                    ) {
                        return false
                    }
                }
            }
        }
        Command.sendData(GetCrcString("0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(" ", "")))
        return true
    }
    func reboot()-> Bool {
        let data = "0A0D00030000F5"
        Command.sendData(GetCrcString(data))
        Command.timer.zeroing()
        while (true) {
            if (Command.timer.stop() > 20 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A")) {
                return false
            }
            if (Rx.length == 14) {
                return true
            }
            usleep(1000*1000)
        }
        
    }
    func GetVerion(caller: (_ a:String,_ b:Bool)->Void) {
        let data = "0A0A000EFFFFFFFFFFFFFFFFFFFFFFFF00F5"
        Command.sendData(GetCrcString(data))
        Command.timer.zeroing()
        while (true) {
            if (Command.timer.stop() > 15 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A") ) {
                caller("", false)
                return
            }
            if (Rx.length >= 36) {
                caller(Rx.substring(8, 16), true)
                return
            }
            usleep(100*1000)
        }
    }
    
    func WriteBootloader(act: JzActivity, Ind: Int, filename: String, caller: (_ b:Int)->Void,finish:(_ b:Bool)->Void) {
        let sb = PublicBeans.getX2()
        var Long = 0
        if (sb.length % Ind == 0) {
            Long = sb.count / Ind
        } else {
            Long = sb.count / Ind + 1
        }
        for i in 0..<Long {
            var b=i
            if(b>=255){b=b-255}
            var row=(b).toHexString()
            while(row.count<2){
                row="0\(row)".uppercased()
            }
            let cont=row.uppercased()
            if (i == Long - 1) {
                let data=ObdCommand.bytesToHex([UInt8](sb.substring(i * Ind, sb.length).data(using: .utf8)!))
                let length = (data.count/2)+3
                ObdCommand.check(ObdCommand.Convert(data,length.toHexString(),cont))
                caller(100)
                finish(true)
            } else {
                let data=ObdCommand.bytesToHex([UInt8](sb.sub(i*Ind..<i*Ind+Ind).data(using: String.Encoding.utf8)!))
                
                let length = [UInt8](sb.sub(i*Ind..<i*Ind+Ind).data(using: String.Encoding.utf8)!).count + 3
                caller(i * 100 / Long)
                if (!ObdCommand.check(ObdCommand.Convert(data,length.toHexString(),cont))) {
                    finish(false)
                }
            }
        }
        finish(true)
    }
    func Convvvert(_ data: String,_ length: String)-> String {
        var length = length
        var command = "0A02LX00F5"
        while (length.length < 4) {
            length = "0\(length)"
        }
        command = command.replace("L", length).replace("X", data)
        return command
    }
    
    func check(_ data: String)->Bool {
        var fal = 0
        Command.sendData(GetCrcString(data))
        Command.timer.zeroing()
        while (fal < 5) {
            if (Command.timer.stop() > 2 || cancel) {
                Command.sendData(GetCrcString(data))
                Command.timer.zeroing()
                fal += 1
            }
            if (Rx.length >= 14 && Rx == GetCrcString("F502000300F40A") || Rx.contains("F50B000301F70A")) {
                return true
            }
            usleep(100*1000)
        }
        return false
    }
    
    func GetHard() {
        let data = "0A0C000EFFFFFFFFFFFFFFFFFFFFFFFF00F5"
        Command.sendData(GetCrcString(data))
        while (true) {
            if (Command.timer.stop() > 15 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A") || cancel) {
                return
            }
            if (Rx.length >= 14) {
                //                    if(Rx.contains(GetCrcString("F500000302F40A"))){caller.result(2);}
                //                    if(Rx.contains(GetCrcString("F500000301F40A"))){caller.result(1);}
                return
            }
            usleep(1000*100)
        }
    }
    func HandShake(caller: (_ a:Int)->Void) {
        let data = "0A0000030000F5"
        Command.sendData(GetCrcString(data))
        while (true) {
            if (Command.timer.stop() > 15 || Rx == GetCrcString("F51C000301000A") || Rx == GetCrcString("F51C000302000A") || cancel) {
                caller(-1)
                return
            }
            if (Rx.length >= 14) {
                if (Rx.contains(GetCrcString("F500000302F40A"))) {
                    caller(2)
                    return
                }
                if (Rx.contains(GetCrcString("F500000301F40A"))) {
                    caller(1)
                    return
                }
                if (Rx.contains(GetCrcString("F501000300F70A"))) {
                    caller(1)
                    return
                }
                caller(-1)
                return
            }
            usleep(100*1000)
        }
    }
    public  func askVersion()->Bool{
            NSLog("跳轉成功")
            Command.sendData(GetCrcString("0ADC000100FFF5"))
            Command.timer.zeroing()
            var fal = 0
            while (true) {
                if(Command.timer.stop()>2 ){
                    Command.timer.zeroing()
                    fal+=1
                }
                if(fal==4){
                    return true
                }
                if(Command.rx.count>=54){
                    SharePre.ogversion=Command.rx.sub(8..<50)
                    NSLog("ogversion",Command.rx.sub(8..<20))
                    usleep(1000*200)
                    return true
                }
                usleep(1000*100)
            }
    }
    
    func IdCopy(_ caller: (_ a:Bool)->Void,_ obd:[Md_Idcopy],_ copyNext:(_ a:Bool,_ position:Int)->Void) {
        var hex = PublicBeans.getHEX()
        while (hex.length < 2) {
            hex = "\(hex)"
        }
        for i in 0..<obd.count{
            if(!obd[i].readable){continue}
            var Original_ID = obd[i].vid
            while (Original_ID.length < 8) {
                Original_ID = "0\(Original_ID)"
            }
            var New_ID = obd[i].newid
            while (New_ID.length < 8) {
                New_ID = "0\(New_ID)"
            }
            let data =
                "0A 11 00 0E Original_ID Original_Long New_ID New_Long hex 00 ff f5".replace(
                    " ",
                    ""
                ).replace(
                    "Original_Long",
                    "0\(PublicBeans.getidcount())"
                )
                .replace("New_Long", "0\(PublicBeans.getidcount())").replace("Original_ID", Original_ID)
                .replace("New_ID", New_ID).replace("hex", hex)
            Command.sendData(GetCrcString(data))
            var fal=0
            while (true) {
                if (Command.timer.stop() > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") ) {
                    fal+=1
                    if(fal==4){
                        copyNext(false, i)
                        break
                    }else{
                        Command.sendData(GetCrcString(data))
                    }
                }
                if(retry){
                    fal+=1
                    if(fal==4){
                        copyNext(false, i)
                        break
                    }else{
                        Command.sendData(GetCrcString(data))
                    }
                }
                if(cancel){
                    caller(false)
                    return
                }
                
                if (Rx.length >= 36) {
                    let idcount = Int(Rx.substring(17, 18))!
                    if (Rx.contains(obd[i].vid.substring(8 - idcount))) {
                        copyNext(true,i)
                    } else {
                        copyNext(false,i)
                    }
                    break
                }
                usleep(100*1000)
            }
            sleep(1)
        }
        caller(true)
    }
    
    
    func WriteBootloader(_ Ind:Int,_ progrss:(_ a:Int)->Void,_ finish:(_ a:Bool)->Void){
        askVersion()
        usleep(1000*500)
        if(SharePre.mcuinit.contains(SharePre.ogversion)){
            finish(true)
            return
        }else{
            goPrOG()
            goBootloader()
            let sb = PublicBeans.getMcu()
            var Long = 0
            if (sb.length % Ind == 0) {
                Long = sb.count / Ind
            } else {
                Long = sb.count / Ind + 1
            }
            for i in 0..<Long {
                var b=i
                if(b>=255){b=b-255}
                var row=(b).toHexString()
                while(row.count<2){
                    row="0\(row)".uppercased()
                }
                let cont=row.uppercased()
                if (i == Long - 1) {
                    let data=ObdCommand.bytesToHex([UInt8](sb.substring(i * Ind, sb.length).data(using: .utf8)!))
                    let length = (data.count/2)+3
                    ObdCommand.check(ObdCommand.Convert(data,length.toHexString(),cont))
                    progrss(100)
                    finish(writeVersion())
                    return
                } else {
                    let data=ObdCommand.bytesToHex([UInt8](sb.sub(i*Ind..<i*Ind+Ind).data(using: String.Encoding.utf8)!))
                    let length = [UInt8](sb.sub(i*Ind..<i*Ind+Ind).data(using: String.Encoding.utf8)!).count + 3
                    progrss(i * 100 / Long)
                    if(!ObdCommand.check(ObdCommand.Convert(data,length.toHexString(),cont))) {
                        finish(false)
                        return
                    }
                }
            }
            finish(true)
            
        }
        
    }
    func writeVersion()->Bool{
        SharePre.ogversion=SharePre.mcuinit
        let a="0ADA0015DDFFF5".replace("DD",OgCommand.bytesToHex([UInt8](SharePre.mcuinit.data(using: String.Encoding.utf8)!)))
        Command.sendData(GetCrcString(a))
        Command.timer.zeroing()
        var fal=0
        while(true){
            if(Command.timer.stop()>1 || cancel){
                fal+=1
                if(fal==1){return false}
                Command.timer.zeroing()
                Command.sendData(GetCrcString(a))
            }
            if(Command.rx.count==14){
                askVersion()
                return true
            }
        }
    }
    func goPrOG()->Bool{
        if(!Command.goState(BootloaderState.Bootloader)){return false}
        let a="0A8D00030000F5"
        Command.timer.zeroing()
        Command.sendData(GetCrcString(a))
        while(true){
            if(Command.timer.stop()>3 || cancel){
                return false
            }
            if(Command.rx.count>=14){
                return true
            }
            usleep(1000*100)
        }
    }
    
    func goBootloader()->Bool{
        let a="0ADD010100FFF5"
        Command.timer.zeroing()
        Command.sendData(GetCrcString(a))
        var fal=0
        while(true){
            if(Command.timer.stop() > 20 || cancel){
                fal+=1
                if(fal==1){
                    return false
                }
                Command.timer.zeroing()
                Command.sendData(GetCrcString(a))
            }
            if(Command.rx.contains("F5DD010100DD0AF501000300F70A")||Command.rx.contains("F5DD010100280AF501000300F70A")){
                return true
            }
            usleep(100*1000)
        }
    }
    
    
}
