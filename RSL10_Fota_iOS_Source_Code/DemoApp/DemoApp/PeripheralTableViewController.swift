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
 * Class Name: PeripheralTableViewController.swift
 ******************************************************************************/

import FotaLibrary
import BleLibrary
import Foundation

class PeripheralTableViewController: UITableViewController {
    
    //MARK: Properties
    var peripherals = [FotaPeripheral]()
    
    //MARK: members
    var manager: FotaPeripheralManager = AppDelegate.shared().peripheralManager
    
    //MARK: eventhandlers
    private var _bleProducListChangedHandler: EventHandlerProtocol?
    private var _isBusyHandler: EventHandlerProtocol?
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        //Register events
        registerEvents()
        
                // if a peripheral is selected, teardown and dispose
                if AppDelegate.shared().peripheralManager.selected != nil
                {
                    do{
                        try AppDelegate.shared().peripheralManager.selected?.teardown()
                        AppDelegate.shared().peripheralManager.selected?.dispose()
                        AppDelegate.shared().peripheralManager.selected = nil
                    }
                    catch
                    {
                        //Ignore error
                    }
                }
        
        refreshControl = UIRefreshControl()
        refreshControl?.attributedTitle = NSAttributedString(string: "Searching for peripherals")
        refreshControl?.addTarget(self, action: #selector(refreshPeripheralList(_:)), for: .valueChanged)
        
        refreshPeripheralList(self)
        refreshControl?.beginRefreshing()
    }
        
//     MARK: - Table view data source
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return peripherals.count
    }
    
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Table view cell are reused and should be dequeued using a cell identifier
        let cellIdentifier = "PeripheralTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? PeripheralTableViewCell else {
            fatalError("The dequeued cell is not an instance of PeripheralTableViewCell")
        }
        
        
        // Feches the appropriate peripheral for the data srouce layout
        let peripheral = peripherals[indexPath.row]
        
        cell.nameLabel.text = peripheral.name
        cell.uuidLabel.text = peripheral.uuid.uuidString
        cell.rssiLabel.text = peripheral.rssi.description + "db"
        
        return cell
    }
    
    // MARK: - Navigation
    //Prepare application for moving to next view
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        super.prepare(for: segue, sender: sender)
        
        switch (segue.identifier ?? "")
        {
        case "ShowDevice":
            
            guard let bleDeviceViewController = segue.destination as? BleDeviceViewController
                else {
                    fatalError("Unexpected destinatino: \(segue.destination)")
            }
            
            guard let selectedDeviceCell = sender as? PeripheralTableViewCell
                else{
                    fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = tableView.indexPath(for: selectedDeviceCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            let p = peripherals[indexPath.row]
            
            AppDelegate.shared().peripheralManager.stopScan()
            AppDelegate.shared().peripheralManager.selected = p
            
            deregisterEvents()
        default:
            fatalError("Unexpected Segue Identifier; \(segue.identifier)")
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        EndRefresh()
    }
    
    //MARK: Events
    func onBleListChanged(args: EmptyEventArgs)
    {
        DispatchQueue.main.async {
            //            print("-- DispatchQueue.main.async: On blelist change")
            self.peripherals = AppDelegate.shared().peripheralManager.peripherals;
            self.tableView.reloadData()
        }
    }
    
    func onIsBusyChanged(args: IsBusyEventArgs)
    {
        if !args.isBusy
        {
            EndRefresh()
        }
        UIApplication.shared.isNetworkActivityIndicatorVisible = args.isBusy
    }
    
    //MARK: Private functions
    @objc private func refreshPeripheralList(_ sender: Any)
    {
        do{
            if AppDelegate.shared() != nil
            {
                AppDelegate.shared().peripheralManager.selected?.dispose()
                AppDelegate.shared().peripheralManager.selected = nil
            }
            
            try AppDelegate.shared().peripheralManager.clear()
            
            if(AppDelegate.shared().peripheralManager.isBluetoothEnabled())
            {
                // Start scaning for peripherals
                AppDelegate.shared().peripheralManager.startScan()
            }else
            {
                let alert = UIAlertController(title: "Bluetooth Disabled", message: "The app needs bluetooth to be enabled to work correctly", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: "Default action"), style: .default, handler: { _ in
                    alert.dismiss(animated: true, completion: nil)
                    self.EndRefresh()
                }))
                self.present(alert, animated: true, completion: nil)
            }
        }
        catch
        {
            //ignore error
        }
    }
    
    private func EndRefresh()
    {
             refreshControl?.endRefreshing()
    }
    
    private func registerEvents()
    {
        _bleProducListChangedHandler = AppDelegate.shared().peripheralManager.eventBleProductListChanged.addHandler(self, PeripheralTableViewController.onBleListChanged)
        _isBusyHandler = AppDelegate.shared().peripheralManager.eventIsBusyChanged.addHandler(self, PeripheralTableViewController.onIsBusyChanged)
    }
    
    private func deregisterEvents()
    {
        _bleProducListChangedHandler?.dispose()
        _isBusyHandler?.dispose()
    }
    
    //MARK: App Version Function
    
    @IBAction func aboutVersionButton(_ sender: UIBarButtonItem) {
        let alert = UIAlertController(title: "Version", message: getVersion(), preferredStyle: .alert)
        let action = UIAlertAction(title: "OK", style: .default) { (action) in
        }
        alert.addAction(action)
        present(alert, animated: true, completion: nil)
    }
    
    func getVersion() -> String {
        let dictionary = Bundle.main.infoDictionary!
        let version = dictionary["CFBundleShortVersionString"] as! String
        let build = dictionary["CFBundleVersion"] as! String
        return "RSL10 FOTA App Version: \(version)(\(build))"
    }
}
