function getCarView(a) {
    switch (a) {
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
        case "R_4":
            return `<table style="margin-top: 125px;margin-right: -20px;"><tr><td><div class="select" id="n3">3</div></td></tr><tr><td><div class="select" id="n4">4</div></td></tr></table>`
        case "R_8":
            return `
<table style="margin-top: 125px;margin-right: -20px">
<tr>
<td>
<div class="select" id="n3">3</div>
</td>
<td>
<div class="select" id="n7">7</div>
</td>
</tr>
<tr>
<td><div class="select" id="n4">4</div></td>
<td><div class="select" id="n8">8</div></td>
</tr>
</table>`
        case "R_12":
            return `
<table style="margin-top: 70px;margin-right: -20px">
<tr>
<td>
<div class="select" id="n4">4</div>
</td>
<td>
<div class="select" id="n10">10</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n5">5</div>
</td>
<td>
<div class="select" id="n11">11</div>
</td>
</tr>
<tr>
<td><div class="select" id="n6">6</div></td>
<td><div class="select" id="n12">12</div></td>
</tr>
</table>`
        case "R_16":
            return `
<table style="margin-top: 40px;margin-right: -20px">
<tr>
<td>
<div class="select" id="n5">5</div>
</td>
<td>
<div class="select" id="n13">13</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n6">6</div>
</td>
<td>
<div class="select" id="n14">14</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n7">7</div>
</td>
<td>
<div class="select" id="n15">15</div>
</td>
</tr>
<tr>
<td><div class="select" id="n8">8</div></td>
<td><div class="select" id="n16">16</div></td>
</tr>
</table>`
        case "R_20":
            return `
<table style="margin-top: 8px;margin-right: -20px">
<tr>
<td>
<div class="select" id="n6">6</div>
</td>
<td>
<div class="select" id="n16">16</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n7">7</div>
</td>
<td>
<div class="select" id="n17">17</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n8">8</div>
</td>
<td>
<div class="select" id="n18">18</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n9">9</div>
</td>
<td>
<div class="select" id="n19">19</div>
</td>
</tr>
<tr>
<td><div class="select" id="n10">10</div></td>
<td><div class="select" id="n20">20</div></td>
</tr>
</table>`
        case 'F_6_24':
            return  `
<table style="margin-top: 60px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>    
<td >
<div class="select" id="n6" style="margin-top: 60px;">F</div>
</td>
<td >
<div class="select" id="n5" style="margin-top: 60px;">E</div>
</td>
</tr>
</table>`
        case 'F_8_224':
            return  `
<table style="margin-top: 60px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
</td>
<td>
<div class="select" id="n4" style="margin-top: 30px;">D</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n8" >H</div>
</td>
<td >
<div class="select" id="n7" >G</div>
</td>
</tr>
</table>`
        case 'F_10_244':
            return  `
<table style="margin-top: 47px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n6" style="margin-top: 50px;">F</div>
</td>
<td>
<div class="select" id="n5" style="margin-top: 50px;">E</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n10" >J</div>
</td>
<td >
<div class="select" id="n9" >I</div>
</td>
</tr>
</table>`
        case 'N_4':
            return  `<table style="margin-top: 65px;margin-right: -20px;">
<tr><td><div class="select" id="n2">B</div></td></tr>
<tr><td><div class="select" id="n4" style="margin-top: 60px;">D</div></td></tr>
</table>`
        case 'N_6':
            return  `
<table style="margin-top: 60px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n6" style="margin-top: 60px;">F</div>
</td>
<td >
<div class="select" id="n5" style="margin-top: 60px;">E</div>
</td>
</tr>
</table>`
        case 'N_8_224':
            return `
<table style="margin-top: 50px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
</td>
<td>
<div class="select" id="n4" style="margin-top: 40px;">D</div>
</td>
</tr>
<tr>
<td><div class="select" id="n8">H</div></td>
<td><div class="select" id="n7">G</div></td>
</tr>
</table>`
        case 'N_8_242':
            return `
<table style="margin-top: 50px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n6" style="margin-top: 40px;">F</div>
</td>
<td>
<div class="select" id="n5" style="margin-top: 40px;">E</div>
</td>
</tr>
<tr>
<td></td>
<td><div class="select" id="n8">H</div></td>
</tr>
</table>`
        case 'N_10_244':
            return  `
<table style="margin-top: 50px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n6" style="margin-top: 40px;">F</div>
</td>
<td>
<div class="select" id="n5" style="margin-top: 40px;">E</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n10" >J</div>
</td>
<td >
<div class="select" id="n9" >I</div>
</td>
</tr>
</table>`
        case 'N_10_2224':
            return  `
<table style="margin-top: 35px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
</td>
<td>
<div class="select" id="n4" style="margin-top: 10px;">D</div>
</td>
</tr>
<tr>
<td>
</td>
<td>
<div class="select" id="n6" style="margin-top: 10px;">F</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n10" >J</div>
</td>
<td >
<div class="select" id="n9" >I</div>
</td>
</tr>
</table>`
        case 'N_12':
            return  `
<table style="margin-top: 35px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
</td>
<td>
<div class="select" id="n4" style="margin-top: 10px;">D</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n8" style="margin-top: 10px;">H</div>
</td>
<td>
<div class="select" id="n7" style="margin-top: 10px;">G</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n12" >L</div>
</td>
<td >
<div class="select" id="n11" >K</div>
</td>
</tr>
</table>`
        case 'N_14':
            return  `
<table style="margin-top: 35px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n2">B</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n6" style="margin-top: 10px;">F</div>
</td>
<td>
<div class="select" id="n5" style="margin-top: 10px;">E</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n10" style="margin-top: 10px;">J</div>
</td>
<td>
<div class="select" id="n9" style="margin-top: 10px;">I</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n14" >N</div>
</td>
<td >
<div class="select" id="n13" >M</div>
</td>
</tr>
</table>`
    }
}

