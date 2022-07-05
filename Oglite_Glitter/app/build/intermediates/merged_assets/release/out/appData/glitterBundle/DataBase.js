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
            success()

        }
    }
}