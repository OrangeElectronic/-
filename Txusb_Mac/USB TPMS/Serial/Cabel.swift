//
//  Cabel.swift
//  Peripheral
//
//  Created by 王建智 on 2019/7/31.
//  Copyright © 2019 KoKang Chu. All rights reserved.
//

import Foundation
import ORSSerial
class SerialPortDemoController: NSObject, ORSSerialPortDelegate, NSUserNotificationCenterDelegate {
    var timer: Timer?
    var main:MainPrace!
    @objc var serialPortManager = ORSSerialPortManager.shared()
    @objc let availableBaudRates = [300, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200, 230400]
    @objc dynamic var shouldAddLineEnding = false
    @objc dynamic var serialPort: ORSSerialPort! {
        didSet {
            oldValue?.close()
            oldValue?.delegate = nil
            serialPort?.delegate = self
        }
    }
    
    func SeeAvailable(_ act:MainPrace){
        main=act
        serialPortManager = ORSSerialPortManager.shared()
        print(serialPortManager.availablePorts)
        for i in serialPortManager.availablePorts{
            if(i.name.contains("usbserial")){
                print("isconnect")
                serialPort = i
                serialPort.baudRate = 115200
                serialPort.open()
                main.UsbIsConnect=true
                main.NoConnect.isHidden=true
                serialPort.send("0AFE10000754504D537331F5".HexToByte()!) // someData is an NSData object
            }
        }
//        serialPort.close()
//        serialPort=serialPort
    }
    func serialPortWasRemovedFromSystem(_ serialPort: ORSSerialPort) {
        self.serialPort = nil
        main.UsbIsConnect=false
        main.NoConnect.isHidden=false
    }
    func serialPort(_ serialPort: ORSSerialPort, didReceive data: Data) {
        print(bytesToHex([UInt8](data)))
        for i in 0...data.count-1{
            main.TmpData=main.TmpData+String(format:"%02X",data[i])
        }
        print("USB:\(main.TmpData)")
        if(main.TmpData.count==main.CheckLen||main.CheckLen==0){
            main.Rx=main.TmpData
        }
//        if let string = NSString(data: data, encoding: String.Encoding.utf8.rawValue) {
//           print(string)
//        }
    }
    func bytesToHex(_ bt:[UInt8])->String{
        var re=""
        for i in 0..<bt.count{
            re=re.appending(String(format:"%02X",bt[i]))
        }
        return re
    }
}
