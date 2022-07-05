//
//  SelectYear.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
class SelectYear: UIViewController,UITableViewDataSource,UITableViewDelegate {
    var it=PublicBeans.getYear()
    let deledate = UIApplication.shared.delegate as! AppDelegate
    var act:ViewController?=nil
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(it.count%2==0){
            return (it.count/2)
        }else{return (it.count/2)+1}
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: "Year", for: indexPath) as!Year
        let place=indexPath.row*2
        cell.b1.setTitle(it[place].year, for:.normal )
        cell.name1=it[place].year
        
        cell.act=act
        if(place+1<it.count){
            cell.b2.setTitle(it[place+1].year, for:.normal)
             cell.name2=it[place+1].year
            cell.b2.isHidden=false
        }else{
            cell.b2.isHidden=true
        }
        return cell
    }
    

    @IBOutlet var tit: UILabel!
    @IBOutlet var tb: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tb.separatorStyle = .none
        self.tb.bounces=false
        query()
        tit.text=SetLan.Setlan("Select_Year")
         print(it.count)
    }
    func query(){
         self.tb.reloadData()
    }

}
