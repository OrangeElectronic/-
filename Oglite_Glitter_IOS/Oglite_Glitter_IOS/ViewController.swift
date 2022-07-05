
import UIKit
import Glitter_IOS
import Glitter_BLE
import JzOsHttpExtension
import ZipArchive
import JzOsTaskHandler
import JzOsSqlHelper
class ViewController: UIViewController {
    let glitterAct=GlitterActivity.getInstance()
    @IBOutlet var container: UIView!
    
    override func viewDidLoad() {
        //取得裝置資訊
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "getSystemVersion", function: {
            request in
            request.responseValue["version"]=UIDevice.current.systemVersion
            request.responseValue["model"]=UIDevice.modelName
            request.responseValue["make"]="Apple"
            request.finish()
        }))
        //BLE更新
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "updateBle", function: {
            item in
            DispatchQueue.global().async {
                print("updateBle-function")
                sleep(5)
                let result=HttpCore.get(item.receiveValue["rout"] as! String,10)
                if(result==nil){
                    item.responseValue["result"]=false
                    print("updateBle--error")
                }else{
                    item.responseValue["result"]=Ble_Update().updateBle(result!)
                    print("updateBle--dsuccess--result:\(item.responseValue["result"]!)--")
                }
                item.finish()
            }
        }))
        
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "openQrScanner", function: {
            request in
            let scanner=Page_Scanner()
            self.addChild(scanner)
            self.view.addSubview(scanner.view)
            scanner.view.frame=self.view.safeAreaLayoutGuide.layoutFrame
            self.glitterAct.didMove(toParent: self)
            scanner.scanback = {a in
                request.responseValue["data"]=a
                request.finish()
                return ()
            }
        }))
        //檔案下載並解壓縮
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "FileDonload", function: {
            request in
            DispatchQueue.global().async {
                let rout=(request.receiveValue["serverRout"] as! String) + "?zip=true"
                print("result->rout->\(rout)")
                let timeOut=request.receiveValue["timeOut"] as! Int
                let fileName=request.receiveValue["fileName"] as! String
                let file=HttpCore.get(rout,TimeInterval(30*1000))
                let dst =  NSHomeDirectory() + "/Documents/\(fileName).zip"
                let routArray=dst.split(separator: "/")
                print("result->startCreateRout:\(routArray.count)")
                print("result->createRout:\(dst.sub(0..<(dst.count-routArray[routArray.count-1].count)))")
                let fm = FileManager.default
                if !fm.fileExists(atPath: dst) {
                    try! fm.createDirectory(atPath: dst.sub(0..<(dst.count-routArray[routArray.count-1].count-1)), withIntermediateDirectories: true, attributes: nil)
                    try! fm.createFile(atPath: dst, contents: nil, attributes: nil)
                }
                let urlfrompath = URL(fileURLWithPath: dst)
                print("加載路徑:\(urlfrompath)")
                if(file==nil){
                    print("下載失敗")
                    request.responseValue["result"]=false
                    request.finish()
                }else{
                    do{
                        try file?.write(to: urlfrompath)
                        do {
                            let date=Date().timeIntervalSince1970
                            let unzipPath=(NSHomeDirectory() + "/Documents/\(date)/")
                            let fm = FileManager.default
                            print("result->date->\(date)")
                            try? fm.createDirectory(atPath: unzipPath, withIntermediateDirectories: true, attributes: nil)
                            SSZipArchive.unzipFile(atPath: urlfrompath.path, toDestination: unzipPath)
                            let fileList = try FileManager.default.contentsOfDirectory(atPath:unzipPath)
                            if(fileList.count==1){
                                let loading =  try Data(contentsOf: URL(fileURLWithPath: "\(unzipPath)\(fileList[0])"))
                                print("檔案解壓->\(fileName)-\(loading.count)")
                                var isDirectory:ObjCBool = false
                                let path=NSHomeDirectory() + "/Documents/\(fileName)"
                                let isExist = FileManager.default.fileExists(atPath: path, isDirectory: &isDirectory)
                                if(isExist){
                                    try FileManager.default.removeItem(atPath:NSHomeDirectory() + "/Documents/\(fileName)")
                                }
                                try FileManager.default.copyItem(atPath:  "\(unzipPath)\(fileList[0])", toPath:   NSHomeDirectory() + "/Documents/\(fileName)")
                                request.responseValue["result"]=true
                                request.finish()
                            }else{
                                request.responseValue["result"]=false
                                request.finish()
                            }
                        }
                        catch {
                            print("error:\(error)")
                            print("Something went wrong")
                            request.responseValue["result"]=false
                            request.finish()
                        }
                        
                    }catch{
                        print("error:\(error)")
                        request.responseValue["result"]=false
                        request.finish()
                    }
                }
                print("result->\(request.responseValue["result"])")
            }
        }))
        glitterAct.addJavacScriptInterFace(interface: JavaScriptInterFace(functionName: "toAppStore", function: { request in
            DispatchQueue.main.async {
                let urlString = "itms-apps://itunes.apple.com/app/id1513547293"
                if let url = URL(string: urlString) {
                    //根据iOS系统版本，分别处理
                    if #available(iOS 10, *) {
                        UIApplication.shared.open(url, options: [:],
                                                  completionHandler: {
                            (success) in
                        })
                    } else {
                        UIApplication.shared.openURL(url)
                    }
                }
            }
        }))
        UIApplication.shared.isIdleTimerDisabled = true
        Glitter_BLE.getInstance().create()
    }
    override func viewDidAppear(_ animated: Bool) {
        //Glitter跨平台開發框架
        Glitter_BLE.getInstance().create()
        addChild(glitterAct)
        view.addSubview(glitterAct.view)
        glitterAct.view.frame=view.safeAreaLayoutGuide.layoutFrame
        glitterAct.didMove(toParent: self)
    }
    
}
//FotaExistsOG_LITE-00087A


