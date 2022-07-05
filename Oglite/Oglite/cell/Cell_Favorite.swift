//
//  Cell_Favorite.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2020/2/4.
//  Copyright © 2020 Jianzhi.wang. All rights reserved.
//

import UIKit
import JzOsSqlHelper
import JzOsFrameWork
public class Cell_Favorite: UICollectionViewCell {
    var index=0
    @IBOutlet weak var delbt: UIButton!
    @IBOutlet weak var tit: UILabel!
    var make=""
    var model=""
    var year=""
    var delfunction:(()->Void)! = nil
    public override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    @IBAction func gopage(_ sender: Any) {
        PublicBeans.Make=make
        PublicBeans.Model=model
        PublicBeans.Year=year
        PublicBeans.changeFunction()
    }
    
    @IBAction func delfavorite(_ sender: Any) {
        let sql=SqlHelper("favorite")
        sql.autoCreat()
        sql.exSql("delete  from favorite where make='\(make)' and model='\(model)' and year='\(year)'")
        delfunction()
    }
    public static func getFavorite()->[myfavorite]{
        var result=[myfavorite]()
        let sql=SqlHelper("favorite")
        sql.autoCreat()
        sql.exSql("CREATE TABLE  IF NOT EXISTS `favorite` ( id INTEGER PRIMARY KEY AUTOINCREMENT, make VARCHAR NOT NULL, model VARCHAR NOT NULL,year VARCHAR NOT NULL);")
        sql.query("select * from `favorite` order by id desc", {
            data in
            let fav=myfavorite()
            fav.make=data.getString(1)
            fav.model=data.getString(2)
            fav.year=data.getString(3)
            result.append(fav)
        }, {})
        sql.closeDB()
        NSLog("getfav\(result)")
        return result
    }
    
}
public class myfavorite{
    var make=""
    var model=""
    var year=""
}
