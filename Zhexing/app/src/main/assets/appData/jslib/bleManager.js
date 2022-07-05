var clock = new Clock()
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
var lastRandom=getRandomInt(50,100)
function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min) + min); //The maximum is exclusive and the minimum is inclusive
}
glitter.runJsInterFace("Glitter_BLE_SetCallBack",{},function (response){
    switch (response.function){
        case "needGPS":
            console.log("需要開啟定位來掃描藍芽")
            break
        case "onConnectFalse":
            console.log("藍芽連線失敗")
            break
        case "onConnectSuccess":
            console.log("藍芽連線成功")
            break
        case "onConnecting":
            console.log("藍芽連線中")
            break
        case "onDisconnect":
            console.log("藍芽斷線")
            break
        case "requestPermission":
            glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, '請允許定位權限來打開藍牙',function () {
                glitter.requestPermission(response.data)
            })
            console.log("權限不足"+JSON.stringify(response.data))
            break
        /**
         * readHEX,readBytes,readUTF
         * */
        case "rx":
            var data=response.data
            console.log("bleMessage_rx:"+data.readHEX)
            glitter.command.rx+=data.readHEX
            switch (glitter.selectFunction){
                case "trigger":
                    glitter.share.bleUtil.disConnect()
                    glitter.getGPS(function (result){
                        sensorDataRefresh(data,result,'trigger',0)
                    })
                    break
                case "obd":
                    if(data.readHEX.substring(0,2)==='51'){
                        if(glitter.share.change51!==undefined){
                            glitter.share.change51(data.readHEX)
                        }
                    }
                    break
            }
            console.log("收到藍芽資料"+response.data.readHEX)
            break
        case "tx":
            console.log("傳送藍芽資料"+response.data.readHEX)
            break
        /**
         * deviceList:[device,device.....]
         * device:{name,address,readHEX,readBytes,readUTF}
         * 回條每秒所偵測到的所有藍芽，使用address去做藍芽連線
         * */
        case "scanBack":
            // console.log('scanBack_checker')
            response.device.map(function (device){
                if(glitter.bleList===undefined){
                    glitter.bleList=[]
                }

                // console.log(JSON.stringify(device))
                if((device.name!==undefined)&&((device.name.toLowerCase().indexOf("ble_")!==-1)||(device.name.toLowerCase().indexOf("orange")!==-1)||(device.name.toLowerCase().indexOf("sunlit")!==-1))){
                    if(glitter.bleList.filter(function (data){
                        return data.name===device.name
                    }).length===0){
                        glitter.bleList=glitter.bleList.concat(device)
                    }
                }else{
                    if(glitter.selectFunction!=='obd'){
                        if(device["serviceUUID"]==="B54ADC00-67F9-11D9-9669-0800200C9A66" ||
                            device["serviceUUID"]==="FB349B5F-8000-0080-0010-000022770000" ||
                            device["serviceUUID"]==="E3537AB1-3BA5-4A88-B388-AB8056ADC27B"
                        ){
                            console.log('IOS_裝置名稱'+device.name+" 裝置資料:"+device.readHEX)
                            glitter.getGPS(function (result){
                                sensorDataRefresh(device,result,'scanBack',device.rssi,device.name)
                            })
                        }else if((device.name!==undefined)&&(device.name.indexOf('BLERF')!== -1 || device.name.indexOf('OB')!== -1 || device.name.length===6
                            ||(device.readHEX.substring(10,42)=="669A0C2000086996D911F96700DC4AB5" ||
                               device.readHEX.substring(10,42)=="0000772200001000800000805F9B34FB" ||
                               device.readHEX.substring(10,42)=="7BC2AD5680AB88B3884AA53BB17A53E3"
                            ))){
                            glitter.getGPS(function (result){
                                sensorDataRefresh(device,result,'scanBack',device.rssi,device.name)
                            })
                        }

                    }

                }
            })
            break
    }
})






function readInt(array) {
    var value = 0;
    for (var i = 0; i < array.length; i++) {
        value = (value * 256) + array[i];
    }
    return value;
}


