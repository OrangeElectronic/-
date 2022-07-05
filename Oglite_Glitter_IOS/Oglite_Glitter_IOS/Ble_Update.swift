//
//  Ble_Update.swift
//  Oglite_Glitter_IOS
//
//  Created by Jianzhi.wang on 2021/4/22.
//

import Foundation
import FotaLibrary
import BleLibrary
import JzOsTaskHandler
public class Ble_Update{
    public  static var bleUpdate = false
    var manager: FotaPeripheralManager = AppDelegate.peripheralManager
    var fotaFile: FotaFile?
    public  var bleUpdateResult = false
    private var _stateChangedHandler: EventHandlerProtocol?
    private var _progressHandler: EventHandlerProtocol?
    private var _completedHandler: EventHandlerProtocol?
    private var runQueue = DispatchQueue(label: "fota.RunQueue")
    private var currentProgress: Int? = 0
    private var lastProgress: Int? = nil
    private var throughput: Double = 0.0
    private var throughputTimer: Timer? = nil
    private var controller: FotaController?
    private var currentStep: FotaUpdateStep = FotaUpdateStep.idle
    private var progressLock = DispatchQueue(label: "com.onsemi.fota.BleDeviceViewController.progressLock", attributes:  .concurrent)
    private func run()
    {
        guard manager.selected != nil else
        {
            return
        }
        
        guard fotaFile != nil else
        {
            print("Error: No file selected!")
            return
        }
        
        
        runQueue.async{
            self.currentProgress = 0
            self.lastProgress = 0
            self.throughput = 0
            self.controller = FotaController()
            var options = FotaOptions()
            options.forceUpdate = false
            
            self._progressHandler = self.controller?.eventProgress.addHandler(self, Ble_Update.onProgress)
            self._completedHandler = self.controller?.eventCompleted.addHandler(self, Ble_Update.onCompleted)
            let setup = UpdateSetup(provider: self.controller!, source: self.fotaFile ,options: options)
            
            DispatchQueue.main.async{
                self.throughputTimer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(self.onProgressTimerElapsed(timer:)), userInfo: nil, repeats: true)
            }
            
            do
            {
                try self.manager.selected?.update(updateSetup: setup)
            }
            catch
            {
                print("Update failed: \(error)")
            }
            
            self._progressHandler?.dispose()
            self._completedHandler?.dispose()
            
            if self.manager.selected!.state != PeripheralState.idle
            {
                do{
                    try self.manager.selected?.teardown()
                }
                catch
                {
                    print("Teardown faild")
                }
            }
        }
    }
    
    //更新完成
    func onCompleted(args: FotaCompletedEventArgs)
    {
        DispatchQueue.main.async
        {
            print("Update finnished. Fota: \(args.status.description)")
        }
        bleUpdateResult = (args.status.description=="success")
        self.throughputTimer?.invalidate()
        self.throughputTimer = nil
        Ble_Update.bleUpdate=false
    }
    
    //更新中
    func onProgress(args: FotaProgressEventArgs)
    {
        DispatchQueue.main.async {
            if args.current == 0
            {
                self.currentStep = args.step
                print("\(self.currentStep.description)")
            }
            
            if args.total == 0
            {
                self.currentProgress = 0;
            }
            else
            {
                self.progressLock.sync {
                    self.currentProgress = args.current
                }
                let progress: Float = Float(args.current) / Float(args.total)
                print(progress)
            }
        }
    }
    @objc private func onProgressTimerElapsed(timer: Timer)
    {
        progressLock.sync
        {
            if (currentStep == FotaUpdateStep.updateFotaImage || currentStep == FotaUpdateStep.updateAppImage)
            {
                let progress = currentProgress! - lastProgress!
                lastProgress = currentProgress!
                if currentProgress == 0
                {
                    throughput = 0
                    return
                }
                
                throughput = Double(progress) / 1024
                DispatchQueue.main.async {
                    print("\(self.currentStep.description) - \(String(format: "%.2f", self.throughput))kB/s")
                }
            }
        }
    }
    //更新函示
    public func updateBle(_ data:Data)->Bool{
        Ble_Update.bleUpdate=true
        print("updateBle")
        sleep(1)
        let dst = NSHomeDirectory() + "/Documents/update.fota"
        if let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
            let fileURL = dir.appendingPathComponent("update.fota")
            //writing
            try! data.write(to: fileURL)
        }
        var havrFota=false
        let clock=TaskHandler.newInstance().clock()
        clock.zeroing()
        AppDelegate.peripheralManager.startScan()
        while(clock.stop()<40){
            print("perSize:\(AppDelegate.peripheralManager.peripherals)");
            print("perSize:\(AppDelegate.peripheralManager.peripherals.count)")
            for a in 0..<AppDelegate.peripheralManager.peripherals.count{
                print("name:\(AppDelegate.peripheralManager.peripherals[a].name)")
            }
            
            let fo=AppDelegate.peripheralManager.peripherals.filter({["ON FOTA RSL10","CS8025v100"].contains($0.name)})
            if(fo.count>0){
                print("FotaExists"+fo[fo.count-1].name)
                AppDelegate.peripheralManager.selected = fo[fo.count-1]
                havrFota=true
            }
            if(havrFota){
                break
            }
            usleep(100*1000)
        }
        AppDelegate.peripheralManager.stopScan()
        
        if(havrFota){
            Ble_Update.bleUpdate=true
            print("dst fileExists")
            self.fotaFile = FotaFile(fileName: dst)
            self.run()
            while Ble_Update.bleUpdate {
                sleep(1)
            }
            Ble_Update.bleUpdate=false
            return bleUpdateResult
        }else{
            Ble_Update.bleUpdate=false
            print("noFotaExists")
            return false
        }
    }
}
