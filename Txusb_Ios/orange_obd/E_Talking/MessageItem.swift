//
//  MessageItem.swift
//  txusb
//
//  Created by 王建智 on 2019/10/4.
//  Copyright © 2019 王建智. All rights reserved.
//

import Foundation
class MessageItem {
     var id=[String]();
     var admin=[String]();
     var message=[String]();
     var time=[String]();
     var head=[String]();
     var pick=[String]();
     var file=[String]();
     var reader=[String]();
     var button=false;
     var success=false;
    func clear(){
         id.removeAll()
         admin.removeAll()
         message.removeAll()
         time.removeAll()
         head.removeAll()
         pick.removeAll()
         file.removeAll()
         reader.removeAll()
    }
    func add(_ id:String,_ admin:String,_ file:String,_ message:String,_ time:String,_ head:String,_ pick:String)  {
        self.id.append(id)
        self.admin.append(admin)
        self.message.append(message)
        self.time.append(time)
        self.head.append(head)
        self.pick.append(pick)
        self.file.append(file)
//        self.reader.append(reader)
    }
    func addzero(_ id:String,_ admin:String,_ file:String,_ message:String,_ time:String,_ head:String,_ pick:String){
        self.id.insert(id, at: 0)
        self.admin.insert(admin, at: 0)
        self.message.insert(message, at: 0)
        self.time.insert(time, at: 0)
        self.head.insert(head, at: 0)
        self.pick.insert(pick, at: 0)
        self.file.insert(file, at: 0)
    }
}
