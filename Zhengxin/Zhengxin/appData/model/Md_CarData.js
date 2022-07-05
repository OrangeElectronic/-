"use strict";

class CarInterFace {
    constructor() {
        this.tempData = undefined
        //設定車輛
        this.setData_Setting_Car = function (a) {
            this.tempData = a
            glitter.setPro("Data_Setting_Car", JSON.stringify(a))
        }
        //取得車輛
        this.getData_Setting_Car = function () {

            return this.tempData.filter(function (a){
               return ["N_2","R_4","R_8","R_12","R_16","R_20","F_6_24","F_8_224","F_10_244","N_4","N_6"
               ,"N_8_224","N_8_242","N_10_244","N_10_2224","N_12","N_14"].indexOf(a.carType) !== -1
            })
        }
    }
}

var carInterFace = new CarInterFace()
glitter.getPro("Data_Setting_Car", function (data) {
    try {
        carInterFace.tempData = JSON.parse(data.data)
    }catch (e){}
})

//設定的車輛
class Data_Setting_Car {
    constructor() {
        this.plateNumber = ''
        this.carName = ''
        this.carType = ''
        this.count = ''
        this.make = ''
        this.model = ''
        this.year = ''
        this.weight = ''
        this.sensor = []
        this.isFront = false
    }
}

glitter.getData_Setting_Model = function () {
    return new Data_Setting_Car()
}
glitter.getData_SensorPosition = function (id, wh, barCode) {
    return new SensorPosition(id, wh, barCode)
}

//Sensor的放置位置和輪胎條碼
class SensorPosition {
    constructor(id, wh, barCode) {
        this.id = id
        this.wh = wh
        this.barCode = barCode
    }
}

//Car資料庫儲存
class CarSetting {
    constructor() {
        this.admin = ''
        this.plate = ''
        this.json = ''
    }
}


// //車輛資料
// data class Data_Setting_Car(
// var plateNumber: String,
// var carName: String,
// var carType: String,
// var count: String,
// var make: String,
// var model: String,
// var year: String,
// var weight: String,
// var sensor: ArrayList<SensorPosition>,
// var isFront: Boolean = true
// )
//
// //Sensor的放置位置和輪胎條碼
// data class SensorPosition(
// var id: String,
// var wh: String,
// var barCode: String
// )
//
// //Car資料庫儲存
// data class CarSetting(
// var admin: String = "NA",
// var plate: String = "NA",
// var json: String = "NA"
// )