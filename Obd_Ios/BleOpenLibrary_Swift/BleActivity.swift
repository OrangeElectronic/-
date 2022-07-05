//
//  BleActivity.swift
//  txusb
//
//  Created by 王建智 on 2019/10/19.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import CoreBluetooth
open class BleActivity: UIViewController,CBCentralManagerDelegate, CBPeripheralDelegate {
    enum SendDataError: Error {
        case CharacteristicNotFound
    }
   open  var ScannerChange:UIViewController!
    open var Pagememory=[UIViewController]()
    open var rootView: UIView!
    open var selectedPageContainer: UIViewController!
    open var IsConnect=false
    open var connectblename=""
    open var ISRUN=false
    open var haveble=false
    open var bles:[String]=[String]()
    open var centralManager: CBCentralManager!
    // 儲存連上的 peripheral，此變數一定要宣告為全域
    open var connectPeripheral: CBPeripheral!
    // 記錄所有的 characteristic
    open var charDictionary = [String: CBCharacteristic]()
    let C001_CHARACTERISTIC = "8D81"
   open func isPaired() -> Bool {
        let user = UserDefaults.standard
        if let uuidString = user.string(forKey: "KEY_PERIPHERAL_UUID")
        {
            print("uuid是\(uuidString)")
            let uuid = UUID(uuidString: uuidString)
            let list = centralManager.retrievePeripherals(withIdentifiers: [uuid!])
            if list.count > 0 {
                connectPeripheral = list.first!
                connectPeripheral.delegate = self
                return true
            }
        }
        return false
    }
    
    open func centralManagerDidUpdateState(_ central: CBCentralManager) {
        // 先判斷藍牙是否開啟，如果不是藍牙4.x ，也會傳回電源未開啟
        guard central.state == .poweredOn else {
            // iOS 會出現對話框提醒使用者
            haveble=false
            return
        }
        haveble=true
        if(isPaired()){
            print("yes")
            unpair()}
        centralManager.scanForPeripherals(withServices: nil, options: nil)
    }
    open func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
        
