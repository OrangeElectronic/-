function getCarView(a) {
    switch (a) {
        case 'N_2':
            waitSwitch=function () {
                $('.CarPlace').css('background-image', 'url(../img/motorcycle.png)')
                $('.CarPlace').css('background-size', '150% 150%')
                $('.CarPlace').css('background-position','center')
            }
            return ``
        case "R_4":
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_trailer4_rear.png)')
            }
            return `<table style="width: 100%;margin-top: 135px">
        <tr style="height: 40px;">
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w3"></td>
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w1"></td>
        </tr><tr style="height: 40px;">
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w4"></td>
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w2"></td>
        </tr>
        </table>`

        case "R_8":
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_trailer8_rear.png)')
            }
            return `<table style="width: 100%;margin-top: 135px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w3">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w5">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w4">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w8">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
        </tr>
        </table>`
        case "R_12":
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_trailer12_rear.png)')
            }
            return `<table style="width: 100%;margin-top: 70px">
 <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w4">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w10">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w7">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
        <tr style="height: 40px;width: 100%;display: flex;margin-top :25px">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w5">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w11">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w12">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w9">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
        </table>`
        case "R_16":
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_trailer16_rear.png)')
            }
            return `<table style="width: 100%;margin-top: 43px;">
 <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w5">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w13">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w9">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
 <tr style="height: 40px;width: 100%;display: flex;margin-top: 27px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w14">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w10">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
        </tr>
        <tr style="height: 40px;width: 100%;display: flex;margin-top :10px">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w7">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w15">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w11">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w16">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w12">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w4">
        </td>
        </tr>
        </table>`
        case "R_20":
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_trailer20_rear.png)')
            }
            return `<table style="width: 100%;margin-top: 15px;">
 <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w16">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w11">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
 <tr style="height: 40px;width: 100%;display: flex;margin-top: 13px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w7">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w17">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w12">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
        </tr>
 <tr style="height: 40px;width: 100%;display: flex;margin-top: 27px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w18">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w13">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
        <tr style="height: 40px;width: 100%;display: flex;margin-top :10px">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w9">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w19">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w14">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w4">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w10">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w20">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w15">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
        </tr>
        </table>`
        case 'F_6_24':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_trailer6_front.png)')
            }
            return `<table style="width: 100%;margin-top: 70px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 70px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w4">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
        </table>`
        case 'F_8_224':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_8wheels224.png)')
            }
            return `<table style="width: 100%;margin-top: 60px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
         <tr style="height: 40px;width: 100%;display: flex;margin-top: 50px;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w4">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top:10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
        </tr>
        </table>`
        case 'F_10_244':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_trailer10_front.png)')
            }
            return `<table style="width: 100%;margin-top: 50px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
         <tr style="height: 40px;width: 100%;display: flex;margin-top: 70px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 0px" id="w4">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top:5px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w10">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w9">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 0px" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
        </tr>
        </table>`
        case 'N_4':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_4wheels.png)')
            }
            return `<table style="width: 100%;margin-top: 75px">
        <tr style="height: 40px;">
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w2"></td>
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w1"></td>
        </tr>
        <tr style="height: 40px;">
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w4" style="margin-top: 65px;"></td>
        <td><img src="../img/icon_wheel_normal.png" class="wheel" id="w3" style="margin-top: 65px;"></td>
        </tr>
        </table>`
        case 'N_6':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_6wheels.png)')
            }
            return `<table style="width: 100%;margin-top: 70px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 75px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w4">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
        </table>`
        case 'N_8_224':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_8wheels224.png)')
            }
            return `<table style="width: 100%;margin-top: 60px">
  <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w2">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 16px;" id="w1">
        </td>
        </tr>
        <tr style="height: 40px;width: 100%;margin-top:50px;display: flex;">
      <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w4">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 16px;" id="w3">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
        </tr>
        </table>`
        case 'N_8_242':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_8wheels242.png)')
            }
            return `<table style="width: 100%;margin-top: 60px">
  <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w2">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 16px;" id="w1">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;margin-top:50px;display: flex;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w4">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
         <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
      <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w8">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 16px;" id="w7">
        </td>
        </tr>
        </table>`
        case 'N_10_244':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_10wheels244.png)')
            }
            return `<table style="width: 100%;margin-top: 60px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
         <tr style="height: 40px;width: 100%;display: flex;margin-top: 50px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 0px" id="w4">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top:10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w10">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w9">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 0px" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
        </tr>
        </table>`
        case 'N_10_2224':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_10wheels2224.png)')
            }
            return `<table style="width: 100%;margin-top: 40px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
         <tr style="height: 40px;width: 100%;display: flex;margin-top: 25px;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w4">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
        <tr style="height: 40px;width: 100%;display: flex;margin-top: 25px;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w6">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top:10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w10">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w9">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: 0px" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
        </tr>
        </table>`
        case 'N_12':
              waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_12wheels.png)')
            }
            return `<table style="width: 100%;margin-top: 40px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
         <tr style="height: 40px;width: 100%;display: flex;margin-top: 25px;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w4">
        </td>
         <td style="width: 50%;padding-left: 50px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
        <tr style="height: 40px;width: 100%;display: flex;margin-top :25px">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w12">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w11">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w10">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w9">
        </td>
        </tr>
        </table>`
        case 'N_14':
            waitSwitch = function () {
                $('.CarPlace').css('background-image', 'url(../img/img_14wheels.png)')
            }
            return `<table style="width: 100%;margin-top: 40px">
        <tr style="height: 40px;width: 100%;display: flex;">
        <td style="width: 50%;padding-left: 30px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w2">
        </td>
         <td style="width: 50%;padding-left: 45px;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w1">
        </td>
        </tr>
         <tr style="height: 40px;width: 100%;display: flex;margin-top: 25px;">
       <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w6">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w5">
        </td>
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w4" style="margin-left: 0;">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w3">
        </td>
        </tr>
        <tr style="height: 40px;width: 100%;display: flex;margin-top :25px">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w10">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w9">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w8">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w7">
        </td>
        </tr>
          <tr style="height: 40px;width: 100%;display: flex;margin-top: 10px;">
        <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" id="w14">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w13">
        </td>
         <td style="width: 50%;display: flex;">
        <img src="../img/icon_wheel_normal.png" class="wheel" style="margin-left: -3px" id="w12">
        <img src="../img/icon_wheel_normal.png" class="wheel2" id="w11">
        </td>
        </tr>
        </table>`
    }
}

