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
 * Class Name: TaskStartedEventArgs.swift
 ******************************************************************************/

import Foundation
public struct TaskStartedEventArgs {
    
    public var task: TaskProtocol
    {
        return _task
    }
    
    private var _task: TaskProtocol
    
    init(task: TaskProtocol){
        self._task = task
    }
}
