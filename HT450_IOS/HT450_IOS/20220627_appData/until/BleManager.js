//var clock = glitter.getClock()
//var disconnectTimer = undefined
glitter.disconnectTimer = undefined
glitter.dfu_timer=undefined
glitter.runJsInterFace("Glitter_BLE_SetCallBack",{},function (response){
    switch (response.function){
        case "needGPS":
            //console.log("需要開啟定位來掃描藍芽")
            glitter.openDiaLog('dialog/Dia_Info_Problem.html', 'Dia_Info_Problem', false, true, {title:'請打開GPS允許藍牙定位'},
             function () {
                //glitter.bleStartScan()
            })
            break

        case "onConnectFalse":
            //glitter.bleStartScan()
            console.log("藍芽連線失敗")
            break
        case "onConnectSuccess":
            console.log("藍芽連線成功")
            glitter.runJsInterFace("Glitter_BLE_StopScan", {}, function (response) {
                console.log(response.result)
            })
            glitter.runJsInterFace("Glitter_BLE_SetNotify", {
                rxChannel:"8D81"
            }, function (response) {
                console.log(response.result)
            })
            function connectSuccess(){
                var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
                glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:Red;font-size:20px">onConnectSuccess</p>
                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:Red">` + timeInMs + `</p>`

                glitter.closeDiaLogWithTag("Dia_Scan_Ble")
                glitter.closeDiaLogWithTag("Dia_Info_Bt_False")

                if(glitter.page_show_truck_ble){
                    glitter.setHome('page/Page_Home_Sreen.html','Page_Home_Sreen',{})
                }else if(glitter.ble_DFU){
                    glitter.closeDiaLogWithTag("Dia_Info_Progress_ble")
                    glitter.openDiaLog('dialog/Dia_Info_Progress.html', 'Dia_Info_Progress_ble', false, false,{name:"燒錄中"})
                    glitter.runJsInterFace("DFU", {start_DFU:"true"}, function (response) {

                    })

                    // glitter.runJsInterFace("DFU_Progress", {start_DFU:"true"}, function (response) {
                    //     if(response.success==="success"){
                    //         glitter.closeDiaLogWithTag("Dia_Info_ble_Progress")
                    //     }else if(response.error!==undefined){
                    //         glitter.closeDiaLogWithTag("Dia_Info_ble_Progress")
                    //     }else{
                    //         glitter.deflationTime(response.percent)
                    //     }
                    // })
                }else{
                    glitter.startHomeCommand()
                }

                console.log("ble--onConnectSuccess--")
                glitter.titleBarData.bleConnect = true
                glitter.titleBarData.update()
            }
            if(glitter.disconnectTimer){
                clearInterval(glitter.disconnectTimer)
                glitter.disconnectTimer=undefined
            }else{
                connectSuccess()
            }

            break

        case "onConnecting":
            console.log("藍芽連線中")
            break
        case "onDisconnect":
            //glitter.bleStartScan()
            //console.log("藍芽斷線")

            function disconnect(){
                var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
                glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:Red;font-size:20px">onConnectFalse</p>
                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:Red">` + timeInMs + `</p>`

                //glitter.bleStartScan()
                //主畫面
                if(glitter.page_home_sreen_ble){
                    //glitter.bleAddress=""
                }
                //選擇裝置 主畫面
                if(glitter.bleAddress!=="" && (glitter.page_show_truck_ble || glitter.page_home_sreen_ble)){
                    //glitter.bleConnectDailog()
                }

                glitter.bleList=[]

                console.log("ble--onDisconnect--")
                glitter.titleBarData.bleConnect = false
                glitter.titleBarData.update()

                //在logo隱私權畫面、DTC更新時，不會出現選擇藍芽視窗
                if(!glitter.page_logo_show_ble && !glitter.ble_DFU){
                    console.log("dfu_timer:"+"DiaLog"+","+glitter.ble_DFU)

                    glitter.openDiaLog('dialog/Dia_Scan_Ble.html','Dia_Scan_Ble',false,false,function(){

                    })
                }
            }

            if (glitter.dfu_timer) {
                console.log("dfu_timer:true")

                function error_Dialog() {

                    if(glitter.ble_DFU_success){
                        //glitter.openDiaLog('dialog/Dia_Info_Success.html', 'Dia_Info_Success', false, true)
                    }else{
                        glitter.openDiaLog('dialog/Dia_Info_Fail.html', 'Dia_Info_Fail', false, true)
                    }
                }

                //正常模式掃描連線藍芽
                glitter.bleStartScan(function (name) {

                    var clock=glitter.getClock()
                    var timer = setInterval(function () {
                        if (glitter.bleList.filter(function (data) {
                            return data.name !== "DfuTarg"
                        }).length > 0){
                            //console.log("dfu_timer_end:3s")
                            if (clock.stop() > 6*1000) {
                                function reConnect() {
                                    glitter.bleAddress = glitter.main_bleAddress
                                    glitter.ble_DFU = false
                                    glitter.dfu_timer = undefined
                                    glitter.bleConnectDailog(function () {
                                        error_Dialog()
                                    })
                                }
                                reConnect()
                                clearInterval(timer)
                            }
                        }
                    },10)

                    //setTimeout(reConnect,5000)
                })
                //setTimeout(error_Dialog,3000)
                //setTimeout(glitter.bleConnectDailog,3000)
            }
            if(glitter.ble_DFU && glitter.dfu_timer===undefined){
                glitter.ble_DFU_success=false

                //DFU模式掃描連線藍芽
                glitter.bleStartScan(function (name) {
                    console.log("bleStartScan:"+name)

                    //setTimeout(glitter.bleConnectDailog,3000)

                    var clock=glitter.getClock()
                    glitter.dfu_timer = setInterval(function () {
                        //if (clock.stop() > 5*1000) {
                        if (glitter.bleList.filter(function (data) {
                            return data.name === "DfuTarg"
                        }).length > 0) {
                            //console.log("dfu_timer:3s")
                            if (clock.stop() > 3*1000) {
                                function connect(){
                                    glitter.main_bleAddress = glitter.bleAddress
                                    glitter.bleAddress = "00:1C:97:FF:FF:FF"
                                    glitter.bleConnectDailog()
                                    clearInterval(glitter.dfu_timer)
                                }
                                connect()
                                //setTimeout(connect,3000)
                            }
                        }
                        // glitter.dfu_timer=undefined
                        //}
                    }, 10)
                })
            }else{
                if(glitter.disconnectTimer){clearInterval(glitter.disconnectTimer);}
                var clock=glitter.getClock()
                glitter.disconnectTimer = setInterval(function () {
                    if (clock.stop() > 5*1000) {
                        disconnect()
                        clearInterval(glitter.disconnectTimer)
                        glitter.disconnectTimer=undefined
                    }
                },10)
            }

            break

        case "requestPermission":
            console.log("權限不足"+JSON.stringify(response.data))
            glitter.runJsInterFace("PerMission_Request",{
                permission:response.data
            },function (response){
                console.log("請求結果"+response.result)
            })
            break
        /**
         * readHEX,readBytes,readUTF
         * */
        case "rx":
            //console.log("收到藍芽資料"+response.data.readHEX)
            var hex = response.data.readHEX
            console.log("ble--rx--" + hex)
            var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
            // glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:blue;font-size:20px">Rx:${hex}</p>
            //                  <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:blue">` + timeInMs + `</p>`
            glitter.uploadData.command_memory += `RX:${hex}` +" ,"+timeInMs+"\n"

            // if(glitter.command.rx.length<20){
            //     glitter.command.rx+=hex
            // }else{
            //     glitter.command.rx=hex
            // }
            //**
            glitter.share.home.commadMessage("RX:"+hex)
            //glitter.share.home.commadMessage("RX:"+"53EC2F2F2F40EC01D30A")
            //glitter.share.home.commadMessage("RX:"+"53ECC1C2C24003003E0A")
            //glitter.share.home.commadMessage("RX:"+"53E30462626464FFFF0A")
            glitter.tx_Command_Memory=glitter.tx_Command_Memory+"rx："+hex+"\n"

            if (glitter.CarType === "Tread_depth") {
                glitter.Tread_depth(hex)
            }

            glitter.command.rx += hex
            //glitter.command.rx = hex
            // if(glitter.command.rx.length<20){
            //     glitter.command.rx+=hex
            // }else{
            //     glitter.command.rx=hex
            // }
            break

        case "tx":
            //console.log("傳送藍芽資料"+response.data.readHEX)
            var hex = response.data.readHEX
            var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss:S");
            // glitter.command.memory += `<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">Tx:${hex}</p>
            //                  <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">` + timeInMs + `</p>`
            glitter.uploadData.command_memory += `TX:${hex}` +" ,"+timeInMs+"\n"
            console.log("ble--tx--" + hex)

            break
        /**
         * deviceList:[device,device.....]
         * device:{name,address,readHEX,readBytes,readUTF,rssi}
         * 回條每秒所偵測到的所有藍芽，使用address去做藍芽連線
         * */
        case "scanBack":
           // console.log(JSON.stringify(response))
            console.log("scanBack:true")
            response.device.map(function (response){
                console.log("device:"+response.name)
                //console.log("device:"+response.name+","+response.readHEX)
                if (glitter.bleList === undefined) {
                    glitter.bleList = []
                }
                //response.name.indexOf("HT470A") === -1 &&
                if (response.name.indexOf("HT430") === -1 && response.name.indexOf("HT471A") === -1
                    && (response.name.indexOf("DfuTarg") === -1)) {
                    //console.log("JJ_false:"+device.name)
                    //console.log("response_rssi:"+response.rssi)

                    // if(glitter.bleAddress!==undefined){
                    //     if(glitter.bleAddress===response.address){
                    //         console.log()
                    //         glitter.bleRssi(response.rssi)
                    //     }
                    // }
                    return
                } else if (response.address === "60:C0:BF:0D:B0:CC") { //暫時放
                    //console.log("have_problem_BLE")
                    return;
                }
                //預防不在DFU模式，而掃到DfuTarg
                if(response.name.indexOf("DfuTarg") !== -1 && !glitter.ble_DFU){
                    return;
                }
//if(device.name.indexOf("JJ")===-1){return}

                if (glitter.bleList.filter(function (data) {
                    return data.name === response.name
                }).length === 0) {
                    glitter.bleList = glitter.bleList.concat(response)
                }
            })

            break
    }
})

