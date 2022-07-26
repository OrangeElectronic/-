"use strict";

function onCreate() {
    //Determine which page you want to jump to!!
    // glitter.setHome('widget/seekbar.html','seekbar',{})
    // return
    //glitter.setHome('page/Page_Logo_Show.html','Page_Logo_Show',{})
    //**
    glitter.demo = (glitter.deviceType === glitter.deviceTypeEnum.Web) || false
    glitter.app="HT430"
    //glitter.app="HT471A"

    glitter.titleBarData = {
        //text: 'HT430',
        text: 'HT430',
        withBack: false,
        withHome: false,
        withSetting: false,
        bleConnect: false,
        update: function () {
        }
    }
    glitter.setSystem = {setLanguage:"en",keepScreen: "false", engineer: "false",firmware1:"---",firmware2:"---",device_name:"---",appVersion:"---"}

    //callback多個回調的控制()
    glitter.callbacks = jQuery.Callbacks()
    glitter.share.titleButtonCallback=[{},{}]
    var titleButtonPage=""
    glitter.titleButton = function (screen,page) {
        console.log("titleButtonScreen:"+screen+","+titleButtonPage+"="+page)

        console.log("titleButtonScreen:"+JSON.stringify(glitter.share.titleButtonCallback)+" length")

        glitter.share.titleButtonCallback.map(function (result){
            console.log("titleButtonScreen:"+result.pageTag)
            if(result.pageTag===page){
                result.click_callback(screen)
            }
        })
    }
    // glitter.titleButton = function (screen,page) {
    //     console.log("titleButtonScreen:"+screen+","+titleButtonPage+"="+page)
    //     //if(page===titleButtonPage){
    //         var pageArray=[]
    //
    //         // for(var i=0;i<glitter.share.titleButtonCallback.length;i++){
    //         //     console.log("titleButtonCallback:"+glitter.share.titleButtonCallback[i].pageTag)
    //         //     if(glitter.share.titleButtonCallback[i].pageTag===page){
    //         //         pageArray=pageArray.concat(i)
    //         //     }
    //         // }
    //         // if(pageArray.length>=2){
    //         //     console.log("pageTag:"+pageArray[0])
    //         //     //glitter.share.titleButtonCallback.splice(pageArray[0],1)
    //         //     glitter.share.titleButtonCallback = glitter.share.titleButtonCallback.filter(function (value, index, arrSelf){
    //         //         return pageArray[0]!==index
    //         //     })
    //         // }
    //
    //     // var callback_length = glitter.share.titleButtonCallback.length
    //      console.log("titleButtonScreen:"+JSON.stringify(glitter.share.titleButtonCallback)+" length")
    //     // if(callback_length>1 && glitter.share.titleButtonCallback[0].pageTag!==page){
    //     //     var arrayCallback = []
    //     //     arrayCallback = glitter.share.titleButtonCallback.filter(function (value, index, arrSelf){
    //     //         return (callback_length-1)===index
    //     //     })
    //     //     glitter.share.titleButtonCallback = arrayCallback
    //     // }
    //
    //         glitter.share.titleButtonCallback.map(function (result){
    //             console.log("titleButtonScreen:"+result.pageTag)
    //             if(result.pageTag===page){
    //                 result.click_callback(screen)
    //             }
    //         })
    //     //}
    // }

    glitter.share.orientationPageCallback=[]
    glitter.share.orientationPageCallback.push({callback:function (screen) {
        glitter.screenPage=screen
        console.log("orientationPage:"+screen)
    },tag:"Entry"})
    glitter.orientationPage = function (screen,page) {
        var pageArray = []
        var pageIndex = []

        glitter.share.orientationPageCallback.map(function (result,index){
            if(pageArray.indexOf(result.tag)===-1){
                pageArray = pageArray.concat(result.tag)
                //result.callback(screen)
            }else{
                pageIndex = pageIndex.concat(pageArray.indexOf(result.tag))
            }
        })

        glitter.share.orientationPageCallback = glitter.share.orientationPageCallback.filter(function (value, index, arrSelf) {
            if(pageIndex.indexOf(index)===-1){
                value.callback(screen)
            }
            return pageIndex.indexOf(index)===-1
        })

    }

    var  vertial=false
    window.addEventListener('deviceorientation', handleFunc, false);
    function handleFunc(evnet){
        var alpha = event.alpha;
        var beta = event.beta;
        var gamma = event.gamma;

        //console.log("device_alpha:"+alpha)
        //console.log("device_beta:"+beta)
        //console.log("device_gamma:"+gamma)

        if(alpha > 45 && alpha < 135){
            if(!vertial) {
                vertial = true
                //glitter.orientationPage("PORTRAIT")
            }
        }else if(alpha < 45 || alpha > 315){
            if(vertial){
                vertial=false
                //glitter.orientationPage("LANDSCAPE")
            }
        }else if(alpha > 225 && alpha < 315){
            if(!vertial) {
                vertial = true
                //glitter.orientationPage("PORTRAIT")
            }
        }else if(alpha > 135 && alpha < 255){
            if(vertial){
                vertial=false
                //glitter.orientationPage("LANDSCAPE")
            }
        }

    }

    //多國語言載入，以及語言的控制(有運用底層SQLite)
    function initLanguage(callback) {
        if(glitter.deviceType === glitter.deviceTypeEnum.Web){
            callback()
            return
        }

        //rout為要預載的檔案路徑[以Glitter專案包作為相對路徑，例："database/test.db"]
        //name為資料庫名稱，(當資料庫不存在時則會進行創建，存在則會覆蓋)
        glitter.runJsInterFace("DataBase_InitByAssets", {
            rout: "alllan.db",
            name: "alllan"
        }, function (response) {
            if (response.result) {
                var sqls = "select * from `language`"
                //string為sql語法
                //name為資料庫名稱，(當資料庫不存在時則會進行創建)
                glitter.runJsInterFace("DataBase_Query",
                    {string: sqls, name: "alllan"},
                    function (language) {
                        if (language.result) {
                            //console.log(JSON.stringify(language.data))
                            glitter.language = language.data
                            callback()
                            //console.log(glitter.getLanguage("3"))
                        } else {
                            initLanguage(callback)
                            console.log(language.result)
                        }
                    })
            } else {
                initLanguage(callback)
                console.log(response.result)
            }
        })

        // //glitter.setSystem.setLanguage = "en"
        // glitter.getPro("language", function (result) {
        //     console.log("language:" + result.data)
        //     if (result.data !== undefined) {
        //         glitter.setSystem.setLanguage = result.data
        //     }else{
        //         glitter.setSystem.setLanguage = "en"
        //     }
        //     glitter.getLanguage = function (id) {
        //         return glitter.language.filter(function (value, index, arrSelf) {
        //             return value.id === id
        //         })[0][glitter.setSystem.setLanguage]
        //     }
        // })

    }

    //加進要全域的js，以及載入未上傳成功的資料
    function initScript(callback) {

        glitter.addMtScript(['until/BleManager.js', 'until/Command.js','until/Home_Command.js', 'jslib/MysqDatabase.js', 'jslib/base64.js'], function () {
            //glitter.setTitleBar("titleBar.html")
            //glitter.goBack()
            if(glitter.demo){
                callback()
                return
            }

            //調用底層的手機資料
            glitter.devicePhone()
            //載入未上傳成功的資料
            glitter.getPro("command_logdata", function (result) {
                console.log("command_logdata:" + result.data)
                //glitter.uploadData.command_logdata = result.data
                glitter.updateMemmory({logdata: result.data})
                glitter.setPro("command_logdata", "")
                glitter.uploadData.command_logdata = ""
            })

            callback()
        }, function () {
            initScript(callback)
        })
    }

    //程式開始位置*
    initLanguage(function () {
        initScript(function () {
            //回調資料庫的語言(此資料庫已存成JSON格式)
            glitter.getLanguage = function (id) {
                if(glitter.deviceType === glitter.deviceTypeEnum.Web){
                    return id
                }

                return glitter.language.filter(function (value, index, arrSelf) {
                    return value.id === id
                })[0][glitter.setSystem.setLanguage]
            }

            //初始值設定
            setInit()
            //獲得紀錄
            getData()
            //設定單位
            setUnit()
            glitter.setHome('page/Page_Logo_Show.html', 'Page_Logo_Show', {})
            //glitter.setHome('page/Page_Setting_TPMS.html', 'Page_Setting_TPMS', {})
        })
    })

    //聲音控制(有運用到底層)
    glitter.playSound =function(bool){
        //audio.play()

        glitter.runJsInterFace("playSound", {play:bool}, function (response) {
            // if(bool){
            //     glitter.runJsInterFace("changeSound", {music:glitter.tpmsValue.music}, function (response) {
            //
            //     })
            // }
        })

        // var x = document.getElementById("myAudio");
        // console.log("playSound:"+x.id)
        //
        // if(x.muted){
        //     x.muted=false
        // }
        //
        // if (x.paused) { //判讀是否播放
        //     x.paused=false;
        //     x.play(); //沒有就播放
        // }

    }

    //複製goBack，應用於不同返回頁面使用
    glitter.copyGoBack = glitter.goBack.clone()
    //glitter.copytitleButton = glitter.titleButton.clone()
    glitter.changePageListener = function (tag) {
        console.log("changePage:" + tag)
        glitter.changePageTag = tag

        //是否開啟畫面旋轉(有運用到底層)
        function turnScreen(open){
            glitter.runJsInterFace("turnScreen", {open:open}, function (response) {
                console.log("turnScreen:"+response.turn)
            })
        }

        if (tag !== "Page_Setting_TPMS") {
            glitter.goBack = glitter.copyGoBack.clone()
        }
        titleButtonPage=""
        glitter.titleBarData.open = true
        glitter.page_logo_show_ble = false
        glitter.page_show_truck_ble = false
        glitter.page_home_sreen_ble = false
        glitter.changeTitlePage=false
        turnScreen(true)
        switch (tag) {
            case "Page_Logo_Show":
                glitter.page_logo_show_ble = true
                turnScreen(false)
                glitter.titleBarData.text = "Page_Logo_Show"
                glitter.titleBarData.open = false
                break
            case "Page_Show_Truck":
                turnScreen(false)
                glitter.playSound("false")
                glitter.page_show_truck_ble = true
                glitter.setTitleBar("titleBar.html")
                glitter.bleAddress = ""
                glitter.titleBarData.withBack = false
                //選擇裝置
                glitter.titleBarData.text = "17"
                glitter.titleBarData.withHome = false
                glitter.titleBarData.withSetting = true
                glitter.changeLanguage()
                break
            case "Page_Home_Sreen":
                //glitter.titleButton = function () {}
                //glitter.titleButton = glitter.copytitleButton
                glitter.select_position=0
                titleButtonPage="home"
                glitter.page_home_sreen_ble = true
                glitter.titleBarData.withBack = true
                //主畫面
                glitter.titleBarData.text = "25"
                glitter.titleBarData.withHome = false
                glitter.titleBarData.withSetting = true
                glitter.changeLanguage()

                glitter.goBack = function () {
                    glitter.runJsInterFace("Glitter_BLE_IsConnect", {},
                        function (response) {
                            console.log(response.result)
                            if (response.result || glitter.demo) {
                                //斷開連線?
                                glitter.openDiaLog('dialog/Dia_Info_Bt.html', 'Dia_Info_Bt', false, false, {
                                    title: glitter.getLanguage("32"),
                                    cancel: function () {
                                        glitter.closeDiaLog()
                                    },
                                    ok: function () {
                                        glitter.closeDiaLog()
                                        glitter.goBack = glitter.copyGoBack.clone()
                                        glitter.setHome('page/Page_Show_Truck.html', 'Page_Show_Truck', {})
                                        glitter.runJsInterFace("Glitter_BLE_DisConnect", {},
                                            function (response) {
                                                console.log(response.result)
                                            })
                                    }
                                })
                            } else {
                                glitter.goBack = glitter.copyGoBack.clone()
                                glitter.setHome('page/Page_Show_Truck.html', 'Page_Show_Truck', {})
                                //glitter.goBack()
                            }
                        })
                }
                break
            case "Page_Setting_Function":
                //callbacks.remove(glitter.titleButton)
                //glitter.titleButton = function () {}
                //glitter.titleButton = glitter.copytitleButton
                glitter.select_position=0
                titleButtonPage="setting"
                console.log("titleButtonScreen:Page_Setting_Function")
                glitter.changeTitlePage = true
                glitter.titleBarData.withBack = true
                // 輪胎設定
                glitter.titleBarData.text = "36"
                glitter.titleBarData.withHome = false
                glitter.titleBarData.withSetting = false
                break
            case "Page_Setting_Car":
                //callbacks.remove(glitter.titleButton)
                //glitter.titleButton = function () {}
                //glitter.titleButton = glitter.copytitleButton

                glitter.titleBarData.withBack = true
                // 輪胎設定
                glitter.titleBarData.text = "36"
                glitter.titleBarData.withHome = false
                glitter.titleBarData.withSetting = false
                break
            case "Page_Setting_Enter_ID":
                glitter.titleBarData.withBack = true
                //ID學碼
                var titleName="43"
                if(glitter.enter_ID === glitter.key_In_ID){
                    titleName="43"
                }else if(glitter.enter_ID === glitter.trigger_ID){
                    titleName="122"
                }else if(glitter.enter_ID === glitter.change_ID){
                    titleName="40"
                }
                glitter.titleBarData.text = titleName
                glitter.titleBarData.withHome = true
                glitter.titleBarData.withSetting = false
                break
            case "Page_Setting_RxSet":
                glitter.titleBarData.withBack = true
                //接收器設定
                glitter.titleBarData.text = "102"
                glitter.titleBarData.withHome = true
                glitter.titleBarData.withSetting = false
                break
            case "Page_Setting_TirePositionSet":
                // setLanguages("configuration",function (data) {
                //     glitter.titleBarData.text=data
                // })
                glitter.titleBarData.withBack = true
                //輪胎配置
                glitter.titleBarData.text = "39"
                glitter.titleBarData.withHome = true
                glitter.titleBarData.withSetting = false
                break
            case "Page_Setting_TPMS":
                glitter.titleBarData.withBack = true
                //TPMS 設定
                glitter.titleBarData.text = "41"
                glitter.titleBarData.withHome = true
                glitter.titleBarData.withSetting = false
                glitter.goBack_TPMS()
                break
            case "Page_Setting_Other":
                glitter.goBack = glitter.goMenu
                glitter.titleBarData.withBack = true
                //其他設定
                glitter.titleBarData.text = "44"
                glitter.titleBarData.withHome = true
                glitter.titleBarData.withSetting = false
                break
        }
        glitter.titleBarData.update()
    }


    // glitter.getPro("command_logdata", function (result){
    //     console.log("command_logdata:"+result.data)
    //     //glitter.uploadData.command_logdata = result.data
    //     glitter.updateMemmory({logdata:result.data})
    //     glitter.setPro("command_logdata","")
    //     glitter.uploadData.command_logdata = ""
    // })


}

