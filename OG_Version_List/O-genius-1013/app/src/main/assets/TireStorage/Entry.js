function onCreate() {
    function initial(){
        if(glitter.deviceType===glitter.deviceTypeEnum.Web){glitter.share.account="orangerd"}
        // glitter.webUrl = 'http://192.168.43.61'
        glitter.share.isOgenius=true
        glitter.changePageListener=function (tag){
            console.log(tag)
            if(tag==="Page_Function_Select"){
                glitter.onBackPressed=function (){
                    glitter.share.bleUtil.stopScan()
                    glitter.closeApp()
                }
            }else{glitter.onBackPressed=function (){glitter.goBack()}}
        }

        glitter.share.dataLoading=function (re){
            if(re){
                glitter.openDiaLog('dialog/Dia_Loading.html', 'Dia_Loading', false, false, glitter.language(575), function () {})
            }else{
                glitter.closeDiaLogWithTag('Dia_Loading')
            }
        }
        glitter.share.hint=function (title){
            glitter.openDiaLog('dialog/Dia_Check_Yes.html','Dia_Check_Yes',false,true,{
                title:title,
                callback:function (){glitter.closeDiaLogWithTag('Dia_Check_Yes')}
            },function (){})
        }

        glitter.addMtScript(['glitterBleLibrary/GlBle.js','jslib/bleManager.js'],function (){
            glitter.runJsInterFace("getLan",{},function (response){
                if(response===undefined || (!response.result)){
                    glitter.language=function (id){return id}
                    glitter.getLan=function (id){return id}
                }else{
                    glitter.language=function (id){
                        if(response["data"][`${id}`]===undefined){return id;}
                        return response["data"][`${id}`]}
                    glitter.getLan=function (id){
                        if(response["data"][`${id}`]===undefined){return id;}
                        return response["data"][`${id}`]
                    }
                }
                glitter.setTitleBar('navigation.html')
                if(glitter.deviceType===glitter.deviceTypeEnum.Web){
                    // glitter.openDiaLog('dialog/Dia_Loading.html','Dia_Loading',false,false,{},function (){})
                    glitter.setHome('page/Page_Function_Select.html','Page_Function_Select',{})
                }else{
                    glitter.setHome('page/Page_Function_Select.html','Page_Function_Select',{})
                }
                // glitter.openDiaLog('dialog/Dia_Date_Picker.html','Dia_Date_Picker',false,false,{},function (){})
            })
            try {
                getData()
                getBrand()
                define()
            }catch (e){}
        },function (){})
        glitter.share.getMMY=function (string,response){
            glitter.runJsInterFace("getMMY",{
                string:string
            },response)
        }
        glitter.runJsInterFace("OGAccount",{},function (response){
            if(response.account!==undefined){  glitter.share.account=response.account}
        })
        glitter.share.publicData={
            //輪胎品牌
            brand:[]
        }
        lifeCycle.onResume=function (){
            glitter.runJsInterFace('hideNavagation',{},function (data){})
        }
    }
    glitter.runJsInterFace("webRout",{},function (data){
        if((data===undefined)||data.webRout===undefined){
           glitter.webUrl='http://bento2.orange-electronic.com'
        }else{
            glitter.webUrl=data.webRout
        }
        initial()
    })


}
function define(){
    if (glitter.share.isOgenius) {
        glitter.runJsInterFace("getPressureUnit", {}, function (data) {
            glitter.publicBeans.pressure=data.data.replace(":","")
        })
        glitter.runJsInterFace("getTemUnit", {}, function (data) {
            glitter.publicBeans.tem=data.data.replace(":","")
        })
    }
}
function getBrand(){
    glitter.postRequest('TireHotel','getTireBrand',{},function (response){
        if(response!==undefined){
            glitter.share.publicData.brand=response.data
        }else{
            getBrand()
        }
    })
}
//上網取得資料
function getData(){
    var onUpload=false
    function uploadData(){
        try {
            if(onUpload){return}
            onUpload=true
                glitter.serialUtil.listObject({
                    rout:"waitUpload",
                    limit:1
                },function (response){
                    if(!response.result){return}
                    var size=response.data.length
                    var index=0
                    response.data.map(function (data){
                        glitter.postRequest('TireHotel','uploadAppoint',{
                            data:data.data
                        },function (response){
                            if(response!==undefined){
                                if(response.result){
                                    glitter.serialUtil.storeObject( {
                                        name:data.name,
                                        rout:'upload',
                                        data:response.data
                                    },function (){})
                                }

                                glitter.serialUtil.deleteObject({
                                    name:data.name,
                                    rout:'waitUpload',
                                },function (){})
                            }
                            index=index+1
                            if(index>=size){
                                setTimeout(function (){
                                    onUpload=false
                                },3000)
                            }
                        })
                    })
                    if(size===0){
                        onUpload=false
                    }
                })
        }catch (e){}
    }
    setInterval(function (){
        uploadData()
        console.log("loopSetInterval")
    },5000)
    glitter.share.upload=uploadData
}
function initMySocialModel(){}


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