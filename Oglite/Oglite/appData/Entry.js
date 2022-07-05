"use strict";
function onCreate() {
    glitter.share={}
    //更新檢查
    glitter.share.checkVersion=function (){
        glitter.bleUtil.manager.isConnect(function (result){
            if(result){
                if(glitter.onwaiting){
                    glitter.share.checkVersion()
                    return}
                if (glitter.fileModelInterFace.needInit()) {
                    glitter.openDiaLog('dialog/Dia_Update.html', 'Dia_Update', false, false, {}, function () {
                    })
                } else {
                    glitter.fileModelInterFace.checkNewVersion(function (){
                        if (glitter.fileModelInterFace.needUpdate()) {
                            glitter.openDiaLog('dialog/Dia_Ask_Update.html','Dia_Ask_Update',false,false,{},function (){})
                        }
                    },function (){
                        setTimeout(function (){glitter.share.checkVersion()},200)
                    })
                }
            }else{
                setTimeout(function (){glitter.share.checkVersion()},200)
            }
        })
    }
    //壓力單位換算
    //壓力換算
    glitter.preToSetting = function (a) {
        switch (glitter.publicBeans.pressure) {
            case "kpa":{
                return  a
            }
            case "psi":{
                var num = a/6.89;
                return num.toFixed(1);
            }
            case "bar":{
                return a/100
            }
            default :{
                return  a
            }
        }
    }
    //溫度換算
    glitter.temToSetting = function (a) {
        switch (glitter.publicBeans.tem) {
            case "C":{
                return a
            }
            case "F":{
                var num = a*9/5+32;
                return num.toFixed(0);
            }
            default:{
                return  a
            }
        }
    }
    glitter.canshowConnect=true
        if (glitter.deviceType === glitter.deviceTypeEnum.Web) {
            glitter.getLan = function (id) {
                return id
            }
           glitter.setTitleBar('frag/titleBar.html')
           // glitter.setHome('page/Page_Logo.html','Page_Logo',{})
           // glitter.setHome('page/Page_Program_Detail.html', 'Page_Program_Detail', {})
           // glitter.openDiaLog('dialog/Dia_Loading_Cancel.html','Dia_Loading_Cancel',false,false,{},function (){})
            glitter.setHome('page/Page_Program_Detail.html', 'Page_Program_Detail', {})
            glitter.openDiaLog('dialog/DIa_Battery_Low.html','DIa_Battery_Low',false,false,{},function (){})
        } else {
            glitter.addScript('ApiRequest.js', function () {
            glitter.apiRequest = new ApiRequest()
            glitter.bleList=[]
            glitter.addScript("jslib/bleManager.js")
            glitter.addScript("jslib/BleCommand.js",function (){
                glitter.command=new BleCommand()
            })
            initPublicBeans(function () {
                glitter.setHome('page/Page_Logo.html','Page_Logo',{})
                // alert(glitter.command.toCrc("F51C0001"))

            })
        })
        }

}


//這邊設定你的全域變數
function initPublicBeans(callback) {
    glitter.publicBeans = {
        //用戶帳號
        account:'',
        //用戶密碼
        password:'',
        //地區
        country: 'EU',
        //語言
        language: 'en',
        //是否為Beta版本
        beta: true,
        //本地版本
        localVersion: "NA",
        //當前最新版本
        onlineVersion: 'NA',
        //Ble版本號
        bleVersion:'NA',
        //Fw版本號
        mcuVersion:'NA',
        //SN
        sn:'',
        //睡眠時間
        sleepTime:300,
        //壓力單位(kpa,psi,bar)
        pressure:'kpa',
        //溫度單位(C/F)
        tem:'C',
        //進制單位(auto/dec/hex)
        numeral:'auto',
        //是否開啟聲音
        playSound:true
    }

    //MMY資料庫初始位置載入
    glitter.dataBase.initByLocalFile("mmy","mmy.db",function (){},function (){})
    glitter.dataBase.init("publicBeans", function () {
    glitter.serialUtil.getObject("publicBeans", "publicBeans", function (result) {
            if (result !== undefined) {glitter.copyObj(glitter.publicBeans,result)}
            if(glitter.publicBeans.sleepTime===undefined){glitter.publicBeans.sleepTime=300}
            glitter.addObjObserver(glitter.publicBeans, function (result) {
                console.log("storePublicBeans")
                glitter.serialUtil.storeObject(result, "publicBeans", "publicBeans", function () {},()=>{})
            })
            //儲存PublicBeans
           glitter.storePublicBeans=function (){glitter.serialUtil.storeObject(glitter.publicBeans, "publicBeans", "publicBeans", function () {},()=>{})}
            //取得多國語言
            glitter.checkFileExists("language.db",function (result){
                if(!result){
                    glitter.dataBase.initByFile("language", "alllan.db", function () {
                        glitter.dataBase.query("language", `select ${'`'}${glitter.publicBeans.language}${'`'},id from language `, function (data) {
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
                        }, ()=>{alert("initPublicBeansError1")})
                    }, ()=>{alert("initPublicBeansError2")})
                }else{
                    glitter.dataBase.initByLocalFile("language","language.db",function (){
                        glitter.dataBase.query("language", `select ${'`'}${glitter.publicBeans.language}${'`'},id from language `, function (data) {
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
                        }, ()=>{alert("initPublicBeansError1")})
                    },function (){})
                }
            })
        },()=>{alert("initPublicBeansError3")})
    },()=>{alert("initPublicBeansError4")})
}

