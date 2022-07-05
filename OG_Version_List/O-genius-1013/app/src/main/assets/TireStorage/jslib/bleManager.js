"use strict";
function Clock() {
    return {
        start: new Date(),
        stop: function () {
            return parseInt((new Date()) - (this.start))
        },
        zeroing: function () {
            this.start = new Date()
        }
    }
}
var clock = Clock()
glitter.share.bleCallBack.rx=function(data){
    console.log("bleMessage_rx:"+data.readHEX)
    glitter.Tread_depth(data.readHEX)
}
glitter.share.bleCallBack.tx=function(data){
    console.log("bleMessage_tx:"+data.readHEX)
}

glitter.share.bleCallBack.onDisconnect=function (){
    console.log("bleMessage_onConnectFalse")
    // alert("onConnectFalse")
}
glitter.share.bleCallBack.onConnectSuccess=function (){
    console.log("bleMessage_onConnectSuccess")
    // alert("onConnectFalse")
}

glitter.share.bleCallBack.needGPS=function(){
    // glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, '請打開GPS允許藍牙定位',function () {
    //     glitter.share.bleUtil.startScan()
    // })
}

glitter.share.bleCallBack.requestPermission=function(array){
    // glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, '請允許定位權限來打開藍牙',function () {
    //     glitter.requestPermission(array)
    // })
}
var canConnect=true
glitter.share.bleCallBack.scanBack=function(device,scanRecord,rssi){
    if(glitter.bleList===undefined){
        glitter.bleList=[]
    }
    if(device.name==='Calipers'){
        if(glitter.bleList.filter(function (data){
            return data.name===device.name
        }).length===0){
            glitter.bleList=glitter.bleList.concat(device)
        }
        if(!canConnect){
            return
        }
        glitter.share.bleUtil.isConnect({
            callback:function (result){
                if(!result.result){
                    canConnect=false
                    glitter.share.bleUtil.connect({
                        data: {
                            address: device.address,
                            timeOut: 10
                        }, callback: function (response) {
                            if(response.result){
                                setTimeout(function (){
                                    canConnect=true
                                    glitter.share.bleUtil.writeHex({
                                        data: {
                                            data: "040000",
                                            rxChannel: "0000FF00-0000-1000-8000-00805F9B34FB",
                                            txChannel: "0000FF00-0000-1000-8000-00805F9B34FB"}})
                                },3000)
                            }
                        }
                    })
                }
            }
        })

    }

}
function readInt(array) {
    var value = 0;
    for (var i = 0; i < array.length; i++) {
        value = (value * 256) + array[i];
    }
    return value;
}


glitter.share.bleUtil.start()

//迴圈判斷藍芽是否連線
setInterval(timer,3000)
var diaopen=false
function timer() {
    console.log("loop")
    glitter.share.bleUtil.isConnect({
        callback:function (result){
            if(!result.result){
                glitter.share.bleUtil.startScan()
                console.log("notConnect")
            }
        }
    })

}
glitter.share.bleUtil.disConnect({})