glitter.runJsInterFace("Glitter_BLE_NeedPermission", {}, function (response) {
    if (response.result) {
        //alert("權限請求失敗")
        console.log("權限請求失敗")
    } else {
        //alert("權限請求成功")
        console.log("權限請求成功")
    }
})

glitter.bleStartScan = function(callback){
    if(glitter.page_logo_show_ble){
        return;
    }
    //console.log("bleStartScan:"+glitter.getLanguage("17")+","+glitter.getLanguage("25"))
    //選擇裝置 主畫面
    // if(glitter.page_show_truck_ble || glitter.page_home_sreen_ble){
    //
    // }

    glitter.runJsInterFace("Glitter_BLE_IsConnect", {}, function (response) {
        if (!response.result) {
            glitter.runJsInterFace("Glitter_BLE_StartScan", {}, function (response) {
                console.log(response.result)
                callback(response.name)
            })
        }
    })
}

glitter.bleRecord = function (position) {
    glitter.bleAddress = glitter.bleList[position].address
    glitter.uploadData.ble_serial = glitter.bleList[position].name
    if(glitter.uploadData.ble_serial.indexOf("HT471A")!==-1){
        glitter.app = "HT471A"
    }else if(glitter.uploadData.ble_serial.indexOf("DfuTarg") !== -1){
        console.log("DfuTarg:true")
        //glitter.ble_DFU = true
    } else{
        glitter.app = "HT430"
    }
}
glitter.bleConnectDailog =function (callback){
    // && !glitter.ble_DFU
    if(glitter.disconnectTimer){
        return
    }
    console.log("bleConnectDailog:true")
    glitter.closeDiaLogWithTag("Dia_Progress_Spinner") //暫時放置
    glitter.closeDiaLogWithTag("Dia_Scan_Ble")
    glitter.closeDiaLogWithTag("Dia_Info_Bt_False")
    //glitter.closeDiaLogWithTag("Dia_Info_Progress_ble")

    //連線中
    if(!glitter.ble_DFU){
        glitter.closeDiaLogWithTag("Dia_Info_Progress_ble")
        glitter.openDiaLog('dialog/Dia_Progress_Spinner.html', 'connect_Dia_Progress_Spinner', false, false,{name:glitter.getLanguage("12")})
    }

    glitter.runJsInterFace("Glitter_BLE_Connect", {
        address: glitter.bleAddress,
        //15秒嘗試連線
        timeOut:10
    }, function (response) {
        console.log("ble_connect:"+response.result)
        if(response.result){
            glitter.closeDiaLog('connect_Dia_Progress_Spinner')
            glitter.setSystem.device_name = glitter.bleList.filter(function (data) {
                //console.log("device_name:"+data.address)
                return data.address === glitter.bleAddress
            })[0].name

            glitter.getPro("uid",function (result) {
                console.log("uid:"+result.data)
                if(undefined===result.data){
                    var timeInMs = new Date().format("yyyy-MM-dd hh:mm:ss");
                    glitter.uploadData.uid = glitter.setSystem.device_name.replace("HT430BLE","") +
                        timeInMs.replace(/ /g,"").replace(/-/g,"").replace(/:/g,"")
                    glitter.setPro("uid",glitter.uploadData.uid)
                }else{
                    glitter.uploadData.uid=result.data
                }
            })

            //console.log("device_name:"+glitter.setSystem.device_name)
            // //if(glitter.titleBarData.text === "選擇設備"){
            //**
            // glitter.setHome('page/Page_Home_Sreen.html','Page_Home_Sreen',{})
            // //}
            // //if(glitter.titleBarData.text === "主畫面"){
            //
            // //}
        }else{
            glitter.closeDiaLog('connect_Dia_Progress_Spinner')
            glitter.showToast("連線失敗")
            //homeBleConnectDailog()
            glitter.openDiaLog('dialog/Dia_Scan_Ble.html', 'Dia_Scan_Ble', false, false, function () {

            })

        }

        callback()
    })
}

