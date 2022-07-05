//
//  Favorite.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/14.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
import SQLite3
class Favorite: UITableViewCell {
    let act=(UIApplication.shared.delegate as! AppDelegate).act!
    let deledate = UIApplication.shared.delegate as! AppDelegate
    var FClas:MyFavorite!=nil
    var position=0
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    @IBOutlet var name: UILabel!
    var s19=""
    var make=""
     var model=""
     var year=""
    override func setSelected(_ selected: Bool, animated: Bool) {  super.setSelected(false, animated: false)
//query()
    }
 
    @IBAction func de(_ sender: Any) {
     FClas.modle.remove(at:position)
    FClas.tb.reloadData()
        ViewController.writeshare("\(FClas.modle.count)", "Favcount")
        for i in 0..<FClas.modle.count{
            ViewController.writeshare(FClas.modle[i], "fav\(i)")
        }
    }
    
    func query(){
        if deledate.db != nil {
            let sql="select  `Make`,`Model`,`Year` from `Summary table` where `OBD1`='\(s19)' limit 0,1"
            var statement:OpaquePointer? = nil
            if sqlite3_prepare(deledate.db,sql,-1,&statement,nil) != SQLITE_OK{
                let errmsg=String(cString:sqlite3_errmsg(deledate.db))
                print(errmsg)
            }
            while sqlite3_step(statement)==SQLITE_ROW{
              
                 make = String(cString:sqlite3_column_text(statement,0))
                 model = String(cString:sqlite3_column_text(statement,1))
                year = String(cString:sqlite3_column_text(statement,2))
        name.text="\(make)/\(model)/\(year)"
              
            }
         
            
        }
    }
}
