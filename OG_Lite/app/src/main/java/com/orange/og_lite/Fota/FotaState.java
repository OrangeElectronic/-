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
 * Class Name: FotaState
 ******************************************************************************/

package com.orange.og_lite.Fota;

/**
 * All possible state of the fota peripheral
 */

public enum FotaState {
    /**
     * Peripheral is not used.
     */
    Idle,

    /**
     * Attempt to establish a link to the peripheral.
     */
    EstablishLink,

    /**
     * Peripheral is connected and the services are going to be discovered.
     */
    DiscoveringServices,

    /**
     * The initial checks and readings from the peripherals are done now.
     */
    Initialize,

    /**
     * Firmware update is in progress.
     */
    Update,

    /**
     * The peripheral is ready to be used.
     */
    Ready,

    /**
     * Tear down the peripheral link.
     */
    TearDownLink,
}