function homeBleConnectDailog(){
    //主畫面
    if(glitter.page_home_sreen_ble) {
        glitter.openDiaLog('dialog/Dia_Info_Bt.html', 'Dia_Info_Bt_False', false, false, {
            title: '連線失敗，是否繼續連線',
            cancel: function () {
                glitter.closeDiaLog("Dia_Info_Bt_False")
                glitter.bleAddress = ""
                glitter.goBack = glitter.copyGoBack.clone()
                glitter.setHome('page/Page_Show_Truck.html', 'Page_Show_Truck', {})
                // glitter.runJsInterFace("Glitter_BLE_DisConnect", {},
                //     function (response) {
                //         console.log(response.result)
                //     })
            },
            ok: function () {
                glitter.closeDiaLog("Dia_Info_Bt_False")
                glitter.bleConnectDailog()
                //glitter.bleAddress = ""
            }
        })
    }
}

setInterval(timer, 5000)
setInterval(connectTimer, 5*1000) //3000 5000
var diaopen = false

function timer() {
    if(glitter.demo){
        return
    }
    glitter.runJsInterFace("Glitter_BLE_IsOpen", {}, function (response) {

        if(!response.result){
            console.log("BLE_IsOpen:"+response.result)
            glitter.openDiaLog('dialog/Dia_Info_Problem.html', 'Dia_Info_Problem', false, true, {title:'請開啟藍牙'}, function () {

                glitter.runJsInterFace("Glitter_BLE_StopScan", {}, function (response) {
                    console.log(response.result)
                })
                diaopen = false
            })
        }else{
            glitter.bleStartScan()
        }
    })
    glitter.runJsInterFace("Glitter_BLE_NeedPermission", {}, function (response) {
        if (response.result) {

            // alert("權限請求失敗")
        } else {
            // alert("權限請求成功")
        }
    })
    glitter.runJsInterFace("PerMission_Request",{
        permission:[
            "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION","android.permission.QUERY_ALL_PACKAGES",
            "android.permission.MANAGE_EXTERNAL_STORAGE"]
    },function (response){
        if(response.result){
            //請求權限成功
            //.....your code
        }else{
            //請求權限遭拒絕
            //.....your code
        }
    })
}

