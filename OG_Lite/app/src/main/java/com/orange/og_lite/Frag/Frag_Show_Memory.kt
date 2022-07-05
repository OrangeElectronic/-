package com.orange.og_lite.Frag

import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import kotlinx.android.synthetic.main.frag_show_memory.view.*

class Frag_Show_Memory(var text:String):JzFragement(R.layout.frag_show_memory) {
    override fun viewInit() {
        rootview.texter.text=text
    }


}