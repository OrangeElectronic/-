"use strict";
function onCreate() {
    console.log("onCreateApp")
    glitter.publicBeans.hareWareController=new HareWareController()
    glitter.publicBeans.hareWareController.start()
    // glitter.publicBeans.hareWareController.setGpio1(false)
    // glitter.publicBeans.hareWareController.setGpio2(false)
    glitter.publicBeans.hareWareController.setGpio(2,1)
    // glitter.publicBeans.hareWareController.setGpio(1,1)
    // alert(glitter.publicBeans.hareWareController.getGpio(0))
    alert(glitter.publicBeans.hareWareController.getGpio(2))
}

class HareWareController{
    constructor() {
        this.start=function (){window.HardwareController.start();}
        this.getGpio=function (a){return window.HardwareController.getGpio(a);}
        this.setGpio=function (a,b){ window.HardwareController.setGpio(a,b);}
        this.setGpio1=function (a){ window.HardwareController.setGpio1(a);}
        this.setGpio2=function (a){ window.HardwareController.setGpio2(a);}
    }
}