//初始值設定
function setInit(){

    glitter.uploadData = {
        uid: "---",
        ble_serial: "",
        wheel: ["","",""],
        unit: {},
        rx_count: "",
        function: "",
        ble_connect: "",
        ble_rssi: "",
        command_memory: "",
        command_logdata: ""
    }
    glitter.tpmsValue = {
        pressure_Unit: ["", "kPa", "kPa"],
        temperature_Unit: ["", "°C", "°C"],
        high_pressure: [0, 156, 156],
        low_pressure: [0, 120, 120],
        high_temperature: [0, 60, 60],
        //手動關閉與警報紀錄比對是否關閉
        playSound_Alarm: true,
        music:"01"
    }

    glitter.updateData = {CREATE_PAGE:"CREATE_PAGE",FIRMWARE2:"FIRMWARE2",CAR_ID:"CAR_ID",TIRE_ROW:"TIRE_ROW",SENSOR_ID:"SENSOR_ID",TIRE_STATUS:"TIRE_STATUS",
        CONNECT_RSSI:"CONNECT_RSSI",RSSI_POWER:"RSSI_POWER",TPMS:"TPMS_VALUE"}
    //前車 拖車
    //glitter.tireShow = {titleText:["26","27"],titleBackground:['#FF910B','coral'],titleColor:'white',carID:["","",""],connectRSSI:""}
    glitter.home_init = function (callback){
        glitter.tireShow = {titleText:["26","27"],titleBackground:['#FF910B','coral'],titleColor:'white',carID:["","",""],connectRSSI:""}
        glitter.tireStatus = [[],[],[]]
        //在開啟此頁面時，先創建輪胎狀態
        glitter.tireStatus[1] = glitter.tireStatus[1].concat({})
        glitter.tireStatus[2] = glitter.tireStatus[2].concat({})
        for(var i=1;i<=16;i++){
            var pre=parseInt("FEFE",16)
            var tem=parseInt("FE",16)-50
            // if(glitter.app==="HT450"){
            //     pre=parseInt("FE",16)*6
            // }
            switch (glitter.app){
                case "HT430":
                    pre=parseInt("FE",16)*6
                    break
                default:
                    pre=parseInt("FEFE",16)
                    break
            }

            //closeValue：開啟與關閉警報聲的值
            glitter.tireStatus[1] = glitter.tireStatus[1].concat({id:"",pre:pre,tem:tem,bat:27,open:false,alarm:false})
            glitter.tireStatus[2] = glitter.tireStatus[2].concat({id:"",pre:pre,tem:tem,bat:27,open:false,alarm:false})
        }
        glitter.playSound("false")
        if(glitter.share.home!==undefined){
            glitter.share.home.updatePage(glitter.updateData.CREATE_PAGE)
        }
        callback()
    }
    //Sensor恢復成預設
    glitter.init_TireStatus = function (car,position) {
        var cartype=parseInt(car)
        var pre=parseInt("FEFE",16)
        var tem=parseInt("FE",16)-50

        switch (glitter.app){
            case "HT430":
                pre=parseInt("FE",16)*6
                break
            default:
                pre=parseInt("FEFE",16)
                break
        }
        glitter.tireStatus[cartype][position].pre = pre
        glitter.tireStatus[cartype][position].tem = tem
        glitter.tireStatus[cartype][position].bat = 27
        glitter.share.home.updatePage(glitter.updateData.CREATE_PAGE)
    }
    glitter.home_init(function () {

    })
}