glitter.share.lastBleConnection=undefined
glitter.getPro("lastConnect",function (data){
    if(data.data!==undefined){
        glitter.share.lastBleConnection=data.data
    }
})
//迴圈判斷藍芽是否連線
if(glitter.deviceType!==glitter.deviceTypeEnum.Web){
    setInterval(timer,5000)
}
var diaopen=false
function timer() {
    glitter.runJsInterFace("Glitter_BLE_NeedPermission", {}, function (response) {
        if(response.result){
            if(diaopen){return}
            diaopen=true
            glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(721),function () {
                glitter.runJsInterFace("Glitter_BLE_StopScan", {}, function (response) {})
                diaopen=false
                glitter.runJsInterFace("Glitter_BLE_StartScan", {}, function (response) {})
            })
        }else{
            glitter.runJsInterFace("Glitter_BLE_IsOpen", {}, function (response) {
                if(!response.result){
                    glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, glitter.getLan(720),function () {
                        glitter.runJsInterFace("Glitter_BLE_StopScan", {}, function (response) {})
                        diaopen=false
                    })
                }else{
                    glitter.runJsInterFace("Glitter_BLE_IsDiscovering", {}, function (response) {
                        if(!response.result){
                            glitter.runJsInterFace("Glitter_BLE_StartScan", {}, function (response) {})
                        }
                    })
                }
            })
        }
    })
}

