package com.orange.og_lite.adapter

import android.util.Log
import android.view.View
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.R
import com.orange.og_lite.beans.Bs_SoftKeyboard
import kotlinx.android.synthetic.main.ad_softkeyboard.view.*
import kotlinx.android.synthetic.main.ad_softkeyboard.view.ID
import kotlinx.android.synthetic.main.da_softkeyboard.*
import com.orange.og_lite.Dialog.Da_SoftKeyboard as Da_SoftKeyboard

class Ad_SoftKeyboard(val Da:Da_SoftKeyboard,val myitem: Bs_SoftKeyboard) : JzAdapter(R.layout.ad_softkeyboard)
{
    //var MainText=""
    var MainSelect=0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mView.output.visibility = View.GONE
        holder.mView.input.visibility = View.VISIBLE
        holder.mView.button.text = myitem.num1[position]
        holder.mView.button2.text = myitem.num2[position]
        holder.mView.button3.text = myitem.num3[position]
        holder.mView.button12.text = myitem.num4[position]

        //Bs_SoftKeyboard.Key_In = Bs_SoftKeyboard.Key_In+myitem.num1[position]
        holder.mView.button.setOnClickListener {
//            val text = StringBuilder(Bs_SoftKeyboard.Key_In)
//            text.append(myitem.num1[position])
//            Bs_SoftKeyboard.Key_In = Bs_SoftKeyboard.Key_In + myitem.num1[position]
//            this.notifyDataSetChanged()
            SelectChange(myitem.num1[position])
            //this.notifyDataSetChanged()
        }
        holder.mView.button2.setOnClickListener {
            SelectChange(myitem.num2[position])
            //this.notifyDataSetChanged()
            }
        holder.mView.button3.setOnClickListener {
            SelectChange(myitem.num3[position])
            //this.notifyDataSetChanged()
        }
        holder.mView.button12.setOnClickListener {
            SelectChange(myitem.num4[position])
            //this.notifyDataSetChanged()
        }
    }

    fun SelectChange(Text:String)
    {
        //holder.mView.ID.requestFocus()
        //holder.mView.ID.selectionStart
        //if(MainSelect != holder.mView.ID.text.toString().length) { }
        //if(MainSelect == 0)
        //{
//            val text = StringBuilder(Bs_SoftKeyboard.Key_In)
//            text.append(Text)
//            //Bs_SoftKeyboard.Key_In = text.toString()

            //Bs_SoftKeyboard.Key_In = StringBuilder(Bs_SoftKeyboard.Key_In).append(Text).toString()
//            holder.mView.ID.mSelection=MainSelect
        //}
        //else
        //{
//            val text = StringBuilder(Bs_SoftKeyboard.Key_In)
//            //text.append(text.toString().replace(" ", "").toUpperCase())
//            text.insert(MainSelect,Text)
//            Bs_SoftKeyboard.Key_In = text.toString()
        val lastSelect=myitem.MainSelect
        //var Key_In = ""
        if(!(Da.dialog.ID.text.toString().length>=8)){
            val Key_In = StringBuilder(Da.dialog.ID.text.toString()).insert(myitem.MainSelect,Text).toString()
            Da.dialog.ID.setText(Key_In)
        }
            //Da.IDtext(Da,Bs_SoftKeyboard.Key_In)
        Log.e("setSelection","${lastSelect+1}")
        //if(lastSelect+1>8) {return}

//         if((lastSelect+1==8 || lastSelect+1 == 0) && Da.dialog.ID.text.toString().length==8) {
//             Log.e("setSelection","異常")
//            Da.dialog.ID.setSelection(8)
//            return}

//        if(Da.dialog.ID.text.toString().length == 8) {
//            Log.e("setSelection","=8")
//            Da.dialog.ID.setSelection(lastSelect)
//            return
//        }

        if(Da.dialog.ID.text.toString().length == 8)
        {
            return
        }

        if(Da.dialog.ID.text.toString().length <= 8 && lastSelect+1 <= 8)
        {
            Log.e("setSelection","<8")
            Da.dialog.ID.setSelection(lastSelect+1)
        }


        //if(Da.dialog.ID.text!!.length+1 <= 8){Da.dialog.ID.setSelection(lastSelect+1)}else{Da.dialog.ID.setSelection(8)}
//        val da = JzActivity.getControlInstance().getDialog("Da_SoftKeyboard")!!
//        if(da is Da_SoftKeyboard)
//        {
//
//        }

//            holder.mView.ID.mSelection=MainSelect
        //}

    }

    override fun sizeInit(): Int {
        return myitem.num1.size
    }

}