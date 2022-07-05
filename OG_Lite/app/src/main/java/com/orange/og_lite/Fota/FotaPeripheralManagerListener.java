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
 * Class Name: FotaPeripheralManagerListener
 ******************************************************************************/

package com.orange.og_lite.Fota;

/**
 * Callback for {@link FotaPeripheralManager FotaPeripheralManager} events
 */

public interface FotaPeripheralManagerListener {

    /**
     * Invoked when the selected fota peripheral changed
     */
    void selectedChanged(FotaPeripheral peripheral);

    /**
     * Invoked when the selected fota peripheral is going to be changed
     */
    void selectedChanging(FotaPeripheral peripheral);

    /**
     * Invoked when the {@link FotaPeripheralManager#peripherals() peripherals} list changed
     */
    void onPeripheralsListUpdated();

    /**
     * Invoked when the Bluetooth adapter is enabled
     */
    void onBluetoothEnabled();

    /**
     * Invoked when the Bluetooth adapter is disabled
     */
    void onBluetoothDisabled();

}
