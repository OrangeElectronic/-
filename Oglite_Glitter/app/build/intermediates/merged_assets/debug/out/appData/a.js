function  getLan(code,area){
    return {
        "1":{de:"德文",tw:"中文"},
        "2":{de:"德文",tw:"中文"},
        "3":{de:"hello",tw:"你好"}
    }[code][area]
}


getLan("3","de")