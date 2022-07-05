package app.com.tsport



class ClassA (val callback: ()->Unit){
    init {
        callback()
    }
}
