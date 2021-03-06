"use strict";

class BleCommand {
    constructor() {
        this.txmemory = ''
        this.rx = ''
        this.onSend = false
        this.send = function (data) {
            glitter.command.rx = ''
            glitter.share.bleUtil.isConnect({
                callback: function (response) {
                    data = data.replace(/ /g, "")
                    if (response.result) {
                        if (glitter.deviceType === glitter.deviceTypeEnum.Android) {
                            glitter.share.bleUtil.writeHex({
                                data: {
                                    data: data.replaceAll(' ', ''),
                                    txChannel: "00008D82-0000-1000-8000-00805F9B34FB",
                                    rxChannel: "00008D81-0000-1000-8000-00805F9B34FB"
                                }
                            })
                        } else {
                            console.log("sendIosMessage:" + data)
                            //IOS長度大於350需要做切割封包
                            let spi = 350
                            if (data.length > spi) {
                                var long = 0
                                if (data.length % spi === 0) {
                                    long = data.length / spi
                                } else {
                                    long = (parseInt(data.length / spi, 10) + 1)
                                }
                                var sendIndex = 0

                                function sendIosMessage() {
                                    if (sendIndex === (long - 1)) {
                                        console.log("sendIosMessage:long-1")
                                        glitter.share.bleUtil.writeHex({
                                            data: {
                                                data: ("87" + data.substring(sendIndex * spi, data.length) + "78"),
                                                txChannel: "8D82",
                                                rxChannel: "8D81"
                                            }
                                        })
                                    } else {
                                        var slon = ConvertBase.dec2hex(long)
                                        if (slon.length < 2) {
                                            slon = "0" + slon
                                        }
                                        glitter.share.bleUtil.writeHex({
                                            data: {
                                                data: ("87" + slon + data.substring(sendIndex * spi, sendIndex * spi + spi)),
                                                txChannel: "8D82",
                                                rxChannel: "8D81"
                                            }
                                        })
                                        sendIndex += 1
                                        setTimeout(function () {
                                            sendIosMessage()
                                        }, 100)
                                    }
                                }

                                sendIosMessage()
                            } else {
                                glitter.share.bleUtil.writeHex({
                                    data: {
                                        data: (data),
                                        txChannel: "8D82",
                                        rxChannel: "8D81"
                                    }
                                })
                            }
                        }
                    }
                }
            })
        }
        //寫入OBD Sensor
        this.writeSensor = function (data, callback) {
            var clock = Clock()
            var wh = `${parseInt(data.wh) - 1}`
            var id = data.id
            while (id.length < 6) {
                id = `0${id}`
            }
            glitter.command.send(`0${wh}${id}`.replaceAll(" ", ""), true)
            let timer = setInterval(function () {
                let rx = glitter.command.rx
                try {
                    if (clock.stop() > 2000) {
                        callback(false)
                        clearInterval(timer)
                    }
                    if (rx === "4F4B") {
                        callback(true)
                        clearInterval(timer)
                    }
                } catch (e) {
                    callback(false)
                    clearInterval(timer)
                }
            }, 200);
        }
        /*
        * 讀取Sensor
        * map{id,idCount,bat,kpa,c,vol}
        * */
        this.readSensor = function (callback) {
            glitter.command.cancel = false
            var hex = glitter.mmyInterFace.selectMMY["OBD Product No. (hex)"]
            var lf = glitter.mmyInterFace.selectMMY["LF Power"]
            if (hex.length === 4) {
                hex = hex.substring(2, 4)
            }
            if (lf.length > 2) {
                lf = (`0` + lf)
            }

            function send() {
                glitter.command.send(glitter.command.toCrc(`0A 10 000E 01 00 ${lf} ${hex} 00 00 00 00 00 00 00 00 39 F5`.replace(/ /g, "")))
            }

            glitter.command.commandStart(function () {
                send()
                setTimeout(function () {
                    startRead()
                }, 200)
            })

            function startRead() {
                var clock = Clock()
                var timer = setInterval(function () {
                    console.log('timer')
                    var rx = glitter.command.rx
                    try {
                        if (clock.stop() > 20000 || (rx.indexOf("F51C000301000A") !== -1) || (rx.indexOf("F51C000302000A") !== -1) || (rx.indexOf("F51C000301EB0A") !== -1)) {
                            glitter.command.commandFinish(function () {
                                callback(false)
                            })
                            if(clock.stop() > 20000){
                                switch (glitter.selectFunction.select){
                                    case glitter.selectFunction.enum.IdCopy:{
                                        glitter.mysqlDataBase.insertCopy({
                                            error:`6-1-0`,
                                            exTime:clock.stop()
                                        })
                                        break
                                    }
                                    case glitter.selectFunction.enum.IdCopy_OBD:{
                                        glitter.mysqlDataBase.insertCopy({
                                            error:`6-1-0`,
                                            exTime:clock.stop()
                                        })
                                        break
                                    }
                                    default:{
                                        glitter.mysqlDataBase.insertTrigger({error:'noResponse',id:'NA',exTime:clock.stop(),
                                            tem:'NA',
                                            pre:'NA',
                                            bat:'NA'})
                                        break
                                    }
                                }
                            }else{
                                switch (glitter.selectFunction.select){
                                    case glitter.selectFunction.enum.IdCopy:{
                                        glitter.mysqlDataBase.insertCopy({
                                            error:`6_noResponse`,
                                            exTime:clock.stop()
                                        })
                                        break
                                    }
                                    case glitter.selectFunction.enum.IdCopy_OBD:{
                                        glitter.mysqlDataBase.insertCopy({
                                            error:`6_noResponse`,
                                            exTime:clock.stop()
                                        })
                                        break
                                    }
                                    default:{
                                        glitter.mysqlDataBase.insertTrigger({error:'11-1',id:'NA',exTime:clock.stop(),
                                            tem:'NA',
                                            pre:'NA',
                                            bat:'NA'})
                                        break
                                    }
                                }
                            }

                            clearInterval(timer)
                        }
                        if (glitter.command.cancel) {
                            glitter.command.commandFinish(function () {
                                callback('cancel')
                            })
                            clearInterval(timer)
                        }

                        if (rx.length === 36) {
                            var data = {
                                idCount: parseInt(rx.substring(17, 18), 10),
                                id: rx.substring(16 - parseInt(rx.substring(17, 18), 10), 16),
                                bat: (ConvertBase.hex2bin(rx.substring(28, 30)).substring(3, 4) === "1") ? "ok" : "low",
                                kpa: ConvertBase.hex2dec(rx.substring(22, 26)),
                                c: (ConvertBase.hex2dec(rx.substring(20, 22)) - ConvertBase.hex2dec(rx.substring(18, 20))),
                                vol: (22 + ConvertBase.hex2dec(rx.substring(26, 28)))
                            }
                            if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(0, 1) === "0") {
                                data.c = "NA"
                            }
                            if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(1, 2) === "0") {
                                data.v = "NA"
                            }
                            if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(2, 3) === "0") {
                                data.bat = "NA"
                            }
                            if(data.kpa>100){
                                glitter.mysqlDataBase.insertTrigger({
                                    error:'11-2',
                                    id:data.id,
                                    tem:data.c,
                                    pre:data.kpa,
                                    bat:data.bat,
                                    exTime:clock.stop()
                                })
                            }else{
                                glitter.mysqlDataBase.insertTrigger({
                                    error:'11-3',
                                    id:data.id,
                                    tem:data.c,
                                    pre:data.kpa,
                                    bat:data.bat,
                                    exTime:clock.stop()
                                })
                            }
                            glitter.command.commandFinish(function () {
                                callback(data)
                            })
                            clearInterval(timer)
                        }
                    } catch (e) {
                        glitter.command.commandFinish(function () {
                            callback(false)
                        })
                        clearInterval(timer)
                        alert(e)
                        alert (e.stack)  // this will work on chrome, FF. will no not work on safari
                        alert (e.line)  // this will work on safari but not on IPhone
                    }
                }, 200);
            }
        }
        /*
        * 讀取Sensor多顆
        * Array[{id,idCount,bat,kpa,c,vol}]
        * */
        this.readMultiSensor = function (count, callback,uploadData) {
            glitter.command.cancel = false
            glitter.mmyInterFace.selectMMY.programSize=count
            var hex = glitter.mmyInterFace.selectMMY["OBD Product No. (hex)"]
            var lf = glitter.mmyInterFace.selectMMY["LF Power"]
            if (hex.length === 4) {
                hex = hex.substring(2, 4)
            }
            if (lf.length > 2) {
                lf = (`0` + lf)
            }

            function send() {
                glitter.command.send(glitter.command.toCrc(`0A 10 000E 01 00 ${lf} ${hex} 00 00 00 00 0${count} 00 00 00 39 F5`.replace(/ /g, "")))
            }

            glitter.command.commandStart(function () {
                send()
                setTimeout(function () {
                    startRead()
                }, 200)
            })
            var sensorList = []
            var error = 0

            function startRead() {
                var clock = Clock()
                var timer = setInterval(function () {
                    console.log('timer')
                    var rx = glitter.command.rx
                    try {
                        if (clock.stop() > 20000) {
                            callback(false, sensorList)
                            if(uploadData){
                                if(glitter.selectFunction.select===glitter.selectFunction.enum.Program){
                                    glitter.mysqlDataBase.insertProgram({
                                        error:`noResponse`,
                                        id:sensorList,
                                        exTime:clock.stop(),
                                        inTire:0
                                    })
                                }else{

                                }
                            }
                            clearInterval(timer)
                        }
                        if ((rx.indexOf("F51C000301000A") !== -1) || (rx.indexOf("F51C000302000A") !== -1) || (rx.indexOf("F51C000301EB0A") !== -1)) {
                            error += 1
                            if (error === 3) {
                                callback(false, sensorList)
                                if(uploadData){
                                    if(glitter.selectFunction.select===glitter.selectFunction.enum.Program){
                                        glitter.mysqlDataBase.insertProgram({
                                            error:`1-${count}-${sensorList.length}`,
                                            id:sensorList,
                                            exTime:clock.stop(),
                                            inTire:0
                                        })
                                    }else{
                                        glitter.mysqlDataBase.insertCopy({
                                            error:`7-1-0`,
                                            exTime:clock.stop()
                                        })
                                    }
                                }
                                clearInterval(timer)
                                return
                            }
                            clock.zeroing()
                            send()
                        }
                        if (glitter.command.cancel) {
                            callback('cancel', sensorList)
                            clearInterval(timer)
                        }

                        if (rx.length >= 36) {
                            var data = {
                                idCount: parseInt(rx.substring(17, 18), 10),
                                id: rx.substring(8, 16),
                                bat: "",
                                kpa: "",
                                c: "",
                                vol: (22 + ConvertBase.hex2dec(rx.substring(26, 28)))
                            }
                            if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(0, 1) === "0") {
                                data.c = ""
                            }
                            if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(1, 2) === "0") {
                                data.v = ""
                            }
                            if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(2, 3) === "0") {
                                data.bat = ""
                            }
                            sensorList = sensorList.concat(data)
                            glitter.command.rx = glitter.command.rx.substring(36, glitter.command.rx.length)
                            if (sensorList.length === count) {
                                if(uploadData){
                                    if(glitter.selectFunction.select===glitter.selectFunction.enum.Program){
                                        glitter.mysqlDataBase.insertProgram({
                                            error:`1-${count}-${count}`,
                                            id:sensorList,
                                            exTime:clock.stop(),
                                            inTire:0
                                        })
                                    }else{
                                        glitter.mysqlDataBase.insertCopy({
                                            error:`7-1-1`,
                                            exTime:clock.stop()
                                        })
                                    }
                                }
                                callback(true, sensorList)
                                clearInterval(timer)
                            }
                        }
                    } catch (e) {
                        callback(false, sensorList)
                        clearInterval(timer)
                        alert(e)
                    }
                }, 200);
            }
        }
        //Program
        this.program = function (data, callback) {
            glitter.command.commandStart(function () {
                glitter.mmyInterFace.getS19("text", function (s19text) {
                    if (s19text === undefined) {
                        callback(false)
                        return
                    }
                    s19text = s19text.replace(/\r/g, '').replace(/\n/g, '').replace(/ /g, '')
                    s19text = s19text.substring(1, s19text.length)
                    glitter.command.cancel = false
                    var finishMap = {}
                    var keyList = Object.keys(data)
                    glitter.mmyInterFace.selectMMY.programSize=keyList.length
                    function programFirst() {
                        var clock = Clock()
                        var hex = glitter.mmyInterFace.selectMMY["OBD Product No. (hex)"]
                        var lf = glitter.mmyInterFace.selectMMY["LF Power"]
                        if (hex.length === 4) {
                            hex = hex.substring(2, 4)
                        }
                        if (lf.length > 2) {
                            lf = (`0` + lf)
                        }
                        let B8 = s19text.substring(14, 16)
                        let B9 = s19text.substring(16, 18)
                        let B12 = s19text.substring(22, 24)
                        let B13 = s19text.substring(24, 26)
                        let tx = "0A 10 00 0E  02 CT  Lf Hex 8b 9b 12b 13b 00 00 00 00 ff f5".replace("CT", "0" + keyList.length)
                            .replace("Lf", lf).replace("Hex", hex)
                            .replace("8b", B8).replace("9b", B9).replace("12b", B12).replace("13b", B13)
                            .replace(/ /g, "")
                        glitter.command.send(glitter.command.toCrc(tx), true)
                        let timer = setInterval(function () {
                            let rx = glitter.command.rx
                            try {
                                if (clock.stop() > 20000 || (rx.indexOf("F51C000301000A") !== -1) || (rx.indexOf("F51C000302000A") !== -1) || (rx.indexOf("F51C000301EB0A") !== -1)) {
                                    callback(false)
                                    if(clock.stop()>20000){
                                        if(glitter.selectFunction.select===glitter.selectFunction.enum.Program){
                                            glitter.mysqlDataBase.insertProgram({
                                                error:`2_noResponse`,
                                                id:keyList,
                                                exTime:clock.stop(),
                                                inTire:false
                                            })
                                        }else{
                                            glitter.mysqlDataBase.insertCopy({
                                                error:'8_noResponse',
                                                exTime:clock.stop()
                                            })
                                        }
                                    }else{
                                        if(glitter.selectFunction.select===glitter.selectFunction.enum.Program){
                                            glitter.mysqlDataBase.insertProgram({
                                                error:`2_timeOut`,
                                                id:keyList,
                                                exTime:clock.stop(),
                                                inTire:false
                                            })
                                        }else{
                                            glitter.mysqlDataBase.insertCopy({
                                                error:'8_timeOut',
                                                exTime:clock.stop()
                                            })
                                        }
                                    }
                                    clearInterval(timer)
                                }
                                if (glitter.command.cancel) {
                                    callback('cancel')

                                    clearInterval(timer)
                                }
                                if (rx.length >= (36)) {
                                    var data = (rx.substring(10, 12) === "04") ? s19text.substring(0, 4096) : s19text.substring(0, 6144 * 2)
                                    glitter.command.send('0A16000E00000000000000000000000012F5')
                                    setTimeout(function (){
                                        writeFlush(data)
                                    },2000)
                                    clearInterval(timer)
                                }
                            } catch (e) {
                                callback(false)
                                clearInterval(timer)
                            }
                        }, 200);
                    }

                    function sendCommand(position) {
                        var clock = Clock()
                        var id = data[keyList[position]].id
                        while (id.length < 8) {
                            id = ("0" + id)
                        }
                        glitter.command.send(glitter.command.toCrc(`0A 15 00 0E 0${position + 1} ${id} 00 00 00 00 00 00 00 18 F5`.replace(/ /g, "")), true)
                        let timer = setInterval(function () {
                            let rx = glitter.command.rx
                            try {
                                if (clock.stop() > 20000) {
                                    callback(false)
                                    clearInterval(timer)
                                }
                                if (glitter.command.cancel) {
                                    callback(false)
                                    clearInterval(timer)
                                }
                                if (rx.length >= 36) {
                                    if ((position + 1) < keyList.length) {
                                        sendCommand(position + 1)
                                    } else {
                                        programFirst()
                                    }
                                    clearInterval(timer)
                                }
                            } catch (e) {
                                callback(false)
                                clearInterval(timer)
                            }
                        }, 200);
                    }

                    function checkData(data, place, toNext) {
                        if (place.length < 2) {
                            place = ("0" + place)
                        }
                        let long = (data.length === 400) ? "00CB" : "00" + ConvertBase.dec2hex(parseInt(data.length / 2, 10) + 3)
                        console.log('trySend_data')
                        console.log('trySend_Conversion' + '0A 13' + long)
                        let command = glitter.command.toCrc(`0A 13 ${long} ${data} ${place} FF F5`.replace(/ /g, ''))
                        console.log('trySend' + command)
                        glitter.command.send(command)
                        var clock = Clock()
                        let timer = setInterval(function () {
                            let rx = glitter.command.rx
                            try {
                                if (clock.stop() > 20000 || (rx.indexOf("F51C000301000A") !== -1) || (rx.indexOf("F51C000302000A") !== -1) || (rx.indexOf("F51C000301EB0A") !== -1)) {
                                    glitter.mysqlDataBase.insertProgram({
                                        error:`3`,
                                        id:keyList,
                                        exTime:clock.stop(),
                                        inTire:false
                                    })
                                    callback(false)
                                    clearInterval(timer)
                                }
                                if (glitter.command.cancel) {
                                    callback('cancel')
                                    clearInterval(timer)
                                }
                                if (rx.length >= 36) {
                                    toNext()
                                    clearInterval(timer)
                                }
                            } catch (e) {
                                callback(false)
                                clearInterval(timer)
                            }
                        }, 200);
                    }

                    function writeFlush(data) {
                        let count = (data.length % 400 === 0) ? (data.length / 400) : (parseInt(data.length / 400, 10) + 1)
                        var cp = 0

                        function startProgram() {
                            console.log("countCP count:" + count + " cp:" + cp)
                            if (cp === count) {
                                glitter.command.loadingProgress(95)
                                programCheck(data, count)
                                return
                            } else {
                                glitter.command.loadingProgress(parseInt(cp * 100 / count, 10))
                            }
                            if (finishMap["" + cp]) {
                                cp += 1
                                startProgram()
                                return;
                            }
                            var subData = (cp === count - 1) ? data.substring(400 * cp, data.length) : data.substring(400 * cp, 400 * cp + 400)
                            checkData(subData, ConvertBase.dec2hex(cp + 1), function () {
                                cp += 1
                                startProgram()
                            })
                        }

                        startProgram()
                    }

                    function programCheck(data, count) {
                        let cheeckData = "0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(/ /g, "")
                        glitter.command.send(glitter.command.toCrc(cheeckData))
                        var clock = Clock()
                        let timer = setInterval(function () {
                            let rx = glitter.command.rx
                            try {
                                if (clock.stop() > 20000 || (rx.indexOf("F51C000301000A") !== -1) || (rx.indexOf("F51C000302000A") !== -1) || (rx.indexOf("F51C000301EB0A") !== -1)) {
                                    callback(false)
                                    glitter.mysqlDataBase.insertProgram({
                                        error:`4`,
                                        id:keyList,
                                        exTime:clock.stop(),
                                        inTire:false
                                    })
                                    clearInterval(timer)
                                }
                                if (glitter.command.cancel) {
                                    callback('cancel')
                                    clearInterval(timer)
                                }
                                if (rx.length >= 36 && (rx.indexOf("F513000E00") !== -1)) {
                                    let check = rx.substring(12, 20)
                                    if ((check === "7FFFFFFF") || (check === "000007FF")) {
                                        callback(true)
                                    } else {
                                        var bin = ConvertBase.hex2bin(check)
                                        while (bin.length < count) {
                                            bin = ("0" + bin)
                                        }
                                        bin = bin.split("").reverse().join("")
                                        for (var a = 0; a < count; a++) {
                                            finishMap["" + a] = bin.substring(a, a + 1) === "1";
                                        }
                                        writeFlush(data)
                                    }
                                    clearInterval(timer)
                                }
                            } catch (e) {
                                callback(false)
                                clearInterval(timer)
                            }
                        }, 200);
                    }

                    sendCommand(0)
                })
            })
        }
        //Idcopy
        this.idCopy = function (original, newSensor, callback) {
            glitter.command.cancel = false
            var hex = glitter.mmyInterFace.selectMMY["OBD Product No. (hex)"]
            var lf = glitter.mmyInterFace.selectMMY["LF Power"]
            var idCount = glitter.mmyInterFace.selectMMY["ID_Count"]
            if (hex.length === 4) {
                hex = hex.substring(2, 4)
            }
            if (lf.length > 2) {
                lf = (`0` + lf)
            }
            while (original.length < 8) {
                original = "0" + original
            }
            while (newSensor.length < 8) {
                newSensor = "0" + newSensor
            }

            function startCopy() {
                glitter.command.send(glitter.command.toCrc(`0A 11 00 0E ${original} 0${idCount} ${newSensor} 0${idCount} ${hex} 00 ff f5`.replace(/ /g, "")))
                var clock = Clock()
                var timer = setInterval(function () {
                    console.log('timer')
                    var rx = glitter.command.rx
                    try {
                        if (clock.stop() > 20000 || (rx.indexOf("F51C000301000A") !== -1) || (rx.indexOf("F51C000302000A") !== -1) || (rx.indexOf("F51C000301EB0A") !== -1)) {
                            callback(false)
                            clearInterval(timer)
                        }
                        if (glitter.command.cancel) {
                            callback('cancel')
                            clearInterval(timer)
                        }

                        if (rx.length === 36) {
                            callback(true)
                            clearInterval(timer)
                        }
                    } catch (e) {
                        callback(false)
                        clearInterval(timer)
                        alert(e)
                    }
                }, 200);
            }

            startCopy()
        }
        //確認燒錄是否成功
        this.checkPrId = function (callback, count) {
            var clock = Clock()
            var sensorList = []
            var hex = glitter.mmyInterFace.selectMMY["OBD Product No. (hex)"]
            var lf = glitter.mmyInterFace.selectMMY["LF Power"]
            if (hex.length === 4) {
                hex = hex.substring(2, 4)
            }
            if (lf.length > 2) {
                lf = (`0` + lf)
            }
            let text = glitter.command.toCrc("0A 10 000E 01 02 LF HEX 00 00 00 00 00 00 00 00 39 F5".replace(
                "HEX", hex).replace(/ /g, "").replace("LF", lf))
            glitter.command.send(text, true)
            let timer = setInterval(function () {
                let rx = glitter.command.rx
                try {
                    if (clock.stop() > 20000 || (rx.indexOf("F51C000301000A") !== -1) || (rx.indexOf("F51C000302000A") !== -1) || (rx.indexOf("F51C000301EB0A") !== -1)) {
                        if(clock.stop()>20000){
                            glitter.mysqlDataBase.insertProgram({
                                error:`"5-${count}-${sensorList.size}"`,
                                id:sensorList,
                                exTime:clock.stop(),
                                inTire:false
                            })
                        }else{
                            glitter.mysqlDataBase.insertProgram({
                                error:`5`,
                                id:sensorList,
                                exTime:clock.stop(),
                                inTire:false
                            })
                        }
                        callback(sensorList)
                        clearInterval(timer)
                    }
                    if (glitter.command.cancel) {
                        callback('cancel')
                        clearInterval(timer)
                    }
                    if (rx.length >= 36) {
                        var data = {
                            idCount: parseInt(rx.substring(17, 18), 10),
                            id: rx.substring(8, 16),
                            bat: (ConvertBase.hex2bin(rx.substring(28, 30)).substring(3, 4) === "1") ? "ok" : "low",
                            kpa: ConvertBase.hex2dec(rx.substring(22, 26)),
                            c: (ConvertBase.hex2dec(rx.substring(20, 22)) - ConvertBase.hex2dec(rx.substring(18, 20))),
                            vol: (22 + ConvertBase.hex2dec(rx.substring(26, 28))),
                            success: function () {
                            }
                        }
                        if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(0, 1) === "0") {
                            data.c = "NA"
                        }
                        if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(1, 2) === "0") {
                            data.v = "NA"
                        }
                        if (ConvertBase.hex2bin(rx.substring(28, 30)).substring(2, 3) === "0") {
                            data.bat = "NA"
                        }
                        sensorList = sensorList.concat(data)
                        glitter.command.rx = glitter.command.rx.substring(36, glitter.command.rx.length)
                        if (sensorList.length === count) {
                            callback(sensorList)
                            clearInterval(timer)
                        }
                    }
                } catch (e) {
                    callback(sensorList)
                    clearInterval(timer)
                }
            }, 200);

        }
        //Crc塞入值
        this.toCrc = function (data) {
            return insertCrc(data)
        }
        //執行結束
        this.commandFinish = function (callback) {
            glitter.command.send("0AE100030200F5")
            setTimeout(function () {
                if (glitter.publicBeans.playSound) {
                    glitter.command.send("0AE200030000F5")
                }
            }, 200)
            setTimeout(function () {
                if (glitter.publicBeans.playSound) {
                    glitter.command.send("0AE000030000F5")
                }
                callback()
            }, 400)
        }
        //開始執行
        this.commandStart = function (callback) {
            glitter.command.send("0AE100030100F5")
            setTimeout(function () {
                callback()
            }, 200)
        }
        //OBD讀取
        this.readObd = function () {
        }
        //加載OBD
        this.loadingOBD = function (callback) {
            glitter.command.send(insertCrc("0A0D00030000F5"))
            //FW中的版本號取得
            var obdVersion = ''
            //當前下載的版本
            var localVersion = glitter.publicBeans.localVersion.obdList[(glitter.mmyInterFace.selectMMY["OBD2"].substring(0, glitter.mmyInterFace.selectMMY["OBD2"].length - 1)) + "L"].replace(".srec", "")

            //取得ＯＢＤ版本號
            function getObdVersion(toNext) {
                var clock = Clock()
                glitter.command.send(glitter.command.toCrc("0ACF000100FFF5"))
                let timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (clock.stop() > 2000) {
                        callback(false)
                        clearInterval(timer)
                    }
                    if (glitter.command.cancel) {
                        rx = ""
                        callback('cancel')
                        clearInterval(timer)
                    }
                    if (rx.length === 56) {
                        obdVersion = rx.substring(8, 52)
                        setTimeout(function () {
                            toNext()
                        }, 5000)
                        clearInterval(timer)
                    }
                }, 200)
            }

            //跳轉至燒錄OBD
            function goPrOBD(toNext) {
                var clock = Clock()
                glitter.command.send(insertObdCrc("0A8D00030100F5"))
                let timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (clock.stop() > 3000) {
                        toNext()
                        clearInterval(timer)
                    }
                    if (glitter.command.cancel) {
                        callback('cancel')
                        clearInterval(timer)
                    }
                    if (rx.length === 14) {
                        toNext()
                        clearInterval(timer)
                    }
                }, 200)
            }

            //跳轉至Bootloader
            function goBootloader(toNext) {
                var clock = Clock()
                glitter.command.send(glitter.command.toCrc("0ACD010100FFF5"))
                let timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (clock.stop() > 10000) {
                        toNext()
                        clearInterval(timer)
                    }
                    if (glitter.command.cancel) {
                        callback('cancel')
                        clearInterval(timer)
                    }
                    if ((rx.indexOf("F5CD010100380AF501000300F70A") !== -1) || (rx.indexOf("F5CD010100CD0AF501000300F70A") !== -1)) {
                        toNext()
                        clearInterval(timer)
                    }
                }, 200)
            }

            //寫入Flush
            function writeFlush(toNext) {
                glitter.getFile("obd/" + (glitter.mmyInterFace.selectMMY["OBD2"].substring(0, glitter.mmyInterFace.selectMMY["OBD2"].length - 1)) + "L", "text", function (text) {
                    let sb = (text.replace(/\n/g, "").replace(/\r/g, "").replace(/ /g, ""))
                    if (glitter.deviceType === glitter.deviceTypeEnum.Android) {
                        sb = sb.substring(1, sb.length)
                    }
                    let long = 0
                    let Ind = 298
                    if (sb.length % Ind === 0) {
                        long = sb.length / Ind
                    } else {
                        long = parseInt(sb.length / Ind, 10) + 1
                    }
                    var i = 0

                    function start() {
                        if (i === long) {
                            toNext()
                            return
                        }
                        var b = i
                        if (b >= 255) {
                            b -= 255
                        }

                        var hexLong = ConvertBase.dec2hex(b)
                        while (hexLong.length < 2) {
                            hexLong = `0${hexLong}`
                        }
                        var data = undefined
                        var length = undefined
                        if (i === long - 1) {
                            data = stringToHex(sb.substring(i * Ind, text.length))
                            length = "" + parseInt(data.length / 2, 10) + 3
                            glitter.command.loadingProgress(95)
                        } else {
                            data = bytesToHex(stringToBytes(sb.substring(i * Ind, i * Ind + Ind)))
                            length = "" + ConvertBase.dec2hex(parseInt(data.length / 2, 10) + 3)
                            glitter.command.loadingProgress(parseInt(b * 100 / long, 10))
                            console.log("data==" + sb.substring(i * Ind, i * Ind + Ind))
                        }

                        while (length.length < 4) {
                            length = "0" + length
                        }
                        if (hexLong === 'F5') {
                            hexLong = "00"
                        }
                        if (hexLong.length > 2) {
                            hexLong = "00"
                        }

                        function send() {
                            console.log(`bleMessage_initial:0A02${length}${hexLong}`)
                            glitter.command.send(insertObdCrc(`0A02${length}${hexLong}${data}00F5`))
                        }

                        let clock = Clock()
                        send()
                        var fal = 0;
                        let timer = setInterval(function () {
                            let rx = glitter.command.rx
                            if (clock.stop() > 3000) {
                                clock.zeroing()
                                fal += 1
                                send()
                            }
                            if (fal >= 20) {
                                callback(false)
                                clearInterval(timer)
                            }
                            if (glitter.command.cancel) {
                                callback('cancel')
                                clearInterval(timer)
                            }
                            if (rx.length >= 16 || (b === long - 1)) {
                                i += 1
                                start()
                                clearInterval(timer)
                            }
                        }, 20)
                    }

                    start()
                })
            }

            //寫入版本號
            function writeVersion(toNext) {
                var clock = Clock()
                glitter.command.send("0ACA0015DDFFF5".replace("DD", bytesToHex(stringToBytes(localVersion))))
                let timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (clock.stop() > 2000) {
                        toNext()
                        clearInterval(timer)
                    }
                    if (glitter.command.cancel) {
                        callback('cancel')
                        clearInterval(timer)
                    }
                    if (rx.length === 14) {
                        toNext()
                        clearInterval(timer)
                    }
                }, 200)
            }

            //要先跳轉至Bootloader
            setTimeout(() => {
                glitter.command.goState(BootloaderState.Bootloader, function () {
                    getObdVersion(function () {
                        console.log("localVersion:", bytesToHex(stringToBytes(localVersion)) + "obdVersion:" + obdVersion)
                        if (bytesToHex(stringToBytes(localVersion)).toUpperCase() === obdVersion) {
                            glitter.command.goState(BootloaderState.Obd_App, function () {
                                callback(true)
                            })
                        } else {
                            goPrOBD(function () {
                                goBootloader(function () {
                                    writeFlush(function () {
                                        writeVersion(function () {
                                            callback(true)
                                        })
                                    })
                                })
                            })
                        }
                    })
                })
            }, 2000)
        }
        //讀取OBD ID
        this.getOBDid = function (callback) {
            glitter.command.cancel = false
            let obdVersion = "60BF00010" + glitter.mmyInterFace.selectMMY.Wheel_Count + "FF0A";
            let clock = Clock()
            glitter.command.send(glitter.command.toCrc(obdVersion))
            var fal = 0
            let timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 2500) {
                    if (fal >= 20) {
                        callback(false)
                        glitter.mysqlDataBase.insertOBD('ReadFalse')
                        clearInterval(timer)
                    } else {
                        fal = fal + 1
                        clock.zeroing()
                        glitter.command.send(glitter.command.toCrc(obdVersion))
                    }
                }
                if (glitter.command.cancel) {
                    callback('cancel')
                    clearInterval(timer)
                }
                if (rx.length === 52) {
                    glitter.mysqlDataBase.insertOBD('ReadSuccess')
                    var sensorList = []
                    for (var a = 0; a < 5; a++) {
                        sensorList = sensorList.concat(glitter.command.rx.substring((a + 1) * 8, (a + 2) * 8))
                    }
                    callback(sensorList)
                    clearInterval(timer)
                }
            }, 200)
        }
        //寫入OBD ID
        this.writeObd = function (itemArray, callback) {
            glitter.command.cancel = false

            function writeSingle(data, toNext) {
                glitter.command.send(insertObdCrc(data))
                setTimeout(function () {
                    toNext()
                }, 50)
            }

            function writeCommand() {
                writeSingle("60A200FFFFFFFFC20A", function () {
                    var position = 0
                    var positionarray = ["4", "1", "2", "3", "5"]

                    function write() {
                        while (itemArray[position].length < 8) {
                            itemArray[position] = "0" + itemArray[position]
                        }
                        writeSingle("60A20XidFF0A".replace("id", itemArray[position]).replace("X", positionarray[position]), function () {
                            position++
                            if (position === itemArray.length) {
                                writeSingle("60A2FFFFFFFFFF3D0A", function () {
                                })
                            } else {
                                write()
                            }
                        })
                    }

                    write()
                })
            }

            writeCommand()
            var fal = 0
            var clock = Clock()
            let timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 5000) {
                    fal += 1
                    clock.zeroing()
                    writeCommand()
                }
                if (glitter.command.cancel) {
                    callback('cancel')
                    clearInterval(timer)
                }
                if (fal >= 10) {
                    glitter.mysqlDataBase.insertOBD('WriteFalse')
                    callback(false)
                    clearInterval(timer)
                }
                if (rx.length >= 18) {
                    callback(true)
                    glitter.mysqlDataBase.insertOBD('WriteSuccess')
                    clearInterval(timer)
                }
            }, 200)
        }
        //跳區
        this.goState = function (state, callback) {
            switch (state) {
                case BootloaderState.Bootloader:
                    glitter.command.send(insertCrc("0A0D00030000F5"))
                    break
                case BootloaderState.Og_App:
                    glitter.command.send(insertCrc("0A0D00030100F5"))
                    break
                case BootloaderState.Obd_App:
                    glitter.command.send(insertCrc("0A0D00030200F5"))
                    break
            }
            setTimeout(function () {
                callback()
            }, 2000)
        }
        //讀取ble版本號
        this.getBleVersion = function (success, error) {
            function send() {
                if (glitter.deviceType === glitter.deviceTypeEnum.Ios) {
                    glitter.command.send("0AF901F5")
                } else {
                    glitter.command.send("0AF902F5")
                }
            }

            var clock = Clock()
            send()
            var fal = 0
            var timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 2000) {
                    fal++
                    clock.zeroing()
                }
                if (fal >= 3) {
                    error()
                    clearInterval(timer)
                }
                if (rx.length >= 56) {
                    glitter.publicBeans.bleVersion = String.fromCharCode(...hexToBytes(rx.substring(8, 56)))
                    console.log("bleVersion--->" + glitter.publicBeans.bleVersion)
                    success()
                    clearInterval(timer)
                }
            }, 200)
        }
        //取得位置
        this.getState = function (success, error) {
            function send() {
                glitter.command.send("0A0000030000F5")
            }

            var clock = Clock()
            send()
            var fal = 0
            var timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 2000) {
                    fal++
                    clock.zeroing()
                }
                if (fal >= 3) {
                    error()
                    clearInterval(timer)
                }
                if (rx.length >= 14) {
                    switch (rx.substring(8, 10)) {
                        case "01":
                            glitter.DongleState = BootloaderState.Bootloader
                            break
                        case "02":
                            glitter.DongleState = BootloaderState.Og_App
                            break
                        case "03":
                            glitter.DongleState = BootloaderState.Obd_App
                            break
                    }
                    success()
                    clearInterval(timer)
                }
            }, 200)
        }
        //取得FW版本
        this.askVersion = function (success, error) {
            function send() {
                glitter.command.send("0ADC000100FFF5")
            }

            var clock = Clock()
            send()
            var fal = 0
            var timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 2000) {
                    fal++
                    clock.zeroing()
                }
                if (fal >= 3) {
                    success()
                    clearInterval(timer)
                }
                if (rx.length >= 54) {
                    let temversion = String.fromCharCode(...hexToBytes(rx.substring(8, 54)))
                    glitter.publicBeans.mcuVersion = temversion.substring(0, temversion.length - 2)
                    success()
                    clearInterval(timer)
                }
            }, 200)
        }
        //讀取SN
        this.readSN = function (success, error) {
            function send() {
                glitter.command.send("0ADF00010000F5")
            }

            var clock = Clock()
            send()
            var fal = 0
            var timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 2000) {
                    fal++
                    clock.zeroing()
                }
                if (fal >= 3) {
                    success()
                    clearInterval(timer)
                }
                if (rx.length >= 24) {
                    glitter.publicBeans.sn = rx.substring(8, 20)
                    success()
                    clearInterval(timer)
                }
            }, 200)
        }
        //睡眠時間設定
        this.sleep = function (success, error) {
            function send() {
                var time = ConvertBase.dec2hex(glitter.publicBeans.sleepTime)
                while (time.length < 4) {
                    time = ("0" + time)
                }
                glitter.command.send("0AE50004" + time + "00F5")
            }

            var clock = Clock()
            send()
            var timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 2000) {
                    success()
                    clearInterval(timer)
                }
                if (rx.length >= 14) {
                    success()
                    clearInterval(timer)
                }
            }, 200)
        }
        //更新FW
        this.updateFW = function (data, result) {
            //跳轉至燒錄FW
            function goPrFW(toNext) {
                function send() {
                    glitter.command.send(insertCrc("0A0D00030000F5"))
                    setTimeout(function () {
                        glitter.command.send(insertCrc("0A8D00030000F5"))
                    }, 200)
                }

                var clock = Clock()
                send()
                var timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (clock.stop() > 3000) {
                        toNext()
                        clearInterval(timer)
                    }
                    if (rx.length >= 28) {
                        toNext()
                        clearInterval(timer)
                    }
                }, 200)
            }

            //跳轉至Bootloader
            function goBootloader(toNext) {
                function send() {
                    glitter.command.send(insertCrc("0ADD010100FFF5"))
                }

                var clock = Clock()
                send()
                var fal = 0
                var timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (fal >= 1) {
                        result(false)
                        clearInterval(timer)
                    }
                    if (clock.stop() > 10000) {
                        send()
                        fal++
                        clock.zeroing()
                    }
                    if ((rx.length >= 28)) {
                        toNext()
                        clearInterval(timer)
                    }
                }, 200)
            }

            //寫入Flush
            function writeFlush(toNext) {
                glitter.openDiaLog('dialog/Dia_Loading_Progress.html', 'Dia_Loading_Progress', false, false, glitter.getLan(8))
                var sb = (data.replace(/\n/g, "").replace(/\r/g, ""))
                var long = 0
                var Ind = 40
                if (sb.length % Ind === 0) {
                    long = sb.length / Ind
                } else {
                    long = parseInt(sb.length / Ind, 10) + 1
                }
                var i = 0

                function start() {
                    if (i === long) {
                        glitter.closeDiaLogWithTag("Dia_Loading_Progress")
                        setTimeout(function () {
                            toNext()
                        }, 1000)
                        return
                    }
                    var b = i
                    if (b >= 255) {
                        b -= 255
                    }
                    var hexLong = ConvertBase.dec2hex(b)
                    while (hexLong.length < 2) {
                        hexLong = `0${hexLong}`
                    }
                    var data = undefined
                    var length = undefined
                    if (i === long - 1) {
                        data = bytesToHex(stringToBytes(sb.substring(i * Ind, sb.length)))
                        length = "" + ConvertBase.dec2hex(parseInt(data.length / 2, 10) + 3)
                        glitter.command.loadingProgress(95)
                    } else {
                        data = bytesToHex(stringToBytes(sb.substring(i * Ind, i * Ind + Ind)))
                        length = "" + ConvertBase.dec2hex(parseInt(data.length / 2, 10) + 3)
                        glitter.command.loadingProgress(parseInt((i * 100) / long, 10))
                    }
                    while (length.length < 4) {
                        length = "0" + length
                    }
                    if (hexLong.toUpperCase() === 'F5') {
                        hexLong = "00"
                    }
                    if (hexLong.length > 2) {
                        hexLong = "00"
                    }

                    function send() {
                        glitter.command.send(insertObdCrc(`0A02${length}${hexLong}${data}00F5`))
                    }

                    let clock = Clock()
                    send()
                    var fal = 0;
                    let timer = setInterval(function () {
                        let rx = glitter.command.rx
                        if (clock.stop() > 5000) {
                            clock.zeroing()
                            fal += 1
                            send()
                        }
                        if (fal >= 3) {
                            glitter.closeDiaLogWithTag("Dia_Loading_Cancel")
                            result(false)
                            clearInterval(timer)
                        }
                        if (rx.length >= 14) {
                            i += 1
                            start()
                            clearInterval(timer)
                        }
                    }, 100)
                }

                start()
            }

            //寫入版本號
            function writeVersion(toNext) {
                function send() {
                    glitter.command.send(insertCrc("0ADA0015DDFFF5".replace("DD", glitter.command.toHex(glitter.command.stringToBytes(glitter.publicBeans.onlineVersion.mcuVerion)))))
                }

                var clock = Clock()
                send()
                var fal = 0
                var timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (fal >= 1) {
                        result(false)
                        clearInterval(timer)
                    }
                    if (clock.stop() > 10000) {
                        send()
                        fal++
                        clock.zeroing()
                    }
                    if ((rx.length === 14)) {
                        setTimeout(function () {
                            toNext()
                        }, 200)
                        clearInterval(timer)
                    }
                }, 200)
            }

            //仿同步
            goPrFW(function () {
                goBootloader(function () {
                    writeFlush(function () {
                        writeVersion(function () {
                            glitter.publicBeans.mcuVersion = glitter.publicBeans.onlineVersion.mcuVerion
                            glitter.command.goState(BootloaderState.Og_App)
                            result(true)
                        })
                    })
                })
            })
        }
        //更新Bootloader
        this.updateBootloader = function (data, result) {
            //跳轉至Bootloader
            function goPrBoot(toNext) {
                function send() {
                    glitter.command.send("0A8D00030200F5")
                }

                var clock = Clock()
                send()
                var timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (clock.stop() > 2000) {
                        clock.zeroing()
                        result(false)
                        clearInterval(timer)
                    }
                    if (rx.length >= 14) {
                        toNext()
                        clearInterval(timer)
                    }
                }, 200)
            }

            //跳轉至燒錄Bootloader
            function goPrBootloader(toNext) {
                function send() {
                    glitter.command.send("0AED010100FFF5")
                }

                var clock = Clock()
                send()
                var timer = setInterval(function () {
                    var rx = glitter.command.rx
                    if (clock.stop() > 10000) {
                        clock.zeroing()
                        result(false)
                        clearInterval(timer)
                    }
                    if (rx.length >= 28) {
                        toNext()
                        clearInterval(timer)
                    }
                }, 200)
            }

            //寫入Flush
            function writeFlush(toNext) {
                glitter.openDiaLog('dialog/Dia_Loading_Progress.html', 'Dia_Loading_Progress', false, false, glitter.getLan(8))
                var sb = (data.replace(/\n/g, "").replace(/\r/g, ""))
                var long = 0
                var Ind = 40
                if (sb.length % Ind === 0) {
                    long = sb.length / Ind
                } else {
                    long = parseInt(sb.length / Ind, 10) + 1
                }
                var i = 0

                function start() {
                    if (i === long) {
                        glitter.closeDiaLogWithTag("Dia_Loading_Progress")
                        setTimeout(function () {
                            toNext()
                        }, 1000)
                        return
                    }
                    var b = i
                    if (b >= 255) {
                        b -= 255
                    }
                    var hexLong = ConvertBase.dec2hex(b)
                    while (hexLong.length < 2) {
                        hexLong = `0${hexLong}`
                    }
                    var data = undefined
                    var length = undefined
                    if (i === long - 1) {
                        data = bytesToHex(stringToBytes(sb.substring(i * Ind, sb.length)))
                        length = "" + ConvertBase.dec2hex(parseInt(data.length / 2, 10) + 3)
                        glitter.command.loadingProgress(95)
                    } else {
                        data = bytesToHex(stringToBytes(sb.substring(i * Ind, i * Ind + Ind)))
                        length = "" + ConvertBase.dec2hex(parseInt(data.length / 2, 10) + 3)
                        glitter.command.loadingProgress(parseInt((i * 100) / long, 10))
                    }
                    while (length.length < 4) {
                        length = "0" + length
                    }
                    if (hexLong.toUpperCase() === 'F5') {
                        hexLong = "00"
                    }
                    if (hexLong.length > 2) {
                        hexLong = "00"
                    }

                    function send() {
                        glitter.command.send(insertObdCrc(`0A02${length}${hexLong}${data}00F5`))
                    }

                    let clock = Clock()
                    send()
                    var fal = 0;
                    let timer = setInterval(function () {
                        let rx = glitter.command.rx
                        if (clock.stop() > 5000) {
                            clock.zeroing()
                            fal += 1
                            send()
                        }
                        if (fal >= 3) {
                            glitter.closeDiaLogWithTag("Dia_Loading_Cancel")
                            result(false)
                            clearInterval(timer)
                        }
                        if (rx.length >= 14) {
                            i += 1
                            start()
                            clearInterval(timer)
                        }
                    }, 100)
                }

                start()
            }

            goPrBoot(function () {
                goPrBootloader(function () {
                    writeFlush(function () {
                        result(true)
                    })
                })
            })
        }
        //檢查電池
        this.getBattery = function (result) {
            function send() {
                glitter.command.send("0AEE00000000F5")
            }

            var clock = Clock()
            send()
            var timer = setInterval(function () {
                var rx = glitter.command.rx
                if (clock.stop() > 2000) {
                    clock.zeroing()
                    clearInterval(timer)
                }
                if (rx.length >= 14) {
                    glitter.share.battery = rx.substring(8, 10)
                    result(glitter.share.battery)
                    clearInterval(timer)
                }
            }, 200)
        }
        //判斷Bootloader版本號
        this.getBootloaderVersion = function (result) {
            function send() {
                glitter.command.send("0AEC00010000F5")
            }

            let clock = Clock()
            send()
            let timer = setInterval(function () {
                let rx = glitter.command.rx
                if (clock.stop() > 2000) {
                    glitter.publicBeans.bootloaderVersion = undefined
                    clock.zeroing()
                    result(false)
                    clearInterval(timer)
                }
                if (rx.length >= 14 && (rx.substring(0, 8) === "F5EC0103")) {
                    let bootloaderVersion = rx.substring(8, 10)
                    result(bootloaderVersion)
                    glitter.publicBeans.bootloaderVersion = bootloaderVersion
                    glitter.storePublicBeans()
                    clearInterval(timer)
                }
            }, 200)
        }
        //是否能被連線
        this.canConnect = true
        //CRC to string
        this.toCrcString = function () {
            return insertCrc("0ADA0015DDFFF5".replace("DD", glitter.command.toHex(glitter.command.stringToBytes(glitter.publicBeans.onlineVersion.mcuVerion.replace(".srec", "")))))
        }
        this.BootloaderState = BootloaderState
        this.stringToBytes = stringToBytes
        this.toHex = bytesToHex
    }
}