//檔案下載Model
glitter.fileModelInterFace = {
    domain:'https://bento2.orange-electronic.com',
    //加載進度
    progress:0,
    //所有檔案
    allFileCount:0,
    //加載回條
    progressCallBack:function (progress){},
    //檢查新版本
    checkNewVersion:function (success,error){
        $.ajax({
            type: "GET",
            url: `${this.domain}/getVersion?country=${glitter.publicBeans.country}&type=OG_lite&beta=${(glitter.publicBeans.beta) ? "true" : "false"}`,
            timeout: 5000,
            success: function (data) {
                glitter.publicBeans.onlineVersion=JSON.parse(data)
                success()
            },
            error: function (data) {
                error()
            }
        });
    },
    //判斷是否需要初次加載
    needInit:function (){return (glitter.publicBeans.localVersion==='NA')||(glitter.publicBeans.localVersion==={})},
    //判斷是否需要更新
    needUpdate:function (){
        var bleUpdate=glitter.publicBeans.onlineVersion.bleVersion.indexOf(glitter.publicBeans.bleVersion)===-1
        var fwUpdate=glitter.publicBeans.onlineVersion.mcuVerion.indexOf(glitter.publicBeans.mcuVersion)===-1
        return (JSON.stringify(glitter.publicBeans.localVersion)!==JSON.stringify(glitter.publicBeans.onlineVersion))||(bleUpdate)||(fwUpdate)},
    //開始更新
    startUpdate:function (callback,finish){
        this.checkNewVersion(function (){
           let onlineDate= glitter.publicBeans.onlineVersion
           glitter.fileModelInterFace.allFileCount=  (4 + Object.keys(glitter.publicBeans.onlineVersion.s19List).length + Object.keys(glitter.publicBeans.onlineVersion.obdList).length + Object.keys(glitter.publicBeans.onlineVersion.s18List).length) + 28
           glitter.fileModelInterFace.progress=0
           glitter.fileModelInterFace.progress+=1
           glitter.fileModelInterFace.progressCallBack=callback
           //下載MMY
            glitter.fileModelInterFace.checkMMYUpdate(function (result){
           if(result){
               glitter.fileModelInterFace.progress+=1
               callback(glitter.fileModelInterFace.progress)
               glitter.fileModelInterFace.checkLanguage(function (result){
                   if(result){
                       glitter.fileModelInterFace.progress+=1
                       glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                       glitter.fileModelInterFace.checkS19(function (result){
                        if(result){
                            glitter.fileModelInterFace.checkOBD(function (result){
                                if(result){
                                    glitter.fileModelInterFace.checkFW(function (result){
                                        if(result){
                                            glitter.fileModelInterFace.checkBle(function (result){
                                                glitter.publicBeans.localVersion=glitter.publicBeans.onlineVersion
                                                finish(result)
                                                if(result){
                                                    glitter.publicBeans.bleVersion=glitter.publicBeans.onlineVersion.bleVersion
                                                    setTimeout(function (){
                                                        glitter.canshowConnect=true
                                                    },2000)
                                                }
                                            })
                                        }else{   finish(false)}
                                    })
                                }else{
                                    finish(false)
                                }
                            })
                        }else{
                            finish(false)
                        }
                    })
                   }else{finish(false)}
               })
           }else{finish(false)}})
        },function (){finish(false)})
    },
    //檢查MMY是否要更新
    checkMMYUpdate:function (callback){
        let onlineDate= glitter.publicBeans.onlineVersion
        let localData = glitter.publicBeans.localVersion
        if(onlineDate.mmyVersion!==localData.mmyVersion){
            glitter.downloadFile( (glitter.publicBeans.beta)?`${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Database/MMY/${glitter.publicBeans.country}/${glitter.publicBeans.onlineVersion.mmyVersion}` :
                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Database/MMY/${glitter.publicBeans.country}/${glitter.publicBeans.onlineVersion.mmyVersion}`
                ,"mmy.db",60*1000,function (result){
                    if(result){
                        glitter.dataBase.initByLocalFile("mmy","mmy.db",function (){
                          callback(true)
                        },function (){})
                    }else{
                        callback(false)
                    }
                })
        }else{
            callback(true)
        }
    },
    //檢查語言是否要更新
    checkLanguage:function (callback){
        let onlineDate= glitter.publicBeans.onlineVersion
        let localData = glitter.publicBeans.localVersion
        if(onlineDate.lanVersion!==localData.lanVersion){
            glitter.downloadFile( (glitter.publicBeans.beta)?`${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Language/${onlineDate.lanVersion}` :
                `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Language/${onlineDate.lanVersion}`
                ,"language.db",60*1000,function (result){
                    if(result){
                        glitter.dataBase.initByLocalFile("language","language.db",function (){
                            glitter.dataBase.query("language", `select ${'`'}${glitter.publicBeans.language}${'`'},id from language `, function (data) {
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
                                callback(true)
                            }, ()=>{alert("initPublicBeansError1")})
                        },function (){})
                    }else{
                        callback(false)
                    }
                })
        }else{
            callback(true)
        }
    },
    //檢查所有s19檔案
    checkS19:function (callback){
        let allS19List=Object.keys(glitter.publicBeans.onlineVersion.s19List)
        var excute=0
        var complete=true
        allS19List.map(function (data){
            glitter.fileModelInterFace.downloadS19(data,function (result){
                excute+=1;
                complete=(complete&&result)
                if(result){
                glitter.fileModelInterFace.progress+=1
                    glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                }
                if(excute===allS19List.length){
                    callback(complete)
                }
            })
        })
    },
    //下載s19檔案
    downloadS19:function (name,callback){
        let onlineDate= glitter.publicBeans.onlineVersion.s19List
        glitter.downloadFile( (glitter.publicBeans.beta)?`${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Database/SensorCode/SIII/${name}/${onlineDate[name]}` :
            `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Database/SensorCode/SIII/${name}/${onlineDate[name]}`
            ,"s19/"+name,5*1000,function (result){
                if(result){
                    glitter.fileModelInterFace.progress+=1
                    glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                    callback(true)
                }else{
                    console.log("downloadS19_False"+name)
                    callback(false)
                }
            })
    },
    //檢查所有OBD檔案
    checkOBD:function (callback){
        let allS19List=Object.keys(glitter.publicBeans.onlineVersion.obdList)
        var excute=0
        var complete=true
        allS19List.map(function (data){
            glitter.fileModelInterFace.downloadOBD(data,function (result){
                excute+=1;
                complete=(complete&&result)
                if(result){
                    glitter.fileModelInterFace.progress+=1
                    glitter.fileModelInterFace.progressCallBack(glitter.fileModelInterFace.progress)
                }
                if(excute===allS19List.length){
                    callback(complete)
                }
            })
        })
    },
    //檢查ＦＷ更新
    checkFW:function (callback){
        if(glitter.publicBeans.onlineVersion.mcuVerion.indexOf(glitter.publicBeans.mcuVersion)!==-1){
            callback(true)
            return
        }
        glitter.apiRequest.getWebResources((glitter.publicBeans.beta)?`${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Firmware/${glitter.publicBeans.onlineVersion.mcuVerion}` :
            `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Firmware/${glitter.publicBeans.onlineVersion.mcuVerion}`,function (data){
            glitter.command.updateFW(data,callback)
        },function (){
            callback(false)
        })
     },
    //下載OBD檔案
    downloadOBD:function (name,callback){
        let onlineDate= glitter.publicBeans.onlineVersion.obdList
        glitter.downloadFile( (glitter.publicBeans.beta)?`${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_Lite_OBD/${name}/${onlineDate[name]}` :
            `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_Lite_OBD/${name}/${onlineDate[name]}`
            ,"obd/"+name,5*1000,function (result){
                if(result){
                    callback(true)
                }else{
                    console.log("downloadOBD_False"+name)
                    callback(false)
                }
            })
    },
    //檢查藍芽更新
    checkBle:function (callback){
        if(glitter.publicBeans.onlineVersion.bleVersion.indexOf(glitter.publicBeans.bleVersion)!==-1){
            callback(true)
            return
        }
       var rout= (glitter.publicBeans.beta)?`${glitter.fileModelInterFace.domain}/Orange%20Cloud/Beta/Drive/OG_lite/Ble%20Firmware/${glitter.publicBeans.onlineVersion.bleVersion}` :
            `${glitter.fileModelInterFace.domain}/Orange%20Cloud/Drive/OG_lite/Ble%20Firmware/${glitter.publicBeans.onlineVersion.bleVersion}`
        glitter.canshowConnect=false
        glitter.command.send("0ACCCC")
     switch (glitter.deviceType){
         case glitter.deviceTypeEnum.Android:
             let id = glitter.callBackId += 1
             glitter.callBackList.set(id,callback)
             window.BleUpdate.updateBle(id,rout)
             break
         case glitter.deviceTypeEnum.Ios:

             break
     }
    }
}
//MMY查詢Model
glitter.mmyInterFace={
    //查詢Make
    queryMake:function (success,error){
        var sql="select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA')  replacer  order by `Make` asc".replace("replacer",this.filterItem())
        glitter.dataBase.query("mmy",sql
        ,success,error)
    },
    //記錄Make選擇
    selectMake:undefined,
    //查詢Model
    queryModel:function (success,error){
        glitter.dataBase.query("mmy","select distinct `Model` from `Summary table` where `Make`='"+this.selectMake+"' and `Direct Fit` not in('SC2575','SC4379')  replacer  order by `Model` asc".replace("replacer",this.filterItem())
            ,success,error)
    },
    //記錄Model選擇
    selectModel:undefined,
    //查詢Year
    queryYear:function (success,error){
        glitter.dataBase.query("mmy","select distinct `Year` from `Summary table` where `Make`='"+this.selectMake+"' and `Model`='"+this.selectModel+"' and `Direct Fit` not in('SC2575','SC4379')  replacer  order by `Year` asc".replace("replacer",this.filterItem())
            ,success,error)
    },
    //查詢我的最愛
    queryLove:function (success,error){
        glitter.serialUtil.listObject("favorite",100,success,error)
    },
    //記錄Year選擇
    selectYeart:undefined,
    set selectYear(name) {
        this.selectYeart=name
        console.log("select * from `Summary table` where `Make`='"+this.selectMake+"' and `Model`='"+this.selectModel+"' and `Year`='"+name+"' limit 0,1")
        glitter.dataBase.query("mmy","select * from `Summary table` where `Make`='"+glitter.mmyInterFace.selectMake+"' and `Model`='"+glitter.mmyInterFace.selectModel+"' and `Year`='"+glitter.mmyInterFace.selectYeart+"' limit 0,1"
            ,function (result){
            if(result.length>0){
                glitter.mmyInterFace.selectMMY=result[0]
                glitter.serialUtil.storeObject(result[0],(glitter.mmyInterFace.selectMake+glitter.mmyInterFace.selectModel+glitter.mmyInterFace.selectYear),"favorite",function (){},function (){})
            }
            },function (){})
    },
    get selectYear(){
        return this.selectYeart
    },
    //紀錄MMY item
    selectMMY:undefined,
    //依照Function進行過濾
    filterItem:function (){
        switch (glitter.selectFunction.select){
            case glitter.selectFunction.enum.ReadSensor:
                return "and `OGL Read`='True'"
            case glitter.selectFunction.enum.IdCopy:
                return "and `OGL CopyID`='True'"
            case glitter.selectFunction.enum.Program:
                return "and `OGL Programe`='True'"
            case glitter.selectFunction.enum.IdCopy_OBD:
                return "and `OGL CopyID`='True' and OBD2 not in('NA')"
            case glitter.selectFunction.enum.ObdRelearn:
                return "and `OGL Auto` != '4'"
        }
        return ""
    },
    //燒錄數量選擇
    numberChoice:1,
    //取得s19
    getS19:function (type,callBack){
        glitter.getFile("s19/"+glitter.mmyInterFace.selectMMY["Direct Fit"],"text",callBack)
    },
}
//選擇的按鈕
glitter.selectFunction={
    select:undefined,
    enum:{
        ReadSensor:0,
        Program:1,
        IdCopy:2,
        ObdRelearn:3,
        IdCopy_OBD:4,
        Relearn_Procedure:5,
        Store_Tire:6,
        Online_Shopping:7,
        Manual:8,
        Setting:9
    },
    switchToPage:function (){
        switch (glitter.selectFunction.select){
            case glitter.selectFunction.enum.ReadSensor:
                glitter.changePage('page/Page_ReadSensor.html','Page_ReadSensor',true,{})
                break
            case glitter.selectFunction.enum.Program:
                glitter.changePage('page/Page_Relearm_Procedure.html','Page_Relearm_Procedure',true,{})
                break
            case glitter.selectFunction.enum.IdCopy:
                glitter.changePage('page/Page_Relearm_Procedure.html','Page_Relearm_Procedure',true,{})
                break
            case glitter.selectFunction.enum.IdCopy_OBD:
                glitter.changePage('page/Page_Relearm_Procedure.html','Page_Relearm_Procedure',true,{})
                break
            case glitter.selectFunction.enum.ObdRelearn:
                glitter.changePage('page/Page_Relearm_Procedure.html','Page_Relearm_Procedure',true,{})
                break
            case glitter.selectFunction.enum.Relearn_Procedure:
                glitter.changePage('page/Page_Relearm_Procedure_Munu.html','Page_Relearm_Procedure_Munu',true,{})
                break
        }
    }
}




