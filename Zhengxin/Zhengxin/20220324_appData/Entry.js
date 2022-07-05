"use strict";

function hexToBase64(str) {
    return btoa(String.fromCharCode.apply(null,
        str.replace(/\r|\n/g, "").replace(/([\da-fA-F]{2}) ?/g, "0x$1 ").replace(/ +$/, "").split(" "))
    );
}

function onCreate() {
    glitter.share.version = '4.1'
    glitter.bleList = []
    String.prototype.hexEncode = function () {
        // utf8 to latin1
        var s = unescape(encodeURIComponent(this))
        var h = ''
        for (var i = 0; i < s.length; i++) {
            h += s.charCodeAt(i).toString(16)
        }
        return h
    }
    initStaticView()
    glitter.addMtScript(['glitterBleLibrary/GlBle.js', 'ApiRequest.js', 'model/Md_CarData.js', 'jslib/bleManager.js', 'jslib/BleCommand.js', "crypto-js/crypto-js.js"], function () {
        glitter.command = new BleCommand()
        //封包解密
        let key = CryptoJS.enc.Hex.parse("4b505a4e486cc505ff19857929d0470c");
        glitter.deCode = function (a) {
            let decrypted = CryptoJS.AES.decrypt(hexToBase64(a), key, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.NoPadding
            });
            return CryptoJS.enc.Hex.stringify(decrypted).toUpperCase()
        }
    }, function () {
    })
    glitter.changePageListener = function (tag) {
        if (['Page_Check_Detail_OBD', 'Page_Engineer'].indexOf(tag) === -1) {
            glitter.command.clearTimer()
        }
        console.log(tag)
    }
    init()
    //OBD數據監測
    glitter.obdData = {
        data: [],
        callback: function (a) {
        }
    }
    glitter.sensorMap = {}
    glitter.ratioCaluate = {
        startTime: new Date(),
        endTime: new Date(),
        plate: '',
        sensor: [],
        triggerMap: {},
        upload: function () {
            var map = {}
            for (var a = 0; a < this.sensor.length; a++) {
                var sensorId = this.sensor[a].id
                if (this.triggerMap[sensorId] === undefined) {
                    map[sensorId] = 0
                } else {
                    map[sensorId] = this.triggerMap[sensorId]
                }
            }
            var map2 = {
                startTime: glitter.ratioCaluate.startTime,
                endTime: glitter.ratioCaluate.endTime,
                plate: glitter.ratioCaluate.plate,
                sensor: JSON.stringify(glitter.ratioCaluate.sensor),
                triggerMap: JSON.stringify(map)
            }
            apiRequest.postRequest({
                request: 'uploadTriggerRatio',
                data: JSON.stringify(map2)
            }, function () {

            }, function () {
                setTimeout(upload, 1000)
            })
        }
    }

}

function init() {
    glitter.serialUtil.deleteRout("sensorData", function () {
    }, function () {
    })
    if(glitter.deviceType===glitter.deviceTypeEnum.Web){
        glitter.webUrl = 'https://bento3.orange-electronic.com'
        glitter.addScript("model/UserInfoMation.js", function () {
            glitter.share.setLanguage('tw', function () {
                glitter.setHome('page/Page_Sign_in.html', 'Page_Sign_in', '')
            })
        })

    }else{
        glitter.addScript("model/UserInfoMation.js", function () {
            glitter.dataBase.initByFile("MMY", "database/1.0.db", function () {
                initSql()
                glitter.getPro('language',function (response){
                    var position="en"
                    var sys =  (navigator.language||navigator.userLanguage||navigator.browserLanguage||navigator.systemLanguage).toLowerCase();
                    if(sys.indexOf("tw")!==-1||sys.indexOf("zh")!==-1){  position="tw"}
                    if(response.data){position=['en', 'tw'][parseInt(response.data)]}
                    glitter.share.setLanguage(position, function () {
                        glitter.setHome('page/Page_Sign_in.html', 'Page_Sign_in', '')
                    })
                })

            }, function () {
                glitter.setHome('page/Page_Sign_in.html', 'Page_Sign_in', '')
            })
        })
    }


}

