var lastRx = ''
//設定藍芽回調函式
glitter.runJsInterFace("Glitter_BLE_SetCallBack", {}, function (response) {
    switch (response.function) {
        case "needGPS":
            console.log("需要開啟定位來掃描藍芽")
            function needGps(){
                glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(148), function () {
                    glitter.share.bleUtil.startScan()
                })
            }
            needGps()
            break
        case "onConnectFalse":

            console.log("藍芽連線失敗")
            break
        case "onConnectSuccess":
            console.log("藍芽連線成功")

            function connectSuccess(){
                glitter.share.canSendData = false
                setTimeout(function () {
                    glitter.share.canSendData = true
                }, 2000)
                glitter.command.txmemory += (`${(new Date()).format("yyyy-MM-dd hh:mm:ss:SSS").replace(".0", "")}:BleConnect\n`)
                console.log("bleMessage_onConnectSuccess")
                glitter.runJsInterFace("JzBleMessage", {
                    text: 'onConnectSuccess'
                }, function () {

                })
                glitter.runJsInterFace("Glitter_BLE_StopScan", {}, function (response) {
                    console.log(response.result)
                })
            }
            connectSuccess()
            break
        case "onConnecting":
            console.log("藍芽連線中")
            break
        case "onDisconnect":
            glitter.bleList=[]
            console.log("藍芽斷線中")
            function disconnect(){
                glitter.command.txmemory += (`${(new Date()).format("yyyy-MM-dd hh:mm:ss:SSS").replace(".0", "")}:BleDisConnect\n`)
                console.log("bleMessage_onConnectFalse")
                glitter.runJsInterFace("JzBleMessage", {
                    text: 'Disconnect'
                }, function () {
                })
            }
            disconnect()
            break
        case "requestPermission":
            console.log("藍芽requestPermission")
            console.log("權限不足" + JSON.stringify(response.data))
            function needPermission(array){
                glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(148), function () {
                    glitter.requestPermission(array)
                })
            }
            needPermission(response.data)
            break
        /**
         * readHEX,readBytes,readUTF
         * */
        case "rx":

        function readRx(data) {
            if (
                (glitter.share.bleUtil.connecting) &&
                ((glitter.share.bleUtil.connecting === 'Calipers') || (glitter.share.bleUtil.connecting.indexOf("B-") !== -1))) {
                glitter.Tread_depth(data.readHEX)
                return
            }
            if (data.readHEX === "0A") {
                data.readHEX = ""
            }
            if (data.readHEX.indexOf('F5E1000301160A') !== -1) {
                data.readHEX = data.readHEX.replace('F5E1000301160A', '')
            }
            if (data.readHEX.indexOf('F5E2000300140A') !== -1) {
                data.readHEX = data.readHEX.replace('F5E2000300140A', '')
            }
            if (data.readHEX.indexOf('F5E0000300160A') !== -1) {
                data.readHEX = data.readHEX.replace('F5E0000300160A', '')
            }
            if (data.readHEX.indexOf('F5E1000302150A') !== -1) {
                data.readHEX = data.readHEX.replace('F5E1000302150A', '')
            }
            if (data.readHEX !== '') {
                glitter.command.txmemory += (`RX ${(new Date()).format("yyyy-MM-dd hh:mm:ss:S").replace(".0", "")}:${data.readHEX}\n`)
            }
            var len = glitter.command.txmemory.length
            if (len > 40000) {
                glitter.command.txmemory = glitter.command.txmemory.substring(len - 40000, len)
            }
            if (data.readHEX.indexOf("F5EE0000FF") !== -1) {
                glitter.share.lowBattery = 1
                setTimeout(function () {
                    glitter.share.lowBattery = 0
                }, 10 * 1000)
                glitter.openDiaLog('dialog/DIa_Battery_Low.html', 'DIa_Battery_Low', false, true, {}, function () {
                })
                glitter.share.setLowBattery()
            }
            console.log("bleMessage_rx:" + data.readHEX)
            if (data.readHEX.indexOf("F50E000300F80A") !== -1) {
                glitter.share.canGoing = true
                glitter.command.rx = glitter.command.rx.replace(/ F50E000300F80A /g, "")
            }
            if ((data.readHEX === "0AF700030000F5")) {
                if(!glitter.onProgram){
                    try {
                        glitter.readRxReceive()
                    } catch (e) {
                    }
                }else{}
            }

            if((data.readHEX.indexOf("F50E000300F80A") === -1) && (data.readHEX !== "0AF700030000F5") ){
                glitter.command.rx += data.readHEX
            }

            glitter.runJsInterFace("bleMessage_rx", {data: data.readHEX}, function (response) {
            })
        }

            readRx(response.data)
            break
        case "tx":

        function readTx(data) {
            if (data.readHEX !== '') {
                glitter.command.txmemory += (`TX ${(new Date()).format("yyyy-MM-dd hh:mm:ss:SSS").replace(".0", "")}:${data.readHEX}\n`)
            }
            var len = glitter.command.txmemory.length
            if (len > 40000) {
                glitter.command.txmemory = glitter.command.txmemory.substring(len - 40000, len)
            }
            console.log("bleMessage_tx:" + data.readHEX)
        }
            readTx(response.data)
            break
        /**
         * device:{name,address}
         * advertise:{readHEX,readBytes,readUTF}
         * 使用address去做藍芽連線
         * */
        case "scanBack":
            function scanBack(deviceList) {
            console.log(JSON.stringify(deviceList))
                deviceList.map(function (device){
                    device.name=device.name??"unkenown device"
                    if ((device.name.toLowerCase().indexOf("og_lite") !== -1) || (device.name.indexOf("Calipers") !== -1) || (device.name.indexOf("B-") !== -1)) {
                        if (glitter.bleList.filter(function (data) {
                            return data.name === device.name
                        }).length === 0) {
                            glitter.bleList = glitter.bleList.concat(device)
                        }
                    } else if (((device.name.toUpperCase().indexOf("ON FOTA RSL10") !== -1) || (device.name.toUpperCase().indexOf("CS8025V100") !== -1)) && (!glitter.onUpdateBle)) {
                        if (!onUpdate) {
                            onUpdate = true
                            var rout = (glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Ble%20Firmware/${glitter.publicBeans.onlineVersion.bleVersion}` :
                                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Ble%20Firmware/${glitter.publicBeans.onlineVersion.bleVersion}`
                            glitter.share.bleUtil.isConnect({
                                callback: function (response) {
                                    if (!response.result) {
                                        glitter.runJsInterFace("updateBle", {rout: rout}, function (result) {
                                            onUpdate = false
                                        })
                                    }
                                }
                            })
                        }
                    }
                })

        }
            scanBack(response.device)
            break
    }
})




