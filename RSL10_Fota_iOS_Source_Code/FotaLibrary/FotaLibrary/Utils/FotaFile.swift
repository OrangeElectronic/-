/******************************************************************************
 * Copyright © 2019, Semiconductor Components Industries, LLC
 * (d/b/a ON Semiconductor). All rights reserved.
 *
 * This code is the property of ON Semiconductor and may not be redistributed
 * in any form without prior written permission from ON Semiconductor.
 * The terms of use and warranty for this code are covered by contractual
 * agreements between ON Semiconductor and the licensee.
 *
 * This is Reusable Code.
 *
 * Class Name: FotaFile.swift
 ******************************************************************************/

import Foundation
public struct FotaFile
{
    //MARK: Properties
    /**
     Get the fota file name
     */
    public var fileName: String
    {
        return _fileName
    }
    
    /**
     Get the fota firmware image
     */
    public var fotaImage: FotaFirmwareImage
    {
        return _fotaImage
    }
    
    /**
     Get the app image
     */
    public var AppImage: FotaFirmwareImage
    {
        return _appImage
    }
    
    //MARK: Members
    private var _fileName: String
    private var _fotaImage: FotaFirmwareImage
    private var _appImage: FotaFirmwareImage
    
    //MARK: Initilizer
    /**
     Instantiates an fota file object. The file shall exist inte the device onbox folder
     
     - parameters:
        - fileName: The name of the fota file
     
     - throws:
        - fatalError
     */
    public init(fileName: String)
    {
        _fileName = fileName
        do
        {
//            var dir = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false)
//            dir.appendPathComponent("Inbox")
//            dir.appendPathComponent(fileName)
            let fileData = try Data(contentsOf: URL(fileURLWithPath: fileName))
            var offset: UInt32 = 0
            
            _fotaImage = FotaFirmwareImage()
            _fotaImage.parse(fileData: fileData, offset: &offset)
            
            _appImage = FotaFirmwareImage()
            _appImage.parse(fileData: fileData, offset: &offset)
            
            NSLog("FotaImage: fota:\(_fotaImage.version)")
            NSLog("FotaImage: app:\(_appImage.version)")
        }
        catch
        {
            fatalError("FotaFile: Error in creating FotaFile: \(error)")
        }
    }    
}