var date=new Date()
//資料解析
//0201061107669A0C2000086996D911F96700DC4AB508FF0000000001DABD0000000000000000000000000000000000000000000000000000000000000000
function sensorDataRefresh(scanRecord,result,type,rssi,device){
    //Sensor類型
    var caseType="default"
    if(glitter.deviceType===glitter.deviceTypeEnum.Ios){
        scanRecord.readHEX="10FF"+scanRecord.readHEX
    }else{
        if(scanRecord.readHEX.indexOf('10FF')!==-1){
            scanRecord.readHEX=scanRecord.readHEX.substring(scanRecord.readHEX.indexOf('10FF'))
        }else if(scanRecord.readHEX.indexOf('13FF')!==-1){
            scanRecord.readHEX=scanRecord.readHEX.substring(scanRecord.readHEX.indexOf('13FF'))
        }else if(scanRecord.readHEX.indexOf('11FF')!==-1){
            scanRecord.readHEX=scanRecord.readHEX.substring(scanRecord.readHEX.indexOf('11FF'))
        }else{
            scanRecord.readHEX=scanRecord.readHEX.substring(scanRecord.readHEX.indexOf('12FF'))
        }
    }
    if(scanRecord["serviceUUID"]==="B54ADC00-67F9-11D9-9669-0800200C9A66"){
        scanRecord.readHEX=`FFFFFFFFFF669A0C2000086996D911F96700DC4AB5${scanRecord.readHEX}`
        console.log("IOS_轉換後"+scanRecord.readHEX)
    }
    if(scanRecord["serviceUUID"]==="FB349B5F-8000-0080-0010-000022770000"){
        scanRecord.readHEX=`FFFFFFFFFF0000772200001000800000805F9B34FB${scanRecord.readHEX}`
        console.log("IOS_轉換後"+scanRecord.readHEX)
    }
    if(scanRecord["serviceUUID"]==="E3537AB1-3BA5-4A88-B388-AB8056ADC27B"){
        scanRecord.readHEX=`FFFFFFFFFF7BC2AD5680AB88B3884AA53BB17A53E3${scanRecord.readHEX}`
        console.log("IOS_轉換後"+scanRecord.readHEX)
    }

    if((scanRecord.readHEX.substring(10,42)=="669A0C2000086996D911F96700DC4AB5" ||
        scanRecord.readHEX.substring(10,42)=="0000772200001000800000805F9B34FB" ||
        scanRecord.readHEX.substring(10,42)=="7BC2AD5680AB88B3884AA53BB17A53E3"
    )){
          console.log('isGogoRo_sensorDataRefresh')
        caseType='gogoro'
    }else{
        if(device!==undefined){console.log(`${device} ScanResult:${scanRecord.readHEX}`)}
        if((scanRecord.readHEX.substring(4,6)==='B8')||(scanRecord.readHEX.substring(4,6)==='E6')){
            scanRecord.readHEX=(scanRecord.readHEX.substring(0,6)+glitter.deCode(scanRecord.readHEX.substring(6,38)))
        }
        //For Artc
        if(scanRecord.readHEX.substring(4,6)==='C7'){
            scanRecord.readHEX=scanRecord.readHEX.substring(0,6)+device+scanRecord.readHEX.substring(6,scanRecord.readHEX.length)
            caseType="artc"
        }
        //For OJ
        if(scanRecord.readHEX.substring(4,6)==='A7'){
            caseType="oj"
        }
    }

    //壓力單位
    var pre=glitter.print(function (){
        if(["artc","oj"].indexOf(caseType)!==-1){
            var value=((parseInt(scanRecord.readHEX.substring(14,18), 16)/10) - 14.6) * 6.89
            if(value<0){value=0}
           return value;
        }else if(caseType==='gogoro'){
            return  parseInt(scanRecord.readHEX.substring(48,52), 16);
        }else{
           return  parseInt(scanRecord.readHEX.substring(20,24), 16);
        }
    })
    console.log(`preData--${scanRecord.readHEX.substring(6,12)}-${scanRecord.readHEX.substring(14,18)}-${pre}`)
    //溫度單位
    var tem=glitter.print(function (){
        if(["artc","oj"].indexOf(caseType)!==-1){
            return  parseInt(scanRecord.readHEX.substring(18,20), 16);
        }else if(caseType==='gogoro'){
            return  parseInt(scanRecord.readHEX.substring(46,48), 16)-55;
        }else{
            return  parseInt(scanRecord.readHEX.substring(24,26), 16)-55;
        }
    })

    if(pre===-55){return}
    var sensorData={}
    if(caseType==='gogoro'){
        sensorData.id=scanRecord.readHEX.substring(54,60)
        sensorData.company=caseType
        //電壓
        sensorData.v=`${parseInt(scanRecord.readHEX.substring(52,54),16)}%`
    }else{
        sensorData.id=scanRecord.readHEX.substring(6,12)
    }
    console.log(`cutResult-${caseType}-id:${sensorData.id}--pre:${pre}--tem:${tem}\n${scanRecord.readHEX}`)
    if(glitter.ratioCaluate.triggerMap[sensorData.id]===undefined){
        glitter.ratioCaluate.triggerMap[sensorData.id]=1
    }else{
        glitter.ratioCaluate.triggerMap[sensorData.id]+=1
    }
    if(glitter.type===glitter.deviceTypeEnum.Android){
        sensorData.data_name=glitter.data_name
    }
    if(["artc","oj"].indexOf(caseType)!==-1){
        //電壓
        sensorData.v=parseInt(scanRecord.readHEX.substring(12,14),16)/10
        //是否正在行駛或轉動
        sensorData.isRunning=scanRecord.readHEX.substring(20,22)==="01"
        //X軸加速度
        sensorData.xSpeed=parseInt(scanRecord.readHEX.substring(24,28),16)
        //z軸加速度
        sensorData.zSpeed=parseInt(scanRecord.readHEX.substring(28,32),16)
        //Sensor類型，依照配合廠商
        sensorData.company=caseType
    }
    sensorData.pre=pre
    sensorData.tem=tem
    sensorData.lan=result.latitude
    sensorData.lon=result.longitude
    sensorData.address=result.address
    sensorData.type=type
    sensorData.rssi=(''+rssi)
    sensorData.data=scanRecord.readHEX
    sensorData.time=(new Date()).format("yyyy-MM-dd hh:mm:ss").replace(".0","")

    if((sensorData.pre===0)&&(sensorData.tem===0)){return;}
    if((sensorData.tem>-40) && (sensorData.tem<115)){
    glitter.serialUtil.storeObject(sensorData,sensorData.id,'sensorData',function (data) {},function () {})}
    waitUpload=waitUpload.concat(sensorData)
    console.log('收到廣播封包'+scanRecord.readHEX+"--pre="+scanRecord.readHEX.substring(20,24)+"/"+pre+"--"+scanRecord.readHEX.substring(24,26)+"tem="+tem)
    console.log('收到Sensor資料'+JSON.stringify(sensorData))
    glitter.share.memoryData=[sensorData].concat(glitter.share.memoryData)
    if(glitter.share.memoryData.length>200){
        glitter.share.memoryData.splice(199,1)
    }
}
var waitUpload=[]
setInterval(function (){
    waitUpload.map(function (dat){
        if(glitter.sensorMap[dat.id]===undefined){
            waitUpload=waitUpload.filter(function (data){return data.id===dat.id})
            return
        }
        dat.count=glitter.checkCarName
        dat.plateNumber=glitter.sensorMap[dat.id].plateNumber
        dat['wh']=glitter.sensorMap[dat.id]['wh']
        apiRequest.postRequest({
            request:'storeData',
            data:JSON.stringify(dat),
            plateNumber:dat.plateNumber
        },function (){
            waitUpload=waitUpload.filter(function (data){return data.id===dat.id})
        },function (){})
    })
},10*1000)
// 收到廣播封包0201061107669A0C2000086996D911F96700DC4AB508FF0000000001DABD0000000000000000000000000000000000000000000000000000000000000000