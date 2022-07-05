glitter.devicePhone = function (){
    glitter.runJsInterFace("getSystem_Version", {}, function (response) {
        //alert(JSON.stringify(response))
        glitter.phoneVersion=response.version
        glitter.phoneModel=response.model
        glitter.phoneMake=response.make
        if (glitter.deviceType===glitter.deviceTypeEnum.Android) {
            glitter.setSystem.appVersion=response.appVersion
        }else if(glitter.deviceType===glitter.deviceTypeEnum.Ios){
            glitter.setSystem.appVersion=response.app_version
        }
        
    })
}

//mode_data,result_data,logdata
glitter.updateMemmory = function (data) {
    //return

    var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss");

    var user_phone=""
    if (glitter.deviceType===glitter.deviceTypeEnum.Android) {
        user_phone="Android"
    }if (glitter.deviceType===glitter.deviceTypeEnum.Ios) {
        user_phone="iOS"
    }
    var app_name=glitter.app
}

//儲存log，應用在資料上傳
glitter.save_Logdata = function (data) {
    var timeInMs =new Date().format("yyyy-MM-dd hh:mm:ss");

    var user_phone=""
    if (glitter.deviceType===glitter.deviceTypeEnum.Android) {
        user_phone="Android"
    }if (glitter.deviceType===glitter.deviceTypeEnum.Ios) {
        user_phone="iOS"
    }
    var app_name=glitter.app

    glitter.uploadData.command_logdata += data
}
