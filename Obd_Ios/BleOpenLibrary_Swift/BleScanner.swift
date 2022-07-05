//
//  BleScanner.swift
//  BleOpenLibrary_Swift
//
//  Created by 王建智 on 2019/10/24.
//  Copyright © 2019 王建智. All rights reserved.
//


import UIKit
class BleScanner: UIViewController,UITableViewDataSource,UITableViewDelegate{
    var act:BleActivity?=nil
    var timer: Timer?
    @IBOutlet var selectdevice: UILabel!
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return act!.bles.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
         let cell=tableView.dequeueReusableCell(withIdentifier: "BleAdapter", for: indexPath) as! BleAdapter
        cell.blename.text=act!.bles[indexPath.row]
        return cell
    }
    
    
    @IBOutlet var tb: UITableView!
    
   
    @IBOutlet var cancel: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
     selectdevice.text=SetLan.Setlan("Select Device")
cancel.setTitle(SetLan.Setlan("cancel"), for: .normal)
        self.tb.separatorStyle = .none
        self.tb.bounces=false
        self.timer = Timer.scheduledTimer(timeInterval: 3, target: self, selector: #selector(updatemessage), userInfo: nil, repeats: true)
    }

    @IBAction func close(_ sender: Any) {
        self.timer!.invalidate()
        self.willMove(toParent: nil)
        view.removeFromSuperview()
        self.removeFromParent()
        self.dismiss(animated: true, completion: nil)
    }
    @ objc func updatemessage(){
        self.tb.reloadData()
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        act?.connectblename=act!.bles[indexPath.row]
        act?.Connecting()
    act!.centralManager.scanForPeripherals(withServices: nil, options: nil)
        close(self)
    }
  
}
