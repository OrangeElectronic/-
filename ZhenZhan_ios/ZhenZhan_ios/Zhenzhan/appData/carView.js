

function getLeft(a) {
    switch (a) {
        case "R_4":
            return `<table style="margin-top: 50px;margin-right: -20px;">
<tr>
<td><div class="select" id="n1">1</div></td>
</tr>
<tr>
<td><div class="select" id="n3" style="margin-top: -10px">3</div></td>
</tr>
</table>`
        case "R_6_222":
            return `<table style="margin-top: 50px;margin-right: -20px;">
<tr>
<td><div class="select" id="n1">1</div></td>
</tr>
<tr>
<td><div class="select" id="n3" style="margin-top: -10px;">3</div></td>
</tr>
<tr>
<td><div class="select" id="n5" style="margin-top: -10px;">5</div></td>
</tr>
</table>`
        case "R_8_44":
            return   `
<table style="margin-top: 50px;margin-right: -20px">
<tr >
<td><div class="select" id="n1" >1</div></td>
<td><div class="select" id="n2" >2</div></td>
</tr>
<tr>
<td ><div class="select" id="n5" style="margin-top: -10px;">5</div></td>
<td ><div class="select" id="n6" style="margin-top: -10px;">6</div></td>
</tr>
</table>`
        case "R_8_2222":
            return `
<table style="margin-top: 0px;margin-right: -20px;">
<tr><td><div class="select" id="n1" >1</div></td></tr>
<tr><td><div class="select" id="n3" style="margin-top: -10px;">3</div></td></tr>
<tr><td><div class="select" id="n5" style="margin-top: -10px;">5</div></td></tr>
<tr><td><div class="select" id="n7" style="margin-top: -10px;">7</div></td></tr>
</table>`
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
<table style="margin-top: 50px;margin-right: -20px">
<tr>
<td><div class="select" id="n1">1</div></td>
<td><div class="select" id="n2">2</div></td>
</tr>
<tr>
<td><div class="select" id="n5" style="margin-top: -10px;">5</div></td>
<td><div class="select" id="n6" style="margin-top: -10px;">6</div></td>
</tr>
<tr>
<td><div class="select" id="n9" style="margin-top: -10px;">9</div></td>
<td><div class="select" id="n10" style="margin-top: -10px;">10</div></td>
</tr>
</table>`
        case "R_16":
            return `
<table style="margin-top: 20px;margin-right: -20px">
<tr>
<td><div class="select" id="n1">1</div></td>
<td><div class="select" id="n2">2</div></td>
</tr>
<tr>
<td><div class="select" id="n5" style="margin-top: -10px;">5</div></td>
<td><div class="select" id="n6" style="margin-top: -10px;">6</div></td>
</tr>
<tr>
<td><div class="select" id="n9" style="margin-top: -10px;">9</div></td>
<td><div class="select" id="n10" style="margin-top: -10px;">10</div></td>
</tr> 
<tr>
<td><div class="select" id="n13" style="margin-top: -10px;">13</div></td>
<td><div class="select" id="n14" style="margin-top: -10px;">14</div></td>
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
        case 'F_6':
            return  `
<table style="margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n1" >1</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n3" style="margin-top: 10px;">3</div>
</td>
<td >
<div class="select" id="n4" style="margin-top: 10px;">4</div>
</td>
</tr>
</table>`
        case 'F_8_224':
            return  `
<table style="margin-top: 10px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
</td>
<td>
<div class="select" id="n3" style="margin-top: 10px;">3</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n5" style="margin-top: -10px;">5</div>
</td>
<td >
<div class="select" id="n6" style="margin-top: -10px;">6</div>
</td>
</tr>
</table>`
        case 'F_10_244':
            return  `
<table style="margin-top: 20px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n3" style="margin-top: 10px;">3</div>
</td>
<td>
<div class="select" id="n4" style="margin-top: 10px;">4</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n7" style="margin-top: -10px;">7</div>
</td>
<td >
<div class="select" id="n8" style="margin-top: -10px;">8</div>
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
        case 'F_8_242':
            return `
<table style="margin-top: 30px;margin-right: -20px">
<tr>
<td></td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n3" style="margin-top: 10px;">3</div>
</td>
<td>
<div class="select" id="n4" style="margin-top: 10px;">4</div>
</td>
</tr>
<tr>
<td></td>
<td><div class="select" id="n7">7</div></td>
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
        case 'F_10_2224':
            return  `
<table style="margin-top: 30px;margin-right: -20px">
<tr>
<td></td>
<td><div class="select" id="n1">1</div></td>
</tr>
<tr>
<td></td>
<td><div class="select" id="n3" style="margin-top: -10px;">3</div></td>
</tr>
<tr>
<td></td>
<td><div class="select" id="n5" style="margin-top: -10px;">5</div></td>
</tr>
<tr>
<td ><div class="select" style="margin-top: -10px;" id="n7" >7</div></td>
<td ><div class="select" style="margin-top: -10px;" id="n8" >8</div></td>
</tr>
</table>`
        case 'F_12':
            return  `
<table style="margin-top: 35px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
</td>
<td>
<div class="select" id="n3" style="margin-top: -10px;">3</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n5" style="margin-top: -10px;">5</div>
</td>
<td>
<div class="select" id="n6" style="margin-top: -10px;">6</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n9" style="margin-top: -10px;">9</div>
</td>
<td >
<div class="select" id="n10" style="margin-top: -10px;">10</div>
</td>
</tr>
</table>`
        case 'F_14':
            return  `
<table style="margin-top: 35px;margin-right: -20px">
<tr>
<td>
</td>
<td>
<div class="select" id="n1">1</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n3" style="margin-top: -10px;">3</div>
</td>
<td>
<div class="select" id="n4" style="margin-top: -10px;">4</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n7" style="margin-top: -10px;">7</div>
</td>
<td>
<div class="select" id="n8" style="margin-top: -10px;">8</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n11" style="margin-top: -10px;">11</div>
</td>
<td >
<div class="select" id="n12" style="margin-top: -10px;">12</div>
</td>
</tr>
</table>`
    }
}

