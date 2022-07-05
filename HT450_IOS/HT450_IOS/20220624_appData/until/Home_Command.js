glitter.startHomeCommand = function () {
    function startHT450(){
        //讀取輪軸、全部感測器ID、警報值、全部感測器狀態
        function startTruck(car,callback){
            readCarRow(car,function () {
                readAllID(car,function () {
                    readTPMS(car,function () {
                        readAllStatus(car,function () {
                            callback()
                        })
                    })
                })
            })
        }
        function startSyncTime(callback){
            sync_time("00",function (){
                sync_time("01",function (){
                    callback()
                })
            })
        }

        function startCommad(){
            readfirmware("02",function () {
                //前車成功，才能換後車
                readCarID("01",function (){
                    readCarID("02",function (result){
                        if(result==="false"){
                            truckRear_Enable=false
                        }else{
                            truckRear_Enable=true
                        }
                        console.log("readCarID:"+result)
                        //顯示卡車與狀態
                        startTruck("01",function (){
                            if(truckRear_Enable){
                                startTruck("02",function (){
                                    //時間同步
                                    startSyncTime(function (){
                                        glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                                    })
                                })
                            }else{
                                //時間同步
                                startSyncTime(function (){
                                    glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                                })
                            }
                        })
                    })
                })
            })
        }

        glitter.app = "HT430"
        //readfirmware("00",function () {
        readfirmware("01",function (result){
            console.log("readfirmware:"+result+","+glitter.app)
            if(result==="false"){
                glitter.app = "HT450"
                glitter.home_init()

                readfirmware("01",function (result){
                    startCommad()
                })
            }else{
                glitter.home_init()
                startCommad()
            }

        })
        //})
    }

    function startHT471A(){
        readfirmware("02",function () {
            readCarID("02",function (){
                readCarRow("02",function () {
                    readAllID("02",function () {
                        readTPMS("02",function () {
                            readAllStatus("02",function () {
                                sync_time("01",function (){
                                    readConnectRSSI("02")
                                })
                            })
                        })
                    })
                })
            })
        })
    }

    //glitter.playSound("false")
    glitter.openDiaLog('dialog/Dia_Progress_Spinner.html', 'Dia_Progress_Spinner', false, false)
    if(glitter.app==="HT430"){
        setTimeout(startHT450,2000)
    }else if(glitter.app==="HT471A"){
        setTimeout(startHT471A,2000)
    }
}

//讀取韌體版本：00主機,01前車,02後車
function readfirmware(car,callback){
    glitter.command.readfirmware(
        car,
        function (result) {

            if(result==="false"){
                //**
                //glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                glitter.showToast("firmware Command Fail")
                //readAllCommand()
            }else{
                //glitter.closeDiaLog()
                glitter.share.home.updateUI(car,glitter.updateData.FIRMWARE2,result)
            }
            //{mode_data:"",result_data:"",logdata:true}
            glitter.updateMemmory({mode_data:"讀取-韌體版本"+car,result_data:(result!=="false" ? "success":"fail")})

            //if(result!=="false"){
            callback(result)
            //}
        })
}

//讀取卡車ID：前車01,後車02
function readCarID(car,callback){
    glitter.command.readCarID(
        car,
        function (result) {

            if(result==="false"){
                //**
                //glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                glitter.showToast("CarID Command Fail")
            }else{
                glitter.share.home.updateUI(car,glitter.updateData.CAR_ID,result)
            }
            //{mode_data:"",result_data:"",logdata:true}
            glitter.updateMemmory({mode_data:"讀取-板車ID"+car,result_data:(result!=="false" ? "success":"fail")})

            //if(result!=="false"){
            callback(result)
            //}
        })
}

function readCarRow(car,callback){

    glitter.command.readRowWheels(
        car,
        function (result) {
            console.log("carRow:"+result)
            //result = "02000004"

            if(result==="false"){
                //**
                //glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                glitter.showToast("CarRow Command Fail")
            }else{
                glitter.share.home.updateUI(car,glitter.updateData.TIRE_ROW,result)
            }

            glitter.updateMemmory({mode_data:"讀取-輪軸"+car,result_data:(result!=="false" ? "success" : "fail")})

            //if(result!=="false"){
            callback()
            //}

        })
}

function readAllID(car,callback){

    glitter.command.readAllSensorID(car,16,function (read_data) {
        console.log("ble-" + read_data)

        if (read_data.length===0 || read_data[0]=== "false") {
            //glitter.closeDiaLog()
            glitter.showToast("SensorID Command Fail")
        } else {
            glitter.share.home.updateUI(car,glitter.updateData.SENSOR_ID,read_data)
        }

        //"讀取-輪胎ID",(read_data.length===0 || read_data[0]=== "false" ? "fail" : "success")
        glitter.updateMemmory({mode_data:"讀取-輪胎ID"+car,result_data:(read_data.length===0 || read_data[0]=== "false" ? "fail" : "success")})

        //if(read_data.length!==0 || read_data[0]!== "false"){
        callback()
        //}
    })

}

function readTPMS(car,callback){

    glitter.command.readUnitRange(car,function (data) {

        if(data[0] === "false"){
            //glitter.closeDiaLog()
            console.log("readTPMS:"+JSON.stringify(data))
            glitter.showToast("UnitRange Command Fail")
        }else{
            glitter.share.home.updateUI(car,glitter.updateData.TPMS,data)
        }
        //"讀取-警報值",(data.length===0 ? "fail" : "success")
        glitter.updateMemmory({mode_data:"讀取-警報值"+car,result_data:(data.length===0 ? "fail" : "success")})

        //if(data.length !== 0){
        callback()
        //}
    })
}

function readAllStatus(car,callback){

    glitter.command.readAllTireStatus(car,16,function (read_data) {
        console.log("ble-" + read_data)

        if (read_data[0] === "false") {
            //glitter.closeDiaLog()
            glitter.showToast("TireStatus Command Fail")
        } else {
            glitter.share.home.updateUI(car,glitter.updateData.TIRE_STATUS,read_data)
        }

        //"讀取-胎溫胎壓狀態",(read_data[0] === "false" ? "fail" : "success")
        glitter.updateMemmory({mode_data:"讀取-胎溫胎壓狀態"+car,result_data:(read_data[0] === "false" ? "fail" : "success")})

        //if (read_data[0] !== "false") {
        callback()
        //}
    })

}

//時間同步：前車00,後車01
function sync_time(car,callback){
    glitter.command.sync_time(
        car,
        function (result) {

            if(!result){
                //**
                //glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                glitter.showToast("Command Fail")
            }else{

            }
            //"讀取-連線藍芽強度",(result === "false" ? "fail" : "success")
            glitter.updateMemmory({mode_data:"時間校正"+car,result_data:(result ? "success" : "fail")})
            //if (result) {
            callback()
            //}
        })
}

function readConnectRSSI(car){

    glitter.command.readConnectRSSI(
        car,
        function (result) {

            if(result==="false"){
                //**
                glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                glitter.showToast("Command Fail")
            }else{
                //glitter.closeDiaLog()
                glitter.closeDiaLogWithTag('Dia_Progress_Spinner')
                glitter.share.home.updateUI(car,glitter.updateData.CONNECT_RSSI,result)

            }
            //"讀取-連線藍芽強度",(result === "false" ? "fail" : "success")
            glitter.updateMemmory({mode_data:"讀取-連線藍芽強度",result_data:(result === "false" ? "fail" : "success")})

        })
}