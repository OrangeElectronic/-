//
//  Extend.swift
//  Peripheral
//
//  Created by 王建智 on 2019/7/25.
//  Copyright © 2019 KoKang Chu. All rights reserved.
//

import Foundation
import Cocoa
extension NSImage.Name {
    static let img_car = NSImage.Name("img_car")
    static let img_car95 = NSImage.Name("img_car95")
     static let img_rectangle_Blue = NSImage.Name("img_rectangle_Blue")
     static let img_rectangle_error = NSImage.Name("img_rectangle_error")
     static let img_rectangle_gray = NSImage.Name("img_rectangle_gray")
     static let id1 = NSImage.Name("ID1")
     static let id2 = NSImage.Name("id2")
     static let Prbackg = NSImage.Name("graybtn")
    static let Prbackb = NSImage.Name("button_normal")
    static let pr1 = NSImage.Name("pr1")
    static let PR2 = NSImage.Name("PR2")
     static let img_rectangle_Green = NSImage.Name("img_rectangle_Green")
     static let img_rectangle_white = NSImage.Name("img_rectangle_white")
     static let icon_Round_error = NSImage.Name("icon_Round_error")
     static let icon_Round_normal = NSImage.Name("icon_Round_normal")
     static let icon_Round_OK = NSImage.Name("icon_Round_OK")
    static let img_Mode_menu = NSImage.Name("img_Mode menu")
    static let img_Dropdownmenu_Blue=NSImage.Name("img_Drop down menu_Blue")
    static let img_Dropdownmenu_gray=NSImage.Name("img_Drop down menu_gray")
    static let img_Dropdownmenu_white=NSImage.Name("img_Drop down menu_white")
    static let icon_Click_OK=NSImage.Name("icon_Click_OK")
    static let icon_Click_NO=NSImage.Name("icon_Click_NO")
     static let icon_Click_normal=NSImage.Name("icon_Click_normal")
    
    
    
}
extension NSTextField {

    func SetPlaceText(_ fontsizt:CGFloat,_ color:NSColor,_ text:String){
        let st = NSMutableParagraphStyle()
        st.alignment = NSTextAlignment.center
        let font = NSFont.systemFont(ofSize: fontsizt)
        let attrs = [NSAttributedString.Key.foregroundColor: color, NSAttributedString.Key.font: font,NSAttributedString.Key.paragraphStyle:st]
        let placeHolderStr = NSAttributedString(string: text, attributes: attrs)
        (self.cell as! NSTextFieldCell).placeholderAttributedString = placeHolderStr
    }
    
}
extension String {
    func  sub(_ range: CountableRange<Int>) -> String {
        let idx1 = index(startIndex, offsetBy: max(0, range.lowerBound))
        let idx2 = index(startIndex, offsetBy: min(self.count, range.upperBound))
        return String(self[idx1..<idx2])
    }
    
    func replace(_ target: String, _ withString: String) -> String
    {
        return self.replacingOccurrences(of: target, with: withString, options: NSString.CompareOptions.literal, range: nil)
    }
    
    func HexToByte() -> Data? {
        let trimmedString = self.trimmingCharacters(in: CharacterSet(charactersIn: "<> ")).replacingOccurrences(of: " ", with: "")
        
        // make sure the cleaned up string consists solely of hex digits, and that we have even number of them
        
        let regex = try! NSRegularExpression(pattern: "^[0-9a-f]*$", options: .caseInsensitive)
        
        let found = regex.firstMatch(in: trimmedString, options: [], range: NSMakeRange(0, trimmedString.count))
        if found == nil || found?.range.location == NSNotFound || trimmedString.count % 2 != 0 {
            return nil
        }
        
        // everything ok, so now let's build NSData
        
        let data = NSMutableData(capacity: trimmedString.count / 2)
        
        var index = trimmedString.startIndex
        while index < trimmedString.endIndex {
            let byteString = String(trimmedString[index ..< trimmedString.index(after: trimmedString.index(after: index))])
            let num = UInt8(byteString.withCString { strtoul($0, nil, 16) })
            data?.append([num] as [UInt8], length: 1)
            index = trimmedString.index(after: trimmedString.index(after: index))
        }
        return data as Data?
    }
    
}