glitter.bleAddress=""
var cantConnect=false
function connectTimer(){
    if(glitter.demo){
        return
    }
    if(cantConnect){
        return;
    }
    if(glitter.page_logo_show_ble){
        return;
    }
    //選擇裝置 主畫面
    if(glitter.page_show_truck_ble || glitter.page_home_sreen_ble) {
    }

    glitter.runJsInterFace("Glitter_BLE_IsConnect", {}, function (response) {
        //console.log(response.result)
        if (!response.result) {
            glitter.runJsInterFace("Glitter_BLE_IsDiscovering", {}, function (response) {
                //console.log(response.result)
                if (response.result) {
                    if (glitter.bleList !== undefined) {
                        //glitter.bleList[0].address!==glitter.bleAddress
                        var ble_Address_array = function () {
                            return glitter.bleList.map(function (value, index, array) {
                                value.address === glitter.bleAddress
                                //console.log("ble_Address_array:"+value.address+","+glitter.bleAddress)
                            })
                        }
                        console.log("ble_Address_array:" + ble_Address_array.length + "," + (glitter.bleList.length))
                        //if(glitter.bleList.length > 0 && ble_Address_array.length===0){
                        if (glitter.bleList.length === 1 && ble_Address_array.length === 0) {

                            glitter.bleRecord(0)
                            // glitter.bleAddress = glitter.bleList[0].address
                            // glitter.uploadData.ble_serial = glitter.bleList[0].name
                            //
                            // if (glitter.uploadData.ble_serial.indexOf("HT471A") !== -1) {
                            //     glitter.app = "HT471A"
                            // }else if(glitter.uploadData.ble_serial.indexOf("DfuTarg") !== -1){
                            //     console.log("DfuTarg:true")
                            //     glitter.ble_DFU = true
                            // } else {
                            //     console.log("DfuTarg:false")
                            //     glitter.app = "HT430"
                            //     //glitter.app = "HT471A"
                            // }

                            setTimeout(function () {
                                cantConnect = false;
                            }, 15 * 1000)
                            glitter.bleConnectDailog()
                        }
                    }
                } else {
                    glitter.bleStartScan()
                }
            })
        }
    })
}


