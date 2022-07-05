//定義公用
glitter.share.publicData={}
function getBrand(){
    glitter.postRequest('TireHotel','getTireBrand',{},function (response){
        if(response!==undefined){
            glitter.share.publicData.brand=response.data
        }else{
            getBrand()
        }
    })
}
getBrand()