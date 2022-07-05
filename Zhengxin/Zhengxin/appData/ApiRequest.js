"use strict";
class ApiRequest {
    constructor() {
        //Domain路徑
        this.domain = "https://bento3.orange-electronic.com"
        //api路徑
        this.apiRoot = this.domain + '/Zhexing';
        //登入帳號
        this.account = ""
        //資料請求標頭
        this.postWithDialog = function (map, success, error) {
            console.log(`domain:${this.domain} request:`+JSON.stringify(map))
            glitter.openDiaLog('dialog/Dia_DataLoading.html','Dia_DataLoading',false,false,{})
            $.ajax({
                type: "POST",
                url: this.apiRoot,
                data: '' + JSON.stringify(map),
                timeout: 1000*30,
                success: function (data) {
                    glitter.closeDiaLogWithTag('Dia_DataLoading')
                    try {
                        success(JSON.parse(data))
                    }catch (e) {
                        alert (e.stack)  // this will work on chrome, FF. will no not work on safari
                        alert (e.line)  // this will work on safari but not on IPhone
                        var map={}
                        map.result="false"
                        success(map)
                    }

                },
                error: function (data) {
                    console.log('error' +JSON.stringify(data))
                    glitter.closeDiaLogWithTag('Dia_DataLoading')
                    error(data)
                    glitter.openDiaLog('dialog/Dia_DisConnect.html','disconnect',false,true,{})
                }
            });
        }
        //資料請求標頭
        this.postTextWithDialog = function (text, success, error) {
            $.ajax({
                type: "POST",
                url: "https://bento3.orange-electronic.com/exsql",
                data: text,
                timeout: 1000*30,
                success: function (data) {
                    glitter.closeDiaLogWithTag('Dia_DataLoading')
                    try {
                        success(data)
                    }catch (e) {
                        success("false")
                    }

                },
                error: function (data) {
                    console.log('error' +JSON.stringify(data))
                    glitter.closeDiaLogWithTag('Dia_DataLoading')
                    error(data)
                    glitter.openDiaLog('dialog/Dia_DisConnect.html','disconnect',false,true,{})
                }
            });
        }
        //資料請求標頭
        this.postText = function (text, success, error) {
            $.ajax({
                type: "POST",
                url: "https://bento3.orange-electronic.com/exsql",
                data: text,
                timeout: 1000*30,
                success: function (data) {
                    glitter.closeDiaLogWithTag('Dia_DataLoading')
                    try {
                        success(data)
                    }catch (e) {
                        success("false")
                    }

                },
                error: function (data) {
                    console.log('error' +JSON.stringify(data))
                    error(data)
                }
            });
        }
        //
        this.postRequest= function (map, success, error) {
            $.ajax({
                type: "POST",
                url: this.apiRoot,
                data: '' + JSON.stringify(map),
                timeout: 5000,
                success: function (data) {
                    try {
                        success(JSON.parse(data))
                    }catch (e) {
                        alert (e.stack)  // this will work on chrome, FF. will no not work on safari
                        alert (e.line)  // this will work on safari but not on IPhone
                        var map={}
                        map.result="false"
                        success(map)
                    }

                },
                error: function (data) {
                    error(data)
                   }
            });
        }
    }
}

var apiRequest=new ApiRequest()