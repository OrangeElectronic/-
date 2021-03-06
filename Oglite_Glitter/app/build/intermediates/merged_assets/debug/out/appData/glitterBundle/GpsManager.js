"use strict";
class GpsCallBack {
    constructor() {
        /*
        * 定位未開啟
        * */
        this.notOpen = function () {
        }
        /*
        * 使用者拒絕定位
        * */
        this.denied = function () {
        }
        /*
       * 使用者同意定位
       * */
        this.grant = function () {
        }
    }

}

class GpsManager{
    constructor() {
        /*
        * 請求並查看定位權限
        * 返回值GpsCallBack
        * */
        this.requestGPSPermission=function (){
            switch (glitter.deviceType){
                case appearType.Ios:
                    window.webkit.messageHandlers.requestGPSPermission.postMessage('');
                    break
                case appearType.Android:
                    glitter.runJsInterFace("GpsManager_Status",{},function (response){
                        switch (response.result){
                            case 'notOpen':
                                glitter.gpsUtil.callback.notOpen()
                                break
                            case 'denied':
                                glitter.gpsUtil.callback.denied()
                                break
                            case 'grant':
                                glitter.gpsUtil.callback.grant()
                                break
                        }
                    })
                    break
                case appearType.Web:break
            }

        }
    }
}
