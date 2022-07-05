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
 * Class Name: TaskCharacteristic.swift
 ******************************************************************************/

import Foundation
@available(iOS 11.0, *)
class TaskCharacteristic: TaskPeripheral {
    
    var characteristic: Characteristic
    {
        return _characteristic
    }
    
    private let _characteristic : Characteristic
    
    init(_ peripheral: PeripheralBase, _ manager: PeripheralManagerBaseProtocol, _ characteristic: Characteristic, _ timeout: Int)
    {
        self._characteristic = characteristic
        super.init(peripheral, manager, timeout)
    }
}
