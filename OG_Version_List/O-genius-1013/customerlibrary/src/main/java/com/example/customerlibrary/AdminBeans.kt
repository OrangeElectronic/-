package com.example.customerlibrary

import com.google.firebase.messaging.FirebaseMessaging
import com.jzsql.lib.mmySql.JzSqlHelper
import com.orange.jzchi.jzframework.JzActivity
import javax.security.auth.callback.Callback

class AdminBeans {
    companion object {
        interface result{
            fun result(a:Boolean)
        }
        fun setMessageInstance(admin:String,password:String,callback: result){
            this.admin=admin
            this.password=password
            FirebaseMessaging.getInstance().subscribeToTopic(admin).addOnCompleteListener {
                callback.result(true)
            }.addOnFailureListener {
                callback.result(false)
            }
        }
        var admin=""
        var password=""
    }

}