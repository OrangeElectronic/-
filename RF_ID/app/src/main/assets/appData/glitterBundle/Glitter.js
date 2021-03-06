/*Read only*/
/*Please dont change*/
"use strict";

//頁面切換動畫

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var Animator = function Animator() {
    _classCallCheck(this, Animator);

    this.translation = 0;
    this.rotation = 1;
    this.verticalTranslation = 2;
};

//應用類型


var AppearType = function AppearType() {
    _classCallCheck(this, AppearType);

    this.Web = 0;
    this.Android = 1;
    this.Ios = 2;
};

//Html類型


var HtmlType = function HtmlType() {
    _classCallCheck(this, HtmlType);

    this.Page = 0;
    this.Dialog = 1;
    this.Frag = 2;
};

var Glitter = function Glitter() {
    _classCallCheck(this, Glitter);

    this.webUrl = '';
    this.callBackId = 0;
    this.callBackList = new Map();
    this.deviceTypeEnum = new AppearType();
    var getUrl = window.location;
    //公用變數
    this.publicBeans = {};
    this.baseUrl = getUrl.protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];
    //Html類型
    this.htmlType = new HtmlType();
    //轉場動畫
    this.animator = new Animator();
    //當前顯示裝置類型
    this.deviceType = new AppearType().Web;
    //現在所有的Iframe
    this.iframe = [];
    //現在所有的Frag
    this.ifrag = [];
    //現在所有的Dialog
    this.dialog = [];
    //現在所有的Sheet
    this.sheetList = [];
    //Web的紀錄
    this.webPro = {};
    //PageIndex
    this.pageIndex = 0;
    //PageIndex
    this.bottomSheet = [];
    //View
    this.getBoundingClientRect = {};
    //存紀錄
    this.setPro = function (tag, data) {
        switch (glitter.deviceType) {
            case appearType.Web:
            {
                glitter.webPro[tag] = data;
                waitSetPro(tag, data);
                break;
            }
            case appearType.Android:
            {
                window.GL.setPro(tag, data);
                break;
            }
            case appearType.Ios:
            {
                var map = {
                    tag: tag,
                    data: data
                };
                window.webkit.messageHandlers.setPro.postMessage(JSON.stringify(map));
                break;
            }
        }
    };
    //取紀錄
    this.getPro = function (tag, fun) {
        var id = glitter.callBackId += 1;
        glitter.callBackList.set(id, fun);
        switch (glitter.deviceType) {
            case appearType.Web:
            {
                glitter.callBackList.get(id)(glitter.webPro[tag]);
                glitter.callBackList.delete(id);
                return;
            }
            case appearType.Android:
            {
                glitter.callBackList.get(id)(window.GL.getPro(tag));
                glitter.callBackList.delete(id);
                return;
            }
            case appearType.Ios:
            {
                var map = {
                    tag: tag,
                    callback: id
                };
                window.webkit.messageHandlers.getPro.postMessage(JSON.stringify(map));
                break;
            }
        }
    };
    //設定某頁面的資料
    this.setPageData = function (tag, objName, value) {
        return window.Jz.setPageData(tag, objName, value);
    };
    //取得某頁面的資料
    this.getPageData = function (tag, data) {
        return window.Jz.getPageData(tag, data);
    };
    //取得MainActivity的資料
    this.getActivityData = function () {
        return JSON.parse(window.Jz.getActivityData());
    };
    //取得MainActivity的資料
    this.setActivityData = function (objName, value) {
        return window.Jz.setActivityData(objName, value);
    };
    //取得現在Html的Tag名稱
    this.getTagName = function () {
        return window.Jz.getTagName();
    };
    //Log顯示
    this.logE = function (a, b) {
        window.Jz.logE(a, b);
    };
    //設定首頁
    this.setHome = function (url, tag, obj) {
        this.showLoadingView();
        glitter.pageIndex = glitter.pageIndex + 1;
        var map = {};
        map.id = tag;
        map.obj = obj;
        map.goback = true;
        map.pageIndex = glitter.pageIndex;
        glitter.iframe = [];
        glitter.iframe = glitter.iframe.concat(map);
        $('#framePlace').empty();
        $('#framePlace').append('<iframe style="display: none;" name="' + map.id + '" src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + url + '?tag=' + tag + '&pageIndex=' + glitter.pageIndex + '&type=Page"  id="' + tag + '"></iframe>');
        glitter.changeWait = function () {
            $('#' + tag).show();
        };
        glitter.changePageListener(tag);
    };
    //設定側滑選單
    this.setNavigation = function (src, obj) {
        if (window.drawer === undefined) {
            glitter.addScript('glitterBundle/NaviGation.js');
        }
        $("#Navigation").html('');
        $("#Navigation").html('<iframe src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + src + '"  style="width: 100%;"></iframe>');
    };
    //顯示加載畫面
    this.showLoadingView = function () {
        $('#loadingView').show();
    };
    //設定加載畫面
    this.setLoadingView = function (link) {
        $('#loadingView').hide();
        $('#loadingView').append('<iframe  src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + link + '" style="width: 100%;height: 100%;background-color: rgba(0, 0, 0, 0.5);"></iframe>');
    };
    //關閉加載畫面
    this.hideLoadingView = function () {
        $('#loadingView').hide();
    };
    //打開側滑選單
    this.openNavigation = function () {
        window.drawer.open();
    };
    //關閉側滑選單
    this.closeNavigation = function () {
        window.drawer.close();
    };
    //開關側滑選單
    this.toggleNavigation = function () {
        window.drawer.toggle();
    };
    //按鈕監聽
    this.keyEventListener = function (obj) {};
    //頁面切換
    this.changePage = function (link, tag, goBack, obj) {
        this.showLoadingView();
        glitter.pageIndex = glitter.pageIndex + 1;
        var map = {};
        map.id = tag;
        map.obj = obj;
        map.goback = goBack;
        map.pageIndex = glitter.pageIndex;
        glitter.iframe = glitter.iframe.concat(map);
        $('#framePlace').append('<iframe name="' + tag + '" src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + link + '?tag=' + tag + '&pageIndex=' + glitter.pageIndex + '&type=Page"  id="' + tag + '" style="background-color: white;width: 100%;height: 100%;display: none;"></iframe>');
        glitter.changeWait = function () {
            $('#' + tag).show();
            for (var i = 0; i < glitter.iframe.length - 1; i++) {
                //console.log(glitter.iframe)
                $('#' + glitter.iframe[i].id).hide();
            }
            document.getElementById(glitter.iframe[glitter.iframe.length - 1].id).contentWindow.lifeCycle.onPause();
            glitter.changePageListener(tag);
        };
    };
    //頁面切換防止白頻
    this.changeWait = function () {};
    //Fragment切換
    this.changeFrag = function (root, link, tag, obj) {
        glitter.pageIndex = glitter.pageIndex + 1;
        root.innerHTML = '';
        var ife = [];
        for (var a = 0; a < glitter.ifrag.length; a++) {
            if (glitter.ifrag[a].id !== 'Frag-' + tag) {
                ife = ife.concat(glitter.ifrag[a]);
            }
        }
        glitter.ifrag = ife;
        var map = {};
        map.id = 'Frag-' + tag;
        map.obj = obj;
        map.goback = false;
        map.pageIndex = glitter.pageIndex;
        glitter.ifrag = glitter.ifrag.concat(map);
        if (link.indexOf('http') !== -1) {
            root.innerHTML = '<iframe name="' + map.id + '" src="' + link + '?tag=' + map.id + '&pageIndex=' + glitter.pageIndex + '&type=Frag"  id="' + map.id + '" style="width: 100%;height: 100%;border-width: 0;"></iframe>';
        } else {
            root.innerHTML = '<iframe name="' + map.id + '" src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + link + '?tag=' + map.id + '&pageIndex=' + glitter.pageIndex + '&type=Frag"  id="' + map.id + '" style="width: 100%;height: 100%;border-width: 0;"></iframe>';
        }
    };
    //頁面切換與動畫
    this.changePageWithAnimation = function (link, tag, goBack, animator, obj) {
        this.showLoadingView();
        glitter.pageIndex = glitter.pageIndex + 1;
        var map = {};
        map.id = tag;
        map.obj = obj;
        map.goback = goBack;
        map.pageIndex = glitter.pageIndex;
        document.getElementById(glitter.iframe[glitter.iframe.length - 1].id).contentWindow.lifeCycle.onPause();
        glitter.iframe = glitter.iframe.concat(map);
        $('#framePlace').append('<iframe name="' + map.id + '" src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + link + '?tag=' + tag + '&pageIndex=' + glitter.pageIndex + '&type=Page"  id="' + tag + '" style="background-color: white;width: 100%;height: 100%;"></iframe>');
        if (animator === this.animator.translation) {
            var frame = function frame() {
                if (pos <= 0) {
                    clearInterval(id);
                    for (var i = 0; i < glitter.iframe.length - 1; i++) {
                        $('#' + glitter.iframe[i].id).hide();
                    }
                } else {
                    pos -= 10;
                    elem.style.transform = 'translateX(' + pos + 'px)';
                }
            };

            var elem = document.getElementById(tag);
            elem.style.transform = 'translateX(' + $('#' + tag).width() + 'px)';
            var pos = $('#' + tag).width();
            var id = setInterval(frame, 1);

            glitter.changePageListener(tag);
        }
    };
    //顯示Dialog
    this.openDiaLog = function (url, tag, swipe, cancelable, obj, dismiss) {
        if (glitter.dialog.filter(function (item) {
            return item.id === 'Dialog-' + tag;
        }).length !== 0) {
            return;
        }
        glitter.pageIndex += 1;
        var map = {};
        map.pageIndex = glitter.pageIndex;
        map.id = 'Dialog-' + tag;
        map.obj = obj;
        map.dismiss = dismiss;
        map.cancelable = cancelable;
        glitter.dialog = glitter.dialog.concat(map);
        $('#diaPlace').show();
        $('#diaPlace').append('<iframe name="' + map.id + '" src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + url + '?tag=' + map.id + '&pageIndex=' + glitter.pageIndex + '&type=Dialog"  id="' + map.id + '" style="display: none;z-index:' + glitter.pageIndex + ';position: absolute;"></iframe>');
        var element = document.getElementById(map.id);
        if (!swipe) {
            element.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
        }
        setTimeout("$('#" + map.id + "').show()", 200);
    };
    //關閉所有Dialog
    this.closeDiaLog = function () {
        var tempDialog = glitter.dialog;
        glitter.dialog = [];
        for (var i = 0; i < tempDialog.length; i++) {
            if (tempDialog[i].dismiss !== undefined) {
                tempDialog[i].dismiss();
            }
            $('#' + tempDialog[i].id).remove();
        }
        $('#diaPlace').html('');
        $('#diaPlace').hide();
    };
    //取得Dialog參數內容
    this.getDialog = function (tag) {
        for (var i = 0; i < this.dialog.length; i++) {
            if (this.dialog[i].id === tag) {
                return this.dialog[i];
            }
        }
    };
    //關閉特定頁面Dialog
    this.closeDiaLogWithTag = function (tag) {
        //console.log(tag)
        var tempArray = [];
        for (var i = 0; i < glitter.dialog.length; i++) {
            var id = glitter.dialog[i].id;
            if (id === 'Dialog-' + tag || id === tag) {
                if (glitter.dialog[i].dismiss !== undefined) {
                    glitter.dialog[i].dismiss();
                }
                $('#' + id).remove();
            } else {
                tempArray = tempArray.concat(glitter.dialog[i]);
            }
        }
        glitter.dialog = tempArray;
        if (glitter.dialog.length === 0) {
            $('#diaPlace').html('');
            $('#diaPlace').hide();
        }
    };
    //判斷此頁面為何種類型
    this.getHtmlType = function (tag) {
        for (var i = 0; i < glitter.dialog.length; i++) {
            if (glitter.dialog[i].id === tag) {
                return this.htmlType.Dialog;
            }
        }
        for (var a = 0; a < glitter.iframe.length; a++) {
            if (glitter.iframe[a].id === tag) {
                return this.htmlType.Page;
            }
        }
    };
    //取得切換頁面的夾帶資料
    this.getObjectBundle = function (tag) {
        for (var i = 0; i < this.iframe.length; i++) {
            if (this.iframe[i].id === tag) {
                return this.iframe[i].obj;
            }
        }
        for (var i = 0; i < this.ifrag.length; i++) {
            if (this.ifrag[i].id === tag) {
                return this.ifrag[i].obj;
            }
        }
    };
    //顯示上滑Dialog
    this.openBottomSheet = function (url, tag, obj, height, topHandle) {
        glitter.pageIndex = glitter.pageIndex + 1;
        var map = {};
        map.id = 'BottomSheet-' + tag;
        map.obj = obj;
        map.pageIndex = glitter.pageIndex;
        $("body").append("<div id=\"" + map.id + "\" style=\"z-index:" + map.pageIndex + ";width: 100%;height: 100%;\"><div class=\"c-bottom-sheet__scrim\"><div class=\"c-bottom-sheet__sheet\"><div class=\"c-bottom-sheet__handle\"><span></span><span></span></div><div id=\"c-bottom-sheet__list\" class=\"c-bottom-sheet__list\" style=\"overflow-x: hidden;\"><iframe  name=\"" + map.id + "\" src=\"" + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + url + "?tag=" + map.id + "&pageIndex=" + glitter.pageIndex + "&type=BottomSheet\" id=\"" + map.id + "\" style=\"height: 100%\"></iframe></div></div></div>");
        glitter.bottomSheet = glitter.bottomSheet.concat(map);
        if (topHandle) {
            $('#' + map.id + ' .c-bottom-sheet__handle').show();
        } else {
            $('#' + map.id + ' .c-bottom-sheet__handle').hide();
        }
        $('#' + map.id + ' .c-bottom-sheet__sheet').css('height', height);
        $('#' + map.id + ' .c-bottom-sheet__list').css('height', height);
        try {
            glitter.sheetList = glitter.sheetList.concat(new BottomSheet(map.id, map.pageIndex));
            glitter.sheetList[glitter.sheetList.length - 1].activate();
        } catch (e) {}
    };
    //Sheet滾動視圖處理程序
    this.sheetHandler = {
        addScrollviewHandler: function addScrollviewHandler(e) {
            var sheetLenstiner = glitter.sheetList[glitter.sheetList.length - 1];
            e.scroll(function () {
                var scrollTop = e.scrollTop();
                if (sheetLenstiner !== undefined) {
                    sheetLenstiner.dragAble = scrollTop === 0;
                    if (scrollTop !== 0) {
                        sheetLenstiner.sheet.style.setProperty("transform", "translateY(0)");
                        sheetLenstiner.sheet.style.setProperty("transition", "unset");
                    }
                }
            });
        },
        addDragListener: function addDragListener(e) {
            var sheetLenstiner = glitter.sheetList[glitter.sheetList.length - 1];
            new TouchDragListener({
                el: e,
                touchStartCallback: function touchStartCallback(_ref) {
                    var el = _ref.el;
                    var active = _ref.active;
                    var initialY = _ref.initialY;
                    var currentY = _ref.currentY;
                    var yOffset = _ref.yOffset;

                    sheetLenstiner.dragAble = true;
                },
                touchEndCallback: function touchEndCallback(_ref2) {
                    var el = _ref2.el;
                    var active = _ref2.active;
                    var initialY = _ref2.initialY;
                    var currentY = _ref2.currentY;
                    var yOffset = _ref2.yOffset;

                    sheetLenstiner.dragAble = true;
                },
                touchMoveCallback: function touchMoveCallback(_ref3) {
                    var el = _ref3.el;
                    var active = _ref3.active;
                    var initialY = _ref3.initialY;
                    var currenty = _ref3.currenty;
                    var yOffset = _ref3.yOffset;

                    sheetLenstiner.dragAble = true;
                }
            });
        }
    };
    //關閉上滑Dialog
    this.closeBottomSheet = function () {
        var tempAr = glitter.sheetList;
        for (var a = 0; a < tempAr.length; a++) {
            tempAr[a].deactivate();
        }
        glitter.sheetList = [];
        glitter.bottomSheet = [];
    };
    //關閉上滑Dialog
    this.closeBottomSheetWithTag = function (tag) {
        var tempAr = glitter.sheetList;
        var data = [];
        var bts = [];
        for (var a = 0; a < tempAr.length; a++) {
            if (tempAr[a].id === 'BottomSheet-' + tag) {
                tempAr[a].deactivate();
                data = tempAr.filter(function (data) {
                    return data.id !== 'BottomSheet-' + tag;
                });
                bts = glitter.bottomSheet.filter(function (data) {
                    return data.id !== 'BottomSheet-' + tag;
                });
            }
        }
        glitter.sheetList = data;
        glitter.bottomSheet = bts;
    };
    //Dialog是否正在顯示
    this.diaIsShowing = function (tag) {
        return window.Jz.diaIsShowing(tag);
    };
    //跳轉至系統特定功能
    this.intent = function (string) {
        window.Jz.intent(string);
    };
    //返回上一頁
    this.goBack = function () {
        if (glitter.dialog.length > 0 && $('#diaPlace').css('display') !== 'none') {
            var last = glitter.dialog[glitter.dialog.length - 1];
            if (last.cancelable) {
                glitter.closeDiaLogWithTag(last.id);
                return;
            } else {
                return;
            }
        }
        var index = 0;
        for (var i = 0; i < glitter.iframe.length; i++) {
            var tindex = glitter.iframe.length - i - 1;
            if (tindex <= 0) {
                this.goBackOnRootPage();
                return;
            }
            if (glitter.iframe[tindex].goback) {
                index = tindex;
                break;
            }
        }
        if (index <= 0) {
            this.goBackOnRootPage();
            return;
        }
        document.getElementById(glitter.iframe[glitter.iframe.length - 1].id).contentWindow.lifeCycle.onDestroy();
        var tag = glitter.iframe[index].id;
        $('#' + tag).remove();
        //console.log("remove" + tag)
        glitter.iframe.splice(index, 1);
        //console.log("show" + glitter.iframe[index - 1].id)
        $('#' + glitter.iframe[index - 1].id).show();
        document.getElementById(glitter.iframe[index - 1].id).contentWindow.lifeCycle.onResume();
        glitter.changePageListener(glitter.iframe[index - 1].tag);
    };
    //添加script內容
    this.addScript = function (url, callback, callbackError) {
        var script = document.createElement('script');
        script.setAttribute('src', this.baseUrl + url);
        try {
            if (script.readyState) {
                //IE
                script.onreadystatechange = function () {
                    if (script.readyState === "loaded" || script.readyState === "complete") {
                        script.onreadystatechange = null;
                        callback();
                    }
                };
            } else {
                //其餘瀏覽器支援onload
                script.onload = function () {
                    if (callback !== undefined) {
                        callback();
                    }
                };
            }
            document.getElementsByTagName("head")[0].appendChild(script);
        } catch (e) {

            if (null !== callbackError) {
                alert(e);
                if (callbackError !== undefined) {
                    callbackError();
                }
            }
        }
    };
    //添加css
    this.addCss = function (fileName) {
        var head = document.head;
        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = fileName;
        head.appendChild(link);
    };
    //返回鍵的監聽
    this.onBackPressed = function () {
        this.goBack();
    };
    //設定title Bar
    this.setTitleBar = function (url) {
        $("#titleBar").show();
        $("#titleBar").html('<iframe  src="' + this.baseUrl + (glitter.deviceType === appearType.Android ? '' : '/') + url + '" id="titleBarFrame" style="height:60px;width: 100%;"></iframe>');
        $("#framePlace").css("height", "calc(100% - 60px)");
    };
    //關閉應用
    this.closeApp = function () {
        //console.log("closeApp")
        switch (glitter.deviceType) {
            case appearType.Android:
                window.GL.closeApp();
                break;
            case appearType.Ios:
                window.webkit.messageHandlers.closeApp.postMessage("");
                break;
            case appearType.Web:
                window.close();
                break;
        }
    };
    //當已反回到RootPage時，在按下返回鍵時所需執行的動作
    this.goBackOnRootPage = function () {};
    //返回首頁
    this.goMenu = function () {
        var tempFrame = [];
        tempFrame = tempFrame.concat(glitter.iframe[0]);
        $('#' + glitter.iframe[0].id).show();
        for (var i = 1; i < glitter.iframe.length; i++) {
            $('#' + glitter.iframe[i].id).remove();
        }
        glitter.iframe = tempFrame;
        glitter.changePageListener(glitter.iframe[0].id);
    };
    //頁面切換監聽
    this.changePageListener = function (tag) {};
    //顯示BARCODE掃描
    this.openQrScanner = function (callback) {
        switch (glitter.deviceType) {
            case appearType.Android:
                glitter.qrScanBack = callback;
                window.GL.openQrScanner();
                break;
            case appearType.Web:
                glitter.qrScanBack = callback;
                callback(JSON.parse(window.GL.getGPS()));
                break;
            case appearType.Ios:
                glitter.qrScanBack = callback;
                window.webkit.messageHandlers.openQrScanner.postMessage('');

                break;
        }
    };
    //掃描回條
    this.qrScanBack = function (data) {};
    //權限請求
    this.requestPermission = function (array) {
        window.GL.requestPermission(array);
    };
    /*
    * 取得定位
    * [latitude,longitude,address]
    * */
    this.getGPS = function (callback) {
        switch (glitter.deviceType) {
            case appearType.Android:
                callback(JSON.parse(window.GL.getGPS()));
                break;
            case appearType.Web:
                callback(JSON.parse(window.GL.getGPS()));
                break;
            case appearType.Ios:
                var id = glitter.callBackId += 1;
                glitter.callBackList.set(id, function (result) {
                    callback(JSON.parse(result));
                });
                var imap = {
                    callback: id
                };
                window.webkit.messageHandlers.getGPS.postMessage(JSON.stringify(imap));
                break;
        }
    };
    /*
    * 取得資料庫
    * */
    this.dataBase = new DataBase();
    /*
    * 序列化工句
    * */
    this.serialUtil = new Serialization();
    /*
    * 播放
    * */
    this.playSound = function (rout) {
        switch (glitter.deviceType) {
            case appearType.Android:
                window.GL.playSound(rout);
                break;
            case appearType.Ios:
                var imap = {
                    rout: rout
                };
                window.webkit.messageHandlers.playSound.postMessage(JSON.stringify(imap));
                break;
            case appearType.Web:
                break;
        }
    };
    /*
    * 定位套件GpsUtil
    * */
    this.gpsUtil = {
        callback: new GpsCallBack(),
        manager: new GpsManager()
    };
    /*
    * 藍牙套件
    * */
    this.bleUtil = {
        callback: new GlBle(),
        manager: new BleControl()
    };
    /*
    * Unicode轉換
    * */
    this.unicodeToString = function (data) {
        try {
            return JSON.parse("{\"unicode\":\"" + data.replaceAll("\\\\u", "\\u") + "\"}").unicode;
        } catch (e) {
            //console.log(e)
            return data;
        }
    };
    this.stringToUnicode = function (data) {
        return data.split('').map(function (value, index, array) {
            var temp = value.charCodeAt(0).toString(16).toLowerCase();
            if (temp.length > 2) {
                return "\\\\u" + temp;
            }
            return value;
        }).join('');
    };
    this.showToast = function (string, sec) {
        $('#toast').html(string);
        $('#toast').show();
        setTimeout(function () {
            $('#toast').hide();
        }, sec === undefined ? 2000 : sec);
    };
    /**
     * 取得夾帶參數
     * **/
    this.getUrlParameter = function (sParam) {
        var sPageURL = window.location.search.substring(1),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
            }
        }
    };
    /**
     * 取得Browser 類型
     * {Android,IOS,Desktop}
     * **/
    this.getBrowserDeviceType = function () {
        var a = navigator.userAgent;
        var isAndroid = a.indexOf('Android') > -1 || a.indexOf('Adr') > -1; //android终端
        var isiOS = !!a.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        if (isAndroid) {
            return "Android";
        }
        if (isiOS) {
            return "IOS";
        }
        return "Desktop";
    };
    /**
     * 開啟新視窗
     * Android and Ios呼叫 ｗｅｂｖｉｅｗ
     * */
    this.openNewTab = function (link) {
        switch (glitter.deviceType) {
            case glitter.deviceTypeEnum.Web:
                window.open(link);
                break;
            case glitter.deviceTypeEnum.Android:
                window.GL.openNewTab(link);
                break;
            case glitter.deviceTypeEnum.Ios:
                window.open(link);
                break;
        }
    };
    /**
     * 添加觀察者模式
     * */
    this.addObserver = function (map, obj, callback) {
        if (map.GlitterJsonStringConverSion === undefined) {
            map.GlitterJsonStringConverSion = JSON.parse(JSON.stringify(map));
        }
        if (map.ObServerCallBack === undefined) {
            map.ObServerCallBack = {};
        }
        if (map.ObServerCallBack[obj] === undefined) {
            map.ObServerCallBack[obj] = [];
        }
        map.ObServerCallBack[obj] = map.ObServerCallBack[obj].concat(callback);
        var keys = Object.keys(map);

        var _loop = function _loop() {
            var keyVa = keys[a];
            if (keyVa === obj) {
                Object.defineProperty(map, keyVa, {
                    get: function get() {
                        return map.GlitterJsonStringConverSion[keyVa];
                    },
                    set: function set(v) {
                        map.GlitterJsonStringConverSion[keyVa] = v;
                        var workSuccess = [];
                        this.ObServerCallBack[keyVa].map(function (it) {
                            try {
                                it(v);
                                workSuccess = workSuccess.concat(it);
                            } catch (e) {
                                alert(e);
                            }
                        });
                        this.ObServerCallBack[keyVa] = workSuccess;
                    }
                });
                callback(map[keyVa]);
            }
        };

        for (var a = 0; a < keys.length; a++) {
            _loop();
        }
    };
    /**
     移除html標籤
     * */
    this.removeTag = function (str) {
        return str.replace(/<[^>]+>/g, ""); //去掉所有的html標記
    };
    /**
     判斷滾動位置，和滑動到哪裡
     * */
    this.addScrollListener = function (e, obj) {
        e.scroll(function () {
            var map = {
                scrollTop: $(this)[0].scrollTop,
                scrollHeight: $(this)[0].scrollHeight,
                windowHeight: $(this).height()
            };
            if (map.scrollTop + map.windowHeight >= map.scrollHeight) {
                if (obj.scrollBtn !== undefined) {
                    obj.scrollBtn();
                }
            }
            if (map.scrollTop === 0) {
                if (obj.scrollTp !== undefined) {
                    obj.scrollTp();
                }
            }
        });
    };
    /*
    * 判斷是否到底部
    * */
    this.isScrollBtn = function (e) {
        var map = {
            scrollTop: e[0].scrollTop,
            scrollHeight: e[0].scrollHeight,
            windowHeight: e.height()
        };
        return map.scrollTop + map.windowHeight >= map.scrollHeight;
    };
    /*
    * 判斷是否到頂部
     * */
    this.isScrollTp = function (e) {
        var map = {
            scrollTop: e[0].scrollTop,
            scrollHeight: e[0].scrollHeight,
            windowHeight: e.height()
        };
        return map.scrollTop === 0;
    };
    /*
    * 滑動到底部
    * */
    this.scrollToBtn = function (e) {
        var map = {
            scrollTop: e[0].scrollTop,
            scrollHeight: e[0].scrollHeight,
            windowHeight: e.height()
        };
        e.scrollTop(map.scrollHeight - map.windowHeight);
    };
};

var LifeCycle = function LifeCycle(props) {
    _classCallCheck(this, LifeCycle);

    this.onResume = function () {};
    this.onPause = function () {};
    this.onDestroy = function () {};
};

//Application生命週期


var lifeCycle = new LifeCycle();
//glitter變數
var glitter = new Glitter();
//glitter變數
var rootGlitter = glitter;
//顯示類型
var appearType = new AppearType();
// 監聽鍵盤按鍵事件，並回傳所按的按鍵為何
window.addEventListener('keydown', function (e) {
    var arrat = glitter.dialog;
    for (var i = 0; i < arrat.length; i++) {
        if (arrat[i].cancelable) {
            glitter.closeDiaLog(arrat[i].id);
        }
    }
});

function parseCookie() {
    var cookieObj = {};
    var cookieAry = document.cookie.split(';');
    var cookie;
    for (var i = 0, l = cookieAry.length; i < l; ++i) {
        cookie = jQuery.trim(cookieAry[i]);
        cookie = cookie.split('=');
        cookieObj[cookie[0]] = cookie[1];
    }
    return cookieObj;
}

function getCookieByName(name) {
    var value = parseCookie()[name];
    if (value) {
        value = decodeURIComponent(value);
    }

    return value;
}

function waitGetPro() {
    $.ajax({
        type: "POST",
        url: glitter.webUrl + "/getPro",
        timeout: 5000,
        success: function success(data) {
            glitter.webPro = JSON.parse(data);
            onCreate();
        },
        error: function error(data) {
            waitGetPro();
        }
    });
}

var onWait = false;

function waitSetPro(key, data) {
    if (onWait) {
        var re = function re() {
            waitSetPro(key, data);
        };

        setTimeout(re, 100);
        return;
    }
    onWait = true;
    $.ajax({
        type: "POST",
        url: glitter.webUrl + "/setPro?key=" + key,
        data: data,
        timeout: 5000,
        success: function success(data) {
            onWait = false;
        },
        error: function error(d) {
            //console.log(d)
            onWait = false;
            waitSetPro(key, data);
        }
    });
}

$(document).ready(function () {
    if (window.GL !== undefined) {
        glitter.deviceType = appearType.Android;
    } else if (navigator.userAgent === 'iosGlitter') {
        glitter.deviceType = appearType.Ios;
    }
    switch (glitter.deviceType) {
        case appearType.Web:
        {
            console.log('appearType.Web')
            glitter.baseUrl = "../";
            waitGetPro();
            break;
        }
        case appearType.Android:
        {
            console.log('appearType.Android')
            onCreate();
            break;
        }
        case appearType.Ios:
        {
            console.log('appearType.IOS')
            glitter.baseUrl = "../";
            onCreate();
            break;
        }
    }
});

glitterInitial();

function glitterInitial() {
    if (glitter.deviceType !== glitter.deviceTypeEnum.Android) {
        window.history.pushState(null, null, "#");
        window.addEventListener("popstate", function (e) {
            glitter.goBack();
            window.history.pushState(null, null, "#");
        });
    }
    if (navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPad/i)) {
        /* iOS hides Safari address bar */
        window.addEventListener("load", function () {
            setTimeout(function () {
                window.scrollTo(0, 1);
            }, 1000);
        });
    }
    glitter.getBoundingClientRect = $('html').get(0).getBoundingClientRect();
}