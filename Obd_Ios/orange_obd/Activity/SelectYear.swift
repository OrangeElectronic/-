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
        var it=[model]()
    
    @IBOutlet var tit: UILabel!
    let deledate = UIApplication.shared.delegate as! AppDelegate
 let act=(UIApplication.shared.delegate as! AppDelegate).act!
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
    

    @IBOutlet var tb: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tb.separatorStyle = .none
        self.tb.bounces=false
        print(act.Selectmodel)
        print(act.Selectmake)
        query()
         print(it.count)
         tit.text=SetLan.Setlan("Select_Year")
    }
    func query(){
        if deledate.db != nil {
            let sql="select distinct Year from `Summary table` where model='\(act.Selectmodel)' and make='\(act.Selectmake)' and `OBD1` not in('NA') order by Year asc"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
                let item=model()
                let iid = sqlite3_column_text(statement,0)
                if iid != nil{
                    let iids = String(cString: iid!)
                    item.year=iids
                    print("name\(iids)")
                }
                self.it.append(item)
            }
            self.tb.reloadData()
        }
    }

}
