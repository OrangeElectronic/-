
//MMY查詢Model
glitter.mmyInterFace={
    //查詢Make
    queryMake:function (success,error){
        var sql="select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA')  replacer  order by `Make` asc".replace("replacer",this.filterItem())

        glitter.dataBase.query("mmy",sql
            ,success,error)
    },
    //記錄Make選擇
    selectMake:undefined,
    //查詢Model
    queryModel:function (success,error){
        glitter.dataBase.query("mmy","select distinct `Model` from `Summary table` where `Make`='"+this.selectMake+"'   replacer  order by `Model` asc".replace("replacer",this.filterItem())
            ,success,error)
    },
    //記錄Model選擇
    selectModel:undefined,
    //查詢Year
    queryYear:function (success,error){
        glitter.dataBase.query("mmy","select  `Year`,`Relearn code` from `Summary table` where `Make`='"+this.selectMake+"' and `Model`='"+this.selectModel+"'    replacer  order by `Year` asc".replace("replacer",this.filterItem())
            ,success,error)
    },
    //查詢我的最愛
    queryLove:function (success,error){
        glitter.serialUtil.listObject("favorite",50,success,error)
    },
    //記錄Year選擇
    selectYeart:undefined,
    set selectYear(name) {
        this.selectYeart=name
        console.log("select * from `Summary table` where `Make`='"+this.selectMake+"' and `Model`='"+this.selectModel+"' and `Year`='"+name+"' limit 0,1")
        glitter.dataBase.query("mmy","select * from `Summary table` where `Make`='"+glitter.mmyInterFace.selectMake+"' and `Model`='"+glitter.mmyInterFace.selectModel+"' and `Year`='"+glitter.mmyInterFace.selectYeart+"' limit 0,1"
            ,function (result){
                if(result.length>0){
                    glitter.mmyInterFace.selectMMY=result[0]
                    glitter.serialUtil.deleteObject((glitter.mmyInterFace.selectMake+glitter.mmyInterFace.selectModel+glitter.mmyInterFace.selectYear),"favorite",function (){
                    },function (){

                    })
                    glitter.serialUtil.storeObject(result[0],(glitter.mmyInterFace.selectMake+glitter.mmyInterFace.selectModel+glitter.mmyInterFace.selectYear),"favorite",function (){},function (){})
                }
            },function (){})
    },
    get selectYear(){
        console.log("selectYeart:"+this.selectYeart)
        return this.selectYeart
    },
    getRelearnMode:function (callback){
        var qu=(glitter.mmyInterFace.selectMMY["Relearn code"].indexOf('INDIRECT')!==-1)?`RelearnProcedureText`:`RelearnProcedure`
        glitter.dataBase.query("mmy","select * from `"+qu+"` where `RelearnNumber`='"+glitter.mmyInterFace.selectMMY["Relearn code"]+"' limit 0,1"
            ,function (result){
                callback(result[0])
            },function (){})
    },
    //紀錄MMY item
    selectMMYt:undefined,
    set selectMMY(name){
        this.selectMMYt=name
        var sql=`insert into \`oglitememory\`.\`mmy_select_function\` (make,model,year,type,\`function\`,account) values 
        ('${this.selectMMYt.Make}','${this.selectMMYt.Model}','${this.selectMMYt.Year}',
         '${glitter.share.mmySelectType}','${glitter.selectFunction.select}','${glitter.publicBeans.account}')`
        glitter.mysqlDataBase.insertSql(sql)
    },
    get selectMMY(){
        return this.selectMMYt
    },
    //依照Function進行過濾
    filterItem:function (){
        var indirect=''
        if(glitter.share.apkverVersion!='1.61'){
            indirect=' || (`Relearn code` like \'%INDIRECT%\')'
        }
        switch (glitter.selectFunction.select){
            case glitter.selectFunction.enum.ReadSensor:
                return "and (`OGL Read`='True')"+indirect
            case glitter.selectFunction.enum.IdCopy:
                return "and (`OGL CopyID`='True')"+indirect
            case glitter.selectFunction.enum.Program:
                return "and (`OGL Programe`='True')"+indirect
            case glitter.selectFunction.enum.IdCopy_OBD:
                return "and `OGL CopyID`='True' and OBD2 not in('NA')"
            case glitter.selectFunction.enum.ObdRelearn:
                return "and `OGL Auto` != '4'"
        }
        return ""
    },
    //燒錄數量選擇
    numberChoice:1,
    //取得s19
    getS19:function (type,callBack){
        glitter.runJsInterFace("FileManager_GetFile",{
            route:"s19/"+glitter.mmyInterFace.selectMMY["Direct Fit"],
            type:"text"
        },function (response){
            if(response.result){
                callBack(response.data)
            }else{
                callBack()
            }
        })
    },
}