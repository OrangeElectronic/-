package com.orange.tpms.ue.kt_frag

import android.util.Log
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Command.Cmd_Rfid
import kotlinx.android.synthetic.main.frag_test_engineer_mode.view.*

class Frag_Enginer_Mode:JzFragement(R.layout.frag_test_engineer_mode) {
    var lastRFID=""
    override fun viewInit() {
        rootview.button10.setOnClickListener {
            Cmd_Rfid.readRfid {
                if(it!=null){
                    rootview.textView6.text = ("${rootview.textView6.text}\n\nGetRfid-->>$it")
                    lastRFID=it
                }
            }
        }
        rootview.button12.setOnClickListener {
            Cmd_Rfid.writeRfid( lastRFID,rootview.rfidtext.text.toString()) {
                rootview.textView6.text = ("${rootview.textView6.text}\n\nWriteResult-->>$it")
            }
        }
        rootview.button13.setOnClickListener {
            Cmd_Rfid.existRfid(lastRFID){
                rootview.textView6.text = ("${rootview.textView6.text}\n\nRfidExists-->>$it")
            }
        }
        rootview.button14.setOnClickListener {
            Thread{
                Cmd_Rfid.startRfid()
            }.start()
        }
        rootview.read_area.setOnClickListener {
            Cmd_Rfid.readUserArea(lastRFID){
                rootview.textView6.text = ("${rootview.textView6.text}\n\nreadUserArea-->>$it")
            }
        }
        rootview.write_area.setOnClickListener {
            Cmd_Rfid.writeUserArea(lastRFID,rootview.rfidtext2.text.toString()){
                rootview.textView6.text = ("${rootview.textView6.text}\n\nwrite_area-->>$it")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("RfID","Destroy")
        Thread{
            Cmd_Og.open()
        }.start()
    }
}