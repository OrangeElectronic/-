"use strict"

class Command {
    constructor() {
        this.rx = ''
        this.send = function (data,CarType) {
            glitter.command.tx=''
            glitter.command.tx=data
            glitter.share.bleUtil.isConnect({
                callback: function (response) {
                    data = data.replace(/ /g, "")
                    if (response.result) {
                        var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss:S");
                        glitter.command.memory+=`<p style="width: calc(100% - 20px);word-break: break-all;color:green;font-size:20px">MyTx->${data}</p>
                                             <p style="width: calc(100% - 20px);margin-top:-5px;word-break: break-all;color:green">`+timeInMs+`</p>`
                        console.log('MyTx->'+data)
                        glitter.command.rx = ''

                        if(CarType === "Tread_depth"){
                            glitter.share.bleUtil.writeHex({
                                data: {
                                    data: data.replace(/ /g, ''),
                                    rxChannel: "0000FF00-0000-1000-8000-00805F9B34FB",
                                    txChannel: "0000FF00-0000-1000-8000-00805F9B34FB"
                                }
                            })
                            return
                        }
                        glitter.share.bleUtil.writeHex({
                             data: { //正式
                                 data: data.replace(/ /g, ''),
                                 rxChannel: "E093F3B5-00A3-A9E5-9ECA-40056E0EDC24",
                                 txChannel: "E093F3B5-00A3-A9E5-9ECA-40046E0EDC24"
                             }
                        })
                    }
                }
            })

        }

        //開啟胎紋深度
        this.tread_depth_Open=function (callback){
            glitter.command.send("04 00 00"
                .replace(/ /g, ""),"Tread_depth")

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')

                try {
                    if (clock.stop() > 1000) {
                        callback("true")
                        clearInterval(timer)
                    }
                }catch (e){
                    callback("false")
                    clearInterval(timer)}
            }, 200);
        }

        //胎紋深度歸零
        this.tread_depth_Clear=function (callback){
            glitter.command.send("07 00 00"
                 .replace(/ /g, ""),"Tread_depth")

            var clock=Clock()
            var timer = setInterval(function() {
                //console.log('timer')

                try {
                    if (clock.stop() > 1000) {
                        callback("true")
                        clearInterval(timer)
                    }
                }catch (e){
                    callback("false")
                    clearInterval(timer)}
            }, 200);
        }
    }
}

function Clock() {
    return {
        start: new Date(),
        stop: function () {
            return parseInt((new Date()) - (this.start))
        },
        zeroing: function () {
            this.start = new Date()
        }
    }
}

glitter.command = new Command()



