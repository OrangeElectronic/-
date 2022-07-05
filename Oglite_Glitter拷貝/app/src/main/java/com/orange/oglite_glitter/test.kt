package com.orange.oglite_glitter

fun main(){
    var grantResults= arrayOf(0)
    println(grantResults.indices)
    if (grantResults.isNotEmpty()) {
        for (i in grantResults.indices) {
            if (grantResults[i] == 0) {
                println("Pass")
            } else {
                println("error")
            }
        }
    }
}