//區域ENUN
var BootloaderState = {
    Bootloader: 0,
    Og_App: 1,
    Obd_App: 2
}

function hex_to_ascii(str1) {
    var hex = str1.toString();
    var str = '';
    for (var n = 0; n < hex.length; n += 2) {
        str += String.fromCharCode(parseInt(hex.substr(n, 2), 16));
    }
    return str;
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

//插入ＣＲＣ
function insertCrc(data) {
    var byteArray = hexToBytes(data)
    var xor = 0
    for (var a = 0; a < byteArray.length - 2; a++) {
        xor = xor ^ byteArray[a]
    }
    byteArray[byteArray.length - 2] = xor
    return bytesToHex(byteArray)
}

//插入ＣＲＣ
function insertObdCrc(data) {
    var byteArray = hexToBytes(data)
    var xor = byteArray[0]
    for (var a = 1; a < byteArray.length - 2; a++) {
        xor = xor ^ byteArray[a]
    }
    byteArray[byteArray.length - 2] = xor
    return bytesToHex(byteArray)
}

function hexToBytes(hex) {
    for (var bytes = [], c = 0; c < hex.length; c += 2)
        bytes.push(parseInt(hex.substr(c, 2), 16));
    return bytes;
}

// Convert a byte array to a hex string
function bytesToHex(bytes) {
    for (var hex = [], i = 0; i < bytes.length; i++) {
        hex.push((bytes[i] >>> 4).toString(16));
        hex.push((bytes[i] & 0xF).toString(16));
    }
    return hex.join("");
}

function stringToHex(str) {
    var bytes = [];
    for (var i = 0; i < str.length; i++) {
        var char = str.charCodeAt(i);
        bytes.push(char >>> 8);
        bytes.push(char & 0xFF);
    }
    return bytesToHex(bytes);
}

function stringToBytes(str) {
    var ch, st, re = [];
    for (var i = 0; i < str.length; i++) {
        ch = str.charCodeAt(i);
        st = [];
        do {
            st.push(ch & 0xFF);
            ch = ch >> 8;
        } while (ch);
        re = re.concat(st.reverse());
    }   // return an array of bytes
    return re;
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