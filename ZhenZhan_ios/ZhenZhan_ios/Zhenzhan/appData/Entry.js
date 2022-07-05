"use strict";
glitter.webUrl = 'https://bento2.orange-electronic.com'
function onCreate() {
    initPublic()

    glitter.addMtScript(['glitterBleLibrary/GlBle.js','jsLib/BleManager.js','jsLib/Command.js'],function (){
        glitter.setTitleBar("titleBar.html")
        glitter.setHome('main/Page_Select_Home.html', 'Page_Select_Home', '{}')
        glitter.readSensorID=[]
        glitter.readRxID=[]
        glitter.TxRxData= function (){
            //console.log("test")
            glitter.changePage('main/Page_EngineerBle.html',"Page_EngineerBle",true,{});

        }
        glitter.idText = function(data){

        }
        glitter.goBack()
        glitter.changePageListener = function (tag) {
            console.log("changePage" + tag)

            switch (tag){
                case "Page_Select_Home":
                    glitter.titleBarData.text='Orange TPMS'
                    glitter.titleBarData.withHome=true
                    break
                case "Page_Select_Setting":
                    glitter.titleBarData.text='APP資訊'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Setting_Privacy":
                    glitter.titleBarData.text='Orange TPMS'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Setting_Update":
                    glitter.titleBarData.text='關於APP'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Select_Function":
                    if(glitter.CarType==="F"){
                        glitter.titleBarData.text='拖車設定'
                    }
                    if(glitter.CarType==="R"){
                        glitter.titleBarData.text='板車設定'
                    }
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Select_Wheels":
                    glitter.titleBarData.text='車型設定'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Rx_Change":
                    glitter.titleBarData.text='接收器設定'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Write_Sensor_Id":
                    glitter.titleBarData.text='感測器學碼配對'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Write_Sensor_Id_Change_Tire":
                    glitter.titleBarData.text='調胎設定'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_Function_Setting_Unit_Range":
                    glitter.titleBarData.text='警示值設定'
                    glitter.titleBarData.withHome=false
                    break
                case "Page_EngineerBle":
                    glitter.titleBarData.text='工程模式'
                    glitter.titleBarData.withHome=false
                    break
            }
            glitter.titleBarData.update()
        }
    })

}