function getRight(a) {
    switch (a) {
        case "R_4":
            return `<table style="margin-top: 50px;margin-left: -20px;">
<tr>
<td><div class="select" id="n2" >2</div></td>
</tr>
<tr>
<td><div class="select" id="n4" style="margin-top: -10px;">4</div></td>
</tr>
</table>`
        case "R_6_222":
            return `<table style="margin-top: 50px;margin-left: -20px;">
<tr>
<td><div class="select" id="n2">2</div></td>
</tr>
<tr>
<td><div class="select" id="n4" style="margin-top: -10px;">4</div></td>
</tr>
<tr>
<td><div class="select" id="n6" style="margin-top: -10px;">6</div></td>
</tr>
</table>`
        case "R_8_44":
            return  `
<table style="margin-top: 50px;margin-left: -20px">
<tr>
<td><div class="select" id="n3" >3</div></td>
<td><div class="select" id="n4" >4</div></td>
</tr>
<tr>
<td ><div class="select" id="n7" style="margin-top: -10px;">7</div></td>
<td ><div class="select" id="n8" style="margin-top: -10px;">8</div></td>
</tr>
</table>`
        case "R_8_2222":
            return `
<table style="margin-top: 0px;margin-left: -20px;">
<tr><td><div class="select" id="n2">2</div></td></tr>
<tr><td><div class="select" id="n4" style="margin-top: -10px;">4</div></td></tr>
<tr><td><div class="select" id="n6" style="margin-top: -10px;">6</div></td></tr>
<tr><td><div class="select" id="n8" style="margin-top: -10px;">8</div></td></tr>
</table>`
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
<table style="margin-top: 50px;margin-left: -20px;">
<tr>
<td><div class="select" id="n3">3</div></td>
<td><div class="select" id="n4">4</div></td>
</tr>
<tr>
<td><div class="select" id="n7" style="margin-top: -10px;">7</div></td>
<td><div class="select" id="n8" style="margin-top: -10px;">8</div></td>
</tr>
<tr>
<td><div class="select" id="n11" style="margin-top: -10px;">11</div></td>
<td><div class="select" id="n12" style="margin-top: -10px;">12</div></td>
</tr>
</table>`
        case "R_16":
            return `
<table style="margin-top: 20px;margin-left: -20px">
<tr>
<td><div class="select" id="n3">3</div></td>
<td><div class="select" id="n4">4</div></td>
</tr>
<tr>
<td><div class="select" id="n7" style="margin-top: -10px;">7</div></td>
<td><div class="select" id="n8" style="margin-top: -10px;">8</div></td>
</tr>
<tr>
<td><div class="select" id="n11" style="margin-top: -10px;">11</div></td>
<td><div class="select" id="n12" style="margin-top: -10px;">12</div></td>
</tr>
<tr>
<td><div class="select" id="n15" style="margin-top: -10px;">15</div></td>
<td><div class="select" id="n16" style="margin-top: -10px;">16</div></td>
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

        case 'F_6':
            return  `
<table style="margin-left: -20px">
<tr>
<td>
<div class="select" style="" id="n2">2</div>
</td>
<td>
</td>
</tr>
<tr>
<td >
<div class="select" id="n5" style="margin-top: 10px;">5</div>
</td>
<td >
<div class="select" id="n6" style="margin-top: 10px;">6</div>
</td>
</tr>
</table>`

        case 'F_8_224':
            return  `
<table style="margin-top: 10px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n2">2</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n4" style="margin-top: 10px;">4</div>
</td>
<td>
</td>
</tr>
<tr>
<td >
<div class="select" id="n7" style="margin-top: -10px;">7</div>
</td>
<td >
<div class="select" id="n8" style="margin-top: -10px;">8</div>
</td>
</tr>
</table>`
        case 'F_10_244':
            return  `
<table style="margin-top: 20px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n2">2</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n5" style="margin-top: 10px;">5</div>
</td>
<td>
<div class="select" id="n6" style="margin-top: 10px;">6</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n9" style="margin-top: -10px;">9</div>
</td>
<td >
<div class="select" id="n10" style="margin-top: -10px;">10</div>
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
        case 'F_8_242':
            return `
<table style="margin-top: 30px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n2">2</div>
</td>
<td></td>
</tr>
<tr>
<td>
<div class="select" id="n5" style="margin-top: 10px;">5</div>
</td>
<td>
<div class="select" id="n6" style="margin-top: 10px;">6</div>
</td>
</tr>
<tr>
<td><div class="select" id="n8">8</div></td>
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
        case 'F_10_2224':
            return `
<table style="margin-top: 30px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n2">2</div>
</td>
<td></td>
</tr>
<tr>
<td>
<div class="select" id="n4" style="margin-top: -10px;">4</div>
</td>
<td></td>
</tr>
<tr>
<td>
<div class="select" id="n6" style="margin-top: -10px;">6</div>
</td>
<td></td>
</tr>
<tr>
<td>
<div class="select" id="n9" style="margin-top: -10px;">9</div>
</td>
<td>
<div class="select" id="n10" style="margin-top: -10px;">10</div>
</td>
</tr>
</table>`
        case 'F_12':
            return  `
<table style="margin-top: 30px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n2">2</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n4" style="margin-top: -10px;">4</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n7" style="margin-top: -10px;">7</div>
</td>
<td>
<div class="select" id="n8" style="margin-top: -10px;">8</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n11" style="margin-top: -10px;">11</div>
</td>
<td >
<div class="select" id="n12" style="margin-top: -10px;">12</div>
</td>
</tr>
</table>`
        case 'F_14':
            return  `
<table style="margin-top: 35px;margin-left: -20px">
<tr>
<td>
<div class="select" id="n2">2</div>
</td>
<td>
</td>
</tr>
<tr>
<td>
<div class="select" id="n5" style="margin-top: -10px;">5</div>
</td>
<td>
<div class="select" id="n6" style="margin-top: -10px;">6</div>
</td>
</tr>
<tr>
<td>
<div class="select" id="n9" style="margin-top: -10px;">9</div>
</td>
<td>
<div class="select" id="n10" style="margin-top: -10px;">10</div>
</td>
</tr>
<tr>
<td >
<div class="select" id="n13" style="margin-top: -10px;">13</div>
</td>
<td >
<div class="select" id="n14" style="margin-top: -10px;">14</div>
</td>
</tr>
</table>`
    }
}