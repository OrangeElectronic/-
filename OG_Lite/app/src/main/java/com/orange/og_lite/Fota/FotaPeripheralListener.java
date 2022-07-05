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
 * Class Name: FotaPeripheralListener
 ******************************************************************************/

package com.orange.og_lite.Fota;

/**
 * Callbacks for {@link FotaPeripheral FotaPeripheral} changes
 */

public interface FotaPeripheralListener {

    /**
     * Invoked when the peripherals RSSI value changed
     * @param rssi The current RSSI value
     */
    void onRssiChanged(int rssi);

    /**
     * Invoked when the peripherals {@link FotaState state} changed
     * @param oldState The old state.
     * @param newState The new state.
     */
    void onStateChanged(FotaState oldState, FotaState newState);

    /**
     * Invoked when the connection was terminated
     * @param fromHost True when the host terminated the connection, false when the user terminated the connection
     */
    void onDisconnected(boolean fromHost);

}