//資料庫
function initSql() {
    glitter.getMake = function (callback) {
        // 讀取資料庫資料
        glitter.dataBase.query("MMY", "select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA')  order by `Make` asc", function (data) {
                callback(data)
            },
            function () {
                glitter.getMake(callback)
            })
    }
    glitter.getModel = function (callback) {
        // 讀取資料庫資料
        glitter.dataBase.query("MMY", "select distinct `Model` from `Summary table` where `Make`='" + glitter.selectMake + "' order by `Model` asc", function (data) {
                callback(data)
            },
            function () {
                glitter.getModel(callback)
            })

    }
    glitter.getYear = function (callback) {
        // 讀取資料庫資料
        glitter.dataBase.query("MMY",
            "select distinct `Year` from `Summary table` where `Model`='" + glitter.selectModel + "' order by `Model` asc",
            function (data) {
                callback(data)
            },
            function () {
                glitter.getYear(callback)
            })
    }

    //壓力換算
    glitter.preToSetting = function (a) {
        switch (userInfoMation.punit) {
            case 0: {
                return a.toFixed(1)
            }
            case 1: {
                var num = a / 6.89;
                return num.toFixed(0);
            }
            case 2: {
                return (a / 100).toFixed(1)
            }
        }
    }

    //溫度換算
    glitter.temToSetting = function (a) {
        switch (userInfoMation.tunit) {
            case 0: {
                return a
            }
            case 1: {
                var num = a * 9 / 5 + 32;
                return num.toFixed(0);
            }
        }
    }

    //壓力換算
    glitter.preToKpa = function (a) {
        switch (userInfoMation.punit) {
            case 0: {
                return a
            }
            case 1: {
                var num = a * 6.89;
                return num.toFixed(2);
            }
            case 2: {
                return a * 100
            }
        }
    }

    //溫度換算
    glitter.temToC = function (a) {
        switch (userInfoMation.tunit) {
            case 0: {
                return a
            }
            case 1: {
                var num = 5 / 9 * (a - 32);
                return num.toFixed(0);
            }
        }
    }
}

//設定多國語言
glitter.share.setLanguage = function (lang, callback) {
    if (glitter.deviceType === glitter.deviceTypeEnum.Web) {
        glitter.postRequest('PublicLogic','getLanguage',{
            language:'tw'
        },function (response){
            if(response.data!==undefined){
                glitter.share.language={}
                response.data.map(function (data){
                    glitter.share.language[data.id]=data["tw"]
                })
            }
            glitter.getLan = function (id) {
                if (glitter.share.language[`${id}`] === undefined) {
                    return id;
                }
                return glitter.share.language[`${id}`]
            }
            glitter.getLan = function (id) {
                if (glitter.share.language[`${id}`] === undefined) {
                    return id;
                }
                return glitter.share.language[`${id}`]
            }
            callback()
        })
    }else {
        glitter.dataBase.initByFile("language", "database/language.db", function () {
            glitter.dataBase.query("language", `select ${'`'}${lang}${'`'}, id from language `, function (data) {
                glitter.getLan = function (id) {
                    let lan = data.filter(function (item) {
                        return item.id === `${id}`
                    })

                    if (lan.length > 0) {
                        return lan[0][lang]
                    } else {
                        return "" + id
                    }
                }
                glitter.getLan = function (id) {
                    let lan = data.filter(function (item) {
                        return item.id === `${id}`
                    })
                    if (lan.length > 0) {
                        return lan[0][lang]
                    } else {
                        return "" + id
                    }
                }
                callback()
            }, () => {
                console.log("initPublicBeansError1")
            })
        }, function () {
            alert('languageFalse')
        })
    }

}


