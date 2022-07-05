"use strict";

class UserInfoMation {
    constructor() {
        //最小壓力值
        this.minPre = 672
        //最大壓力值
        this.maxPre = 896
        //0為KPA 1為PSI 2為BAR
        this.punit = 1
        //0為C 1為F
        this.tunit = 0
        //用戶帳號
        this.admin = ""
        //用戶密碼
        this.password = ""
        //用戶語言
        this.lan = "CN"
        //用戶名稱
        this.name = ""
        //標準壓力值
        this.genalPre = 896
        //溫度警報
        this.temAdvice = 80
        //標準呀百分比
        this.preAdviceRatio = "25"
        //用戶姓氏
        this.first_Name = ""
        //用戶電話
        this.phone = ""
        //用戶公司
        this.companyName = ""
        //是否為管理者
        this.manager = ""
        //是否為駕駛員
        this.driver = ""
        //email
        this.email = ""
        //公司統編
        this.companyNumber = "NA"
        //員工編號
        this.staffNumber = ""
    }
}
var userInfoMation = new UserInfoMation()
//取得用戶紀錄
glitter.getPro("userInfoMation",function (data){
    try {
        var tem=JSON.parse(data.data)
        userInfoMation.admin=tem.admin
        userInfoMation.password=tem.password
    }catch (e) {}})

//儲存用戶紀錄
function storeUserInfo() {
    console.log("storeUserInfo")
    glitter.setPro("userInfoMation",JSON.stringify(userInfoMation))
}
//更新用戶紀錄
function updateUserInfoMation() {
    var map={}
    map["request"]="updateSetting"
    map["data"]=JSON.stringify(userInfoMation)
apiRequest.postWithDialog(map,function (result) {
if(result.result==='true'){
    glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, '更改成功!!')
}else {
    glitter.openDiaLog('dialog/Dia_Info.html', 'info', false, true, '更改失敗!!')
}
},function () {

})
}
