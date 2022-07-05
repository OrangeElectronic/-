//這邊設定你的全域變數
function initPublicBeans(callback) {
    glitter.publicBeans = {
        //用戶帳號
        account: '',
        //用戶密碼
        password: '',
        //地區
        country: 'EU',
        //語言
        language: 'en',
        //是否為Beta版本
        beta: false,
        //本地版本
        localVersion: "NA",
        //當前最新版本
        onlineVersion: 'NA',
        //Ble版本號
        bleVersion: 'NA',
        //Fw版本號
        mcuVersion: 'NA',
        //SN
        sn: '',
        //睡眠時間
        sleepTime: 900,
        //壓力單位(kpa,psi,bar)
        pressure: 'kpa',
        //溫度單位(C/F)
        tem: 'C',
        //進制單位(auto/dec/hex)
        numeral: 'auto',
        //是否開啟聲音
        playSound: true,
        //上次取得的ＩＰ位置
        lastIP: '',
        //上次取得的國家
        lastCountry: '',
        //上次取得的State
        lastState: '',
        //Bootloader版本號
        bootloaderVersion: undefined,
        //mail資料
        mail: ''
    }

    //MMY資料庫初始位置載入
    glitter.runJsInterFace("DataBase_InitByLocal", {
        rout: "mmy.db",
        name: "mmy"
    }, function (response) {
    })
    //HSN/TSN 資料庫初始位置載入
    glitter.runJsInterFace("DataBase_InitByLocal", {
        rout: "hsn.db",
        name: "mmy2"
    }, function (response) {

    })

    glitter.dataBase.init("publicBeans", function () {
        glitter.serialUtil.getObject("publicBeans", "publicBeans", function (result) {
            if (result !== undefined) {
                glitter.copyObj(glitter.publicBeans, result)
            }
            if (glitter.share.cycleUpdate) {
                glitter.publicBeans.beta = true
            }
            if (glitter.publicBeans.sleepTime === undefined) {
                glitter.publicBeans.sleepTime = 300
            }
            glitter.addObjObserver(glitter.publicBeans, function (result) {
                console.log("storePublicBeans")
                glitter.serialUtil.storeObject(result, "publicBeans", "publicBeans", function () {
                }, () => {
                })
            })
            //儲存PublicBeans
            glitter.storePublicBeans = function () {
                glitter.serialUtil.storeObject(glitter.publicBeans, "publicBeans", "publicBeans", function () {
                }, () => {
                })
            }
            //取得多國語言
            glitter.runJsInterFace("FileManager_CheckFileExists", {
                route: "language.db"
            }, function (response) {
                var result = response.result

                function loadLocal() {
                    glitter.runJsInterFace("DataBase_InitByAssets", {
                        rout: "alllan.db",
                        name: "language"
                    }, function (response) {
                        if (response.result) {
                            glitter.runJsInterFace("DataBase_Query",
                                {
                                    string: `select ${'`'}${glitter.publicBeans.language}${'`'}, id
                                             from language `, name: "language"
                                },
                                function (response) {
                                    if (response.result) {
                                        var data = response.data
                                        glitter.getLan = function (id) {
                                            let lan = data.filter(function (item) {
                                                return item.id === `${id}`
                                            })
                                            if (lan.length > 0) {
                                                return lan[0][glitter.publicBeans.language]
                                            } else {
                                                return "" + id
                                            }
                                        }
                                        glitter.getLan = function (id) {
                                            let lan = data.filter(function (item) {
                                                return item.id === `${id}`
                                            })
                                            if (lan.length > 0) {
                                                return lan[0][glitter.publicBeans.language]
                                            } else {
                                                return "" + id
                                            }
                                        }
                                        callback()
                                    } else {
                                        console.log('languageInitError')
                                    }
                                })
                        } else {
                            console.log('languageInitError')
                        }
                    })
                }

                function loadFile() {
                    glitter.runJsInterFace("DataBase_InitByLocal", {
                        rout: "language.db",
                        name: "language"
                    }, function (response) {
                        if (response.result) {
                            glitter.runJsInterFace("DataBase_Query",
                                {
                                    string: `select ${'`'}${glitter.publicBeans.language}${'`'}, id
                                             from language `, name: "language"
                                },
                                function (response) {
                                    if (response.result) {
                                        var data = response.data
                                        glitter.getLan = function (id) {
                                            let lan = data.filter(function (item) {
                                                return item.id === `${id}`
                                            })
                                            if (lan.length > 0) {
                                                return lan[0][glitter.publicBeans.language]
                                            } else {
                                                return "" + id
                                            }
                                        }
                                        callback()
                                    } else {
                                        loadLocal()
                                    }
                                })
                        } else {
                            loadLocal()
                        }
                    })

                }

                if (result) {
                    loadFile()
                } else {
                    loadLocal()
                }
            })

        }, () => {
            console.log("initPublicBeansError3")
        })
    }, () => {
        console.log("initPublicBeansError4")
    })
}