function initPublic() {
    glitter.titleBarData={
        text:'振展',
        withHome:true,
        bleConnect:false,
        update:function (){}
    }
    glitter.carDefine = {
        F_6: {
            position: ["1", "F"],
            bindCount: {"1": "2", "B": "2"},
            value: {"B": "B1", "1": "72"},
            title: {top: '6輪', bottom: ''},
            carCount: 6,
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06"},
            carRow:{"1":"02","2":"04","3":"00","4":"00"}
        },
        F_8_224: {
            position: ["1", "2", "3", "F"],
            bindCount: {"1": "1", "2": "1", "3": "2", "B": "2"},
            value: {"B": "B3", "1": "01", "2": "02", "3": "74"},
            title: {top: '8輪', bottom: '224'},
            carCount: 8,
            //carIndex: {"1": "01", "2": "02", "3": "04", "4": "05", "5": "07", "6": "08", "7": "09", "8": "0A"},
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "06", "5": "07", "6": "08", "7": "09", "8": "0A"},
            carRow:{"1":"02","2":"02","3":"04","4":"00"}
        },
        F_8_242: {
            position: ["1", "2", "3", "F"],
            bindCount: {"1": "2", "2": "2", "3": "1", "B": "1"},
            value: {"B": "B3", "1": "01", "2": "02", "3": "74"},
            title: {top: '8輪', bottom: '242'},
            carCount: 8,
            //carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06", "7": "08", "8": "09"},
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06", "7": "07", "8": "0A"},
            carRow:{"1":"02","2":"04","3":"02","4":"00"}
        },
        F_10_244: {
            position: ["1", "2", "3", "F"],
            bindCount: {"1": "2", "2": "2", "3": "2", "B": "2"},
            value: {"B": "B3", "1": "01", "2": "02", "3": "04"},
            title: {top: '10輪', bottom: '244'},
            carCount: 10,
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06", "7": "07", "8": "08","9":"09","10":"0A"},
            carRow:{"1":"02","2":"04","3":"04","4":"00"}
        },
        F_10_2224: {
            position: ["1", "2", "3", "4", "5", "F"],
            bindCount: {"1": "1", "2": "1", "3": "1", "4": "1", "B": "2", "5": "2"},
            value: {"B": "B5", "1": "01", "2": "02", "3": "03", "4": "04", "5": "76"},
            title: {top: '10輪', bottom: '2224'},
            carCount: 10,
            //carIndex: {"1": "01", "2": "02", "3": "04", "4": "05", "5": "08", "6": "09", "7": "0B", "8": "0C","9":"0D","10":"0E"},
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "06", "5": "07", "6": "0A", "7": "0B", "8": "0C","9":"0D","10":"0E"},
            carRow:{"1":"02","2":"02","3":"02","4":"04"}
        },
        F_12: {
            position: ["1", "2", "3", "4", "5", "F"],
            bindCount: {"1": "1", "2": "1", "3": "2", "4": "2", "B": "2", "5": "2"},
            value: {"B": "B5", "1": "01", "2": "02", "3": "03", "4": "04", "5": "76"},
            title: {top: '12輪', bottom: ''},
            carCount: 12,
            //carIndex: {"1": "01", "2": "02", "3": "04", "4": "05", "5": "07", "6": "08", "7": "09", "8": "0A","9":"0B","10":"0C","11":"0D","12":"0E"},
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "06", "5": "07", "6": "08", "7": "09", "8": "0A","9":"0B","10":"0C","11":"0D","12":"0E"},
            carRow:{"1":"02","2":"02","3":"04","4":"04"}
        },
        F_14: {
            position: ["1", "2", "3", "4", "5", "F"],
            bindCount: {"1": "2", "2": "2", "3": "2", "4": "2", "B": "2", "5": "2"},
            value: {"B": "B5", "1": "01", "2": "02", "3": "03", "4": "04", "5": "76"},
            title: {top: '14輪', bottom: ''},
            carCount: 14,
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06", "7": "07", "8": "08","9":"09","10":"0A","11":"0B","12":"0C","13":"0D","14":"0E"},
            carRow:{"1":"02","2":"04","3":"04","4":"04"}
        },
        R_4: {
            position: ["1","F"],
            bindCount: {"1": "1", "2": "1"},
            value: { "E": "12", "1": "11"},
            title: {top: '4輪', bottom: ''},
            carCount: 4,
            carIndex: {"1": "01", "2": "04", "3": "05", "4": "08"},
            carRow:{"1":"02","2":"02","3":"00","4":"00"}
        },
        R_6_222: {
            position: ["1", "2", "3", "F"],
            bindCount: { "1": "1", "2": "1", "3": "1", "4": "1"},
            value: {"E": "14", "1": "11", "2": "12", "3": "13"},
            title: {top: '6輪', bottom: '222'},
            carCount: 6,
            carIndex: {"1": "01", "2": "04", "3": "05", "4": "08", "5": "09", "6": "0C"},
            carRow:{"1":"02","2":"02","3":"02","4":"00"}
        },
        R_8_44: {
            position: ["1", "F"],
            bindCount: { "1": "2", "2": "2"},
            value: {"E": "12", "1": "11"},
            title: {top: '8輪', bottom: '44'},
            carCount: 8,
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06", "7": "07", "8": "08"},
            carRow:{"1":"04","2":"04","3":"00","4":"00"}
        },
        R_8_2222: {
            position: ["1", "2", "3", "4", "5", "F"],
            bindCount: {"1": "1", "2": "1", "3": "1", "4": "1", "5": "1", "6": "1"},
            value: {"E": "16", "1": "11", "2": "12", "3": "13", "4": "14", "5": "15"},
            title: {top: '8輪', bottom: '2222'},
            carCount: 8,
            carIndex: {"1": "01", "2": "04", "3": "05", "4": "08", "5": "09", "6": "0C", "7": "0D", "8": "10"},
            carRow:{"1":"02","2":"02","3":"02","4":"02"}
        },
        R_12: {
            position: ["1", "2", "3", "F"],
            bindCount: {"1": "2", "2": "2", "3": "2", "4": "2"},
            value: {"E": "14", "1": "11", "2": "12", "3": "13"},
            title: {top: '12輪', bottom: ''},
            carCount: 12,
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06", "7": "07", "8": "08","9":"09","10":"0A","11":"0B","12":"0C"},
            carRow:{"1":"04","2":"04","3":"04","4":"00"}
        }
        ,R_16: {
            position: ["1", "2", "3", "4", "5", "F"],
            bindCount: {"1": "2", "2": "2", "3": "2", "4": "2", "5": "2", "6": "2"},
            value: {"E": "16", "1": "11", "2": "12", "3": "13", "4": "14", "5": "15"},
            title: {top: '16輪', bottom: ''},
            carCount: 16,
            carIndex: {"1": "01", "2": "02", "3": "03", "4": "04", "5": "05", "6": "06", "7": "07", "8": "08","9":"09","10":"0A","11":"0B","12":"0C","13":"0D","14":"0E","15":"0F","16":"10"},
            carRow:{"1":"04","2":"04","3":"04","4":"04"}
        }
    }


}


// fun testFunction(a:String){}