function getLeft(a) {
    switch (a) {
        case "N_2":
            return `<div style="margin-top: 65px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n1">--</div>
</div>
</div>`
        case "R_4":
            return `<div style="margin-top: 65px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n3">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline">
<div  class="intext" id="n4">--</div>
</div>
</div>`
        case "R_8":
            return `<div style="margin-top: 65px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n3">--</div>
<div  class="intext" id="n7">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline">
<div  class="intext" id="n4">--</div>
<div  class="intext" id="n8">--</div>
</div>
</div>`
        case "R_12":
            return `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n4">--</div>
<div  class="intext" id="n10">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline">
<div  class="intext" id="n5">--</div>
<div  class="intext" id="n11">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n2">--</div>
</div>
</div>`
        case "R_16":
            return `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline">
<div  class="intexts" id="n5">--</div>
<div  class="intexts" id="n13">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline">
<div  class="intexts" id="n6">--</div>
<div  class="intexts" id="n14">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n7">--</div>
<div  class="intexts" id="n15">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n8">--</div>
<div  class="intexts" id="n16">--</div>
</div>
</div>
`
        case "R_20":
            return `
            <div style="margin-top: 0px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline">
<div  class="intexts" id="n6">--</div>
<div  class="intexts" id="n16">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline">
<div  class="intexts" id="n7">--</div>
<div  class="intexts" id="n17">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n8">--</div>
<div  class="intexts" id="n18">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n9">--</div>
<div  class="intexts" id="n19">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n10">--</div>
<div  class="intexts" id="n20">--</div>
</div>
</div>
`
        case 'F_6_24':
            return  `<div style="margin-top: 65px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n5">--</div>
</div>
</div>`
        case 'F_8_224':
            return  `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline">
<div  class="intext" id="n4">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline">
<div  class="intext" id="n8">--</div>
<div  class="intext" id="n7">--</div>
</div>
</div>`
        case 'F_10_244':
            return  `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n5">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline">
<div  class="intext" id="n10">--</div>
<div  class="intext" id="n9">--</div>
</div>
</div>`
        case 'N_4':
            return  `<div style="margin-top: 65px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline">
<div  class="intext" id="n4">--</div>
</div>
</div>`
        case 'N_6':
            return  `<div style="margin-top: 65px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n5">--</div>
</div>
</div>`
        case 'N_8_224':
            return `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline">
<div  class="intext" id="n4">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline">
<div  class="intext" id="n8">--</div>
<div  class="intext" id="n7">--</div>
</div>
</div>`
        case 'N_8_242':
            return `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n5">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline">
<div  class="intext" id="n8">--</div>
</div>
</div>`
        case 'N_10_244':
            return   `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline">
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n5">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline">
<div  class="intext" id="n10">--</div>
<div  class="intext" id="n9">--</div>
</div>
</div>`
        case 'N_10_2224':
            return  `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline">
<div  class="intexts" id="n2">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline">
<div  class="intexts" id="n4">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n6">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n10">--</div>
<div  class="intexts" id="n9">--</div>
</div>
</div>`
        case 'N_12':
            return  `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline">
<div  class="intexts" id="n2">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline">
<div  class="intexts" id="n4">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n8">--</div>
<div  class="intexts" id="n7">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n12">--</div>
<div  class="intexts" id="n11">--</div>
</div>
</div>`
        case 'N_14':
            return  `
            <div style="margin-top: 40px;margin-right: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline">
<div  class="intexts" id="n2">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline">
<div  class="intexts" id="n6">--</div>
<div  class="intexts" id="n5">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n10">--</div>
<div  class="intexts" id="n9">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline">
<div  class="intexts" id="n14">--</div>
<div  class="intexts" id="n13">--</div>
</div>
</div>`
    }
}

