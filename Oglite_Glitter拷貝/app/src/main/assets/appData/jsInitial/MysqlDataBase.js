"use strict";

class MysqlDataBase {
    constructor() {
        //OBD紀錄
        this.insertOBD = function (error) {
            let protoCal = ((glitter.mmyInterFace.selectMMY["OBD2"].substring(0, glitter.mmyInterFace.selectMMY["OBD2"].length - 1)) + "L")
            let mmyVersion = glitter.publicBeans.localVersion.mmyVersion
            let date = (new Date()).format("yyyy-MM-dd hh:mm:ss").replace(".0", "")
            let obdVersion = glitter.publicBeans.localVersion.obdList[(glitter.mmyInterFace.selectMMY["OBD2"].substring(0, glitter.mmyInterFace.selectMMY["OBD2"].length - 1)) + "L"]
            // if (error.indexOf("Success") === -1) {
            //     glitter.serialUtil.storeObject({
            //         errorTitle: (glitter.deviceType === glitter.deviceTypeEnum.Android) ? `OGlite(Android) OBD False->${protoCal}` : `OGlite(IOS) OBD False->${protoCal}`,
            //         mmy: `${glitter.mmyInterFace.selectMake}/${glitter.mmyInterFace.selectModel}/${glitter.mmyInterFace.selectYear}`,
            //         directFit: protoCal,
            //         account: glitter.publicBeans.account,
            //         errorType: error,
            //         tx: glitter.command.txmemory,
            //         time: date,
            //         harwareVersion: '1.0',
            //         softVersion: glitter.share.myAppVersion,
            //         fwVersion: glitter.publicBeans.mcuVersion
            //     }, date, "ErrorMessageJson", function () {
            //     }, function () {
            //     })
            // }
            var deviceType = 'IOS'
            switch (glitter.deviceType) {
                case glitter.deviceTypeEnum.Android:
                    deviceType = 'Android'
                    break
            }
            if(!glitter.share.obdClock){
                glitter.share.obdClock=glitter.clock()
            }
            glitter.getGPS(function (result) {
                if (result === undefined) {
                    result = {}
                }
                var sql = `insert into \`oglitememory\`.\`obd_infomation\` (make, model, year, account, tx, errortype,
                                                                            serialnumber, obdVersion, protocal, ip,
                                                                            \`database\`, apkVersion,
                                                                            harewareVersion, country, state,
                                                                            latitude, longitude, address, deviceType,exTime,time,phoneMake,phoneModel,phoneVersion,vin)
                           values ('${glitter.mmyInterFace.selectMake}', '${glitter.mmyInterFace.selectModel}',
                                   '${glitter.mmyInterFace.selectYear}', '${glitter.publicBeans.account}',
                                   '${glitter.command.txmemory}', '${error}', '${glitter.publicBeans.sn}',
                                   '${obdVersion}',
                                   '${protoCal}', '${glitter.publicBeans.lastIP}', '${mmyVersion}',
                                   '${glitter.share.myAppVersion}',
                                   '${(glitter.deviceType === glitter.deviceTypeEnum.Android) ? `OGLite(Android)` : `OGLite(IOS)`}
                                       ', '${glitter.publicBeans.lastCountry}', '${glitter.publicBeans.lastState}',
                                   '${result.latitude}', '${result.longitude}',
                                   '${glitter.stringToUnicode(result.address)}', '${deviceType}','${glitter.share.obdClock.stop()}','${new Date().format('yyyy-MM-dd hh:mm:ss')}','${glitter.share.phoneMake}','${glitter.share.phoneModel}',
                                   '${glitter.share.phoneVersion}','${glitter.mmyInterFace.selectMMY['vin']}')`
                glitter.serialUtil.storeObject({
                    sql: btoa(glitter.stringToUnicode(sql))
                }, date, 'insertSql', function () {
                }, function () {
                })
                glitter.command.txmemory = ''
            })
        }
        //Read紀錄
        this.insertTrigger = function (map) {
            let protoCal = glitter.mmyInterFace.selectMMY["Direct Fit"]
            let mmyVersion = glitter.publicBeans.localVersion.mmyVersion
            let lfPower = glitter.mmyInterFace.selectMMY["OG LF Protocol Num"]
            let date = (new Date()).format("yyyy-MM-dd hh:mm:ss").replace(".0", "")
            // if (["success", "11-2", "11-3"].indexOf(map.error) === -1) {
            //     glitter.serialUtil.storeObject({
            //         errorTitle: (glitter.deviceType === glitter.deviceTypeEnum.Android) ? `OGlite(Android) Read False->${protoCal}` : `OGlite(IOS) Read False->${protoCal}`,
            //         mmy: `${glitter.mmyInterFace.selectMake}/${glitter.mmyInterFace.selectModel}/${glitter.mmyInterFace.selectYear}`,
            //         directFit: protoCal,
            //         account: glitter.publicBeans.account,
            //         errorType: map.error,
            //         tx: glitter.command.txmemory,
            //         time: date,
            //         harwareVersion: '1.0',
            //         softVersion: glitter.share.myAppVersion,
            //         fwVersion: glitter.publicBeans.mcuVersion
            //     }, date, "ErrorMessageJson", function () {
            //     }, function () {
            //     })
            // }
            var deviceType = 'IOS'
            switch (glitter.deviceType) {
                case glitter.deviceTypeEnum.Android:
                    deviceType = 'Android'
                    break
            }
            var sql = `insert into \`oglitememory\`.triggerinfo
                       (devicetype, make, model, year, account, tx, errortype, serialnumber, sensorID, tem, pre, bat,
                        files19, LF_power,
                        directfit, ip, \`database\`, apkVersion, harewareVersion, country, state, exTime, moduleVersion,time,phoneMake,phoneModel,phoneVersion)
                       values ('${deviceType}', '${glitter.mmyInterFace.selectMake}',
                               '${glitter.mmyInterFace.selectModel}',
                               '${glitter.mmyInterFace.selectYear}', '${glitter.publicBeans.account}',
                               '${glitter.command.txmemory}', '${map.error}', '${glitter.publicBeans.sn}',
                               '${map.id}', '${map.tem}', '${map.pre}', '${map.bat}', '${mmyVersion}', '${lfPower}',
                               '${protoCal}'
                                  , '${glitter.publicBeans.lastIP}', '${mmyVersion}', '${glitter.share.myAppVersion}
                                   ', '1.0', '${glitter.publicBeans.lastCountry}', '${glitter.publicBeans.lastState}',
                               ${map.exTime}, '${glitter.publicBeans.mcuVersion}','${new Date().format('yyyy-MM-dd hh:mm:ss')}','${glitter.share.phoneMake}','${glitter.share.phoneModel}','${glitter.share.phoneVersion}')`
            console.log('sql->' + sql)
            glitter.serialUtil.storeObject({
                sql: btoa(glitter.stringToUnicode(sql))
            }, date, 'insertSql', function () {
            }, function () {
            })
            glitter.command.txmemory = ''
        }
        //Program紀錄
        this.insertProgram = function (map) {
            if(!glitter.share.reconnect){glitter.share.reconnect=0}
            glitter.share.bleUtil.isConnect({
                callback: function (response) {
                    if (!response.result) {
                        map.error = "bleDisconnect"
                    }
                    let protoCal = glitter.mmyInterFace.selectMMY["Direct Fit"]
                    let protoCalVersion = glitter.publicBeans.onlineVersion.s19List[protoCal]
                    let mmyVersion = glitter.publicBeans.localVersion.mmyVersion
                    let lfPower = glitter.mmyInterFace.selectMMY["OG LF Protocol Num"]
                    let date = (new Date()).format("yyyy-MM-dd hh:mm:ss").replace(".0", "")
                    let programSize = glitter.mmyInterFace.selectMMY.programSize
                    var sql = `insert into \`oglitememory\`.transfermemory (tx, errortype, serialnumber, make, model,
                                                                            year, account, number, devicetype,
                                                                            directfit, sensorType, files19, sensorID,
                                                                            apkVersion, \`database\`, harewareVersion,
                                                                            ip, country, state, exTime, inTire,
                                                                            moduleVersion, retry, lowBattery,time,phoneMake,phoneModel,phoneVersion)
                               values ('${glitter.command.txmemory}', '${map.error}', '${glitter.publicBeans.sn}',
                                       '${glitter.mmyInterFace.selectMake}', '${glitter.mmyInterFace.selectModel}',
                                       '${glitter.mmyInterFace.selectYear}',
                                       '${glitter.publicBeans.account}', ${programSize},
                                       '${(glitter.deviceType === glitter.deviceTypeEnum.Android) ? 'Android' : 'IOS'}',
                                       '${protoCal}', 'S3', '${protoCalVersion}', '${JSON.stringify(map.id)}',
                                       '${glitter.share.myAppVersion}'
                                          , '${mmyVersion}', '1.0', '${glitter.publicBeans.lastIP}',
                                       '${glitter.publicBeans.lastCountry}', '${glitter.publicBeans.lastState}',
                                       ${map.exTime}, ${map.inTire}, '${glitter.publicBeans.bleVersion}',
                                       ${glitter.share.reconnect}, ${glitter.share.lowBattery},'${new Date().format('yyyy-MM-dd hh:mm:ss')}','${glitter.share.phoneMake}','${glitter.share.phoneModel}','${glitter.share.phoneVersion}')
                    `
                    console.log("insert sql->" + sql)
                    glitter.serialUtil.storeObject({
                        sql: btoa(glitter.stringToUnicode(sql))
                    }, date, 'insertSql', function () {
                    }, function () {
                    })
                    glitter.command.txmemory = ''
                }
            })
        }
        //Copy紀錄
        this.insertCopy = function (map) {
            let protoCal = glitter.mmyInterFace.selectMMY["Direct Fit"]
            let protoCalVersion = glitter.publicBeans.onlineVersion.s19List[protoCal]
            let mmyVersion = glitter.publicBeans.localVersion.mmyVersion
            let lfPower = glitter.mmyInterFace.selectMMY["OG LF Protocol Num"]
            let date = (new Date()).format("yyyy-MM-dd hh:mm:ss").replace(".0", "")
            let programSize = glitter.mmyInterFace.selectMMY.programSize
            if (["success", `1-${programSize}-${programSize}`].indexOf(map.error) === -1) {
                glitter.serialUtil.storeObject({
                    errorTitle: (glitter.deviceType === glitter.deviceTypeEnum.Android) ? `OGlite(Android) Program False->${protoCal}` : `OGlite(IOS) Program False->${protoCal}`,
                    mmy: `${glitter.mmyInterFace.selectMake}/${glitter.mmyInterFace.selectModel}/${glitter.mmyInterFace.selectYear}`,
                    directFit: protoCal,
                    account: glitter.publicBeans.account,
                    errorType: map.error,
                    tx: glitter.command.txmemory,
                    time: date,
                    harwareVersion: '2019002',
                    softVersion: glitter.share.myAppVersion,
                    fwVersion: glitter.publicBeans.mcuVersion
                }, date, "ErrorMessageJson", function () {
                }, function () {
                })
            }
            var sql = `insert into \`oglitememory\`.copy_result (serialnumber, account, tx, make, model, year, number,
                                                                 errortype, devicetype, sensorType,
                                                                 files19, directfit, ip, \`database\`, apkVersion,
                                                                 harewareVersion, country, state,
                                                                 exTime,sensorID,updatesensorID,moduleVersion,time,phoneMake,phoneModel,phoneVersion)
                       values ('${glitter.publicBeans.sn}', '${glitter.publicBeans.account}', '${glitter.command.txmemory}',
                               '${glitter.mmyInterFace.selectMake}', '${glitter.mmyInterFace.selectModel}',
                               '${glitter.mmyInterFace.selectYear}',
                               ${programSize}, '${map.error}',
                               '${(glitter.deviceType === glitter.deviceTypeEnum.Android) ? 'Android' : 'IOS'}', 'S3',
                               '${protoCalVersion}', '${protoCal}', '${glitter.publicBeans.lastIP}', '${mmyVersion}',
                               '${glitter.share.myAppVersion}', '1.0', '${glitter.publicBeans.lastCountry}',
                               '${glitter.publicBeans.lastState}', ${map.exTime},'${map.original}','${map.new}','${glitter.publicBeans.bleVersion}','${new Date().format('yyyy-MM-dd hh:mm:ss')}','${glitter.share.phoneMake}','${glitter.share.phoneModel}','${glitter.share.phoneVersion}')`
            console.log('sql---' + sql)
            glitter.serialUtil.storeObject({
                sql: btoa(glitter.stringToUnicode(sql))
            }, date, 'insertSql', function () {
            }, function () {
            })
            glitter.command.txmemory = ''
        }

        this.insertSql = function (sql, name) {
            $.ajax({
                type: "POST",
                url: glitter.fileModelInterFace.domain + "/exsql",
                data: sql,
                timeout: 1000 * 30,
                success: function (data) {
                    glitter.serialUtil.deleteObject(name, 'insertSql', function () {
                    }, function () {
                    })
                },
                error: function (data) {
                }
            });
        }
    }
}

