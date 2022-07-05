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
   let act=(UIApplication.shared.delegate as! AppDelegate).act!
    
    @IBOutlet var tit: UILabel!
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
    
  var it=[model]()
  let deledate = UIApplication.shared.delegate as! AppDelegate
    @IBOutlet var tb: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tb.bounces=false
        tb.separatorStyle = .none
        print("select make\(act.Selectmake)")
        query()
        print(it.count)
         tit.text=SetLan.Setlan("Select_CAR_Model")
    }
    
    func query(){
        if deledate.db != nil {
            let sql="select distinct model from `Summary table` where make='\(act.Selectmake)' and `OBD1` not in('NA') order by model asc"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let item=model()
                let iid = sqlite3_column_text(statement,0)
                let cname = sqlite3_column_text(statement,1)
                if iid != nil{
                    let iids = String(cString: iid!)
                    item.modele=iids
                    print("name\(iids)")
                }
                self.it.append(item)
            }
            self.tb.reloadData()
        }
    }

}
