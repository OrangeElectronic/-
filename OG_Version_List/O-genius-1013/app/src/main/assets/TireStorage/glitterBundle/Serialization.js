"use strict";
class Serialization {
    constructor() {
        /**
        * 儲存序列化檔案
        * name/rout/data
        * */
        this.storeObject = function (map,response) {
            map.data=JSON.stringify(map.data)
            glitter.runJsInterFace('writeFile',map,response)
        }
        /**
         * 取得序列化物件
         * rout/name
         * */
        this.getObject = function (map,responses) {
          glitter.runJsInterFace("readFile",map,function (response){
              if(response.result){
                  response.data=JSON.parse(response.data)
              }
              responses(response)
          })
        }
        /**
        * 刪除序列化物件
         * rout/name
        * */
        this.deleteObject = function (map, response) {
           glitter.runJsInterFace("deleteFile",map,response)
        }
        /**
        * 列出此路徑序列化物件
        * rout/limit
        * */
        this.listObject = function (map, responses) {
            glitter.runJsInterFace("listFile",map,function (response){
                if(response.result){
                    response.data.map(function (data){
                        data.data=JSON.parse(data.data)
                    })
                }
                responses(response)
            })
        }

        /**
         * 清空此路徑序列化物件
         * rout
         * */
        this.deleteRout = function (map, responses) {
            glitter.runJsInterFace("deleteRout",map,responses)
        }
    }
}