extension String{
    func  sub(_ range: CountableRange<Int>) -> String {
        let idx1 = index(startIndex, offsetBy: max(0, range.lowerBound))
        let idx2 = index(startIndex, offsetBy: min(self.count, range.upperBound))
        return String(self[idx1..<idx2])
    }
}
public extension UIDevice {

    static let modelName: String = {
        var systemInfo = utsname()
        uname(&systemInfo)
        let machineMirror = Mirror(reflecting: systemInfo.machine)
        let identifier = machineMirror.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8, value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }

        func mapToDevice(identifier: String) -> String { // swiftlint:disable:this cyclomatic_complexity
            #if os(iOS)
            switch identifier {
            case "iPod5,1":                                       return "iPod touch (5th generation)"
            case "iPod7,1":                                       return "iPod touch (6th generation)"
            case "iPod9,1":                                       return "iPod touch (7th generation)"
            case "iPhone3,1", "iPhone3,2", "iPhone3,3":           return "iPhone 4"
            case "iPhone4,1":                                     return "iPhone 4s"
            case "iPhone5,1", "iPhone5,2":                        return "iPhone 5"
            case "iPhone5,3", "iPhone5,4":                        return "iPhone 5c"
            case "iPhone6,1", "iPhone6,2":                        return "iPhone 5s"
            case "iPhone7,2":                                     return "iPhone 6"
            case "iPhone7,1":                                     return "iPhone 6 Plus"
            case "iPhone8,1":                                     return "iPhone 6s"
            case "iPhone8,2":                                     return "iPhone 6s Plus"
            case "iPhone8,4":                                     return "iPhone SE"
            case "iPhone9,1", "iPhone9,3":                        return "iPhone 7"
            case "iPhone9,2", "iPhone9,4":                        return "iPhone 7 Plus"
            case "iPhone10,1", "iPhone10,4":                      return "iPhone 8"
            case "iPhone10,2", "iPhone10,5":                      return "iPhone 8 Plus"
            case "iPhone10,3", "iPhone10,6":                      return "iPhone X"
            case "iPhone11,2":                                    return "iPhone XS"
            case "iPhone11,4", "iPhone11,6":                      return "iPhone XS Max"
            case "iPhone11,8":                                    return "iPhone XR"
            case "iPhone12,1":                                    return "iPhone 11"
            case "iPhone12,3":                                    return "iPhone 11 Pro"
            case "iPhone12,5":                                    return "iPhone 11 Pro Max"
            case "iPhone12,8":                                    return "iPhone SE (2nd generation)"
            case "iPhone13,1":                                    return "iPhone 12 mini"
            case "iPhone13,2":                                    return "iPhone 12"
            case "iPhone13,3":                                    return "iPhone 12 Pro"
            case "iPhone13,4":                                    return "iPhone 12 Pro Max"
            case "iPhone14,4":                                    return "iPhone 13 mini"
            case "iPhone14,5":                                    return "iPhone 13"
            case "iPhone14,2":                                    return "iPhone 13 Pro"
            case "iPhone14,3":                                    return "iPhone 13 Pro Max"
            case "iPad2,1", "iPad2,2", "iPad2,3", "iPad2,4":      return "iPad 2"
            case "iPad3,1", "iPad3,2", "iPad3,3":                 return "iPad (3rd generation)"
            case "iPad3,4", "iPad3,5", "iPad3,6":                 return "iPad (4th generation)"
            case "iPad6,11", "iPad6,12":                          return "iPad (5th generation)"
            case "iPad7,5", "iPad7,6":                            return "iPad (6th generation)"
            case "iPad7,11", "iPad7,12":                          return "iPad (7th generation)"
            case "iPad11,6", "iPad11,7":                          return "iPad (8th generation)"
            case "iPad12,1", "iPad12,2":                          return "iPad (9th generation)"
            case "iPad4,1", "iPad4,2", "iPad4,3":                 return "iPad Air"
            case "iPad5,3", "iPad5,4":                            return "iPad Air 2"
            case "iPad11,3", "iPad11,4":                          return "iPad Air (3rd generation)"
            case "iPad13,1", "iPad13,2":                          return "iPad Air (4th generation)"
            case "iPad2,5", "iPad2,6", "iPad2,7":                 return "iPad mini"
            case "iPad4,4", "iPad4,5", "iPad4,6":                 return "iPad mini 2"
            case "iPad4,7", "iPad4,8", "iPad4,9":                 return "iPad mini 3"
            case "iPad5,1", "iPad5,2":                            return "iPad mini 4"
            case "iPad11,1", "iPad11,2":                          return "iPad mini (5th generation)"
            case "iPad14,1", "iPad14,2":                          return "iPad mini (6th generation)"
            case "iPad6,3", "iPad6,4":                            return "iPad Pro (9.7-inch)"
            case "iPad7,3", "iPad7,4":                            return "iPad Pro (10.5-inch)"
            case "iPad8,1", "iPad8,2", "iPad8,3", "iPad8,4":      return "iPad Pro (11-inch) (1st generation)"
            case "iPad8,9", "iPad8,10":                           return "iPad Pro (11-inch) (2nd generation)"
            case "iPad13,4", "iPad13,5", "iPad13,6", "iPad13,7":  return "iPad Pro (11-inch) (3rd generation)"
            case "iPad6,7", "iPad6,8":                            return "iPad Pro (12.9-inch) (1st generation)"
            case "iPad7,1", "iPad7,2":                            return "iPad Pro (12.9-inch) (2nd generation)"
            case "iPad8,5", "iPad8,6", "iPad8,7", "iPad8,8":      return "iPad Pro (12.9-inch) (3rd generation)"
            case "iPad8,11", "iPad8,12":                          return "iPad Pro (12.9-inch) (4th generation)"
            case "iPad13,8", "iPad13,9", "iPad13,10", "iPad13,11":return "iPad Pro (12.9-inch) (5th generation)"
            case "AppleTV5,3":                                    return "Apple TV"
            case "AppleTV6,2":                                    return "Apple TV 4K"
            case "AudioAccessory1,1":                             return "HomePod"
            case "AudioAccessory5,1":                             return "HomePod mini"
            case "i386", "x86_64", "arm64":                                return "Simulator \(mapToDevice(identifier: ProcessInfo().environment["SIMULATOR_MODEL_IDENTIFIER"] ?? "iOS"))"
            default:                                              return identifier
            }
            #elseif os(tvOS)
            switch identifier {
            case "AppleTV5,3": return "Apple TV 4"
            case "AppleTV6,2": return "Apple TV 4K"
            case "i386", "x86_64": return "Simulator \(mapToDevice(identifier: ProcessInfo().environment["SIMULATOR_MODEL_IDENTIFIER"] ?? "tvOS"))"
            default: return identifier
            }
            #endif
        }

        return mapToDevice(identifier: identifier)
    }()

}
