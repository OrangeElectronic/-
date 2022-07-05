
function touchChange(button){
    document.getElementById(button).addEventListener('touchstart', function (event) {
        console.log('touchstart' + ':' + this.id)
        $("#"+button).addClass('contrast')

    }, false);
    document.getElementById(button).addEventListener('touchend', function (event) {
        console.log('touchend' + ':' + this.id)
        $("#"+button).removeClass('contrast')

    }, false);
    document.getElementById(button).addEventListener('touchmove', function (event) {
        //console.log('touchmove')

        if(!(this.offsetTop <= event.touches[0].clientY && this.offsetTop+this.offsetHeight >= event.touches[0].clientY &&
            this.offsetLeft <= event.touches[0].clientX && this.offsetLeft+this.offsetWidth >= event.touches[0].clientX)){
            console.log('notouch:true')
            $("#"+button).removeClass('contrast')
        }

    },false);
}

function touchColorChange(button,color,changeColor){
    document.getElementById(button).addEventListener('touchstart', function (event) {
        //console.log('touchstart' + ':' + this.id)
        $("#"+button).css('background-color',color)

    }, false);
    document.getElementById(button).addEventListener('touchend', function (event) {
        //console.log('touchend' + ':' + this.id)
        $("#"+button).css('background-color',changeColor)

    }, false);
    document.getElementById(button).addEventListener('touchmove', function (event) {
        //console.log('touchmove')

        if(!(this.offsetTop <= event.touches[0].clientY && this.offsetTop+this.offsetHeight >= event.touches[0].clientY &&
            this.offsetLeft <= event.touches[0].clientX && this.offsetLeft+this.offsetWidth >= event.touches[0].clientX)){
            //console.log('notouch:true')
            $("#"+button).css('background-color',color)
        }

    },false);
}