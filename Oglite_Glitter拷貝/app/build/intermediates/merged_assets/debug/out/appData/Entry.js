"use strict";
function onCreate() {
    glitter.isOG=false
    convertBase()
    //取得手機系統資訊
    glitter.runJsInterFace("getSystemVersion",{},function (response){
        glitter.share.phoneMake=response.make
        glitter.share.phoneModel=response.model
        glitter.share.phoneVersion=response.version
    })
    // glitter.webUrl = 'https://bento3.orange-electronic.com'
    glitter.webUrl='http://192.168.7.149'
   function setParameter(){
       //RF測試模式
       glitter.share.rftest=false
       //BLE測試模式(斷線重連)
       glitter.share.bleTest=false
       //允許連線(Test unit)
       glitter.canshowConnect = true
       //是否正在更新藍芽
       glitter.onUpdateBle = true
       //是否正在燒錄中
       glitter.onProgram = false
       //測試模式
       glitter.share.forTest = false
       //低電量警示
       glitter.share.lowBattery = 0
       //自動讀自動燒的功能
       glitter.share.autoRead = true
       //開關更新函示
       glitter.canUpdate = false
       //APP版本
       glitter.share.apkverVersion = '1.73'
       //判斷是否支援298
       glitter.share.support298=true
       //燒錄檔案判斷測試
       glitter.share.fwDataCheckeck=false
       //允許Bootloader更新
       glitter.share.bootloaderUpdate = true
       //循環更新功能
       glitter.share.cycleUpdate=false
   }
    setParameter()

    //公用確認Dialog
    glitter.share.hint = function (title,fun) {
        glitter.openDiaLog('dialog/Dia_Check_Yes.html', 'Dia_Check_Yes', false, true, {
            title: title,
            callback: function () {
                glitter.closeDiaLogWithTag('Dia_Check_Yes')
                if(fun){fun()}
            }
        }, function () {
        })
    }
    //公用ProgressBar
    glitter.share.dataLoading=function (re){
        if(re){
            glitter.openDiaLog('dialog/Dia_Loading.html', 'Dia_Loading', false, false, glitter.getLan(575), function () {})
        }else{
            glitter.closeDiaLogWithTag('Dia_Loading')
        }
    }
    glitter.language=function (lan){
        return glitter.getLan(lan)
    }
    //
    var haveCycleUpdate=false
    glitter.share.checkVersion = function () {
        console.log('glitter.share.checkVersion')
        if(haveCycleUpdate){
            console.log('haveCycleUpdate')
            return}
        glitter.share.bleUtil.isConnect({
            callback: function (result) {
                if (result.result) {
                    if (glitter.onwaiting) {
                        glitter.share.checkVersion()
                        return
                    }
                    if (glitter.fileModelInterFace.needInit()) {
                        glitter.openDiaLog('dialog/Dia_Update.html', 'Dia_Update', false, false, {}, function () {})
                    } else {
                        if(!glitter.share.blockUpdate){
                            glitter.fileModelInterFace.checkNewVersion(function () {
                                if(glitter.share.cycleUpdate){
                                    haveCycleUpdate=true
                                    glitter.openDiaLog('dialog/Dia_Update.html', 'Dia_Update', false, false, {}, function () {})
                                }else{
                                    if (glitter.fileModelInterFace.needUpdate()) {
                                        glitter.openDiaLog('dialog/Dia_Ask_Update.html', 'Dia_Ask_Update', false, false, {}, function () {})
                                    }else if(glitter.fileModelInterFace.needSilenceUpdate()){
                                        glitter.fileModelInterFace.silenceUpdate()
                                    }
                                }
                            }, function () {
                                setTimeout(function () {
                                    glitter.share.checkVersion()
                                }, 200)
                            })
                        }
                    }
                } else {
                    setTimeout(function () {
                        glitter.share.checkVersion()
                    }, 200)
                }
            }
        })
    }
    glitter.changePageListener(function (page) {
        glitter.share.CancelError = false
        glitter.readRxReceive = function () {}

    })
    //畫面重繪
    glitter.repaint = function () {
        document.body.scrollTop = document.body.scrollTop
    }
    //壓力單位換算
    //壓力換算
    glitter.preToSetting = function (a) {
        switch (glitter.publicBeans.pressure) {
            case "kpa": {
                return a
            }
            case "psi": {
                var num = a / 6.89;
                return num.toFixed(1);
            }
            case "bar": {
                return a / 100
            }
            default : {
                return a
            }
        }
    }
    //溫度換算
    glitter.temToSetting = function (a) {
        switch (glitter.publicBeans.tem) {
            case "C": {
                return a
            }
            case "F": {
                var num = a * 9 / 5 + 32;
                return num.toFixed(0);
            }
            default: {
                return a
            }
        }
    }
    glitter.goBackOnRootPage = function () {
        if (glitter.deviceType === glitter.deviceTypeEnum.Android) {
            glitter.openDiaLog('dialog/Dia_Exit.html', 'Dia_Exit', false, false, {}, function () {
            })
        }
    }


    function initial() {
        //網頁視窗模擬用
        if (glitter.deviceType === glitter.deviceTypeEnum.Web) {
            glitter.addMtScript([
                'glitterBleLibrary/GlBle.js',
                'jsInitial/ApiRequest.js',
                'jslib/bleManager.js',
                'jslib/BleCommand.js',
                'jsInitial/MysqlDataBase.js',
                'jsInitial/TireStorage.js',
                'jsInitial/PublicModel.js',
                'jsInitial/MmyModel.js',
                'jsInitial/FileModel.js'
            ], () => {
                glitter.command = new BleCommand()
                glitter.mmyInterFace = {}
                glitter.mmyInterFace.selectMake = "ABARTH"
                glitter.mmyInterFace.selectModel = "124"
                glitter.mmyInterFace.selectYear = "2016-2020"
                glitter.mmyInterFace.selectMMY = {
                    "Make": "ABARTH",
                    "Model": "124",
                    "Year": "2016-2020",
                    "OE Part Num": "BHB637140",
                    "Product Number": "SI2054",
                    "Sensor Type": "Direct Fit",
                    "Relearn code": "BASE30",
                    "Direct Fit": "SI2054",
                    "Sensor": "SP201",
                    "Relearn Procedure (English)": "RL.03_C_1. Inflate all tires to desired pressure according to tire placard.\\n\nRL.04_C_2. Activate all sensors by using the tool.\\n\nRL.13_C_3. Park the car for 15 minutes.\\n\nRL.07_C_4. Start engine. (Choose tire pressure and press reset).\\n\nRL.07_C_5. Drive above 20 mph until new sensor IDs are learned by the ECU.\\n\nRL.07_C_6. Procedure should take up to 20 mins.\\n\nRL.11_E_NOTE:Please open the back cover for programming when sensor be mounted in tires as picture shown.(need to put the tool against the rubber of the tire instead of the rim)",
                    "Relearn Procedure (German)": "RL.03_C_1. Korrigieren Sie den Reifenluftdruck anhand der Herstellervorgabe.\\n\nRL.04_C_2. Aktiviere alle Sensoren mit dem Programmiergerät.\\n\nRL.13_C_3. Parken Sie das Auto 15 Minuten lang.\\n\nRL.07_C_4. Starte den Motor. (Reifendruck wählen und Reset drücken).\\n\nRL.07_C_5. Fahren Sie mit mehr als 30 km/h, bis die ECU neue Sensor-IDs gelernt hat.\\n\nRL.07_C_6. Der Vorgang sollte bis zu 20 Minuten dauern.\\n\nRL.11_D_HINWEIS: Wenn der Sensor wie abgebildet in den Reifen montiert wird, öffnen Sie die Antenne auf der Rückseite des Geräts um den Sensor zu programmieren.",
                    "Relearn Procedure (Italian)": "RL.03_C_1. Regolare tutti i pneumatici alla pressione desiderata in base alla targhetta dei pneumatici.\\n\nRL.04_C_2. Attivare tutti i sensori utilizzando lo strumento.\\n\nRL.13_C_3. Lasciare in sosta l`auto per 15 minuti.\\n\nRL.07_C_4. Avviare il motore. (Scegli la pressione dei pneumatici e premi reset).\\n\nRL.07_C_5.  Guidare a una velocità superiore a 25 km/h e fino a quando l`ECU non apprende i nuovi ID dei sensori.\\n\nRL.07_C_6. La procedura dovrebbe richiedere fino a 20 minuti.\\n\nRL.11_E_NOTE: si prega di aprire il coperchio posteriore per la programmazione quando il sensore è montato sui pneumatici come mostrato nell`immagine. (è necessario appoggiare lo strumento sulla spalla della gomma e non sul cerchio)",
                    "Relearn Procedure (Traditional Chinese)": "RL.03_C_1. 根據輪胎標牌將所有輪胎充氣至所需壓力。\\n\nRL.04_C_2. 使用該工具激活所有傳感器。\\n\nRL.13_C_3. 將車停放 15 分鐘。\\n\nRL.07_C_4. 啟動發動機。 (選擇輪胎壓力並按重置)。\\n\nRL.07_C_5. 以 20 mph 以上的速度行駛，直到 ECU 學習到新的傳感器 ID。\\n\nRL.07_C_6. 過程最多需要 20 分鐘。\\n\nRL.11_E_NOTE：如圖所示，當傳感器安裝在輪胎上時，請打開後蓋進行編程。（需要將工具放在輪胎的橡膠上而不是輪輞上）",
                    "Relearn Procedure (Jane)": "RL.03_C_1. 根据轮胎标牌将所有轮胎充气至所需压力。\\n\nRL.04_C_2. 使用该工具激活所有传感器。\\n\nRL.13_C_3. 将车停放 15 分钟。\\n\nRL.07_C_4. 启动发动机。 (选择轮胎压力并按重置)。\\n\nRL.07_C_5. 以 20 mph 以上的速度行驶，直到 ECU 学习到新的传感器 ID。\\n\nRL.07_C_6. 过程最多需要 20 分钟。\\n\nRL.11_E_NOTE：如图所示，当传感器安装在轮胎上时，请打开后盖进行编程。（需要将工具放在轮胎的橡胶上而不是轮辋上）",
                    "Relearn Procedure (Japanese)": "RL.03_C_1.タイヤのプラカードに従って、すべてのタイヤを希望の空気圧に膨らませます。\\n\nRL.04_C_2.ツールを使用してすべてのセンサーをアクティブにします。\\n\nRL.13_C_3.車を 15 分間駐車します。\\n\nRL.07_C_4.エンジンをつける。 (タイヤの空気圧を選択してリセットを押します)\\n\nRL.07_C_5. ECU が新しいセンサー ID を学習するまで、時速 20 マイル以上で走行してください。\\n\nRL.07_C_6.手続きには最大 20 分かかります。\\n\nRL.11_E_NOTE: 写真のようにセンサーをタイヤに取り付ける場合は、プログラミングのために裏蓋を開いてください (リムの代わりにタイヤのゴムにツールを置く必要があります)。",
                    "Relearn Procedure (French)": "RL.03_C_1. Pomp alle banden op tot de gewenste spanning volgens het bandenplaatje.\\n\nRL.04_C_2. Activeer alle sensoren met behulp van de tool.\\n\nRL.13_C_3. Parkeer de auto 15 minuten.\\n\nRL.07_C_5. Start motor. (Kies bandenspanning en druk op reset).\\n\nRL.07_C_4. Rijd harder dan 20 mph totdat de ECU nieuwe sensor-ID`s heeft geleerd.\\n\nRL.07_C_6. De procedure duurt maximaal 20 minuten.\\n\nRL.11_E_NOTE: Open de achterklep om te programmeren wanneer de sensor in banden wordt gemonteerd zoals afgebeeld. (Je moet het gereedschap tegen het rubber van de band plaatsen in plaats van tegen de velg)",
                    "Relearn Procedure (Dutch)": "RL.03_C_1. Infle todos los neumáticos a la presión deseada de acuerdo con la placa de neumáticos. \\n\nRL.04_C_2. Active todos los sensores con la herramienta. \\n\nRL.13_C_3. Estaciona el auto durante 15 minutos. \\n\nRL.07_C_4. Arrancar el motor. (Elija la presión de los neumáticos y presione reiniciar). \\n\nRL.07_C_5. Conduzca a más de 20 mph hasta que la ECU aprenda los nuevos ID de sensor. \\n\nRL.07_C_6. El procedimiento debería tardar hasta 20 minutos. \\n\nRL.11_E_NOTE: abra la cubierta trasera para programar cuando el sensor se monte en los neumáticos como se muestra en la imagen (es necesario colocar la herramienta contra la goma del neumático en lugar de la llanta)",
                    "Relearn Procedure (Spanish)": "RL.03_C_1. Gonflez tous les pneus à la pression souhaitée selon la plaque signalétique des pneus.\\n\nRL.04_C_2. Activez tous les capteurs à l`aide de l`outil.\\n\nRL.13_C_3. Garez la voiture pendant 15 minutes.\\n\nRL.07_C_4. Démarrer le moteur. (Choisissez la pression des pneus et appuyez sur reset).\\n\nRL.07_C_5. Conduisez à plus de 20 mph jusqu`à ce que de nouveaux ID de capteur soient appris par l`ECU.\\n\nRL.07_C_6. La procédure devrait prendre jusqu`à 20 minutes.\\n\nRL.11_E_REMARQUE : veuillez ouvrir le couvercle arrière pour la programmation lorsque le capteur est monté dans les pneus comme indiqué sur l`image. (besoin de mettre l`outil contre le caoutchouc du pneu au lieu de la jante)",
                    "Relearn Procedure (Danish)": "RL.03_C_1. Pump alle dæk til ønsket tryk i henhold til dækplakat. \\n\nRL.04_C_2. Aktivér alle sensorer ved hjælp af værktøjet. \\n\nRL.13_C_3. Parker bilen i 15 minutter. \\n\nRL.07_C_4. Start motoren. (Vælg dæktryk, og tryk på reset). \\n\nRL.07_C_5. Kør over 20 km / t, indtil nye sensor-id`er læres af ECU`en. \\n\nRL.07_C_6. Fremgangsmåden skal tage op til 20 minutter. \\n\nRL.11_E_NOTE: Åbn bagdækslet til programmering, når sensoren monteres i dæk som vist på billedet. (Skal placeres værktøjet mod dækkets gummi i stedet for fælgen)",
                    "Relearn Procedure (Arabic)": "RL.03_C_1. قم بنفخ كافة الإطارات إلى مستوى الضغط المطلوب وفقًا لبطاقة الإطارات. \\n\nRL.04_C_2. قم بتنشيط كافة أجهزة الاستشعار باستخدام الأداة. \\n\nRL.13_C_3. أوقف السيارة لمدة 15 دقيقة. \\n\nRL.07_C_4. بدء تشغيل المحرك. (اختر ضغط الإطارات واضغط على إعادة الضبط). \\n\nRL.07_C_5. قم بالقيادة بسرعة تزيد عن 20 ميلاً في الساعة حتى يتم التعرف على معرفات المستشعرات الجديدة بواسطة وحدة التحكم الإلكترونية. \\n\nRL.07_C_6. يجب أن يستغرق الإجراء ما يصل إلى 20 دقيقة. \\n\nRL.11_E_NOTE: يرجى فتح الغطاء الخلفي للبرمجة عندما يتم تركيب المستشعر في الإطارات كما هو موضح في الصورة. (تحتاج إلى وضع الأداة مقابل مطاط الإطار بدلاً من الحافة)",
                    "Relearn Procedure (Slovak)": "RL.03_C_1. Nafúkajte všetky pneumatiky na požadovaný tlak podľa štítku pneumatiky. \\n\nRL.04_C_2. Aktivujte všetky senzory pomocou nástroja. \\n\nRL.13_C_3. Zaparkujte auto na 15 minút. \\n\nRL.07_C_4. Naštartujte motor. (Vyberte tlak v pneumatikách a stlačte reset). \\n\nRL.07_C_5. Jeďte rýchlosťou viac ako 20 km / h, kým ECU nezistí nové ID senzorov. \\n\nRL.07_C_6. Postup by mal trvať až 20 minút. \\n\nRL.11_E_NOTE: Ak je snímač namontovaný v pneumatikách, otvorte zadný kryt pre programovanie, ako je to znázornené na obrázku. (Je potrebné namiesto náradia priložiť náradie k gume pneumatiky)",
                    "Relearn Procedure (Czech)": "RL.03_C_1. Nafoukněte všechny pneumatiky na požadovaný tlak podle štítku pneumatiky. \\n\nRL.04_C_2. Aktivujte pomocí nástroje všechny senzory. \\n\nRL.13_C_3. Zaparkujte auto na 15 minut. \\n\nRL.07_C_4. Jeďte rychlostí vyšší než 20 km / h, dokud se ECU nenaučí nová ID senzorů. \\n\nRL.07_C_5. Nastartujte motor. (Zvolte tlak v pneumatikách a stiskněte reset). \\n\nRL.07_C_6. Postup by měl trvat až 20 minut. \\n\nRL.11_E_NOTE: Když je senzor namontován v pneumatikách, otevřete zadní kryt pro programování, , jak je znázorněno na obrázku. (Je třeba namísto ráfku přiložit nářadí k gumě pneumatiky)",
                    "Make_Img": "abarth",
                    "LF": "0",
                    "ID1": "0O",
                    "ID2": "OO",
                    "ID3": "OO",
                    "ID4": "OO",
                    "ID_Count": "8",
                    "Special ID": "0",
                    "SII": "V",
                    "SIII": "V",
                    "SIII_N88": "X",
                    "BSIII_N88": "X",
                    "Read Tire Num": "4",
                    "Product No. (hex)": "002B",
                    "RF Protocol Num": "1A",
                    "LF Protocol Num": "0A",
                    "LF Power": "14",
                    "ID digits": "8",
                    "LF PAD1": "0",
                    "OG Read": "True",
                    "OG CopyID": "True",
                    "OG Programe": "True",
                    "OG CIDP": "True",
                    "openback": "False",
                    "OG Auto": "0",
                    "OG LF Protocol Num": "0",
                    "OBD Product No. (hex)": "002B",
                    "HEX/DEC": "HEX",
                    "LF Power (OBD)": "14",
                    "MMY number": "1001010",
                    "OBD1": "NA",
                    "Wheel_Count": "2",
                    "OBD2": "NA",
                    "OGL Read": "True",
                    "OGL CopyID": "True",
                    "OGL Programe": "True",
                    "OGL CIDP": "True",
                    "OGL Auto": "0"
                }
                glitter.getLan = function (id) {
                    return id
                }
                glitter.setTitleBar('frag/titleBar.html')
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
                    // glitter.setHome('page/Page_Home.html','Page_Home',{})
                    glitter.setHome('page/Page_Comic_Relearm_Procedure.html', 'Page_Comic_Relearm_Procedure', {})
                })
                glitter.publicBeans.account='orangerd'
                // glitter.openDiaLog('dialog/Dia_Send_Email_Success.html','Dia_Send_Email_Success',false,false,{},function (){})
            })
        } else {
            glitter.addMtScript([
                'glitterBleLibrary/GlBle.js',
                'jsInitial/ApiRequest.js',
                'jsInitial/TireStorage.js',
                'jsInitial/PublicModel.js',
                'jsInitial/MmyModel.js',
                'jsInitial/FileModel.js',
            ], () => {
                glitter.apiRequest = new ApiRequest()
                glitter.bleList = []
                glitter.share.myAppVersion = glitter.share.apkverVersion
                initPublicBeans(function () {
                    glitter.addMtScript(['jsInitial/MysqlDataBase.js','jslib/BleCommand.js', 'jslib/bleManager.js'],()=>{
                        glitter.command = new BleCommand()
                        glitter.command.txmemory = ''
                        glitter.mysqlDataBase = new MysqlDataBase()
                        if (glitter.publicBeans.language === "tw") {
                            glitter.fileModelInterFace.domain = 'https://bento3.orange-electronic.com'
                        } else {
                            glitter.fileModelInterFace.domain = 'https://bento2.orange-electronic.com'
                        }
                        // glitter.openDiaLog('dialog/Dia_Reader_Reduce.html','Dia_Reader',false,false,{},function (){})
                        glitter.setHome('page/Page_Logo.html', 'Page_Logo', {})
                    },()=>{
                        alert('initialError')
                    })

                })
            }, (errorText) => {
                console.log("addJavascriptError:" + errorText)
            })
        }

    }
    initial()

}



