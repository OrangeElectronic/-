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
 * Class Name: FotaPeripheralManagerImpl
 ******************************************************************************/

package com.orange.oglite_glitter.Fota;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.util.Log;
import com.onsemi.ble.CreatePeripheralFunction;
import com.onsemi.ble.Peripheral;
import com.onsemi.ble.PeripheralManagerImpl;
import com.onsemi.ble.PeripheralManagerListener;

import java.util.LinkedList;
import java.util.List;


/**
 * The implementation of the peripheral manager for FOTA peripherals
 */

public class FotaPeripheralManagerImpl extends PeripheralManagerImpl<FotaPeripheral> implements FotaPeripheralManager {

    private static String TAG =  "FotaPeripheralManager";

    /**
     * The list of FotaPeripheralManagerListeners
     */
    private LinkedList<FotaPeripheralManagerListener> listenerList;

    /**
     * The constructor
     * @param context The current application context
     */
    public FotaPeripheralManagerImpl(Context context) {
        super(context, new CreatePeripheralFunction<FotaPeripheral>() {
            @Override
            public FotaPeripheral create(BluetoothDevice device, int rssi, ScanRecord scanRecord) {
                return new FotaPeripheralImpl(device, rssi, scanRecord);
            }
        });

        listenerList = new LinkedList<>();
        addListener(new PeripheralManagerListener() {
            @Override  public void onPeripheralsListUpdated() { invokePeripheralsListUpdated(); }
            @Override  public void onPeripheralDiscovered(Peripheral p) {}
            @Override  public void onBluetoothEnabled() { invokeBluetoothEnabled(); }
            @Override  public void onBluetoothDisabled() { invokeBluetoothDisabled(); }
        });

    }

    /**
     * The selected peripheral.
     * The connection to the previous peripheral will be terminated when a new one is set.
     */
    private FotaPeripheral selected;
    public FotaPeripheral getSelected() {
        return selected;
    }

    public void setSelected(FotaPeripheral p) {
        if(selected == p) {
            return;
        }
        invokeSelectedChanging();
        if(selected != null) {
            try {
                selected.teardown();
            }
            catch(Exception ex) {
                Log.e(TAG, "Unselect peripheral failed: " + ex.getMessage());
            }
        }
        selected = p;
        invokeSelectedChanged();
    }

    @Override
    public boolean canRemove(FotaPeripheral p) { return !p.equals(selected); }

    /**
     * Add a FotaPeripheralManagerListener
     * @param listener The listener to add
     */
    public void addListener(FotaPeripheralManagerListener listener)
    {
        synchronized (listenerList) {
            listenerList.add(listener);
        }
    }

    /**
     * Remove a FotaPeripheralManagerListener
     * @param listener The listener to remove
     */
    public void removeListener(FotaPeripheralManagerListener listener)
    {
        synchronized (listenerList) {
            listenerList.remove(listener);
        }
    }

    /**
     * Invokes the selectedChanging callback
     */
    private void invokeSelectedChanging() {
        List<FotaPeripheralManagerListener> listCopy;
        synchronized (listenerList) {
            listCopy = new LinkedList<>(listenerList);
        }
        try {
            for(FotaPeripheralManagerListener listener : listCopy) {
                listener.selectedChanging(selected);
            }
        }
        catch(Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

    }

    /**
     * Invokes the selectedChanged callback
     */
    private void invokeSelectedChanged() {
        List<FotaPeripheralManagerListener> listCopy;
        synchronized (listenerList) {
            listCopy = new LinkedList<>(listenerList);
        }
        try {
            for(FotaPeripheralManagerListener listener : listCopy) {
                listener.selectedChanged(selected);
            }
        }
        catch(Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Invokes the onPeripheralsListUpdated callback
     */
    private void invokePeripheralsListUpdated() {
        List<FotaPeripheralManagerListener> listCopy;
        synchronized (listenerList) {
            listCopy = new LinkedList<>(listenerList);
        }
        try {
            for(FotaPeripheralManagerListener listener : listCopy) {
                listener.onPeripheralsListUpdated();
            }
        }
        catch(Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Invokes the onBluetoothEnabled callback
     */
    private void invokeBluetoothEnabled() {
        List<FotaPeripheralManagerListener> listCopy;
        synchronized (listenerList) {
            listCopy = new LinkedList<>(listenerList);
        }
        try {
            for(FotaPeripheralManagerListener listener : listCopy) {
                listener.onBluetoothEnabled();
            }
        }
        catch(Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }


    /**
     * Invokes the onBluetoothDisabled callback
     */
    private void invokeBluetoothDisabled() {
        List<FotaPeripheralManagerListener> listCopy;
        synchronized (listenerList) {
            listCopy = new LinkedList<>(listenerList);
        }
        try {
            for(FotaPeripheralManagerListener listener : listCopy) {
                listener.onBluetoothDisabled();
            }
        }
        catch(Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

}