function getRight(a) {
    switch (a) {
        case "R_4":
            return `<table style="margin-top: 125px;margin-left: -20px;"><tr><td><div class="select" id="n1">1</div></td></tr><tr><td><div class="select" id="n2">2</div></td></tr></table>`
        case "R_8":
         return   `
<table style="margin-top: 125px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n5">5</div>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td><div class="select" id="n6">6</div></td>
<td><div class="select" id="n2">2</div></td>
</tr>
</table>`
        case "R_12":
            return `
<table style="margin-top: 70px;margin-left: -20px;">
<tr>
<td>
<div class="select" id="n7">7</div>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n8">8</div>
</td>
<td>
<div class="select" id="n2">2</div>
</td>
</tr>
<tr>
<td><div class="select" id="n9">9</div></td>
<td><div class="select" id="n3">3</div></td>
</tr>
</table>`
        case "R_16":
            return `
<table style="margin-top: 40px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n9">9</div>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n10">10</div>
</td>
<td>
<div class="select" id="n2">2</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n11">11</div>
</td>
<td>
<div class="select" id="n3">3</div>
</td>
</tr>
<tr>
<td><div class="select" id="n12">12</div></td>
<td><div class="select" id="n4">4</div></td>
</tr>
</table>`
        case "R_20":
            return `
<table style="margin-top: 8px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n11">11</div>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n12">12</div>
</td>
<td>
<div class="select" id="n2">2</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n13">13</div>
</td>
<td>
<div class="select" id="n3">3</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n14">14</div>
</td>
<td>
<div class="select" id="n4">4</div>
</td>
</tr>
<tr>
<td><div class="select" id="n15">15</div></td>
<td><div class="select" id="n5">5</div></td>
</tr>
</table>`
        case 'F_6_24':
            return  `
<table style="margin-top: 60px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td >
<div class="select" id="n4" style="margin-top: 60px;">D</div>
</td>
<td >
<div class="select" id="n3" style="margin-top: 60px;">C</div>
</td>
</tr>
</table>`
        case 'F_8_224':
            return  `
<table style="margin-top: 60px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n3" style="margin-top: 30px;">C</div>
</td>
<td>
</td>
</tr>
<tr>
<td >
<div class="select" id="n6" >F</div>
</td>
<td >
<div class="select" id="n5" >E</div>
</td>
</tr>
</table>`
        case 'F_10_244':
            return  `
<table style="margin-top: 47px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n4" style="margin-top: 50px;">D</div>
</td>
<td>
<div class="select" id="n3" style="margin-top: 50px;">C</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n8" >H</div>
</td>
<td >
<div class="select" id="n7" >G</div>
</td>
</tr>
</table>`
        case 'N_4':
            return  `<table style="margin-top: 65px;margin-left: -20px;">
<tr><td><div class="select" id="n1">A</div></td></tr>
<tr><td><div class="select" id="n3" style="margin-top: 60px;">C</div></td></tr>
</table>`
        case 'N_6':
            return  `
<table style="margin-top: 60px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td >
<div class="select" id="n4" style="margin-top: 60px;">D</div>
</td>
<td >
<div class="select" id="n3" style="margin-top: 60px;">C</div>
</td>
</tr>
</table>`
        case 'N_8_224':
            return  `
<table style="margin-top: 50px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n3" style="margin-top: 40px;">C</div>
</td>
<td>
</td>
</tr>
<tr>
<td><div class="select" id="n6">F</div></td>
<td><div class="select" id="n5">E</div></td>
</tr>
</table>`
        case 'N_8_242':
            return `
<table style="margin-top: 50px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n4" style="margin-top: 40px;">D</div>
</td>
<td>
<div class="select" id="n3" style="margin-top: 40px;">C</div>
</td>
</tr>
<tr>
<td><div class="select" id="n7">G</div></td>
<td></td>
</tr>
</table>`
        case 'N_10_244':
            return  `
<table style="margin-top: 50px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n4" style="margin-top: 40px;">D</div>
</td>
<td>
<div class="select" id="n3" style="margin-top: 40px;">C</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n8" >H</div>
</td>
<td >
<div class="select" id="n7" >G</div>
</td>
</tr>
</table>`
        case 'N_10_2224':
            return `
<table style="margin-top: 35px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n3" style="margin-top: 10px;">C</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n5" style="margin-top: 10px;">E</div>
</td>
<td>
</td>
</tr>
<tr>
<td >
<div class="select" id="n8" >H</div>
</td>
<td >
<div class="select" id="n7" >G</div>
</td>
</tr>
</table>`
        case 'N_12':
            return  `
<table style="margin-top: 35px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n3" style="margin-top: 10px;">C</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n6" style="margin-top: 10px;">F</div>
</td>
<td>
<div class="select" id="n5" style="margin-top: 10px;">E</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n10" >J</div>
</td>
<td >
<div class="select" id="n9" >I</div>
</td>
</tr>
</table>`
        case 'N_14':
            return  `
<table style="margin-top: 35px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n1">A</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n4" style="margin-top: 10px;">D</div>
</td>
<td>
<div class="select" id="n3" style="margin-top: 10px;">C</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n8" style="margin-top: 10px;">H</div>
</td>
<td>
<div class="select" id="n7" style="margin-top: 10px;">G</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n12" >L</div>
</td>
<td >
<div class="select" id="n11" >K</div>
</td>
</tr>
</table>`
    }
}