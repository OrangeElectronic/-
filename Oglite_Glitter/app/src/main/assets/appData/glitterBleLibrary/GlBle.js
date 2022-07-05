/*Read only*/
/*Please dont change*/
"use strict";

class BleUtil {
    constructor() {
        this.exNative=function (functionName,obj){
            if(obj===undefined){obj={}}
            glitter.runJsInterFace("Glitter_BLE_"+functionName,(obj.data===undefined) ? {}:obj.data,(obj.callback === undefined) ? function (data){
            console.log(functionName+"callbackResponse:\n"+JSON.stringify(data))
            }:obj.callback)
        }
        this.connecting=undefined
        //Start use ble
        this.start = function (obj) {
        this.exNative("Start",obj)
        }
        //StartScan
        this.startScan = function (obj) {
        this.exNative("StartScan",obj)
        }
        //StopScan
        this.stopScan = function (obj) {
        this.exNative("StopScan",obj)
        }
         /**
         * request -> data,txChannel,rxChannel
         */
        this.writeHex = function (obj) {
        this.exNative("WriteHex",obj)
        }
        /**
         * request -> data,txChannel,rxChannel
         */
        this.writeUtf = function (obj) {
        this.exNative("WriteUtf",obj)
        }
        /**
         * request -> data,txChannel,rxChannel
         */
        this.writeBytes = function (obj) {
        this.exNative("WriteBytes",obj)
        }
        //isOpen
        this.isOPen=function (obj) {
        this.exNative("IsOpen",obj)
        }
        /**
         * needPermission
         * If return true you need to request permission
         * */
        this.needPermission=function (obj) {
            if(glitter.deviceType===glitter.deviceTypeEnum.Ios){
                obj.callback({result:false})
                return
            }
            this.exNative("NeedPermission",obj)
        }
        /**
         * isDiscovering
         * You can use this function to check ble is on scanning or not
         * */
        this.isDiscovering=function (obj) {
        this.exNative("IsDiscovering",obj)
        }
        /**
         * request -> address,timeOut
         * */
        this.connect=function (obj){
            var data={
                data:obj.data,
                callback:function (response){
                    if(response.result){
                        glitter.share.bleUtil.connecting=obj.data.name
                    }
                    obj.callback(response)
                }
            }
        this.exNative("Connect",data)
        }
        /**
         * Disconnect
         * */
        this.disConnect=function (obj){
        this.exNative("DisConnect",obj)
        }
        /**
         * isPair
         * response -> result
         * */
        this.isConnect=function (obj){
        this.exNative("IsConnect",obj)
        }
    }
}
glitter.share.bleUtil=new BleUtil()


