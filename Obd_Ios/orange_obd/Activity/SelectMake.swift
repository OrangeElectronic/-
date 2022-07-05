//
//  SelectMake.swift
//  orange_obd
//
//  Created by 王建智 on 2019/7/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
class SelectMake: UIViewController,UITableViewDataSource,UITableViewDelegate{
    var it=[model]()
    
    @IBOutlet var tit: UILabel!
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    let deledate = UIApplication.shared.delegate as! AppDelegate
    @IBOutlet var tb: UITableView!
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(it.count%3==0){ return it.count/3}else{ return (it.count/3)+1}
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: "Make", for: indexPath) as! Make
        let place=indexPath.row*3
        print("image==\(it[place].image)")
        cell.act=act
        cell.b1.setBackgroundImage(UIImage(named: it[place].image), for: .normal)
        cell.name1=it[place].make
    if(place+1<it.count){cell.b2.setBackgroundImage(UIImage(named: it[place+1].image), for: .normal)
            cell.b2.isHidden=false
            cell.name2=it[place+1].make
        }else{cell.b2.isHidden=true}
        if(place+2<it.count){
            cell.b3.setBackgroundImage(UIImage(named: it[place+2].image), for: .normal)
            cell.b3.isHidden=false
            cell.name3=it[place+2].make
        }else{
            cell.b3.isHidden=true
        }
        return cell
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tb.separatorStyle  = .none
        self.tb.bounces=false
        tit.text=SetLan.Setlan("Select_CAR_Make")
query()
    }

    func query(){
        if deledate.db != nil {
            let sql="select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA') and `OBD1` not in('NA') order by `Make` asc"
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
                    item.make=iids
                    print("name\(iids)")
                }
                if cname != nil{
                    let cnames = String(cString: cname!)
                    item.image=cnames
                    print("image\(cnames)")
                }
                self.it.append(item)
            }
            self.tb.reloadData()
            
        }
    }
}
