"use strict"

class Command {
    constructor() {
        this.rx = ''
        this.send = function (data,CarType) {
            glitter.share.bleUtil.isConnect({
                callback: function (response) {
                    data = data.replace(/ /g, "")
                    if (response.result) {
                        var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss:S");
                        glitter.command.memory+=`<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">MyTx->${data}</p>
                                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">`+timeInMs+`</p>`
                        console.log('MyTx->'+data)
                        glitter.command.rx = ''

                        if(CarType === "Tread_depth"){
                            glitter.share.bleUtil.writeHex({
                                data: {
                                    data: data.replace(/ /g, ''),
                                    rxChannel: "0000FF00-0000-1000-8000-00805F9B34FB",
                                    txChannel: "0000FF00-0000-1000-8000-00805F9B34FB"
                                }
                            })
                            return
                        }
                        glitter.share.bleUtil.writeHex({
                             data: { //正式
                                 data: data.replace(/ /g, ''),
                                 rxChannel: "E093F3B5-00A3-A9E5-9ECA-40056E0EDC24",
                                 txChannel: "E093F3B5-00A3-A9E5-9ECA-40046E0EDC24"
                             }
//                            data: { //測試
//                                data: data.replace(/ /g, ''),
//                                rxChannel: "E093F3B5-00A3-A9E5-9ECA-40046E0EDC24",
//                                txChannel: "E093F3B5-00A3-A9E5-9ECA-40056E0EDC24"
//                            }
                        })
                    }
                }
            })

        }
        //讀取資料
        this.readRxID = function (rxCount,callback) {
            //glitter.command.send("5B C1 FF FF FF FF FF FF 01 0B")
             glitter.command.send("5F C1 FF FF FF FF FF FF 01 0B")
            var clock = Clock()
            var rx_ID = []
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= 20) {
                        if ((rx.substring(2, 4) === 'D1') && (rx.substring(rx.length - 2, rx.length) === '0B')) {
                            for (var i = 0; i < (rx.length / 20); i++) {
                                var rxText20 = rx.substring(20 * i, 20 + 20 * i)
                                var rx_text = rxText20.replace("5FD1", "")
                                if (rx_ID.indexOf(rx_text.substring(0, 8)) === -1) {
                                    rx_ID = rx_ID.concat(rx_text.substring(0, 8))
                                }
                            }
                        }
                        console.log("rxCount:"+rxCount+","+rx_ID.length)
                        // if(rxCount===rx_ID.length){
                        //     console.log("sensor_ID_clock:"+clock.stop())
                        //     callback(rx_ID)
                        //     clearInterval(timer)
                        // }
                    }

                    if (clock.stop() > 7000) {
                        callback(rx_ID)
                        clearInterval(timer)
                    }
                } catch (e) {
                    callback(rx_ID)
                    clearInterval(timer)
                }
            }, 200);
        }
        //讀取Rx位置
        this.readRxPosition = function (id,callback) {
            //glitter.command.send(("5B CB id FF FF FF 01 0B").replace("id",id))
            glitter.command.send("5F CB id FF FF FF 01 0B".replace("id",id))
            var clock = Clock()
            var rx_ID = []
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (clock.stop() > 15000) {
                        callback("false")
                        clearInterval(timer)
                    }
                    if (rx.length >= 20) {
                        if ((rx.substring(2, 4) === 'DB') && (rx.substring(rx.length - 2, rx.length) === '0B')) {
                            //for (var i = 0; i < (rx.length / 20); i++) {
                                //var rxText20 = rx.substring(20 * i, 20 + 20 * i)
                                var rxText20 = rx.substring(0, 20)
                                var rx_text = rxText20.replace("5BDB", "")
                                callback(rx_text.substring(0, 8))
                                clearInterval(timer)

                                // if (rx_ID.indexOf(rx_text.substring(0, 8)) === -1) {
                                //     rx_ID = rx_ID.concat(rx_text.substring(0, 8))
                                //     callback(rx_ID)
                                //     clearInterval(timer)
                                // }
                            //}
                        }
                    }
                } catch (e) {
                    callback("false")
                    clearInterval(timer)
                }
            }, 200);
        }

        /*
        * map{id:String,pos:String}
        * callback:function(boolean){}
        * */
        this.writeRxId = function (map, callback) {
            while (map.id.length < 6) {
                map.id = "0" + map.id
            }
            glitter.command.send("5F CA ID Pos FF FF 01 0B".replace('Pos', map.pos).replace("ID", map.id))
            //glitter.command.send("5B CA ID Pos FF FF 01 0B".replace('Pos', map.pos).replace("ID", map.id))
            var clock = Clock()
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= 20) {
                        //DB
                        callback(((rx.substring(2, 4) === 'DA') && (rx.substring(rx.length - 2, rx.length) === '0B')))
                        //callback(((rx.substring(2, 4) === 'BA') && (rx.substring(rx.length - 2, rx.length) === '0B')))
                        clearInterval(timer)
                    } else {
                        if (clock.stop() > 1500) {
                            callback(false)
                            clearInterval(timer)
                        }
                    }
                } catch (e) {
                    callback(false)
                    clearInterval(timer)
                }
            }, 200);
        }
        /***
         * 寫入SensorId
         * callback(boolean)
         * */
        this.writeSensorID = function (Car, ID, Pos, callback) {
            glitter.command.send("53 A1 Car Pos ID FF 01 0A".replace("Car", Car).replace("ID", ID).replace("Pos", Pos).replaceAll(" ", ""), true)
            var clock = Clock()
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (clock.stop() > 1500) {
                        callback(false)
                        clearInterval(timer)
                    }
                    if (rx.length >= 20) {
                        if (rx.substring(2, 4) === "B1" && rx.substring(rx.length - 2, rx.length) === "0A") {
                            callback(true)
                            clearInterval(timer)
                        }
                    }
                } catch (e) {
                    callback(false)
                    clearInterval(timer)
                }
            }, 200);
        }
         /***
         * 讀取單顆Sensor位置與ID
          * Car 01前車 02候車
         * */
            this.readSensorID=function (Car,Pos,callback){
            glitter.command.send("53 D1 Car Pos FF FF FF FF 01 0A".replace("Car",Car).
            replace("Pos",Pos).replaceAll(" ",""))

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')
                var rx=glitter.command.rx
                try {
                    if (clock.stop() > 1500) {
                        callback("false")
                        clearInterval(timer)
                    }
                    if (rx.length >= 20) {
                        if(rx.substring(2,4)==="E1" && rx.substring(rx.length-2,rx.length)==="0A"){
                            let Rx_text= rx.replace("53E1"+Car+Pos,"").substring(0,6)
                            callback(Rx_text)
                            clearInterval(timer)
                        }
                    }
                }catch (e){
                    callback("false")
                    clearInterval(timer)}
            }, 200);
        }
        var carDefine = glitter.carDefine
        var carType=glitter.selectCarType
        /***
         * 讀取全部Sensor位置與ID
         * Car 01前車 02候車
         * */
        this.readAllSensorID = function (Car,carCount,callback) {
            //glitter.command.send("53 D1 Car Pos FF FF FF FF 01 0A")
            glitter.command.send("53 D1 Car FF FF FF FF FF 01 0A".replace("Car",Car).replaceAll(" ",""))
            var clock = Clock()
            var sensor_ID = []
            var timer = setInterval(function () {
                //console.log('timer')
                var rx = glitter.command.rx
                try {
                    if (rx.length >= 20) {
                        if ((rx.substring(2, 4) === 'E1') && (rx.substring(rx.length - 2, rx.length) === '0A')) {
                            //console.log("command.rx:"+rx)

                            for (var i = 0; i < (rx.length / 20); i++) {
                                var rxText20 = rx.substring(20 * i, 20 + 20 * i)
                                var rx_text = rxText20.replace("53E1"+Car, "")
                                if (sensor_ID.indexOf(rx_text.substring(0, 8)) === -1) {
                                    sensor_ID = sensor_ID.concat(rx_text.substring(0, 8))
                                }
                            }
                        }
                        console.log("carCount:"+carCount+","+sensor_ID.length)
                        // if(carCount===sensor_ID.length){
                        //     console.log("sensor_ID_clock:"+clock.stop())
                        //     callback(sensor_ID)
                        //     clearInterval(timer)
                        // }
                    }
                    if (clock.stop() > 7000) {
                        callback(sensor_ID)
                        clearInterval(timer)
                    }
                } catch (e) {
                    callback(sensor_ID)
                    clearInterval(timer)
                }
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
            var H_Temp = TwoBytes(parseInt(high_temp).toString(16),2)
            var H_Pre = TwoBytes(parseInt(high_pre).toString(16),4)
            var H_Pre_h = H_Pre.substring(0,2)
            var H_Pre_l = H_Pre.substring(2,4)
            var L_Pre = TwoBytes(parseInt(low_pre).toString(16),4)
            var L_Pre_h = L_Pre.substring(0,2)
            var L_Pre_l = L_Pre.substring(2,4)

            glitter.command.send("53 A8 Car Temp H_Pre L_Pre 01 0A".
            replace("Car",Car).replace("Temp",H_Temp).
            replace("H_Pre",H_Pre_h+H_Pre_l).
            replace("L_Pre",L_Pre_h+L_Pre_l).
            replaceAll(" ",""))
            // replace("H_Pre",H_Pre_l+H_Pre_h).
            // replace("L_Pre",L_Pre_l+L_Pre_h).
            // replaceAll(" ",""))

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')
                var rx=glitter.command.rx
                try {
                    if (clock.stop() > 15000) {
                        callback(false)
                        clearInterval(timer)
                    }
                    if (rx.length === 20) {
                        if(rx.substring(2,4)==="B8" && rx.substring(rx.length-2,rx.length)==="0A"){
                            callback(true)
                            clearInterval(timer)
                        }
                    }
                }catch (e){
                    callback(false)
                    clearInterval(timer)}
            }, 200);
        }

        //讀取胎壓胎溫範圍
        this.readUnitRange=function (Car,callback){
            var Data = []
            if(Car==="01"){
                glitter.command.send("5F CD Car FF FF FF FF FF FF 0B".replace("Car",Car).
                replaceAll(" ",""))
            }else{
                glitter.command.send("53 A9 Car FF FF FF FF FF 01 0A".replace("Car",Car).
                replaceAll(" ",""))
            }

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')
                var rx=glitter.command.rx
                try {
                    //15000
                    if (clock.stop() > 2000) {
                        callback(Data)
                        clearInterval(timer)
                    }
                    if (rx.length >= 20) {
                        // if(rx.substring(0,2)==="5B" && rx.substring(rx.length-2,rx.length)==="0B") {
                        var rxData=false
                        if(Car==="01"){
                            rxData=rx.substring(2,4)==="DD" && rx.substring(rx.length-2,rx.length)==="0B"
                        }if(Car==="02"){
                            rxData=rx.substring(2,4)==="B9" && rx.substring(rx.length-2,rx.length)==="0A"
                        }
                        if(rxData) {
                            //if(rx.substring(2,4)==="DD" && rx.substring(rx.length-2,rx.length)==="0B") {
                            var R_Text = rx.substring(0,20)
                            var H_Temp = parseInt(R_Text.substring(6, 8), 16).toString()
                            var H_Pre_h = R_Text.substring(8, 10)
                            var H_Pre_l = R_Text.substring(10, 12)
                            // var H_Pre_l = R_Text.substring(8, 10)
                            // var H_Pre_h = R_Text.substring(10, 12)
                            var H_Pre = parseInt(H_Pre_h + H_Pre_l, 16).toString()
                            var L_Pre_h = R_Text.substring(12, 14)
                            var L_Pre_l = R_Text.substring(14, 16)
                            // var L_Pre_l = R_Text.substring(12, 14)
                            // var L_Pre_h = R_Text.substring(14, 16)
                            var L_Pre = parseInt(L_Pre_h + L_Pre_l, 16).toString()
                            console.log("H_Temp"+H_Temp+",H_Pre"+H_Pre+",L_Pre"+L_Pre)
                            Data=Data.concat(H_Temp)
                            Data=Data.concat(H_Pre)
                            Data=Data.concat(L_Pre)
                            return Data
                        }
                    }
                }catch (e){
                    callback(Data)
                    clearInterval(timer)}
            }, 200);
        }

        //寫入軸的輪數
        this.writeRowWheels=function (Car,row1,row2,row3,row4,callback){
            glitter.command.send("53 AA Car row1 row2 row3 row4 FF 01 0A".
            replace("Car",Car).replace("row1", row1).
            replace("row2", row2).replace("row3", row3).
            replace("row4", row4).replace(" ", ""))

            var clock=Clock()
            var timer = setInterval(function() {
                var rx=glitter.command.rx
                try {
                    if (clock.stop() > 1500) {
                        callback(false)
                        clearInterval(timer)
                    }
                    if (rx.length >= 20) {
                        if(rx.substring(2,4)==="BA" && rx.substring(rx.length-2,rx.length)==="0A"){
                            callback(true)
                            clearInterval(timer)
                        }
                    }
                }catch (e){
                    callback(false)
                    clearInterval(timer)}
            }, 200);
        }

        //讀取軸的輪數
        this.readRowWheels=function (Car,callback){
            glitter.command.send("53 AB Car FF FF FF FF FF 01 0A".replace("Car",Car)
                .replaceAll(" ", ""))

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')
                var rx=glitter.command.rx
                try {
                    //2000
                    if (clock.stop() > 2000) {
                        callback("false")
                        clearInterval(timer)
                    }
                    //console.log("command.rx:"+rx)
                    if (rx.length === 20) {
                        if(rx.substring(2,4)==="BB" && rx.substring(rx.length-2,rx.length)==="0A"){

                            callback(rx.substring(6,14))
                            clearInterval(timer)
                        }
                    }
                }catch (e){
                    callback("false")
                    clearInterval(timer)}
            }, 200);
        }

        //專屬於App的指令
        this.exclusive=function (Car,callback){
            glitter.command.send("5F BC Car FF FF FF FF FF 01 0B".replace("Car",Car)
                .replaceAll(" ", ""))

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')

                try {
                    if (clock.stop() > 500) {
                        callback("true")
                        clearInterval(timer)
                    }
                }catch (e){
                    callback("false")
                    clearInterval(timer)}
            }, 200);
        }

        //開啟胎紋深度
        this.Tread_depth_Open=function (callback){
            glitter.command.send("04 00 00"
                .replaceAll(" ", ""),"Tread_depth")

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')

                try {
                    if (clock.stop() > 1000) {
                        callback("true")
                        clearInterval(timer)
                    }
                }catch (e){
                    callback("false")
                    clearInterval(timer)}
            }, 200);
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



