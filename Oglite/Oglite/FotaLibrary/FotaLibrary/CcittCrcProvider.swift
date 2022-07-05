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
 * Class Name: CcittCrcProvider.swift
 ******************************************************************************/

import BleLibrary
import Foundation
struct CcittCrcProvider : CrcProviderProtocol
{
    var endianess: Endianess = Endianess.little
    
    var crcLength: Int = 2
    
    var invert: Bool = true
    
    func calculate(data: Data, offset: Int, length: Int)throws -> Data {
        var crcArray = [UInt8](repeating: 0x00, count: 2)
        var crc: UInt16 = try Crc.calculate(mode: Crc16.Kermit, buffer: [UInt8](data), offset: offset, count: length, crc: 0xFFFF)
        
        if invert
        {
            crc = UInt16(~crc)
        }
        
        if endianess == Endianess.little
        {
            try BufferAccess.WriteUInt16LittleEndian(data: crc, buffer: &crcArray, offset: 0)
        }
        else
        {
            try BufferAccess.WriteUInt16BigEndian(data: crc, buffer: &crcArray, offset: 0)
        }
            
        return Data(crcArray)
    }
}
