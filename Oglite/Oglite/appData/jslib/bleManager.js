var lastRx=''
glitter.bleUtil.callback.rx=function(data){
    if(data.readHEX.indexOf("F5EE0000FF")!==-1){
        glitter.openDiaLog('dialog/DIa_Battery_Low.html','DIa_Battery_Low',false,true,{},function (){})
    }
console.log("bleMessage_rx:"+data.readHEX)
    if(glitter.command.rx!==data.readHEX){
        glitter.command.rx+=data.readHEX
    }
    if(data.readHEX==="0AF700030000F5"){
        try {
            glitter.readRxReceive()
        }catch (e){}
    }
}

glitter.bleUtil.callback.tx=function(data){
    console.log("bleMessage_tx:"+data.readHEX)
}

glitter.bleUtil.callback.onDisconnect=function (){
    console.log("bleMessage_onConnectFalse")
    // alert("onConnectFalse")
}
glitter.bleUtil.callback.onConnectSuccess=function (){
    console.log("bleMessage_onConnectSuccess")
    // alert("onConnectFalse")
}

glitter.bleUtil.callback.needGPS=function(){
    glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(148),function () {
        glitter.bleUtil.manager.startScan()
    })
}

glitter.bleUtil.callback.requestPermission=function(array){
    glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(148),function () {
        glitter.requestPermission(array)
    })
}

glitter.bleUtil.callback.scanBack=function(device,scanRecord,rssi){
    if((device.name.toLowerCase().indexOf("lite")!==-1)){
        if(glitter.bleList.filter(function (data){
            return data.name===device.name
        }).length===0){
            glitter.bleList=glitter.bleList.concat(device)
        }
    }
}
function readInt(array) {
    var value = 0;
    for (var i = 0; i < array.length; i++) {
        value = (value * 256) + array[i];
    }
    return value;
}


glitter.bleUtil.manager.start()


//迴圈判斷藍芽是否連線
setInterval(timer,2000)
var diaopen=false
function timer() {
if(!glitter.bleUtil.manager.gpsEnable()){
    if(diaopen){return}
    diaopen=true
    glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(148),function () {
        glitter.bleUtil.manager.stopScan()
        diaopen=false
        glitter.bleUtil.manager.startScan()
    })
    }
    glitter.bleUtil.manager.isOPen(function (op){
    if(!op){
        glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(532),function () {
            glitter.bleUtil.manager.stopScan()
            diaopen=false
        })
    }else{
        glitter.bleUtil.manager.isConnect(function (result){
            if(result){

            }else{
                glitter.bleUtil.manager.isDiscovering(function (result){
                    if(!result){  glitter.bleUtil.manager.startScan()}
                })
                if(glitter.canshowConnect){
                    glitter.openDiaLog('dialog/Dia_Scan_Ble.html','Dia_Scan_Ble',false,false,function(){ })
                }else{
                    if(pos!==undefined){
                        glitter.connectFunction(pos,true)
                    }
                }
            }
        })
    }
})
}
var canConnect=true
var pos=undefined
glitter.connectFunction=function connect(position,restart){
    if(!canConnect){return}
    canConnect=false
    pos=position
    glitter.bleUtil.manager.isConnect(function (res){
        try {
            if(res&&(glitter.bleList[position].address)){
                glitter.openDiaLog('dialog/Dia_Loading.html', 'Dia_Loading', false, false, glitter.getLan(243))
                glitter.bleUtil.manager.stopScan()
            }else{
                if(!restart){
                    glitter.openDiaLog('dialog/Dia_Loading.html', 'Dia_Loading', false, false, glitter.getLan(243))
                }
                glitter.bleUtil.manager.disConnect()
                glitter.onwaiting=true
                setTimeout(glitter.bleUtil.manager.connect(glitter.bleList[pos].address,15,function (result){
                    //判斷成功則關閉Dialog
                    function success(){
                        canConnect=true
                        glitter.setPro("lastConnect",glitter.bleList[position].name)
                        glitter.bleUtil.manager.stopScan()
                        glitter.closeDiaLogWithTag("Dia_Loading")
                        glitter.closeDiaLogWithTag("Dia_Scan_Ble")
                        glitter.onwaiting=false
                        if(!restart){
                            glitter.share.checkVersion()
                        }
                    }
                    //判斷失敗則跳提醒
                    function error(){
                        canConnect=true
                        glitter.bleUtil.manager.disConnect()
                        glitter.closeDiaLogWithTag("Dia_Loading")
                        glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(256))
                    }
                    //delay
                    function delay(callback){
                        setTimeout(function (){
                            callback()
                        },200)
                    }
                    if(result){
                        if(!restart){
                            glitter.command.getBleVersion(function (){
                                glitter.command.getState(function (){
                                    glitter.command.askVersion(function (){
                                        if(glitter.DongleState===glitter.command.BootloaderState.Obd_App){setTimeout(function (){glitter.command.goState(glitter.command.BootloaderState.Og_App)},50)}
                                        delay(function (){
                                            glitter.command.readSN(function (){
                                                delay(function (){
                                                    glitter.command.sleep(function (){
                                                        success()
                                                    },error)
                                                })
                                            },error)
                                        })
                                    },error)
                                },error)
                            },error)
                        }else{
                            success()
                        }
                    }else{
                        error()
                    }
                }),1000)
            }
        }catch (e){
            alert(e)
        }

    })
}