function getRight(a) {
    switch (a) {
        case "N_2":
            return `<div style="transform:translateY(180px);margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n2">--</div>
</div>
</div>`
        case "R_4":
           return `<div style="margin-top: 65px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline2">
<div  class="intext" id="n2">--</div>
</div>
</div>`
        case "R_8":
            return `<div style="margin-top: 65px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n5">--</div>
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline2">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n2">--</div>
</div>
</div>`
        case "R_12":
            return `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n7">--</div>
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline2">
<div  class="intext" id="n8">--</div>
<div  class="intext" id="n2">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline2">
<div  class="intext" id="n9">--</div>
<div  class="intext" id="n3">--</div>
</div>
</div>`
        case "R_16":
            return `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline2">
<div  class="intexts" id="n9">--</div>
<div  class="intexts" id="n1">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline2">
<div  class="intexts" id="n10">--</div>
<div  class="intexts" id="n2">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n11">--</div>
<div  class="intexts" id="n3">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n12">--</div>
<div  class="intexts" id="n4">--</div>
</div>
</div>`
        case "R_20":
            return `
            <div style="margin-top: 0px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline2">
<div  class="intexts" id="n11">--</div>
<div  class="intexts" id="n1">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline2">
<div  class="intexts" id="n12">--</div>
<div  class="intexts" id="n2">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n13">--</div>
<div  class="intexts" id="n3">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n14">--</div>
<div  class="intexts" id="n4">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n15">--</div>
<div  class="intexts" id="n5">--</div>
</div>
</div>`
        case 'F_6_24':
            return  `<div style="margin-top: 65px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline2">
<div  class="intext" id="n4">--</div>
<div  class="intext" id="n3">--</div>
</div>
</div>`
        case 'F_8_224':
            return  `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline2">
<div  class="intext" id="n3">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline2">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n5">--</div>
</div>
</div>`
        case 'F_10_244':
            return   `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline2">
<div  class="intext" id="n4">--</div>
<div  class="intext" id="n3">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline2">
<div  class="intext" id="n8">--</div>
<div  class="intext" id="n7">--</div>
</div>
</div>`
        case 'N_4':
           return `<div style="margin-top: 65px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline2">
<div  class="intext" id="n3">--</div>
</div>
</div>`
        case 'N_6':
            return  `<div style="margin-top: 65px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;margin-top:30px;display: flex;" class="inline2">
<div  class="intext" id="n4">--</div>
<div  class="intext" id="n3">--</div>
</div>
</div>`
        case 'N_8_224':
            return  `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline2">
<div  class="intext" id="n3">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline2">
<div  class="intext" id="n6">--</div>
<div  class="intext" id="n5">--</div>
</div>
</div>`
        case 'N_8_242':
            return `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline2">
<div  class="intext" id="n4">--</div>
<div  class="intext" id="n3">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline2">
<div  class="intext" id="n7">--</div>
</div>
</div>`
        case 'N_10_244':
            return  `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 85px;display: flex;" class="inline2">
<div  class="intext" id="n1">--</div>
</div>
<div style="height: 85px;display: flex;margin-top:-10px" class="inline2">
<div  class="intext" id="n4">--</div>
<div  class="intext" id="n3">--</div>
</div>
<div style="height: 85px;margin-top:0px;display: flex;" class="inline2">
<div  class="intext" id="n8">--</div>
<div  class="intext" id="n7">--</div>
</div>
</div>`
        case 'N_10_2224':
            return `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline2">
<div  class="intexts" id="n1">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline2">
<div  class="intexts" id="n3">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n5">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n8">--</div>
<div  class="intexts" id="n7">--</div>
</div>
</div>`
        case 'N_12':
            return  `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline2">
<div  class="intexts" id="n1">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline2">
<div  class="intexts" id="n3">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n6">--</div>
<div  class="intexts" id="n5">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n10">--</div>
<div  class="intexts" id="n9">--</div>
</div>
</div>`
        case 'N_14':
            return  `
            <div style="margin-top: 40px;margin-left: -20px; width: calc((100% - 144px)/2 );">
<div style="height: 70px;display: flex;" class="inline2">
<div  class="intexts" id="n1">--</div>
</div>
<div style="height: 70px;display: flex;margin-top:-10px" class="inline2">
<div  class="intexts" id="n4">--</div>
<div  class="intexts" id="n3">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n8">--</div>
<div  class="intexts" id="n7">--</div>
</div>
<div style="height: 70px;margin-top:0px;display: flex;" class="inline2">
<div  class="intexts" id="n12">--</div>
<div  class="intexts" id="n11">--</div>
</div>
</div>`
    }
}