var onUpdate = false

function readInt(array) {
    var value = 0;
    for (var i = 0; i < array.length; i++) {
        value = (value * 256) + array[i];
    }
    return value;
}

//迴圈判斷藍芽是否連線
setInterval(timer, 2000)
var diaopen = false

function timer() {
    console.log(`${glitter.canshowConnect} && ${!glitter.onUpdateBle} && ${!glitter.onProgram}`)
    glitter.share.bleUtil.needPermission({
        callback: function (response) {
            if (response.result) {
                if (diaopen) {
                    return
                }
                diaopen = true
                glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(148), function () {
                    glitter.share.bleUtil.stopScan()
                    diaopen = false
                    glitter.share.bleUtil.startScan()
                })
            } else {
                glitter.share.bleUtil.isOPen({
                    callback: function (response) {
                        if (!response.result) {
                            glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(532), function () {
                                glitter.share.bleUtil.stopScan()
                                diaopen = false
                            })
                        } else {
                            glitter.share.bleUtil.isConnect({
                                callback: function (response) {
                                    if (!response.result) {
                                        glitter.share.bleUtil.startScan()
                                        if (glitter.onProgram) {
                                            if(glitter.share.bleTest){
                                                console.log("tryReconnect" + lastConnectAddress)
                                                glitter.share.bleUtil.connect({
                                                    data: {
                                                        address: lastConnectAddress,
                                                        timeOut: 10
                                                    }, callback: function (response) {
                                                        if (response.result) {
                                                        }
                                                    }
                                                })
                                            }else{
                                                console.log("tryReconnect" + lastConnectAddress)
                                                glitter.share.bleUtil.connect({
                                                    data: {
                                                        address: lastConnectAddress,
                                                        timeOut: 10
                                                    }, callback: function (response) {
                                                        if (response.result) {
                                                        }
                                                    }
                                                })
                                            }

                                        } else {
                                            if (glitter.canshowConnect && (!glitter.onUpdateBle) && !glitter.onProgram) {
                                                glitter.openDiaLog('dialog/Dia_Scan_Ble.html', 'Dia_Scan_Ble', false, false, function () {
                                                })
                                            } else {
                                                if (pos !== undefined) {
                                                    if (!glitter.onUpdateBle) {
                                                        glitter.connectFunction(pos, true)
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (!glitter.onwaiting) {
                                            glitter.closeDiaLogWithTag("ConnectBle")
                                        }
                                        glitter.closeDiaLogWithTag("Dia_Scan_Ble")
                                    }
                                }
                            })
                        }
                    }
                })
            }
        }
    })
}

var canConnect = true
var pos = undefined
var lastConnectAddress = undefined
glitter.connectFunction = function connect(position, restart) {
    if (glitter.onUpdateBle) {
        console.log('onUpdate')
        return;
    }
    if (!canConnect) {
        console.log('!canConnect')
        return
    }
    canConnect = false
    pos = position
    glitter.command.cancel=true
    glitter.share.bleUtil.isConnect({
        callback: function (response) {
            try {
                if (response.result && (glitter.bleList[position].address)) {
                    canConnect=true
                    glitter.openDiaLog('dialog/Dia_Loading.html', 'ConnectBle', false, false, glitter.getLan(243))
                    glitter.share.bleUtil.stopScan()
                } else {
                    if (!restart) {
                    glitter.openDiaLog('dialog/Dia_Loading.html', 'ConnectBle', false, false, glitter.getLan(243))
                    }
                    glitter.onwaiting = true
                    setTimeout(function () {
                        setTimeout(()=>{canConnect=true},1000*10)
                        glitter.share.bleUtil.connect({
                            data: {
                                address: glitter.bleList[pos].address,
                                timeOut: 10
                            },
                            callback: function (response) {
                                canConnect=true
                                glitter.lastConnectAddress = glitter.bleList[pos].address
                                //判斷成功則關閉Dialog
                                function success() {
                                    lastConnectAddress = glitter.bleList[pos].address
                                    canConnect = true
                                    glitter.setPro("lastConnect", glitter.bleList[position].name)
                                    glitter.share.bleUtil.stopScan()
                                    glitter.closeDiaLogWithTag("ConnectBle")
                                    glitter.closeDiaLogWithTag("Dia_Scan_Ble")
                                    glitter.onwaiting = false
                                    if (!restart && glitter.canUpdate) {
                                        glitter.share.checkVersion()
                                    }
                                }
                                //判斷失敗則跳提醒
                                function error() {
                                    glitter.onwaiting = false
                                    canConnect = true
                                    glitter.share.bleUtil.disConnect()
                                    glitter.closeDiaLogWithTag("ConnectBle")
                                    if (glitter.canshowConnect && !glitter.onProgram) {
                                        glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(256))
                                    }
                                }
                                //delay
                                function delay(callback) {
                                    setTimeout(function () {
                                        callback()
                                    }, 500)
                                }

                                if (glitter.onProgram && response.result) {
                                    success()
                                    return
                                }
                                if (response.result) {
                                    console.log("tryBleCommand" + JSON.stringify(response))
                                    setTimeout(function () {
                                        if (!restart) {
                                            glitter.command.getBleVersion(function () {
                                                glitter.command.getState(function () {
                                                    if(glitter.DongleState===glitter.command.BootloaderState.Bootloader){
                                                        glitter.command.askVersion(function () {
                                                            delay(success)
                                                        })
                                                    }else{
                                                        glitter.command.goState(glitter.command.BootloaderState.Og_App,function (){
                                                            glitter.command.askVersion(function () {
                                                                delay(function () {
                                                                    glitter.command.readSN(function () {
                                                                        delay(function () {
                                                                            glitter.command.sleep(function () {
                                                                                glitter.command.getBootloaderVersion(function (result) {
                                                                                    success()
                                                                                })
                                                                            }, error)
                                                                        })
                                                                    }, error)
                                                                })
                                                            }, error)
                                                        })
                                                    }
                                                }, error)
                                            }, error)
                                        } else {
                                            success()
                                        }
                                    }, 3000)
                                } else {
                                    error()
                                }
                            }
                        })
                    }, 1000)
                }
            } catch (e) {
                alert(e)
            }
        }
    })
}
