/*******************************************************************************
 * Copyright (c) 2019, Semiconductor Components Industries, LLC
 * (d/b/a ON Semiconductor). All rights reserved.
 *
 * This code is the property of ON Semiconductor and may not be redistributed
 * in any form without prior written permission from ON Semiconductor.
 * The terms of use and warranty for this code are covered by contractual
 * agreements between ON Semiconductor and the licensee.
 *
 * This is Reusable Code.
 *
 * Class Name: CharacteristicChangedListener
 ******************************************************************************/

package com.onsemi.ble;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Callback for the BluetoothGatt characteristic changed event
 */

interface CharacteristicChangedListener {

    /**
     * Invoke when the characterstic changed
     * @param characteristic The characteristic
     * @param data The data of the characteristic at the time when the data was received.
     *             The characteristics data can change while they are processed
     */
    void onCharacteristicChanged(BluetoothGattCharacteristic characteristic, byte[] data);
}