//標頭View
function initStaticView() {
    //預設TitleBar
    glitter.getTitleBar = function (tit) {
        return `
<div  onclick="toEngineer+=1;if(toEngineer>=10){glitter.changePage('page/Page_Engineer.html', 'Page_Engineer', true,{});}" style="display:flex;align-items: center;height: 60px;width:100%;background-color: #002F51">
    <img src="../img/btn_back.png" style="width: 50px;height: 50px;" onclick="glitter.goBack()">
    <h3 style="color: white;flex: auto;text-align: center;margin: 0;">` + tit + `</h3>
    <img src="../img/btn_home.png"  style="width: 50px;height: 50px;" onclick="glitter.goMenu()">
    </div>
   `
    }
    //預設拖車選擇List
    glitter.rearSelect = function (e) {
        class CarType {
            constructor(name, carType, img) {
                this.name = name
                this.carType = carType
                this.img = img
            }
        }

        var itemList = [
            new CarType("4輪", 'R_4', '../img/icon_pallet_truck_4wheel_rear.png'),
            new CarType("8輪", 'R_8', '../img/icon_pallet_truck_8wheel.png'),
            // new CarType("12輪", 'R_12', '../img/icon_pallet_truck_12wheel.png'),
            // new CarType("16輪", 'R_16', '../img/icon_pallet_truck_16wheel.png'),
            // new CarType("20輪", 'R_20', '../img/icon_pallet_truck_20wheel.png')
        ]
        new GlAdapter(e, function () {
            return itemList.length
        }, function (pos) {
            return `
             <div style="height: 64px;width: calc(100% - 20px);display: flex;align-items: center;margin-left: 10px;" onclick="selectSet('` + itemList[pos].carType + `')">
         <img src="../img/tire.png" style="width: 60px;height: 60px;">
         <h3 style="color: white;line-height: 64px;font-size: 16px;flex: auto;margin-left: 5px;">` + itemList[pos].name + `</h3>
         <img src="` + itemList[pos].img + `" style="height: 64px;">
         </div>
         <div style="width: calc(100% - 20px);margin-left:10px;height: 1px;background-color: #6AC6D7;"></div>
         `
        })
    }
//預設前車選擇List
    glitter.frontSelect = function (e) {
        class CarType {
            constructor(name, carType, img) {
                this.name = name
                this.carType = carType
                this.img = img
            }
        }

        var itemList = [
            new CarType("6 輪 (拖車頭)", 'F_6_24', '../img/icon_6_wheels.png'),
            new CarType("8 輪 (拖車頭)", 'F_8_224', '../img/icon_8_wheels224.png'),
            // new CarType("10 輪 (拖車頭)", 'F_10_244', '../img/icon_10_wheels244.png'),
            new CarType("4 輪", 'N_4', '../img/icon_4_wheels.png'),
            new CarType("6 輪", 'N_6', '../img/icon_6_wheels.png'),
            new CarType("8 輪 (2*2*4)", 'N_8_224', '../img/icon_8wheels242.png'),
            new CarType("8 輪 (2*4*2)", 'N_8_242', '../img/icon_8_wheels224.png'),
            new CarType("10 輪 (2*4*4)", 'N_10_244', '../img/icon_10_wheels244.png'),
            new CarType("10 輪 (2*2*2*4)", 'N_10_2224', '../img/icon_10_wheels_2224.png'),
            new CarType("12 輪", 'N_12', '../img/icon_12_wheels.png'),
            new CarType("14輪", 'N_14', '../img/icon_14wheels.png'),
            new CarType("2輪", "N_2", '../img/motorcycle.jpg')
        ]
        new GlAdapter(e, function () {
            return itemList.length
        }, function (pos) {
            return `
             <div style="height: 64px;width: calc(100% - 20px);display: flex;align-items: center;margin-left: 10px;" onclick="selectSet('` + itemList[pos].carType + `')">
         <img src="../img/tire.png" style="width: 60px;height: 60px;">
         <h3 style="color: white;line-height: 64px;font-size: 16px;flex: auto;margin-left: 5px;">` + itemList[pos].name + `</h3>
         <img src="` + itemList[pos].img + `" style="height: 64px;">
         </div>
         <div style="width: calc(100% - 20px);margin-left:10px;height: 1px;background-color: #6AC6D7;"></div>
         `
        })
    }
}





