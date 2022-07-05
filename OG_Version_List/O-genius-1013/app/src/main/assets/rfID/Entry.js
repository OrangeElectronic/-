"use strict";
function onCreate(){
    // var text='d-!!!##$$-'.replace(/[^A-Za-z0-9]/g,'')
    // alert(text)
    // glitter.openDiaLog('dialog/Dia_Error.html','Dia_Error',false,true,{},function (){})
    // return
    glitter.onBackPressed=function (){
        glitter.closeApp()
    }
    glitter.runJsInterFace("getLan",{},function (response){
        if(response===undefined || (!response.result)){
            glitter.getLan=function (id){
                return id
            }
        }else{
            glitter.getLan=function (id){
                id=`${id}`.replace("jz.","")
                if(response["data"][`${id}`]===undefined){return id;}
                return response["data"][`${id}`]
            }
        }
        glitter.language=glitter.getLan
        glitter.setHome('page/Page_Read_Select.html','Page_Read_Select',{})
    })
    glitter.runJsInterFace("openRFID",{},function (){})
    glitter.setTitleBar('TitleBar.html')
    glitter.changePageListener=function (tag){
        console.log(tag)
        if(tag==="Page_Read_Rfid"){
            glitter.onBackPressed=function (){
                glitter.closeApp()
            }
        }else{glitter.onBackPressed=function (){glitter.goBack()}}
    }
    glitter.goBackOnRootPage=function (){
        glitter.closeApp()
    }
}