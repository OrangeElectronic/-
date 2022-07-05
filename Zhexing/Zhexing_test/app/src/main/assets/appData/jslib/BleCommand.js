"use strict";
class BleCommand{
    constructor() {
        this.rx = ''
        this.onSend=false
        this.onReading=false
        this.timerWainting=[]
        this.clearTimer=function (){
            glitter.command.timerWainting.map(function (data){
                clearInterval(data)
            })
        }
        this.send = function (data) {
            if(this.onSend){return}
            this.onSend=true
            setTimeout(function (){
                glitter.command.onSend=false
            },50)
            glitter.share.bleUtil.isConnect({
                callback:function (response){
                    if(response.result){
                        var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss:S");
                        glitter.txmemory+=`<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">MyTx->${data}</p>
                                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">`+timeInMs+`</p>`
                        console.log('MyTx->'+data)
                        glitter.command.rx = ''
                        // e093f3b5-00a3-a9e5-9eca-40016e0edc24
                        // e093f3b5-00a3-a9e5-9eca-40016e0edc24
                        if(glitter.deviceType===glitter.deviceTypeEnum.Android){
                            glitter.share.bleUtil.writeHex({
                                data:{
                                    data:data.replace(/ /g, ''),
                                    txChannel:"00008D82-0000-1000-8000-00805F9B34FB",
                                    rxChannel:"00008D81-0000-1000-8000-00805F9B34FB"
                                }
                            })
                        }else{
                            glitter.share.bleUtil.writeHex({
                                data:{
                                    data:data.replace(/ /g, ''),
                                    txChannel:"8D82",
                                    rxChannel:"8D81"
                                }
                            })
                        }

                    }else{
                        glitter.openDiaLog('dialog/Dia_Reconnect.html','Dia_Reconnect',false,false,{})
                    }
                }
            })
        }
        //寫入OBD Sensor
        this.writeSensor=function (data,callback){
            var clock = Clock()
            var wh=`${parseInt(data.wh)-1}`
            var id=data.id
            while(id.length<6){
                id=`0${id}`
            }
            glitter.command.send(`0${wh}${id}`.replace(/ /g, ""), true)
            let timer = setInterval(function () {
                let rx = glitter.command.rx
                try {
                    if (clock.stop() > 2000) {
                        callback(false)
                        clearInterval(timer)
                    }
                    if (rx==="4F4B") {
                        callback(true)
                        clearInterval(timer)
                    }
                } catch (e) {
                    callback(false)
                    clearInterval(timer)
                }
            }, 200);
        }
        //讀取OBD Sensor
        this.canRead=true
        this.readSensor=function (callback,length){
            if(  glitter.command.onReading||(!this.canRead)){return}
            glitter.command.send("24 00 00 25".replace( / /g, ""), true)
            this.onReading=true
            var clock = Clock()
            var timer = setInterval(function () {
                var rx = glitter.command.rx
                try {
                    if (clock.stop() > 2000) {
                        callback(false)
                        glitter.command.onReading=false
                        clearInterval(timer)
                    }
                    if (rx.length === ((length*30+6))) {
                        var sensorList=rx.substring(4,304)
                        var sensorMap={}
                        var sensorListData={
                            data:[],
                            time:(new Date()).format("yyyy-MM-dd hh:mm:ss").replace(".0","")
                        }
                        for(var a=0;a<length;a++){
                           var sensorData=sensorList.substring(a*30,a*30+30)
                            let mapData={
                                id:sensorData.substring(2,8),
                                pre:parseInt(sensorData.substring(16,20), 16),
                                tem:parseInt(sensorData.substring(20,22),16)-55,
                                time:(new Date()).format("yyyy-MM-dd hh:mm:ss").replace(".0",""),
                                data:sensorData
                            }
                            console.log("readSensor:"+JSON.stringify(mapData))
                            sensorMap[sensorData.substring(2,8)]=mapData
                            if((mapData.tem>-40) && (mapData.tem<115)){
                                glitter.serialUtil.storeObject(mapData,mapData.id,'sensorData',function (data) {},function () {})
                            }
                            sensorListData.data=sensorListData.data.concat(mapData)
                        }
                        glitter.obdData.data=glitter.obdData.data.concat(sensorListData)
                        glitter.obdData.callback(sensorListData,false)
                        callback(true)
                        glitter.command.onReading=false
                        clearInterval(timer)
                    }
                } catch (e) {
                    callback(false)
                    glitter.command.onReading=false
                    clearInterval(timer)
                }
            }, 200);
        }
        //讀取加速度
        this.readSpeed=function (callback){
            var pos=0
            var error=0
            var sensorListDataAll=[]
            function readerCommand(){
                if(pos===16){
                    callback({
                        result:true,
                        data:sensorListDataAll
                    })
                    return
                }
                if(error===5){
                    callback({
                        result:false
                    })
                    return
                }
                var hex=(pos).toString(16)
                while (hex.length<4){
                    hex=`0${hex}`
                }
                glitter.command.send(`30  ${hex} 31`.replace(/ /g, ""), true)
                var clock = Clock()
                var timer = setInterval(function () {
                    var rx = glitter.command.rx
                    try {
                        if (clock.stop() > 4000) {
                            error++
                            readerCommand()
                            glitter.command.timerWainting=glitter.command.timerWainting.filter(function (data){
                                return data!==timer
                            })
                            clearInterval(timer)
                        }
                        if (rx.length === ((pos===15)?(86*70):(86*50))) {
                           // rx=rx.substring(4)
                            var sensorListData=[]
                            for(var i=0;i<((pos===15)?(70):(50));i++){
                                var sensorList=rx.substring((4+i*86))
                                for(var a=0;a<10;a++){
                                    var data=sensorList.substring(a*8,a*8+8)
                                    sensorListData=sensorListData.concat(data)
                                }
                            }
                            sensorListDataAll=sensorListDataAll.concat(sensorListData)
                            pos++
                            glitter.command.rx=""
                            readerCommand()
                            glitter.command.timerWainting=glitter.command.timerWainting.filter(function (data){
                                return data!==timer
                            })
                            clearInterval(timer)
                        }
                    } catch (e) {
                        error++
                        readerCommand()
                        glitter.command.timerWainting=glitter.command.timerWainting.filter(function (data){
                            return data!==timer
                        })
                        clearInterval(timer)
                    }
                }, 200);
                glitter.command.timerWainting.push(timer)
            }
            readerCommand()
        }
        //讀取加速度
        this.readSpeed2=function (callback){
            var pos=0
            var error=0
            var sensorListDataAll=[]
            function readerCommand(){
                if(pos===32){
                    callback({
                        result:true,
                        data:sensorListDataAll
                    })
                    return
                }
                if(error===5){
                    callback({
                        result:false
                    })
                    return
                }
                var hex=(pos).toString(16)
                while (hex.length<4){
                    hex=`0${hex}`
                }

                glitter.command.send(`32  ${hex} 33`.replace(/ /g, ""), true)
                var clock = Clock()
                var timer = setInterval(function () {
                    var rx = glitter.command.rx
                    try {
                        if (clock.stop() > 4000 ) {
                            error++
                            readerCommand()
                            glitter.command.timerWainting=glitter.command.timerWainting.filter(function (data){
                                return data!==timer
                            })
                            clearInterval(timer)
                        }
                        if (rx.length === 86*50) {
                            var sensorListData=[]
                            for(var i=0;i<50;i++){
                                var sensorList=rx.substring((4+i*86))
                                for(var a=0;a<10;a++){
                                    var data=sensorList.substring(a*8,a*8+8)
                                    sensorListData=sensorListData.concat(data)
                                }
                            }
                            sensorListDataAll=sensorListDataAll.concat(sensorListData)
                            pos++
                            glitter.command.rx=""
                            readerCommand()
                            glitter.command.timerWainting=glitter.command.timerWainting.filter(function (data){
                                return data!==timer
                            })
                            clearInterval(timer)
                        }
                    } catch (e) {
                        error++
                        readerCommand()
                        alert(e.message)
                        glitter.command.timerWainting=glitter.command.timerWainting.filter(function (data){
                            return data!==timer
                        })
                        clearInterval(timer)
                    }
                }, 200);
                glitter.command.timerWainting.push(timer)
            }
            readerCommand()
        }
        //靜態接收率
        this.readRatio=function (callback){
            var counter=0
            var sensorListDataAll=[]
            var bleData=""
            function sender(coun){
                glitter.command.send(`34000${coun}35`.replace(/ /g, ""), true)
                var clock = Clock()
                var timer =setInterval(function (){
                    var rx=glitter.command.rx
                    if(clock.stop() > 4000){
                        callback(false)
                        clearInterval(timer)
                    }
                    if(rx.length===86*50){
                        var sensorListData=[]
                        for(var i=0;i<50;i++){
                            var sensorList=rx.substring((4+i*86))
                            for(var a=0;a<10;a++){
                                var data=sensorList.substring(a*8,a*8+8)
                                sensorListData=sensorListData.concat(data)
                            }
                        }
                        bleData+=rx
                        bleData+="\n\n\n"
                        sensorListDataAll=sensorListDataAll.concat(sensorListData)
                        if(counter===2){
                            callback({
                                result:true,
                                data:sensorListDataAll,
                                bleData:bleData
                            })
                        }else{
                            sender(counter+=1)
                        }
                        clearInterval(timer)
                    }
                },200)
            }
            sender(0)
        }
        this.checkReadSpeed=function (callback){
            glitter.command.send(`2627`.replace(/ /g, ""), true)
            var clock = Clock()
            let timer=setInterval(function (){
                var rx=glitter.command.rx
                if(clock.stop()>1000){
                    callback(false)
                    clearInterval(timer)
                }
                if(rx.length===4){
                    callback(ConvertBase.hex2dec(rx.substring(2,4)+rx.substring(0, 2)))
                    clearInterval(timer)
                }
            },100)
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

var ConvertBase = function (num) {
    return {
        from: function (baseFrom) {
            return {
                to: function (baseTo) {
                    return parseInt(num, baseFrom).toString(baseTo);
                }
            };
        }
    };
};

// binary to decimal
ConvertBase.bin2dec = function (num) {
    return ConvertBase(num).from(2).to(10);
};

// binary to hexadecimal
ConvertBase.bin2hex = function (num) {
    return ConvertBase(num).from(2).to(16);
};

// decimal to binary
ConvertBase.dec2bin = function (num) {
    return ConvertBase(num).from(10).to(2);
};

// decimal to hexadecimal
ConvertBase.dec2hex = function (num) {
    return ConvertBase(num).from(10).to(16);
};

// hexadecimal to binary
ConvertBase.hex2bin = function (num) {
    return ConvertBase(num).from(16).to(2);
};

// hexadecimal to decimal
ConvertBase.hex2dec = function (num) {
    return ConvertBase(num).from(16).to(10);
};

function hexToUtf8(s) {
    return decodeURIComponent(
        s.replace(/\s+/g, '') // remove spaces
            .replace(/[0-9a-f]{2}/g, '%$&') // add '%' before each 2 characters
    );
}