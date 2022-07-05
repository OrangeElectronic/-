//檔案下載Model
glitter.fileModelInterFace = {
    domain: 'https://bento2.orange-electronic.com',
    //加載進度
    progress: 0,
    //所有檔案
    allFileCount: 0,
    //加載回條
    progressCallBack: function (progress) {
    },
    //檢查新版本
    checkNewVersion: function (success, error) {
        $.ajax({
            type: "GET",
            url: `${this.domain}/getVersion?country=${glitter.publicBeans.country}&type=OG_lite&beta=${(glitter.publicBeans.beta) ? "true" : "false"}&autoRead=true&isComic=true&hsntsn=true`,
            timeout: 5000,
            success: function (data) {
                glitter.publicBeans.onlineVersion = JSON.parse(data)
                //判斷是否要使用新版燒錄黨
                glitter.fileModelInterFace.check298Code()
                success()
            },
            error: function (data) {
                error()
            }
        });
    },
    check298Code: function () {
        if(glitter.share.support298){
            if (glitter.fileModelInterFace.check298OrNot(true)) {
                glitter.publicBeans.onlineVersion.bootloaderVersion = glitter.publicBeans.onlineVersion["_298Bt"]
            }
            if(glitter.fileModelInterFace.check298OrNot()){
                glitter.publicBeans.onlineVersion.mcuVersion = glitter.publicBeans.onlineVersion["_298Fw"]
            }
        }

    },
    //判斷是否需要初次加載
    needInit: function () {
        console.log('needInit-' + (glitter.publicBeans.localVersion === 'NA') || (glitter.publicBeans.localVersion === {}))
        return (glitter.publicBeans.localVersion === 'NA') || (glitter.publicBeans.localVersion === {})
    },
    //判斷是否需要更新
    needUpdate: function () {
        var bleUpdate = glitter.publicBeans.onlineVersion.bleVersion.indexOf(glitter.publicBeans.bleVersion) === -1
        var fwUpdate = glitter.publicBeans.onlineVersion.mcuVerion.indexOf(glitter.publicBeans.mcuVersion) === -1
        var localVersion = glitter.publicBeans.localVersion
        var onlineVersion = glitter.publicBeans.onlineVersion
        return (bleUpdate) || (fwUpdate) || (localVersion.lanVersion !== onlineVersion.lanVersion) || (localVersion.mmyVersion !== onlineVersion.mmyVersion) || (localVersion.hsnTsnVersion !== onlineVersion.hsnTsnVersion)
            || (onlineVersion.bootloaderVersion.indexOf(glitter.publicBeans.bootloaderVersion) === -1)
    },
    //判斷是否要靜默更新
    needSilenceUpdate: function () {
        var localVersion = glitter.publicBeans.localVersion
        var onlineVersion = glitter.publicBeans.onlineVersion
        return (JSON.stringify(localVersion.s19List) !== JSON.stringify(onlineVersion.s19List)) ||
            (JSON.stringify(localVersion.obdList) !== JSON.stringify(onlineVersion.obdList))
    },
    //靜默更新
    onSilenceUpdate: false,
    silenceUpdate: function () {
        // alert("靜默更新開始")
        if (glitter.fileModelInterFace.onSilenceUpdate) {
            return
        }
        glitter.fileModelInterFace.onSilenceUpdate = true
        glitter.fileModelInterFace.checkS19(function (result) {
            if (result) {
                glitter.fileModelInterFace.checkOBD(function (result) {
                    // alert(`靜默更新${result}`)
                    glitter.fileModelInterFace.onSilenceUpdate = false
                    if (result) {
                        glitter.publicBeans.localVersion = glitter.publicBeans.onlineVersion
                    } else {
                        glitter.fileModelInterFace.onSilenceUpdate.silenceUpdate()
                    }
                })
            } else {
                console.log(`靜默更新失敗`)
                glitter.fileModelInterFace.onSilenceUpdate = false
                glitter.fileModelInterFace.onSilenceUpdate.silenceUpdate()
            }
        })
    },
    //開始更新
    startUpdate: function (callback, finish) {
        this.checkNewVersion(function () {
            let onlineDate = glitter.publicBeans.onlineVersion
            glitter.fileModelInterFace.allFileCount = (4 + Object.keys(glitter.publicBeans.onlineVersion.s19List).length + Object.keys(glitter.publicBeans.onlineVersion.obdList).length + Object.keys(glitter.publicBeans.onlineVersion.s18List).length) + 28
            glitter.fileModelInterFace.progress = 0
            glitter.fileModelInterFace.progress += 1
            glitter.fileModelInterFace.progressCallBack = callback
            //下載MMY
            glitter.fileModelInterFace.checkMMYUpdate(function (result) {
                if (result) {
                    glitter.fileModelInterFace.checkHsnUpdate(function (result) {
                        if (result) {
                            glitter.fileModelInterFace.progress += 1
                            callback(glitter.fileModelInterFace.progress)
                            glitter.fileModelInterFace.checkLanguage(function (result) {
                                if (result) {
                                    glitter.fileModelInterFace.progress += 1
                                    glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                                    glitter.fileModelInterFace.checkS19(function (result) {
                                        if (result) {
                                            glitter.fileModelInterFace.checkOBD(function (result) {
                                                if (result) {
                                                    glitter.fileModelInterFace.check298Code()
                                                    glitter.fileModelInterFace.checkFW(function (result) {
                                                        if (result) {
                                                            glitter.fileModelInterFace.check298Code()
                                                            glitter.fileModelInterFace.checkBootloader(function (result) {
                                                                if (result) {
                                                                    glitter.publicBeans.bootloaderVersion = glitter.publicBeans.onlineVersion.bootloaderVersion.replace('.srec', '')
                                                                    glitter.onUpdateBle = true
                                                                    glitter.canshowConnect = false
                                                                    glitter.fileModelInterFace.checkBle(function (result) {
                                                                        glitter.publicBeans.localVersion = glitter.publicBeans.onlineVersion
                                                                        finish(result)
                                                                        if (result) {
                                                                            glitter.publicBeans.bleVersion = glitter.publicBeans.onlineVersion.bleVersion
                                                                        } else {
                                                                            glitter.openDiaLog('dialog/Dia_Error_Hint.html', 'Dia_Error_Hint_Ble', false, true, "Ble-" + glitter.getLan(534), function () {
                                                                                setTimeout(function () {
                                                                                    glitter.closeDiaLog()
                                                                                    glitter.share.checkVersion()
                                                                                }, 100)
                                                                            })
                                                                        }
                                                                        glitter.onUpdateBle = false
                                                                        setTimeout(function () {
                                                                            glitter.canshowConnect = true
                                                                        }, 10 * 1000)
                                                                    })
                                                                } else {
                                                                    finish(false)
                                                                }
                                                            })
                                                        } else {
                                                            finish(false)
                                                        }
                                                    })
                                                } else {
                                                    finish(false)
                                                }
                                            })
                                        } else {
                                            finish(false)
                                        }
                                    })
                                } else {
                                    finish(false)
                                }
                            })
                        } else {
                            finish(false)
                        }
                    })
                } else {
                    finish(false)
                }
            })
        }, function () {
            finish(false)
        })
    },
    //檢查MMY是否要更新
    checkMMYUpdate: function (callback) {
        let onlineDate = glitter.publicBeans.onlineVersion
        let localData = glitter.publicBeans.localVersion
        if (onlineDate.mmyVersion !== localData.mmyVersion ) {
            glitter.downloadFile((glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Database/MMY/Comic%20version/${glitter.publicBeans.country}/${glitter.publicBeans.onlineVersion.mmyVersion}` :
                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Database/MMY/Comic%20version/${glitter.publicBeans.country}/${glitter.publicBeans.onlineVersion.mmyVersion}`
                , "mmy.db", 120 * 1000, function (result) {
                    if (result) {
                        glitter.dataBase.initByLocalFile("mmy", "mmy.db", function () {
                            callback(true)
                        }, function () {
                        })
                    } else {
                        callback(false)
                    }
                })
        } else {
            callback(true)
        }
    },
    //檢查HSN/TSN是否要更新
    checkHsnUpdate: function (callback) {
        let onlineDate = glitter.publicBeans.onlineVersion
        let localData = glitter.publicBeans.localVersion
        if (onlineDate.hsnTsnVersion !== localData.hsnTsnVersion) {
            glitter.downloadFile((glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Database/MMY/EU%20HSNTSN/${glitter.publicBeans.onlineVersion.hsnTsnVersion}` :
                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Database/MMY/EU%20HSNTSN/${glitter.publicBeans.onlineVersion.hsnTsnVersion}`
                , "hsn.db", 120 * 1000, function (result) {
                    if (result) {
                        glitter.dataBase.initByLocalFile("mmy2", "hsn.db", function () {
                            callback(true)
                        }, function () {
                        })
                    } else {
                        callback(false)
                    }
                })
        } else {
            callback(true)
        }
    },
    //檢查語言是否要更新
    checkLanguage: function (callback) {
        let onlineDate = glitter.publicBeans.onlineVersion
        let localData = glitter.publicBeans.localVersion
        if (onlineDate.lanVersion !== localData.lanVersion) {
            glitter.downloadFile((glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Language/${onlineDate.lanVersion}` :
                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Language/${onlineDate.lanVersion}`
                , "language.db", 60 * 1000, function (result) {
                    if (result) {
                        glitter.dataBase.initByLocalFile("language", "language.db", function () {
                            glitter.dataBase.query("language", `select ${'`'}${glitter.publicBeans.language}${'`'}, id
                                                                from language `, function (data) {
                                glitter.getLan = function (id) {
                                    try {
                                        let lan = data.filter(function (item) {
                                            return item.id === `${id}`
                                        })
                                        if (lan.length > 0) {
                                            return lan[0][glitter.publicBeans.language]
                                        } else {
                                            return "" + id
                                        }
                                    } catch (e) {
                                        return "" + id
                                    }

                                }
                                glitter.language = function (id) {
                                    try {
                                        let lan = data.filter(function (item) {
                                            return item.id === `${id}`
                                        })
                                        if (lan.length > 0) {
                                            return lan[0][glitter.publicBeans.language]
                                        } else {
                                            return "" + id
                                        }
                                    } catch (e) {
                                        return "" + id
                                    }

                                }
                                callback(true)
                            }, () => {
                                console.log("initPublicBeansError1")
                                callback(true)
                            })
                        }, function () {
                        })
                    } else {
                        callback(false)
                    }
                })
        } else {
            callback(true)
        }
    },
    //檢查所有s19檔案
    checkS19: function (callback) {
        let allS19List = Object.keys(glitter.publicBeans.onlineVersion.s19List)
        var excute = 0
        var complete = true

        function downS19() {
            if (excute === allS19List.length) {
                callback(complete)
            } else {
                glitter.fileModelInterFace.downloadS19(allS19List[excute], function (result) {
                    excute += 1;
                    complete = (complete && result)
                    if (result) {
                        glitter.fileModelInterFace.progress += 1
                        glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                    }
                    downS19()
                })
            }
        }

        downS19()
    },
    //下載s19檔案
    downloadS19: function (name, callback) {
        var fal = 0
        let onlineDate = glitter.publicBeans.onlineVersion.s19List

        function run() {
            glitter.downloadFile((glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Database/SensorCode/OG%20SIII/${name}/${onlineDate[name]}` :
                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Database/SensorCode/OG%20SIII/${name}/${onlineDate[name]}`
                , "s19/" + name, 5 * 1000, function (result) {
                    if (result) {
                        glitter.fileModelInterFace.progress += 1
                        glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                        callback(true)
                    } else {
                        console.log("downloadS19_False" + name)
                        fal += 1
                        if (fal === 5) {
                            callback(false)
                        } else {
                            run()
                        }
                    }
                })
        }

        run()
    },
    //檢查所有OBD檔案
    checkOBD: function (callback) {
        let allS19List = Object.keys(glitter.publicBeans.onlineVersion.obdList)
        var excute = 0
        var complete = true

        function downS19() {
            if (excute === allS19List.length) {
                callback(complete)
            } else {
                glitter.fileModelInterFace.downloadOBD(allS19List[excute], function (result) {
                    excute += 1;
                    complete = (complete && result)
                    if (result) {
                        glitter.fileModelInterFace.progress += 1
                        glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                    }
                    downS19()
                })
            }
        }

        downS19()
    },
    //檢查ＦＷ更新
    checkFW: function (callback) {
        glitter.fileModelInterFace.check298Code()
        if ((glitter.publicBeans.onlineVersion.mcuVerion.indexOf(glitter.publicBeans.mcuVersion) !== -1) && (!glitter.share.cycleUpdate)) {
            callback(true)
            return
        }
        glitter.apiRequest.getWebResources(glitter.print(function () {
            if (glitter.fileModelInterFace.check298OrNot()) {
                return (glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Firmware298/${glitter.publicBeans.onlineVersion.mcuVerion}` :
                    `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Firmware298/${glitter.publicBeans.onlineVersion.mcuVerion}`
            } else {
                return (glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Firmware/${glitter.publicBeans.onlineVersion.mcuVerion}` :
                    `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Firmware/${glitter.publicBeans.onlineVersion.mcuVerion}`
            }
        }), function (data) {
            glitter.command.updateFW(data, callback)
        }, function () {
            callback(false)
        })
    },
    //下載OBD檔案
    downloadOBD: function (name, callback) {
        var fal = 0

        function run() {
            let onlineDate = glitter.publicBeans.onlineVersion.obdList
            glitter.downloadFile((glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_Lite_OBD/${name}/${onlineDate[name]}` :
                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_Lite_OBD/${name}/${onlineDate[name]}`
                , "obd/" + name, 5 * 1000, function (result) {
                    if (result) {
                        callback(true)
                    } else {
                        console.log("downloadOBD_False" + name)
                        fal += 1;
                        if (fal === 5) {
                            callback(false)
                        } else {
                            run()
                        }
                    }
                })
        }

        run()
    },
    //檢查藍芽更新
    checkBle: function (callback) {
        if (glitter.publicBeans.onlineVersion.bleVersion.indexOf(glitter.publicBeans.bleVersion) !== -1) {
            callback(true)
            console.log("不用更新" + glitter.publicBeans.onlineVersion.bleVersion + " with " + glitter.publicBeans.bleVersion)
            return
        }
        console.log("需要更新")
        var rout = (glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Ble%20Firmware/${glitter.publicBeans.onlineVersion.bleVersion}` :
            `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Ble%20Firmware/${glitter.publicBeans.onlineVersion.bleVersion}`
        glitter.onUpdateBle = true
        glitter.command.send("0ACCCC",true)
        setTimeout(function () {
            glitter.share.bleUtil.disConnect()
            setTimeout(function () {
                glitter.runJsInterFace("updateBle", {rout: rout}, function (result) {
                    callback(result.result)
                })
            }, 1000)
        }, 200)
    },
    //檢查Bootloader更新
    checkBootloader: function (callback) {
        // callback(true)
        if (glitter.share.bootloaderUpdate) {
            glitter.fileModelInterFace.check298Code()
            let onlineVersion = glitter.publicBeans.onlineVersion.bootloaderVersion.replace('.srec', '')
            if ((onlineVersion === undefined) || (onlineVersion === glitter.publicBeans.bootloaderVersion)) {
                callback(true)
            } else {
                glitter.command.goState(glitter.command.BootloaderState.Og_App, function () {
                    glitter.apiRequest.getWebResources(glitter.print(function () {
                        if (glitter.fileModelInterFace.check298OrNot(true)) {
                            return (glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Boot298/${onlineVersion}.srec` :
                                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Boot298/${onlineVersion}.srec`
                        } else {
                            return (glitter.publicBeans.beta) ? `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Boot/${onlineVersion}.srec` :
                                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Boot/${onlineVersion}.srec`
                        }
                    }), function (data) {
                        glitter.command.updateBootloader(data, callback)
                    }, function () {
                        callback(false)
                    })
                })
            }
        } else {
            callback(true)
        }

    },
    //判斷燒錄是否跑298長度
    check298OrNot: function (bootloader) {
        if(glitter.share.support298){
            var pars = `${glitter.publicBeans.mcuVersion}`.replace('.srec', '')
            if (bootloader) {
                return ((parseInt(pars.substring(pars.length - 2, pars.length), 10) >= 34))
            } else {
                return (parseInt(`${glitter.publicBeans.bootloaderVersion}`.replace('.srec', ''), 10) >= 18)
            }
        }else{
            return false
        }

    }
}