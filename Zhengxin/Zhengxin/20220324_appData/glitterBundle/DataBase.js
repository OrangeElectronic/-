"use strict";
class DataBase {
    constructor() {
        this.exSql = function (dataBase, text, success, error) {
            glitter.runJsInterFace("DataBase_exSql",{
                string:text,
                name:dataBase
            },function (response){
                if(response.result){
                    success()
                }else{
                    error()
                }
            })
        }
        this.query = function (dataBase, text, success, error) {
            glitter.runJsInterFace("DataBase_Query",{
                string:text,
                name:dataBase
            },function (response){
                if(response.result){
                    success(response.data)
                }else{
                    error()
                }
            })
        }
        this.initByFile = function (dataBase, rout, success, error) {
            glitter.runJsInterFace("DataBase_InitByAssets",{
                rout:rout,
                name:dataBase
            },function (response){
                if(response.result){
                    success()
                }else{
                    error()
                }
            })

        }
        this.initByLocalFile = function (dataBase, rout, success, error) {
            glitter.runJsInterFace("DataBase_InitByLocal",{
                rout:rout,
                name:dataBase
            },function (response){
                if(response.result){
                    success()
                }else{
                    error()
                }
            })
        }
        this.init = function (rout, success, error) {
            switch (glitter.deviceType){
                case appearType.Web:
                    var map = {}
                    map.rout = window.location.pathname.replace("/Glitter", "Glitter").replace("/glitterBundle/home.html", "/") + rout
                    map.dataBase = "Glitter"
                    $.ajax({
                        type: "POST",
                        url: glitter.webUrl+'/DataBase/initByFile',
                        timeout: 60000,
                        data: JSON.stringify(map), // serializes the form's elements.
                        success: function (data) {
                            success()
                        },
                        error: function (data) {
                            error()
                            //console.log("error:" + data)
                        }
                    });
                    break
                case appearType.Android:
                    success()
                    break
                case appearType.Ios:
                    success()
                    break
            }

        }
    }
}