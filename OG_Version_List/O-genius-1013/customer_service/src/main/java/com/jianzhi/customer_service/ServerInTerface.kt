package com.jianzhi.customer_service

import android.net.Uri
import com.jianzhi.customer_service.message


interface ServerInTerface{
    fun getLanguageVersion(callback:vertifycallback)
    /*取得客服訊息*/
    fun getMessage(message:ArrayList<message>, callback:vertifycallback)
    /*傳送客服訊息*/
    fun sendMessage(message:String,callback:vertifycallback)
    /*取得最新客服訊息*/
    fun getTopMessage(message:ArrayList<message>,callback:vertifycallback)
    /*上傳圖片*/
    fun uploadImage(uri: Uri, callback:vertifycallback)
    interface vertifycallback{
        fun success()
        fun internetError()
        fun vertifyFalse()
    }
}

