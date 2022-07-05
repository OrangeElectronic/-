//
//  MyFavorite.swift
//  orange_obd
//
//  Created by 王建智 on 2019/9/14.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit

class MyFavorite: UIViewController,UITableViewDataSource,UITableViewDelegate {
        let act=(UIApplication.shared.delegate as! AppDelegate).act!
    var modle=[String]()
    
    @IBOutlet var Menu: UIButton!
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
            return modle.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
         let cell=tableView.dequeueReusableCell(withIdentifier: "Favorite", for: indexPath) as! Favorite
        cell.name.text=modle[indexPath.row]
        cell.FClas=self
        cell.position=indexPath.row
        return cell
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.tb.separatorStyle  = .none
        self.tb.bounces=false
        getmodle()
        print(modle)
        self.tb.reloadData()
    }
    @IBOutlet var tb: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        Menu.setTitle(SetLan.Setlan("MENU"), for: .normal)
    }
    func addmodle(){
        ViewController.writeshare("\(modle.count)", "Favcount")
        for i in 0..<modle.count{
            ViewController.writeshare(modle[i], "fav\(i)")
        }
    }
    
    @IBAction func Gohome(_ sender: Any) {
        let a=peacedefine().HomePage
        self.act.ChangePage(to: a)
    }
    func getmodle(){
        let a=ViewController.getShare("Favcount")
        if(a != "nodata"){
            let count=Int(a)
            modle=[String]()
            for i in 0..<count!{
              let ap=ViewController.getShare("fav\(i)")
                if(!modle.contains(ap)){ modle.append(ap)}
            }
        }
    }

    @IBAction func addfunction(_ sender: Any) {
       let a=peacedefine().AddFavorite
        a.modle=modle
        act.ChangePage(to: a)
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        act.Selectmake=modle[indexPath.row].components(separatedBy: "/")[0]
        act.Selectmodel=modle[indexPath.row].components(separatedBy: "/")[1]
        act.Selectyear=modle[indexPath.row].components(separatedBy: "/")[2]
        let a=peacedefine().Relarm
        act.ChangePage(to: a)
    }
}
