//
//  SelectModel.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
class SelectModel: UIViewController,UITableViewDataSource,UITableViewDelegate  {
    var act:ViewController?=nil
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
         if(it.count%2==0){ return it.count/2}else{ return (it.count/2)+1}
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: "Model", for: indexPath) as! Model
        cell.act=act
        let place=indexPath.row*2
        cell.n1=it[place].modele
        cell.b1.setTitle(it[place].modele, for: .normal)
        if(place+1<it.count){
            cell.b2.setTitle(it[place+1].modele, for: .normal)
            cell.n2=it[place+1].modele
            cell.b2.isHidden=false
        }else{
            cell.b2.isHidden=true
        }
        return cell
    }
    
    var it=PublicBeans.getModel()
  let deledate = UIApplication.shared.delegate as! AppDelegate
    @IBOutlet var tb: UITableView!
    
    @IBOutlet var tit: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        tb.bounces=false
        tb.separatorStyle = .none
        query()
        tit.text=SetLan.Setlan("Select_CAR_Model")
        print(it.count)
    }
    
    func query(){
        self.tb.reloadData()
    }

}