//取得用戶ＩＰ
function getIpAddress() {
    $.ajax({
        type: "GET",
        url: "https://ifconfig.me/",
        timeout: 1000 * 30,
        success: function (data) {
            glitter.publicBeans.lastIP = data.split("ip_address\">")[1].split("</strong>")[0]
            glitter.storePublicBeans()
            getArea(glitter.publicBeans.lastIP)
        },
        error: function (data) {
            setTimeout(function () {
                getIpAddress()
            }, 3000)
        }
    });
}

getIpAddress()

//取得用戶位置
function getArea(ip) {
    glitter.postRequest("PublicLogic","getIpAddressInfo",{ip:ip},
        function (response){
            glitter.publicBeans.lastCountry =response.data.region
            glitter.publicBeans.lastState = response.data.city
            glitter.storePublicBeans()
        },1000)
}


//插入SQL紀錄
setInterval(function () {
    try {
        glitter.serialUtil.listObject('insertSql', 20, function (data) {
            data.map(function (it) {
                if (JSON.parse(it.data).sql !== undefined) {
                    console.log("convertbefo->" + atob(JSON.parse(it.data).sql))
                    console.log(glitter.unicodeToString(atob(JSON.parse(it.data).sql)))
                    glitter.mysqlDataBase.insertSql(glitter.unicodeToString(atob(JSON.parse(it.data).sql)), it.name)
                }
            })
        }, function () {
        })
    } catch (e) {
    }
}, 1000 * 10)



//記錄裝置最後資訊
setInterval(function (){
    try{
        $.ajax({
            type: "POST",
            url: glitter.fileModelInterFace.domain + "/exsql",
            data: `REPLACE INTO \`oglitememory\`.deviceinformation 
(lan,lon,serial,admin,place,mcuversion,apkversion,type,ip,Dbversion,harwareversion,area,beta,country) VALUES 
(${0},${0},${glitter.publicBeans.sn},'${glitter.publicBeans.account}','${glitter.publicBeans.lastCountry+"/"+glitter.publicBeans.lastState}','${glitter.publicBeans.mcuVersion}','${glitter.share.myAppVersion}','${(glitter.deviceType === glitter.deviceTypeEnum.Android) ? 'Android' : 'IOS'}','${glitter.publicBeans.lastIP}','${glitter.publicBeans.localVersion.mmyVersion}','${1.0}','${glitter.publicBeans.lastState}',${glitter.publicBeans.beta},'${glitter.publicBeans.lastCountry}')`,
            timeout: 1000 * 30,
            success: function (data) {
            },
            error: function (data) {
            }
        });
    }catch (e){}
},60*1000)