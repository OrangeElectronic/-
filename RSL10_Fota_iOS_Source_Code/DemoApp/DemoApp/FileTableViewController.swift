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
 * Class Name: FileTableViewController.swift
 ******************************************************************************/

import FotaLibrary
import UIKit
import Foundation
class FileTableViewController: UITableViewController {
    
    var files = [String]()
    
    var fotaFile: FotaFile?
    
    //Table view cells are reused and should be dequeued using a cell identifier
    let cellIdentifier = "FileTableViewCell"
    
    override func viewWillAppear(_ animated: Bool) {
        backButton()
        
        if files.isEmpty
        {
            let alert = UIAlertController(title: "Warning", message: "No fota file found", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { action in
                switch action.style{
                case .default:
                    print("default")
                case .cancel:
                    //Do nothing
                    break;
                case .destructive:
                    //Do nothing
                    break;
                }}))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        fotaFile = nil;
        
        do {
            var documentsURL = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false)
            documentsURL.appendPathComponent("Inbox")
            let docs = try FileManager.default.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: [], options:  [.skipsHiddenFiles, .skipsSubdirectoryDescendants])
            let fotaFiles = docs.filter{ $0.pathExtension == "fota" }
            
            for url in fotaFiles
            {
                files.append(url.pathComponents.last!)
            }
            
            print("FileTableViewController:  \(files)")
        } catch {
            print(error)
        }
        
    }
    
    private func backButton(){
        let backBarButton = UIBarButtonItem(withCustomType: .backButton,
                                            target: self,
                                            action: #selector(BleDeviceViewController.backButtonAction))
        self.navigationItem.leftBarButtonItem = backBarButton
    }
    
    @objc func backButtonAction() {
        self.navigationController?.popViewController(animated: true)
    }
    
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return files.count
    }
    
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? FileTableViewCell else
        {
            fatalError("The dequeued cell is not an instance of FileTableViewCell")
        }
        
        let fileName = files[indexPath.row]
        
        cell.fileNameLabel.text = fileName
        
        return cell
    }
    
    
    // MARK: - Navigation
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        
        guard let selectedFileCell = sender as? FileTableViewCell
            else{
                fatalError("Unexpected sender: \(String(describing: sender))")
        }
        
        guard let indexPath = tableView.indexPath(for: selectedFileCell) else {
            fatalError("The selected cell is not being displayed by the table")
        }
        
        let selectedFile = files[indexPath.row]
        
        fotaFile = FotaFile(fileName: selectedFile)
    }
}
