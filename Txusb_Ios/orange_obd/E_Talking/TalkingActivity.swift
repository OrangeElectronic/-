//
//  TalkingActivity.swift
//  txusb
//
//  Created by 王建智 on 2019/10/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import UIKit
 import Firebase
class TalkingActivity: ImagePicker, UITableViewDataSource,UITableViewDelegate{
    var timer:Timer?
    var ImageUrl="nodata"
    var talkingwho="orangetpms"
    @IBOutlet var Nointerlabel: UILabel!
    @IBOutlet var ShowImage: UIImageView!
    @IBOutlet var ShowIngImage: UIView!
    @IBOutlet var sender: UITextField!
    @IBOutlet var noInternet: UIView!
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        print("placr\(indexPath.row)")
        if(indexPath.row==it.id.count-1&&it.button==false){
            self.Getmai(it.id[indexPath.row])
        }
        if(it.admin[indexPath.row]==act.etalk.admin){
             let cell=tableView.dequeueReusableCell(withIdentifier: "TalkingAdapter", for: indexPath) as! TalkingAdapter
            cell.position=indexPath.row
            cell.it=self.it
            cell.time.text=it.time[indexPath.row].compareCurrentTime()
                          cell.reply.text=it.message[indexPath.row]
                          if(it.file[indexPath.row] != "nodata"){
                               cell.showim.isHidden=false
                                              cell.showim.rectimage(it.file[indexPath.row])
                          }else{
                              cell.showim.isHidden=true
                          }
            cell.contentView.transform = CGAffineTransform(rotationAngle: .pi)
                return cell
        }else{
             let cell=tableView.dequeueReusableCell(withIdentifier: "TalkingAdapterYou", for: indexPath) as! TalkingAdapterYou
            cell.position=indexPath.row
            cell.it=self.it
            cell.Time.text=it.time[indexPath.row].compareCurrentTime()
                                    cell.reply.text=it.message[indexPath.row]
                                    if(it.file[indexPath.row] != "nodata"){
                                         cell.picture.isHidden=false
                                                        cell.picture.rectimage(it.file[indexPath.row])
                                    }else{
                                        cell.picture.isHidden=true
                                    }
            cell.contentView.transform = CGAffineTransform(rotationAngle: .pi)
                return cell
        }
    }
    override func viewWillAppear(_ animated: Bool) {
         self.timer = Timer.scheduledTimer(timeInterval: 3, target: self, selector: #selector(UpdateMessage), userInfo: nil, repeats: true)
            }
    override func viewWillDisappear(_ animated: Bool) {
        if(timer != nil){
            timer?.invalidate()
        }
    }
 var it=MessageItem()
    var refresh=true
    @IBOutlet var tb: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        Nointerlabel.text=SetLan.Setlan("nointer")
        sender.placeholder=SetLan.Setlan("PleaseMessage")
        act.tit.text=SetLan.Setlan("Online_customer_service")
        tb.separatorStyle = .none
        tb.bounces=false
        tb.transform = CGAffineTransform(rotationAngle: .pi)
        noInternet.isHidden=true
        Getmai("0")
        }
    @ objc func UpdateMessage(){
        if(!refresh){return}
        refresh=false
        if(it.id.count==0){
            refresh=true
            Getmai("0")
            return
        }
        
        DispatchQueue.global().async {
            let originsize=self.it.id.count
            self.act.etalk.GetNewMail(self.it.id[0],self.it,self.talkingwho)
            DispatchQueue.main.async {
                if(originsize != self.it.id.count){self.tb.reloadData()}
                self.refresh=true
            }
        }
    }
    func Getmai(_ take:String){
        if(!refresh){return}
        refresh=false
        act.DataLoading()
        DispatchQueue.global().async {
            self.act.etalk.Getmail(take,self.it,self.talkingwho)
            DispatchQueue.main.async {
                self.act.LoadingSuccess()
                self.refresh=true
                if(self.it.success){
                    self.tb.reloadData()
                    self.noInternet.isHidden=true
                }else{
                    print("失敗")
                     self.noInternet.isHidden=false
                }
            }
        }
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return it.admin.count
     }
    
    func SendMail(){
        let a=sender.text
        self.act.DataLoading()
        DispatchQueue.global().async {
            let response=self.act.etalk.Sendmail(self.talkingwho,self.ImageUrl, a!)
            DispatchQueue.main.async {
                self.ShowIngImage.isHidden=true
                    self.selInmage=nil
                self.ImageUrl="nodata"
                self.act.LoadingSuccess()
                if(response){
                    self.act.view.showToast(text: SetLan.Setlan("sendsuccess"))
                    self.sender.text=""
                }else{
                    self.act.view.showToast(text: SetLan.Setlan("sendfalse"))
                }
            }
            
        }
    }

    @IBAction func GetAlburm(_ sender: Any) {
        fromAlbum()
    }
    
    override func ImageReback(){
        ShowIngImage.isHidden=false
        ShowImage.image=selInmage
    }

    @IBAction func CancelImage(_ sender: Any) {
        ShowIngImage.isHidden=true
        selInmage=nil
        ImageUrl="nodata"
    }
    
    @IBAction func Tosend(_ sender: Any) {
        let a=self.sender.text
    if(a?.count==0){
        act.view.showToast(text: SetLan.Setlan("notempty"))
                return}
        self.act.LoadingSuccess()
        if(self.selInmage==nil){
            SendMail()
        }else{
            SendMailWithPicture()
        }
    }
    func SendMailWithPicture(){
        self.act.DataLoading()
        let currentTime = Date().timeIntervalSince1970
             let storageRef = Storage.storage().reference().child(act.etalk.admin).child("\(currentTime).jpg")
        if let uploadData = selInmage!.jpegData(compressionQuality: 1) {
                 // 這行就是 FirebaseStorage 關鍵的存取方法。

                 storageRef.putData(uploadData, metadata: nil, completion: { (data, error) in

                     if error != nil {

                         // 若有接收到錯誤，我們就直接印在 Console 就好，在這邊就不另外做處理。
                         print("Error: \(error!.localizedDescription)")
                         return
                     }
                        storageRef.downloadURL(completion: { (url, error) in
                           self.act.LoadingSuccess()
                        guard let downloadURL = url else {
                            self.act.view.showToast(text: SetLan.Setlan("sendfalse"))
                           return
                        }
                            self.ImageUrl=downloadURL.absoluteString
                            self.SendMail()
                        print(downloadURL)
                     })
                 })

             }
    }
}