//獲得紀錄
function getData(){
    if(glitter.demo){
        return
    }

    console.log("getData:true")
    glitter.getPro("language", function (result) {
        console.log("language:" + result.data)
        if (result.data !== undefined) {
            glitter.setSystem.setLanguage = result.data
        }else{
            glitter.setSystem.setLanguage = "en"
        }
    })
    glitter.getPro("keepScreen", function (result) {
        console.log("keepScreen:" + result.data)
        // if (result.data === undefined) {
        //     glitter.setSystem.keepScreen = false
        // }else{
        //     glitter.setSystem.keepScreen = result.data
        // }
        if (result.data !== undefined) {
            glitter.setSystem.keepScreen = result.data
        }

        glitter.runJsInterFace("keepScreen", {keep:glitter.setSystem.keepScreen}, function (response) {

        })
    })
    glitter.getPro("engineer", function (result) {
        console.log("engineer:" + result.data)
        if (result.data !== undefined) {
            glitter.setSystem.engineer = result.data
        }
    })
    console.log("Entry:true")
    glitter.getPro("pressure_Unit", function (unit) {
        console.log("pressure_Unit1:" + unit.data)
        if (unit.data !== undefined) {
            glitter.tpmsValue.pressure_Unit = JSON.parse(unit.data)
            console.log("pressure_Unit2:" + glitter.tpmsValue.pressure_Unit)
        }
    })
    glitter.getPro("temperature_Unit", function (unit) {
        console.log("temperature_Unit:" + unit.data)
        if (unit.data !== undefined) {
            glitter.tpmsValue.temperature_Unit = JSON.parse(unit.data)
        }
    })
    glitter.getPro("setNotice", function (result) {
        console.log("setNotice:" + result.data)

        if (result.data !== undefined) {
            if(result.data==="true"){
                glitter.tpmsValue.playSound_Alarm = true
            }else{
                glitter.tpmsValue.playSound_Alarm = false
            }
        }else{
            glitter.tpmsValue.playSound_Alarm = true
        }

        //glitter.playSound(glitter.tpmsValue.playSound_Alarm.toString())
    })

    glitter.getPro("setMusic", function (result) {
        console.log("setMusic:" + result.data)

        if (result.data !== undefined) {
            glitter.tpmsValue.music = result.data
        }else{
            glitter.tpmsValue.music = "01"
        }

        glitter.runJsInterFace("changeSound", {music:glitter.tpmsValue.music}, function (response) {

        })
    })

}