//選擇的按鈕
glitter.selectFunction = {
    select: undefined,
    enum: {
        ReadSensor: 'ReadSensor',
        Program: 'Program',
        IdCopy: 'IdCopy',
        ObdRelearn: 'ObdRelearn',
        IdCopy_OBD: 'IdCopy_OBD',
        Relearn_Procedure: 'Relearn_Procedure',
        Store_Tire: 'Store_Tire',
        Online_Shopping: 'Online_Shopping',
        Manual: 'Manual',
        Setting: 'Setting',
        OSOM: 'OSOM'
    },
    switchToPage: function () {
        glitter.command.goState(glitter.command.BootloaderState.Og_App, function () {
        })


        function executeChange() {
            //當判斷選擇的車種為Indirect時跳轉至學馬模式，反之跳轉至對應選擇的按鈕
            if ((glitter.mmyInterFace.selectMMY["Direct Fit"]==='INDIRECT' || glitter.mmyInterFace.selectMMY["Relearn code"].indexOf('INDIRECT') !== -1) && (glitter.selectFunction.select !== glitter.selectFunction.enum.OSOM)) {
                glitter.changePage('page/Page_Comic_Relearm_Procedure.html', 'Page_Comic_Relearm_Procedure', true, {})
                return
            }
            switch (glitter.selectFunction.select) {
                case glitter.selectFunction.enum.ReadSensor:
                    if (glitter.share.rftest) {
                        glitter.changePage('page/Page_ReadSensor_RF_TEST.html', 'Page_ReadSensor_RF_TEST', true, {})
                    } else {
                        glitter.changePage('page/Page_ReadSensor.html', 'Page_ReadSensor', true, {})
                    }
                    break
                case glitter.selectFunction.enum.Program:
                    glitter.changePage('page/Page_Number_Choice.html', 'Page_Number_Choice', true, {})
                    break
                case glitter.selectFunction.enum.IdCopy:
                    glitter.changePage('page/Page_Idcopy_Selection.html', 'Page_Idcopy_Selection', true, {})
                    break
                case glitter.selectFunction.enum.IdCopy_OBD:
                    glitter.changePage('page/Page_ReadSensor_ToCopy.html', 'Page_ReadSensor_ToCopy', true, {})
                    break
                case glitter.selectFunction.enum.ObdRelearn:
                    glitter.changePage('page/Page_Comic_Relearm_Procedure.html', 'Page_Comic_Relearm_Procedure', true, {
                        goBack: true
                    })
                    break
                case glitter.selectFunction.enum.Relearn_Procedure:
                    glitter.changePage('page/Page_Comic_Relearm_Procedure.html', 'Page_Comic_Relearm_Procedure', true, {})
                    break
                case glitter.selectFunction.enum.OSOM:
                    glitter.share.tireStorage.Make = glitter.mmyInterFace.selectMake
                    glitter.share.tireStorage.Model = glitter.mmyInterFace.selectModel
                    glitter.share.tireStorage.Year = glitter.mmyInterFace.selectYear
                    glitter.changePage('tireStorage/Page_Excute_Selection.html', 'Page_Excute_Selection', true, glitter.share.tireStorage)
                    break
            }
        }

        var dir = JSON.parse(glitter.mmyInterFace.selectMMY["Direct Fit and OE"])
        if (dir.length === 1) {
            glitter.mmyInterFace.selectMMY["Direct Fit"] = dir[0]['Mode']
            executeChange()
        } else {
            glitter.changePage('page/Page_Select_OE.html','Page_Select_OE',true,{
                callback:function (data){
                    glitter.mmyInterFace.selectMMY["Direct Fit"] = data['Mode']
                    executeChange()
                }
            })
        }

    }
}
