"use strict";
function onCreate() {
    //set drawer
    glitter.webUrl='http://bento3.orange-electronic.com';

    //glitter.setNavigation('NavaGation.html', {})
    lifeCycle.onResume = function () {
        ogInterFace.hideNavagation()
    }

    glitter.goBackOnRootPage=function () {glitter.closeApp()}

    glitter.runJsInterFace("getLan",{},function (response){
        if(response===undefined || (!response.result)){
            ogInterFace.getLan=function (id){
                return id
            }
        }else{
            ogInterFace.getLan=function (id){
                id=`${id}`.replace("jz.","")
                if(response["data"][`${id}`]===undefined){return id;}
                return response["data"][`${id}`]
            }
        }
        glitter.setTitleBar('TitleBar.html')
        glitter.getLan=ogInterFace.getLan
        if(glitter.deviceType===glitter.deviceTypeEnum.Web){
            glitter.setHome(`page/Page_Add_Ship.html`, `Page_Add_Ship`, '{}')
        }else{
            glitter.runJsInterFace("OGAccount",{},function (response){
                glitter.account=response.account
                glitter.country=(glitter.getUrlParameter('country')==="US") ? "US" :"EU"
                if(glitter.getUrlParameter('Add_Ship')==="true"){
                    glitter.setHome(`page/Page_Add_Ship.html`, `Page_Add_Ship`, '{}')
                }else {
                    glitter.setHome(`page/OrderListPage.html`, `OrderListPage`, '{}')
                }
            })
        }

    })

}

class PublicBeans {
    constructor() {
        this.apiRoot = 'http://bento2.orange-electronic.com';
        //掃描回條
        this.scanBack = function (id) {
            alert(id)
        }
        //開始掃描QrCode
        this.scanSensor = function () {
            glitter.runJsInterFace('scanSensor',{},function (data){})
        }
    }
}

class OgInterFace {
    constructor() {
        this.closeKeyBoard = function () {
            glitter.runJsInterFace('hideKeyBoard',{},function (data){})
        }
        this.hideNavagation = function () {
            glitter.runJsInterFace('hideNavagation',{},function (data){})
        }
        this.playBeet=function (){
            glitter.runJsInterFace('playBeet',{},function (data){})
        }
    }
}
var publicBeans = new PublicBeans()
var ogInterFace = new OgInterFace()