//單位設定
function setUnit() {
    glitter.pre_Unit = function (pre_value, unit) {
        if (pre_value === '') {
            return '&nbsp'
        }
        var pre = pre_value
        // if(range==="HIGH"){
        //     dataTPMS.high_pressure = pre
        // }else if(range==="LOW"){
        //     dataTPMS.low_pressure = pre
        // }

        if (unit === "bar") {
            pre = pre_value / 100
            pre = Math.round(pre * 10) / 10
        } else if (unit === "kPa") {
            pre = pre_value
        } else if (unit === "psi") {
            pre = pre_value / 6.89
            pre = Math.round(pre)
        }
        return pre
    }
    glitter.tem_Unit = function (tem_value, unit) {
        if (tem_value === '') {
            return '&nbsp'
        }

        var tem = tem_value
        //dataTPMS.high_temperature = tem
        if (unit === "°C") {
            tem = tem_value
        } else if (unit === "°F") {
            tem = (parseFloat(tem_value) * 9 / 5) + 32
        }
        return Math.round(tem)
    }
}


Function.prototype.clone = function () {
    var that = this;
    var temp = function temporary() {
        return that.apply(this, arguments);
    };
    for (var key in this) {
        if (this.hasOwnProperty(key)) {
            temp[key] = this[key];
        }
    }
    return temp;
};







