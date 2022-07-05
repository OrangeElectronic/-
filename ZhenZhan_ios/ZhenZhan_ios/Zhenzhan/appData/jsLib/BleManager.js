glitter.share.bleCallBack.rx = function (data) {
    var hex = data.readHEX
    console.log("ble--rx--" + hex)
    var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
    glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:blue;font-size:20px">Rx:${hex}</p>
                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:blue">` + timeInMs + `</p>`
    // if(glitter.command.rx.length<20){
    //     glitter.command.rx+=hex
    // }else{
    //     glitter.command.rx=hex
    // }

    if (glitter.CarType === "Tread_depth") {
        glitter.Tread_depth(hex)
    }

    glitter.command.rx += hex
}
glitter.share.bleCallBack.tx = function (data) {
    var hex = data.readHEX
    var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
    glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">Tx:${hex}</p>
                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">` + timeInMs + `</p>`
    console.log("ble--tx--" + hex)
}
glitter.share.bleCallBack.onDisconnect = function () {
    var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
    glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:Red;font-size:20px">onConnectFalse</p>
                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:Red">` + timeInMs + `</p>`
    glitter.share.bleUtil.startScan()
    console.log("ble--onConnectFalse--")
    glitter.titleBarData.bleConnect = false
    glitter.titleBarData.update()
}
glitter.share.bleCallBack.onConnectSuccess = function () {
    var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
    glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:Red;font-size:20px">onConnectSuccess</p>
                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:Red">` + timeInMs + `</p>`
    glitter.share.bleUtil.stopScan()
    console.log("ble--onConnectSuccess--")
    glitter.titleBarData.bleConnect = true
    glitter.titleBarData.update()

}
glitter.share.bleCallBack.needGPS = function () {
    glitter.openDiaLog('dialog/Dia_Info_Problem.html', 'Dia_Info_Problem', false, true, '請打開GPS允許藍牙定位', function () {
        glitter.share.bleUtil.startScan()
    })
}

glitter.share.bleCallBack.requestPermission = function (array) {
    glitter.openDiaLog('dialog/Dia_Info_Problem.html', 'Dia_Info_Problem', false, true, '請允許定位權限來打開藍牙', function () {
        glitter.requestPermission(array)
    })
}

glitter.share.bleCallBack.scanBack = function (device, scanRecord) {
    if (glitter.bleList === undefined) {
        glitter.bleList = []
    }

    if (glitter.CarType === "Tread_depth" && device.name !== "Calipers") {
        //console.log("Tread_depth_false:"+device.name)
        return
    } else if (glitter.CarType !== "Tread_depth" && device.name.indexOf("JJ") === -1) {
        //console.log("JJ_false:"+device.name)
        return
    } else if (device.address === "60:C0:BF:0D:B0:CC") { //暫時放
        //console.log("have_problem_BLE")
        return;
    }
//if(device.name.indexOf("JJ")===-1){return}
    if (glitter.bleList.filter(function (data) {
        return data.name === device.name
    }).length === 0) {
        glitter.bleList = glitter.bleList.concat(device)
    }
}

glitter.share.bleUtil.start()


setInterval(timer, 5000)
var diaopen = false

function timer() {
    glitter.share.bleUtil.needOpenGps({
        callback: function (response) {
            if (response.result) {
                if (diaopen) {
                    return
                }
                diaopen = true
                glitter.openDiaLog('dialog/Dia_Info_Problem.html', 'Dia_Info_Problem', false, true, '請打開GPS允許藍牙定位', function () {
                    glitter.share.bleUtil.stopScan()
                    diaopen = false
                })
            } else {
                glitter.share.bleUtil.isOPen({
                    callback: function (response) {
                        if (!response.result) {
                            glitter.openDiaLog('dialog/Dia_Info_Problem.html', 'Dia_Info_Problem', false, true, '請開啟藍牙', function () {
                                glitter.share.bleUtil.stopScan()
                                diaopen = false
                            })
                        }
                    }
                })
            }
        }
    })
}


