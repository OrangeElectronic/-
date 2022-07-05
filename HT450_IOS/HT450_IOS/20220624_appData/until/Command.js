"use strict"

class Command {
    constructor() {
        this.rx = ''
        //HT430
        glitter.app=""

        //斷線判斷
        function disConnect(callback){
            callback(glitter.disconnectTimer)
            return glitter.disconnectTimer
        }

        this.send = function (type,data) {
            if(type==="FW"){
                glitter.runJsInterFace("Glitter_BLE_IsConnect", {
                }, function (response) {
                    //console.log(response.result)
                    data = data.replace(/ /g, "")
                    if (response.result) {
                        var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss");
                        glitter.command.memory+=`<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">MyTx->${data}</p>
                                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">`+timeInMs+`</p>`
                        glitter.tx_Command_Memory=glitter.tx_Command_Memory+"tx："+data+"\n"
                        glitter.command.rx = ''

                        glitter.runJsInterFace("Glitter_BLE_WriteHex",
                            glitter.print(function () {
                                if(glitter.deviceType===glitter.deviceTypeEnum.Android){
                                    return {
                                        //正式
                                        data: data.replace(/ /g, ''),
                                        rxChannel: "00001530-1212-EFDE-1523-785FEABCD123",
                                        txChannel: "00001530-1212-EFDE-1523-785FEABCD123"
                                    }
                                }else{
                                    return  {
                                        data: data.replace(/ /g, ''),
                                        rxChannel: "1530",
                                        txChannel: "1530"
                                    }
                                }
                            })
                            , function (response) {
                                console.log(response.result)
                            })
                    }
                })
                return
            }

            if(type==="OTA"){
                glitter.runJsInterFace("Glitter_BLE_IsConnect", {
                }, function (response) {
                    //console.log(response.result)
                    data = data.replace(/ /g, "")
                    if (response.result) {
                        var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss");
                        glitter.command.memory+=`<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">MyTx->${data}</p>
                                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">`+timeInMs+`</p>`
                        glitter.tx_Command_Memory=glitter.tx_Command_Memory+"tx："+data+"\n"
                        glitter.command.rx = ''

                        glitter.runJsInterFace("Glitter_BLE_WriteHex",
                            glitter.print(function () {
                                if(glitter.deviceType===glitter.deviceTypeEnum.Android){
                                    return {
                                        //正式
                                        data: data.replace(/ /g, ''),
                                        rxChannel: "00008d82-0000-1000-8000-00805f9b34fb",
                                        txChannel: "00008d82-0000-1000-8000-00805f9b34fb"
                                    }
                                }else{
                                    return  {
                                        data: data.replace(/ /g, ''),
                                        rxChannel: "8D82",
                                        txChannel: "8D82"
                                    }
                                }
                            })
                            , function (response) {
                                console.log(response.result)
                            })
                    }
                })
                return
            }

            function AddCheckByte(hex){
                var byte2 = hex.substring(0,2)
                var hexArray=[byte2]
                var checkbyte= parseInt(byte2,16); //mData[0]
                var byteSize=8
                if(glitter.app==="HT430"){
                    byteSize=7
                }
                //7
                for(var i=1;i<byteSize;i++){
                    byte2 = hex.substring(i*2,i*2+2)
                    hexArray = hexArray.concat(byte2)
                    checkbyte ^= parseInt(byte2,16); //mData[i]
                }

                if(checkbyte.toString(16).length<2){
                    checkbyte = "0"+checkbyte.toString(16)
                }
                hexArray=hexArray.concat(checkbyte.toString(16))
                hexArray=hexArray.concat(hex.substring(hex.length-2,data.length))
                var newHex = ""
                for (var i=0;i<hexArray.length;i++){
                    newHex=newHex+hexArray[i].toUpperCase()
                }
                return newHex;
            }
            data=data.replace(/ /g, "")
            if(glitter.app==="HT430" && data.length===20){
                data=data.substring(0,16)+data.substring(18,20)
            }
            console.log("data_Tx:"+type+","+data)
            data=AddCheckByte(data)

            //**
            glitter.share.home.commadMessage("TX:"+data)
            glitter.command.rx = ''

            if(glitter.demo){
                return
            }

            glitter.runJsInterFace("Glitter_BLE_IsConnect", {
            }, function (response) {
                console.log(response.result)
                data = data.replace(/ /g, "")
                if (response.result) {
                    var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss");
                    glitter.command.memory+=`<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">MyTx->${data}</p>
                                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">`+timeInMs+`</p>`
                    glitter.tx_Command_Memory=glitter.tx_Command_Memory+"tx："+data+"\n"
                    //console.log('MyTx->'+data)
                    //glitter.command.rx = ''
                    //glitter.share.home.commadMessage("TX:"+data)
                    glitter.command.rx = ''

                    glitter.runJsInterFace("Glitter_BLE_WriteHex",
                         glitter.print(function () {
                             if(glitter.deviceType===glitter.deviceTypeEnum.Android){
                                 return {
                                     //正式
                                     data: data.replace(/ /g, ''),
                                     rxChannel: "00008d81-0000-1000-8000-00805f9b34fb",
                                     txChannel: "00008d81-0000-1000-8000-00805f9b34fb"

                                     //測試
                                     //data: data.replace(/ /g, ''),
                                     // rxChannel: "49535343-8841-43F4-A8D4-ECBE34729BB3",
                                     // txChannel: "49535343-1E4D-4BD9-BA61-23C647249616"
                                     //rxChannel: "49535343-1E4D-4BD9-BA61-23C647249616",
                                     //txChannel: "49535343-8841-43F4-A8D4-ECBE34729BB3"

                                     //測試
                                     //data: data.replace(/ /g, ''),
                                     //rxChannel: "E093F3B5-00A3-A9E5-9ECA-40046E0EDC24",
                                     //txChannel: "E093F3B5-00A3-A9E5-9ECA-40056E0EDC24"
                                 }
                             }else{
                                 return  {
                                     data: data.replace(/ /g, ''),
                                     rxChannel: "8D81",
                                     txChannel: "8D81"
                                 }
                             }
                         })
                        , function (response) {
                        console.log(response.result)
                    })

                }
            })
        }

        //讀取韌體版本
        this.readfirmware=function (Car,callback){
            try {
                if(glitter.demo){
                    callback("20220107")
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                var firmware = []
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    firmware=[]
                    clock.zeroing()
                    var tx = "53 ED Car FF FF FF FF FF 05 0A".replace("Car", Car)
                        .replace(/ /g, "")
                    glitter.command.send("讀取韌體版本", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback("false")
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length >= rxSize) {
                        //console.log("command.rx:" + rx)

                        // if (rx.substring(2,6) === "FD"+Car && rx.substring(18, 20) === "0A") {
                        //     console.log("readfirmware:"+rx.substring(6, 14))
                        //     callback(rx.substring(6, 14))
                        //     clearInterval(timer)
                        // }

                        for (var i = 0; i < (rx.length / rxSize); i++) {
                            var rxText20 = rx.substring(rxSize * i, rxSize + rxSize * i)
                            if ((rxText20.substring(0, 6) === '53FD'+Car) && (rxText20.substring(rxSize-2, rxSize) === '0A')){
                                var rx_text = rxText20.replace("53FD"+Car, "")
                                callback(rx_text.substring(0,8))
                                clearInterval(timer)
                            }
                        }
                    }

                }, 20);
            } catch (e) {
                callback("false")
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //讀取車ID
        this.readCarID=function (Car,callback){
            try {
                if(glitter.demo){
                    callback("31324142")
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx = "53 D0 Car FF FF FF FF FF 05 0A".replace("Car", Car)
                        .replace(/ /g, "")
                    glitter.command.send("讀取車ID", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback("false")
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length >= rxSize) {
                        //console.log("command.rx:" + rx)

                        // if (glitter.app==="HT450" && rx.substring(2, 4) === "D0" && rx.substring(18, 20) === "0A") {
                        //     callback(rx.substring(6, 14))
                        //     clearInterval(timer)
                        // }
                        // if (glitter.app==="HT471A" && rx.substring(2, 4) === "E0" && rx.substring(18, 20) === "0A") {
                        //     callback(rx.substring(4, 12))
                        //     clearInterval(timer)
                        // }

                        for (var i = 0; i < (rx.length / rxSize); i++) {
                            var rxText20 = rx.substring(rxSize * i, rxSize + rxSize * i)
                            switch (glitter.app){
                                case "HT471A":
                                    if ((rxText20.substring(0, 4) === '53E0') && (rxText20.substring(rxSize-2, rxSize) === '0A')){
                                        var rx_text = rxText20.replace("53E0", "")
                                        callback(rx_text.substring(0,8))
                                        clearInterval(timer)
                                    }
                                    break
                                default:
                                    if((rxText20.substring(0, 6) === '53D0'+Car) && (rxText20.substring(rxSize-2, rxSize) === '0A')) {
                                        var rx_text = rxText20.replace("53D0"+Car, "")
                                        callback(rx_text.substring(0,8))
                                        clearInterval(timer)
                                    }
                                    break
                            }
                        }
                    }

                }, 200);
            } catch (e) {
                callback("false")
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //設定車ID
        this.writeCarID=function (Car,carID,callback){
            try {
                if(glitter.demo){
                    callback(true)
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx = "53 A0 type carID FF 05 0A".replace("carID", carID).replace("type",Car)
                        .replace(/ /g, "")
                    switch (glitter.app){
                        case "HT471A":
                            tx = "53 A0 carID FF FF 05 0A".replace("carID", carID)
                                .replace(/ /g, "")
                            break
                        default:
                            tx = "53 A0 type carID FF 05 0A".replace("carID", carID).replace("type",Car)
                                .replace(/ /g, "")
                            break
                    }

                    glitter.command.send("設定車ID", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        if (rx.substring(2, 4) === "B0" && rx.substring(rxSize-2, rxSize) === "0A") {
                            //確認資料
                            glitter.command.send("設定車ID",rx.replace("B0","C1"))
                            callback(true)
                            clearInterval(timer)
                        }
                    }

                }, 200);
            } catch (e) {
                callback(false)
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //讀取軸的輪數
        this.readRowWheels=function (Car,callback){
            try {
                if(glitter.demo){
                    callback("02040000")
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx = "53 AB Car FF FF FF FF FF 05 0A".replace("Car", Car)
                        .replace(/ /g, "")
                    glitter.command.send("讀取輪軸", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    var notConnect = disConnect(function (bool) {
                        if(bool){
                            console.log("notConnect:true")
                            reCommand=0
                            send()
                        }
                    })

                    if (clock.stop() > 2000 && !notConnect) {
                        reCommand++
                        if(reCommand===3){
                            callback("false")
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        if (rx.substring(2, 4) === "BB" && rx.substring(rxSize-2,rxSize) === "0A") {
                            callback(rx.substring(6, 14))
                            clearInterval(timer)
                        }
                    }

                }, 200);
            } catch (e) {
                callback("false")
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //寫入軸的輪數
        this.writeRowWheels=function (Car,row1,row2,row3,row4,callback){
            if(glitter.demo){
                callback(true)
                return
            }

            glitter.uploadData.command_memory=""
            var reCommand=0
            var clock=Clock()
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            function send(){
                clock.zeroing()
                glitter.command.send("設定輪軸","53 AA Car row1 row2 row3 row4 FF 01 0A".
                replace("Car",Car).replace("row1", row1).
                replace("row2", row2).replace("row3", row3).
                replace("row4", row4).replace(/ /g, ""))
            }
            send()

            var timer = setInterval(function() {
                var rx=glitter.command.rx
                try {
                    if (clock.stop() > 1500) {
                        reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    if (rx.length >= rxSize) {
                        if(rx.substring(2,4)==="BA" && rx.substring(rx.length-2,rx.length)==="0A"){
                            //確認資料
                            glitter.command.send("設定輪軸",rx.replace("BA","C1"))
                            callback(true)
                            clearInterval(timer)
                        }
                    }
                }catch (e){
                    callback(false)
                    clearInterval(timer)}
            }, 200);
        }

        /***
         * 讀取全部Sensor位置與ID
         * Car 01前車 02候車
         * */
        this.readAllSensorID = function (Car,carCount,callback) {
            if(Car==="01"){
                carCount=14
            }
            if(Car==="02"){
                carCount=16
            }
            //Car="03"
            if(glitter.demo){
                var carPosition=parseInt(Car,16)
                var tireStatus=glitter.tireStatus[carPosition]
                var test_ID = []
                for(var i=0;i<carCount;i++){
                    var addPosition=0
                    if(Car==="01"){
                        addPosition=0
                    }
                    if(Car==="02" && glitter.app!=="HT471A"){
                        addPosition=14
                    }
                    var position=(i+1+addPosition).toString(16)
                    if(position.length!==2){
                        position = "0"+position
                    }
                    if(tireStatus[i+1].id===""){
                        test_ID = test_ID.concat(position + "11111"+(i).toString(16).toUpperCase())
                    }else{
                        test_ID = test_ID.concat(position + tireStatus[i+1].id)
                    }
                }
                callback(test_ID)
                return
            }

            //glitter.command.send("53 D1 Car Pos FF FF FF FF 01 0A")

            glitter.uploadData.command_memory=""
            var clock = Clock()
            var sensor_ID = []
            var reCommand=0
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            function send(){
                sensor_ID=[]
                clock.zeroing()
                glitter.command.send("讀取ID","53 D1 Car FF FF FF FF FF 01 0A".replace("Car",Car).replace(/ /g, ""))
            }
            send()

            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= rxSize*carCount) {
                        // (rx.substring(2, 4) === 'E1')
                        //if ((rx.substring(2, 4) === 'D1') && (rx.substring(rx.length - 2, rx.length) === '0A')) {
                            //console.log("command.rx:"+rx)

                            for (var i = 0; i < (rx.length / rxSize); i++) {
                                var rxText20 = rx.substring(rxSize * i, rxSize + rxSize * i)
                                //var rx_text = rxText20.replace("53E1"+Car, "")

                                switch (glitter.app) {
                                    case "HT471A":
                                        if ((rxText20.substring(0, 4) === '53E1') && (rxText20.substring(rxText20.length - 2, rxText20.length) === '0A')){
                                            var rx_text = rxText20.replace("53E1"+Car, "")
                                            if (sensor_ID.indexOf(rx_text.substring(0, 8)) === -1) {
                                                sensor_ID = sensor_ID.concat(rx_text.substring(0, 8))
                                            }
                                        }
                                        break
                                    default:
                                        if ((rxText20.substring(0, 4) === '53D1') && (rxText20.substring(rxText20.length - 2, rxText20.length) === '0A')){
                                            var rx_text = rxText20.replace("53D1"+Car, "")
                                            if (sensor_ID.indexOf(rx_text.substring(0, 8)) === -1) {
                                                sensor_ID = sensor_ID.concat(rx_text.substring(0, 8))
                                            }
                                        }
                                        break
                                }
                            }

                            if(sensor_ID.length === carCount){
                                callback(sensor_ID)
                                clearInterval(timer)
                            }
                        //}
                        console.log("carCount:"+carCount+","+sensor_ID.length)

                        // if(carCount===sensor_ID.length){
                        //     console.log("sensor_ID_clock:"+clock.stop())
                        //     callback(sensor_ID)
                        //     clearInterval(timer)
                        // }
                    }
                    var notConnect = disConnect(function (bool) {
                        if(bool){
                            console.log("notConnect:true")
                            reCommand=0
                            send()
                        }
                    })
                    //7000 3000 5000
                    if (clock.stop() > 3000 && !notConnect) {
                        reCommand++
                        if(reCommand===3){
                            callback(["false"])
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                } catch (e) {
                    callback(["false"])
                    clearInterval(timer)
                }
            }, 200);
        }
        //設定SensorID
        this.writeSensorID=function (Car,Pos,SensorID,callback){
            try {
                if(glitter.demo){
                    callback(true)
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx=""
                    switch (glitter.app){
                        case "HT471A":
                            tx = "53 A1 Car Pos SensorID FF 05 0A".replace("Car",Car).replace("Pos",Pos).replace("SensorID", SensorID)
                                .replace(/ /g, "")
                            break
                        default:
                            tx = "53 A2 Car Pos SensorID FF 05 0A".replace("Car",Car).replace("Pos",Pos).replace("SensorID", SensorID)
                                .replace(/ /g, "")
                            break
                    }

                    glitter.command.send("設定SensorID", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){

                        switch (glitter.app){
                            case "HT471A":
                                if (rx.substring(2, 4) === "B1" && rx.substring(rxSize-2, rxSize) === "0A") {
                                    callback(true)
                                    clearInterval(timer)
                                }
                                break
                            default:
                                if (rx.substring(2, 4) === "B2" && rx.substring(rxSize-2, rxSize) === "0A") {
                                    //確認資料
                                    glitter.command.send("設定SensorID",rx.replace("B2","C1"))
                                    callback(true)
                                    clearInterval(timer)
                                }
                                break
                        }


                    }

                }, 200);
            } catch (e) {
                callback(false)
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }
        //洩壓學碼SensorID
        this.deflationSensorID=function (Car,Pos,callback){
            try {
                if(glitter.demo){
                    callback(true)
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var start=false
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx=""
                    switch (glitter.app) {
                        case "HT471A":
                            tx = "53 C5 Car Pos FF FF FF FF 05 0A".replace("Car",Car).replace("Pos",Pos)
                                .replace(/ /g, "")
                            break
                        default:
                            start=true
                            tx = "53 A1 Car Pos FF FF FF FF 05 0A".replace("Car",Car).replace("Pos",Pos)
                                .replace(/ /g, "")
                            break
                    }

                    glitter.command.send("洩壓設定SensorID", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (!start && clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback("false")
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    if (start){
                        //console.log("deflationTime:"+clock.stop())
                        glitter.deflationTime(Math.round(clock.stop()/1000))
                    }
                    if (start && clock.stop() > 60*1000) {
                        callback("false")
                        clearInterval(timer)
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length >= rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        //&& rx.substring(0, 4) === "53D5" && rx.substring(18, 20) === "0A"
                        switch (glitter.app){
                            case "HT471A":
                                if (!start && rx.substring(0, 4) === "53D5" && rx.substring(rxSize-2, rxSize) === "0A") {
                                    start=true
                                    glitter.closeDiaLogWithTag("Dia_Info_Progress")
                                    glitter.openDiaLog('dialog/Dia_Info_Progress.html', 'Dia_Info_Progress', false, false,{name:"洩壓學碼中"})
                                    clock.zeroing()
                                }
                                if (start && rx.substring(0, 4) === "53E1" && rx.substring(rxSize-2, rxSize) === "0A") {
                                    callback(rx.substring(6, 12))
                                    clearInterval(timer)
                                }
                                break
                            default:
                                // if (start && rx.substring(0, 4) === "53B1" && rx.substring(18, 20) === "0A") {
                                //     //確認資料
                                //     glitter.command.send("洩壓設定SensorID",rx.replace("53B1","53C1"))
                                //     callback(rx.substring(6, 12))
                                //     clearInterval(timer)
                                // }
                                for (var i = 0; i < (rx.length / rxSize); i++) {
                                    var rxText20 = rx.substring(rxSize * i, rxSize + rxSize * i)
                                    if ((rxText20.substring(0, 4) === '53B1') && (rxText20.substring(rxText20.length - 2, rxText20.length) === '0A')) {
                                        var rx_text = rxText20.replace("53B1" + Car, "")
                                        //確認資料
                                        glitter.command.send("洩壓設定SensorID",rxText20.replace("53B1","53C1"))
                                        callback(rx_text.substring(2, 8))
                                        clearInterval(timer)
                                    }
                                }
                                break
                        }
                    }

                }, 200);
            } catch (e) {
                callback("false")
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }
        /***
         * 讀取全部Sensor狀態
         * Car 01前車 02候車
         * */
        this.readAllTireStatus = function (Car,carCount,callback) {
            if(Car==="01"){
                carCount=14
            }
            if(Car==="02"){
                carCount=16
            }
            //Car="03"

            if(glitter.demo){
                var test_Status = []
                for(var i=0;i<carCount;i++){
                    var addPosition=0
                    if(Car==="01"){
                        addPosition=0
                    }
                    if(Car==="02" && glitter.app!=="HT471A"){
                        addPosition=14
                    }
                    var position=(i+1+addPosition).toString(16)
                    if(position.length!==2){
                        position = "0"+position
                    }
                    if(i < 4){
                        test_Status = test_Status.concat(position + "FE" + "FEFE")
                        //test_Status = test_Status.concat(position + "6E" + "03E8")
                    }else if(i < 8){
                        test_Status = test_Status.concat(position + "82" + "0303")
                    }
                    if(i >= 8){
                        //test_Status = test_Status.concat(position + "82" + "05DC")
                        test_Status = test_Status.concat(position + "FF" + "FFFF")
                    }
                }
                callback(test_Status)
                return
            }

            //glitter.command.send("53 D1 Car Pos FF FF FF FF 01 0A")

            glitter.uploadData.command_memory=""
            var clock = Clock()
            var sensor_Status = []
            var reCommand=0
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            function send(){
                sensor_Status = []
                clock.zeroing()
                glitter.command.send("讀取狀態","53 A6 Car FF FF FF FF FF 01 0A".replace("Car",Car).replace(/ /g, ""))
            }
            send()

            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= rxSize*carCount) {
                        //if ((rx.substring(2, 4) === 'B6') && (rx.substring(rx.length - 2, rx.length) === '0A')) {
                            //console.log("command.rx:"+rx)

                        // for (var i = 0; i < (rx.length / 20); i++) {
                        //     var rxText20 = rx.substring(20 * i, 20 + 20 * i)
                        //     //var rx_text = rxText20.replace("53E1"+Car, "")
                        //     if ((rxText20.substring(0, 4) === '53E1') && (rxText20.substring(rxText20.length - 2, rxText20.length) === '0A')){
                        //         var rx_text = rxText20.replace("53E1"+Car, "")
                        //         if (sensor_ID.indexOf(rx_text.substring(0, 8)) === -1) {
                        //             sensor_ID = sensor_ID.concat(rx_text.substring(0, 8))
                        //         }
                        //     }
                        // }

                            for (var i = 0; i < (rx.length / rxSize); i++) {
                                var rxText20 = rx.substring(rxSize * i, rxSize + rxSize * i)
                                if ((rxText20.substring(0, 4) === '53B6') && (rxText20.substring(rxText20.length - 2, rxText20.length) === '0A')) {
                                    var rx_text = rxText20.replace("53B6" + Car, "")
                                    if (sensor_Status.indexOf(rx_text) === -1) {
                                        sensor_Status = sensor_Status.concat(rx_text)
                                    }
                                }
                                // if (sensor_Status.indexOf(rx_text.substring(0, 2)) === -1) {
                                //     sensor_Status = sensor_Status.concat(rx_text.substring(0, 8))
                                // }
                            }

                            if(sensor_Status.length === carCount){
                                callback(sensor_Status)
                                clearInterval(timer)
                            }
                        //}
                        console.log("carCount:"+carCount+","+sensor_Status.length)
                        // if(carCount===sensor_ID.length){
                        //     console.log("sensor_ID_clock:"+clock.stop())
                        //     callback(sensor_ID)
                        //     clearInterval(timer)
                        // }
                    }
                    var notConnect = disConnect(function (bool) {
                        if(bool){
                            console.log("notConnect:true")
                            reCommand=0
                            send()
                        }
                    })

                    //7000
                    if (clock.stop() > 3000 && !notConnect) {
                        reCommand++
                        if(reCommand===3){
                            callback(["false"])
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                } catch (e) {
                    callback(["false"])
                    alert(e)
                    alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                    alert(e.line)  // this will work on safari but not on IPhone
                    clearInterval(timer)
                }
            }, 200);
        }

        //讀取序列號資料
        this.readRxID = function (rxCount,callback) {
            if(glitter.demo){
                var rxID = ["A3333303","44444404","11111101","22222202","A55555FF","66666606","77777707"]
                //var rxID = ["333333FF","444444FF","111111FF","555555FF"]
                //var rxID = ["31111101","32222202","33333303"]
                callback(rxID)
                return
            }

            //glitter.command.send("5B C1 FF FF FF FF FF FF 01 0B")
            //glitter.command.send("5F C1 FF FF FF FF FF FF 01 0B")
            glitter.uploadData.command_memory=""
            glitter.command.send("讀取序列號","53 C2 FF FF FF FF FF FF 01 0A")
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            var clock = Clock()
            var rx_ID = []
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= rxSize) {
                        //if ((rx.substring(2, 4) === 'D2') && (rx.substring(rx.length - 2, rx.length) === '0A')) {
                            for (var i = 0; i < (rx.length / rxSize); i++) {
                                var rxText20 = rx.substring(rxSize * i, rxSize + rxSize * i)
                                if ((rxText20.substring(0, 4) === '53D2') && (rxText20.substring(rxText20.length - 2, rxText20.length) === '0A')) {
                                    var rx_text = rxText20.replace("53D2", "")
                                    if (rx_ID.indexOf(rx_text.substring(0, 8)) === -1) {
                                        rx_ID = rx_ID.concat(rx_text.substring(0, 8))
                                    }
                                }
                            }
                       //}
                        console.log("rxCount:"+rxCount+","+rx_ID.length)
                        // if(rxCount===rx_ID.length){
                        //     console.log("sensor_ID_clock:"+clock.stop())
                        //     callback(rx_ID)
                        //     clearInterval(timer)
                        // }
                    }

                    var notConnect = disConnect(function (bool) {
                        if(bool){
                            console.log("notConnect:true")
                            //reCommand=0
                            //send()
                            clock.zeroing()
                            glitter.command.send("讀取序列號","53 C2 FF FF FF FF FF FF 01 0A")
                            rx_ID = []
                        }
                    })

                    if (clock.stop() > 8000 && !notConnect) {
                        callback(rx_ID)
                        clearInterval(timer)
                    }
                } catch (e) {
                    callback(rx_ID)
                    clearInterval(timer)
                }
            }, 10);
            //200
        }

        /*
        * map{id:String,pos:String}
        * callback:function(boolean){}
        * */
        this.writeRxId = function (map, callback) {
            if(glitter.demo){
                callback(true)
                return
            }

            while (map.id.length < 6) {
                map.id = "0" + map.id
            }

            glitter.uploadData.command_memory=""
            var clock = Clock()
            var reCommand=0
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            function send(){
                clock.zeroing()
                glitter.command.send("設定序列號","53 CA ID Pos FF FF 01 0A".replace('Pos', map.pos).replace("ID", map.id))
            }
            send()

            //glitter.command.send("5B CA ID Pos FF FF 01 0B".replace('Pos', map.pos).replace("ID", map.id))
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= rxSize) {
                        //DB
                        if((rx.substring(2, 4) === 'DA') && (rx.substring(rx.length - 2, rx.length) === '0A')){
                            callback(true)
                            clearInterval(timer)
                        }
                        //callback(((rx.substring(2, 4) === 'DA') && (rx.substring(rx.length - 2, rx.length) === '0A')))
                        //callback(((rx.substring(2, 4) === 'BA') && (rx.substring(rx.length - 2, rx.length) === '0B')))

                    }

                    if (clock.stop() > 1500) {
                            reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                } catch (e) {
                    callback(false)
                    clearInterval(timer)
                }
            }, 200);
        }

        /*
        * map{id:String,pos:String}
        * callback:function(boolean){}
        * */
        this.writeSwapTire = function (car,position, callback) {
            if(glitter.demo){
                callback(true)
                return
            }

            glitter.uploadData.command_memory=""
            var tx="53 A3 car Pos FF FF FF 01 0A".replace("car",car).replace('Pos', position)
            var clock = Clock()
            var reCommand=0
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            function send(){
                clock.zeroing()
                glitter.command.send("設定換輪",tx)
            }
            send()

            //glitter.command.send("5B CA ID Pos FF FF 01 0B".replace('Pos', map.pos).replace("ID", map.id))
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= rxSize) {
                        //DB
                        if((rx.substring(2, 4) === 'B3') && (rx.substring(rx.length - 2, rx.length) === '0A')){
                            //確認資料
                            glitter.command.send("設定換輪",tx.replace(/ /g, "").replace("53A3","53C1"))
                            callback(true)
                            clearInterval(timer)
                        }
                        //callback(((rx.substring(2, 4) === 'DA') && (rx.substring(rx.length - 2, rx.length) === '0A')))
                        //callback(((rx.substring(2, 4) === 'BA') && (rx.substring(rx.length - 2, rx.length) === '0B')))

                    }

                    if (clock.stop() > 1500) {
                        reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                } catch (e) {
                    callback(false)
                    clearInterval(timer)
                }
            }, 200);
        }

        //讀取胎壓胎溫範圍
        this.readUnitRange=function (Car,callback){

            var Data = []

            // callback(Data)
            // return

            if(glitter.demo){
                var H_Tem = (parseInt("6E", 16)-50)
                var H_Pre = parseInt("05DC", 16) //03E8 04B0
                var L_Pre = parseInt("0258", 16) //01F4 0258
                Data=Data.concat(H_Tem)
                Data=Data.concat(H_Pre)
                Data=Data.concat(L_Pre)
                callback(Data)
                return
            }

            glitter.uploadData.command_memory=""
            //glitter.tx_Command_Memory=""
            var reCommand = 0
            var clock = Clock()
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            function send() {
                clock.zeroing()
                // if(Car==="01"){
                //     glitter.command.send("讀取警報值","5F CD Car FF FF FF FF FF FF 0B".replace("Car",Car).
                //     replace(/ /g, ""))
                // }else{
                    glitter.command.send("讀取警報值","53 A9 Car FF FF FF FF FF 01 0A".replace("Car",Car).
                    replace(/ /g, ""))
                //}
            }
            send()

            var timer = setInterval(function() {

                //console.log('timer')
                var rx=glitter.command.rx
                try {
                    //15000
                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback(["false"])
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    if (rx.length >= rxSize) {
                        // if(rx.substring(0,2)==="5B" && rx.substring(rx.length-2,rx.length)==="0B") {
                        var rxData=false
                        // if(Car==="01"){
                        //     rxData=rx.substring(2,4)==="DD" && rx.substring(rx.length-2,rx.length)==="0B"
                        // }if(Car==="02"){
                            rxData=rx.substring(2,6)==="B9"+Car && rx.substring(rx.length-2,rx.length)==="0A"
                        //}
                        if(rxData) {
                            //if(rx.substring(2,4)==="DD" && rx.substring(rx.length-2,rx.length)==="0B") {
                            var R_Text = rx.substring(0,rxSize)
                            var H_Temp=0
                            var H_Pre=0
                            var L_Pre=0
                            switch (glitter.app){
                                case "HT430":
                                    H_Temp = (parseInt(R_Text.substring(6, 8), 16)-50)
                                    H_Pre = parseInt(R_Text.substring(8, 10), 16)*6
                                    L_Pre = parseInt(R_Text.substring(10, 12), 16)*6
                                    break
                                default:
                                    H_Temp = (parseInt(R_Text.substring(6, 8), 16)-50)

                                    var H_Pre_h = R_Text.substring(8, 10)
                                    var H_Pre_l = R_Text.substring(10, 12)
                                    H_Pre = parseInt(H_Pre_l + H_Pre_h, 16)

                                    var L_Pre_h = R_Text.substring(12, 14)
                                    var L_Pre_l = R_Text.substring(14, 16)
                                    L_Pre = parseInt(L_Pre_l + L_Pre_h, 16)
                                    break
                            }

                            console.log("H_Temp"+H_Temp+",H_Pre"+H_Pre+",L_Pre"+L_Pre)
                            Data=Data.concat(H_Temp)
                            Data=Data.concat(H_Pre)
                            Data=Data.concat(L_Pre)
                            callback(Data)
                            clearInterval(timer)
                        }
                    }
                }catch (e){
                    callback(["false"])
                    clearInterval(timer)}
            }, 200);
        }

        function TwoBytes(old_bytes,size){
            var old_Bytes= old_bytes
            for(var i=old_bytes.length;i<size;i++){
                old_Bytes = "0"+old_Bytes
            }
            return old_Bytes
        }
        //寫入胎壓胎溫範圍
        this.writeUnitRange=function (Car,high_temp,high_pre,low_pre,callback){
            var H_Temp = TwoBytes(parseInt(high_temp+50).toString(16),2).toUpperCase()
            var H_Pre,H_Pre_h,H_Pre_l,L_Pre,L_Pre_h,L_Pre_l

            switch (glitter.app) {
                case "HT430":
                    H_Pre = TwoBytes((high_pre/6).toString(16),2).toUpperCase()
                    L_Pre = TwoBytes((low_pre/6).toString(16),2).toUpperCase()
                    break
                default:
                    H_Pre = TwoBytes(parseInt(high_pre).toString(16),4).toUpperCase()
                    H_Pre_h = H_Pre.substring(0,2)
                    H_Pre_l = H_Pre.substring(2,4)
                    L_Pre = TwoBytes(parseInt(low_pre).toString(16),4).toUpperCase()
                    L_Pre_h = L_Pre.substring(0,2)
                    L_Pre_l = L_Pre.substring(2,4)
                    break
            }

            if(glitter.demo){
                callback(true)
                return
            }
            // glitter.command.send("53 A8 Car Temp H_Pre L_Pre 01 0A".
            // replace("Car",Car).replace("Temp",H_Temp).
            // replace("H_Pre",H_Pre_h+H_Pre_l).
            // replace("L_Pre",L_Pre_h+L_Pre_l).
            // replace(/ /g, ""))
            // // replace("H_Pre",H_Pre_l+H_Pre_h).
            // // replace("L_Pre",L_Pre_l+L_Pre_h).
            // // replaceAll(" ",""))

            glitter.uploadData.command_memory=""
            //glitter.tx_Command_Memory=""
            var reCommand = 0
            var clock = Clock()
            //18 20
            var rxSize=20
            if(glitter.app==="HT430"){
                rxSize=18
            }
            function send() {
                clock.zeroing()

                switch (glitter.app) {
                    case "HT430":
                        glitter.command.send("設定警報值","53 A8 Car Temp H_Pre L_Pre FF FF 01 0A".
                        replace("Car",Car).replace("Temp",H_Temp).
                        replace("H_Pre",H_Pre).replace("L_Pre",L_Pre).
                        replace(/ /g, ""))
                        break
                    default:
                        glitter.command.send("設定警報值","53 A8 Car Temp H_Pre L_Pre 01 0A".
                        replace("Car",Car).replace("Temp",H_Temp).
                        replace("H_Pre",H_Pre_l+H_Pre_h).
                        replace("L_Pre",L_Pre_l+L_Pre_h).
                        replace(/ /g, ""))
                        break
                }
            }
            send()

            var timer = setInterval(function() {
                //console.log('timer')
                var rx=glitter.command.rx
                try {
                    //15000
                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    if (rx.length === rxSize) {
                        if(rx.substring(2,4)==="B8" && rx.substring(rx.length-2,rx.length)==="0A"){
                            //確認資料
                            glitter.command.send("設定警報值",rx.replace("B8","C1"))
                            callback(true)
                            clearInterval(timer)
                        }
                    }
                }catch (e){
                    callback(false)
                    clearInterval(timer)}
            }, 200);
        }

        //讀取藍芽連線的RSSI(前車與後車連接的距離)
        this.readConnectRSSI=function (Car,callback){
            try {
                if(glitter.demo){
                    callback("C3")
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx = "53 AF FF FF FF FF FF FF 05 0A".replace("Car", Car)
                        .replace(/ /g, "")
                    glitter.command.send("讀取RSSI", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback("false")
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        if (rx.substring(2, 4) === "BF" && rx.substring(rxSize-2, rxSize) === "0A") {
                            callback(rx.substring(4, 6))
                            clearInterval(timer)
                        }
                    }

                }, 200);
            } catch (e) {
                callback("false")
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //讀取RSSI強度
        this.readRSSI=function (Car,callback){
            try {
                if(glitter.demo){
                    callback("C3")
                    return
                }

                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                function send(){
                    clock.zeroing()
                    var tx = "53 E4 FF FF FF FF FF FF 05 0A".replace("Car", Car)
                        .replace(/ /g, "")
                    glitter.command.send("讀取RSSI", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback("false")
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        if (rx.substring(2, 4) === "F4" && rx.substring(rxSize-2, rxSize) === "0A") {
                            callback(rx.substring(4, 6))
                            clearInterval(timer)
                        }
                    }

                }, 200);
            } catch (e) {
                callback("false")
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //設定RSSI強度
        this.writeRSSI=function (Car,RSSI,callback){
            try {
                if(glitter.demo){
                    callback(true)
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx = "53 E7 RSSI FF FF FF FF FF FF 0A".replace("Car", Car).replace("RSSI", RSSI)
                        .replace(/ /g, "")
                    glitter.command.send("設定RSSI強度", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        if (rx.substring(2, 4) === "E7" && rx.substring(rxSize-2, rxSize) === "0A") {
                            callback(true)
                            clearInterval(timer)
                        }
                    }

                }, 200);
            } catch (e) {
                callback(false)
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //時間校正 00 01
        this.sync_time=function (Car,callback) {
            try {
                if (glitter.demo) {
                    callback(true)
                    return
                }

                glitter.uploadData.command_memory = ""
                var reCommand = 0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send() {
                    clock.zeroing()
                    var tx = "53 E0 Car FF FF FF FF FF FF 0A".replace("Car", Car).replace(/ /g, "")
                    glitter.command.send("時間校正", tx)
                }

                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if (reCommand === 3) {
                            callback(false)
                            clearInterval(timer)
                        } else {
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        if (rx.substring(0, 6) === "53F0" + Car && rx.substring(rxSize-2, rxSize) === "0A") {
                            callback(true)
                            clearInterval(timer)
                        }
                    }

                }, 200);
            } catch (e) {
                callback(false)
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }

        //設定自動定位
        this.writeAutoPosition=function (Car,open,callback){
            try {
                if(glitter.demo){
                    callback(true)
                    return
                }

                glitter.uploadData.command_memory=""
                var reCommand=0
                var clock = Clock()
                //18 20
                var rxSize=20
                if(glitter.app==="HT430"){
                    rxSize=18
                }
                function send(){
                    clock.zeroing()
                    var tx = "53 AF Car open FF FF FF FF FF 0A".replace("Car", Car).replace("open", open)
                        .replace(/ /g, "")
                    glitter.command.send("設定自動定位", tx)
                }
                send()
                var timer = setInterval(function () {
                    //console.log('timer')
                    var rx = glitter.command.rx

                    if (clock.stop() > 2000) {
                        reCommand++
                        if(reCommand===3){
                            callback(false)
                            clearInterval(timer)
                        }else{
                            send()
                        }
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === rxSize) {
                        //console.log("command.rx:" + rx)
                        //if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){
                        if (rx.substring(0, 8) === "53BF"+Car+open && rx.substring(rxSize-2, rxSize) === "0A") {
                            callback(true)
                            clearInterval(timer)
                        }
                    }

                }, 200);
            } catch (e) {
                callback(false)
                console.log("command:" + e)
                alert(e)
                alert(e.stack)  // this will work on chrome, FF. will no not work on safari
                alert(e.line)  // this will work on safari but not on IPhone
                clearInterval(timer)
            }
        }
    }
}

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

glitter.command = new Command()