        guard let deviceName = peripheral.name else {
            return
        }
        print("找到藍牙裝置:\(deviceName)")
        if !(bles.contains(deviceName)){bles.append(deviceName)
        }
        guard deviceName.range(of: connectblename) != nil
            else {
                return
        }
        central.stopScan()
        let user = UserDefaults.standard
        user.set(peripheral.identifier.uuidString, forKey: "KEY_PERIPHERAL_UUID")
        user.synchronize()
        connectPeripheral = peripheral
        connectPeripheral.delegate = self
        centralManager.connect(connectPeripheral, options: nil)
        print("最大傳輸單元\( connectPeripheral.maximumWriteValueLength(for: .withResponse))")
    }
    /* 3號method */
    open func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        // 清除上一次儲存的 characteristic 資料
        charDictionary = [:]
        // 將觸發 4號method
        peripheral.discoverServices(nil)
    }
    /* 4號method */
    open func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        guard error == nil else {
            print(error!.localizedDescription)
            return
        }
        
        for service in peripheral.services! {
            // 將觸發 5號method
            connectPeripheral.discoverCharacteristics(nil, for: service)
        }
    }
    /* 5號method */
    open func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        guard error == nil else {
            print(error!.localizedDescription)
            return
        }
        self.IsConnect=false
        for characteristic in service.characteristics! {
            let uuidString = characteristic.uuid.uuidString
            charDictionary[uuidString] = characteristic
            if(uuidString=="8D81"){
                self.IsConnect=true
                print("yes")
                connectPeripheral.setNotifyValue(true, for: characteristic)
            }
            print(uuidString)
            peripheral.discoverDescriptors(for: characteristic)
        }
    }
    
    /* 將資料傳送到 peripheral */
   open func sendData(_ data:String,_ len:Int)  {
        guard let characteristic = charDictionary["8D82"] else {
            print("寫入失敗")
            return
        }
        CheckLen=len
        TmpData=""
        Rx=""
     var Tda=""
        let spi=350
        if(data.count>spi){
             var long=0
            if(data.count%spi==0){long=data.count/spi}else{
                long=data.count/spi+1
            }
            for i in 0..<long{
                let pastTime = Date().timeIntervalSince1970
                while(GetTime(pastTime)<0.05){
                    
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
                connectPeripheral.setNotifyValue(true, for: charDictionary[C001_CHARACTERISTIC]!)
                connectPeripheral.writeValue(
                    Tda.HexToByte()!,
                    for: characteristic,
                    type: .withoutResponse
                )
                print("傳送\(Tda)\n資料長度\(Tda.count)/\(Tda.HexToByte())")
            }
        }else{
            print("傳送\(data)\n資料長度\(data.count)/\(data.HexToByte())")
            connectPeripheral.writeValue(
                data.HexToByte()!,
                for: characteristic,
                type: .withoutResponse
            )
        }
        TX(data)
    }
   open func GetTime(_ timeStamp: Double)-> Double{
        let currentTime = Date().timeIntervalSince1970
        let reduceTime : TimeInterval = currentTime - timeStamp
        return reduceTime
    }
    /* 將資料傳送到 peripheral 時如果遇到錯誤會呼叫 */
   open func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        if error != nil {
            print("寫入資料錯誤: \(error!)")
        }else{
            
        }
    }
    open var CheckLen=0
    open var TmpData=""
    open var Rx=""
    /* 取得 peripheral 送過來的資料 */
  open  func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        guard error == nil else {
            print(error!)
            return
        }
        let data = characteristic.value
        //            let string = "> " + String(data: data as Data, encoding: .utf8)!
        for i in 0...data!.count-1{
            TmpData=TmpData+String(format:"%02X",data![i])
        }
        if(TmpData.count==CheckLen){Rx=TmpData
            print("---------收到數據ㄦ---------")
            print(Rx)
            RX()
        }
    }
    /* 斷線處理 */
   open func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        print("連線中斷")
        IsConnect=false
        connectblename=""
        view.showToast(text: "Bluetooth is disconnected")
        Disconnect()
        if isPaired() {
            centralManager.connect(connectPeripheral, options: nil)
        }
    }
    /* 解配對 */
    open func unpair() {
        let user = UserDefaults.standard
        user.removeObject(forKey: "KEY_PERIPHERAL_UUID")
        user.synchronize()
        guard connectPeripheral != nil else {
            return
        }
        centralManager.cancelPeripheralConnection(connectPeripheral)
    }
    open func peripheral(_ peripheral: CBPeripheral, didDiscoverDescriptorsFor characteristic: CBCharacteristic, error: Error?) {
        print("*******************************************************")
        
        if error != nil {
            print("\(error.debugDescription)")
            return
        }
        if ((characteristic.descriptors) != nil) {
            
            for x in characteristic.descriptors!{
                let descript = x as CBDescriptor?
                print("function name: DidDiscoverDescriptorForChar \(String(describing: descript?.description))")
            }
            
        }
    }
    open func peripheral(_ peripheral: CBPeripheral, didWriteValueFor descriptor: CBDescriptor, error: Error?) {
        print("--------send-------")
        print()
    }
    open func GoScanner(_ page:UIViewController){
        ScannerChange=page
        let a=UIStoryboard(name: "Ble", bundle: nil).instantiateViewController(withIdentifier: "BleScanner")as!BleScanner
        a.act=self
        addChild(a)
        self.view.addSubview(a.view)
        a.view.frame = self.view.bounds
        a.didMove(toParent: self)
    }
    open func SwipePage(to newViewController: UIViewController){
        addChild(newViewController)
        self.view.addSubview(newViewController.view)
        newViewController.view.frame = self.view.bounds
        newViewController.didMove(toParent: self)
    }
    override open func viewDidLoad() {
        super.viewDidLoad()
    }
    
    open func Disconnect(){
        
    }
     open func ChangePage(to newViewController: UIViewController) {
            Pagememory.append(newViewController)
            if selectedPageContainer != nil {
                selectedPageContainer.willMove(toParent: nil)
        selectedPageContainer.view.removeFromSuperview()
                selectedPageContainer.removeFromParent()
            }
            addChild(newViewController)
        self.rootView.addSubview(newViewController.view)
            newViewController.view.frame = rootView.bounds
            newViewController.didMove(toParent: self)
            self.selectedPageContainer = newViewController
        PageChangeLinster(newViewController)
        }

        open func GoBack() {
            if(Pagememory.count<2){return}
            if selectedPageContainer != nil {
                selectedPageContainer.willMove(toParent: nil)
                selectedPageContainer.view.removeFromSuperview()
                selectedPageContainer.removeFromParent()
            }
    let newViewController=Pagememory[Pagememory.count-2];
            addChild(newViewController)
            self.rootView.addSubview(newViewController.view)
            newViewController.view.frame = rootView.bounds
            newViewController.didMove(toParent: self)
            self.selectedPageContainer = newViewController
            Pagememory.remove(at: Pagememory.count-1)
            PageChangeLinster(newViewController)
        }
    open func PageChangeLinster(_ controler:UIViewController){
        
    }
    open func RX(){}
    open func TX(_ data:String){}
    open func LoadIng(_ a:String){
        
    }
    open func LoadingSuccess(){
        
    }
    open func Connecting(){
        centralManager.scanForPeripherals(withServices: nil, options: nil)
        LoadIng(SetLan.Setlan("paired with your device"))
        DispatchQueue.global().async {
             var fal=0
                  while(true){
                      if(self.IsConnect||fal==10){
                          DispatchQueue.main.async {
                              self.LoadingSuccess()
                              if(self.IsConnect){
                                self.ChangePage(to: self.ScannerChange)
                              }
                              if(fal==10){self.connectblename=""}
                          }
                          break
                      }
                      sleep(1)
                      fal+=1
                  }
              }
    }
    static public func getShare(_ name:String)->String{
            let preferences = UserDefaults.standard
            let currentLevelKey = name
            if preferences.object(forKey: currentLevelKey) == nil {
                return "nodata"
            } else {
                let currentLevel = preferences.string(forKey: currentLevelKey)!
                return currentLevel
            }
        }
      
    static public func writeshare(_ name:String,_ key:String){
            let preferences = UserDefaults.standard
            preferences.set(name,forKey: key)
            let didSave = preferences.synchronize()
            if !didSave {
                print("saverror")
            }
    }
}