//判斷是否為10禁制
glitter.share.isDEC = function () {
    if (glitter.deviceType === glitter.deviceTypeEnum.Web) {
        return true
    }
    switch (glitter.publicBeans.numeral) {
        case "auto":
            return glitter.mmyInterFace.selectMMY["HEX/DEC"] === "DEC"
        case "dec":
            return true
        case "hex":
            return false
        default:
            return false
    }
}
//禁制轉換工具
function convertBase(){

    glitter.ConvertBase = function (num) {
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
    glitter.ConvertBase.bin2dec = function (num) {
        return  glitter.ConvertBase(num).from(2).to(10);
    };

// binary to hexadecimal
    glitter.ConvertBase.bin2hex = function (num) {
        return  glitter.ConvertBase(num).from(2).to(16);
    };

// decimal to binary
    glitter. ConvertBase.dec2bin = function (num) {
        return  glitter.ConvertBase(num).from(10).to(2);
    };

// decimal to hexadecimal
    glitter.ConvertBase.dec2hex = function (num) {
        return  glitter.ConvertBase(num).from(10).to(16);
    };

// hexadecimal to binary
    glitter.ConvertBase.hex2bin = function (num) {
        var sring =  glitter.ConvertBase(num).from(16).to(2)
        while (sring.length < 8) {
            sring = ("0" + sring)
        }
        return sring;
    };

// hexadecimal to decimal
    glitter.ConvertBase.hex2dec = function (num) {
        return  glitter.ConvertBase(num).from(16).to(10);
    };
}
//自動化測試工具
var autoTestInterVal=undefined
glitter.share.autoTesting=function (){
    if(autoTestInterVal){clearInterval(autoTestInterVal)}
    function triggerTest(){
        function setPass(){
            return {
                //測試完畢的Make
                passMake:0,
                //測試完畢的Model
                passModel:0,
                //測試完畢的Year
                passYear:0,
                //測試的次數
                testTime:0,
                //是否操作過Program
                haveProgram:false,
                //Program失敗次數
                programFalse:0
            }
        }
        var pass=setPass()
        var clock=glitter.clock()
        autoTestInterVal=setInterval(function (){
            function excute(){
                var page=glitter.getNowPage()
                switch (page.tag){
                    case "Page_Home":
                        glitter.closeDiaLog()
                        if(pass.haveProgram){
                            page.window.document.getElementById('item0').click()
                        }else{
                            page.window.document.getElementById('item1').click()
                            }
                        break
                    case "Page_Vehicle_Selection":
                        if(pass.haveProgram){
                            setTimeout(function (){glitter.selectFunction.switchToPage();},200)
                        }else{
                            glitter.changePage('page/Page_Select_Make.html','Page_Select_Make',true,{})
                        }
                        break
                    case "Page_Select_Make":
                        if(!page.window.document.getElementById('item'+pass.passMake)){
                            alert('測試完畢')
                        }else {
                            page.window.document.getElementById('item'+pass.passMake).click()
                        }
                        break
                    case "Page_Select_Model":
                        if(!page.window.document.getElementById('item'+pass.passModel)){
                            pass.passMake++
                            pass.passModel=0
                            pass.passYear=0
                            glitter.goBack()
                        }else{
                            page.window.document.getElementById('item'+pass.passModel).click()
                        }
                        break
                    case "Page_Select_Year":
                        if(!page.window.document.getElementById('item'+pass.passYear)){
                            pass.passModel++
                            pass.passYear=0
                            glitter.goBack()
                        }else{
                            page.window.document.getElementById('item'+pass.passYear).click()
                        }
                        break
                    case "Page_ReadSensor":
                        if(page.window.toggle){
                            clock.zeroing()
                            pass.testTime++
                            if(pass.testTime===6){
                                pass.testTime=0
                                pass.passYear++
                                pass.haveProgram=false
                                glitter.goMenu()
                            }else{
                                page.window.document.getElementById('next').click()
                            }
                        }else{
                            if( clock.stop()>5000){
                                glitter.command.cancel=true
                            }

                        }
                        break
                    case "Page_Comic_Relearm_Procedure":
                        pass.passYear++
                        glitter.goBack()
                        break
                    case "Page_Number_Choice":
                        page.window.document.getElementById('click3').click()
                        break
                    case "Page_Program_Select":
                        glitter.selectWay=0;
                        pass.programFalse=0
                        glitter.changePage('page/Page_Program_Detail.html','Page_Program_Detail',true,{})
                        break
                    case "Page_Program_Detail":
                        console.log('Page_Program_Detail')
                        glitter.closeDiaLogWithTag('DIa_Place_Position')
                        if(page.window.toggle){
                            if(page.window.document.getElementById('next').innerText===glitter.getLan(135)){
                                pass.testTime=0
                                glitter.goMenu()
                                pass.haveProgram=true
                            }else if(pass.programFalse===1){
                                pass.testTime=0
                                glitter.goMenu()
                                pass.haveProgram=true
                            }else {
                                pass.programFalse++
                                page.window.document.getElementById('next').click()
                            }
                        }
                        break
                }
            }

            glitter.runJsInterFace("Glitter_BLE_IsConnect", {
            }, function (response) {
                if(response.result){
                    excute()
                }
            })

        },1000)
    }
    triggerTest()
}


