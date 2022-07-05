"use strict";
glitter.webUrl = glitter.webUrl = "https://bento2.orange-electronic.com"

function onCreate() {
    glitter.setHome('page/register.html', 'register')
    glitter.publicBeans = new PublicBeans()
    glitter.ogInterFace = new OgInterFace()
}

class PublicBeans {
    constructor() {
        this.apiRoot = 'https://bento2.orange-electronic.com';
        //掃描回條
        this.scanBack = function (id) {
            alert(id)
        }
        //開始掃描QrCode
        this.scanSensor = function () {
            window.OG.scanSensor()
        }
    }
}

class OgInterFace {
    constructor() {
        this.closeKeyBoard = function () {
            window.OG.closeKeyBoard()
        }
        this.hideNavagation = function () {
            window.OG.setNaVaGation(true)
        }
        //取得語言
        this.getLan = function (id) {
            if (window.OG === undefined) {
                return id
            }
            return window.OG.getLan(id)
        }
        this.playBeet = function () {
            window.OG.playBeet()
        }
    }
}
