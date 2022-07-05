package com.orange.tpms.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.de.rocket.Rocket
import com.de.rocket.ue.injector.BindView
import com.jianzhi.jzcrashhandler.Act_Show_Error
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.model.InformationBean
import com.orange.tpms.ue.kt_frag.Frag_SettingPager_Set_Languages
import com.orange.tpms.ue.testFrag.Frag_Test_Source
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.getFix

class InformationAdapter(context: Context) :
    BaseRecyclerAdapter<InformationBean, InformationAdapter.ViewHolder>(context) {
    var press=0
    var press1=0
    var press2=0
    var press3=0
    var press4=0
    var press5=0
    var press6=0
    private var select = 0//默认选中第一个

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflate(R.layout.item_information, parent))
    }

    override fun onBindView(holder: ViewHolder, index: Int) {
        holder.tvTitle!!.text = getItem(index)!!.title
        if(getItem(index)!!.title == "jz.66".getFix()){
            Log.e("holder.itemView","click")
            holder.itemView.setOnClickListener {
                press+=1
                if(press>=10){
                    press=0
                    JzActivity.getControlInstance().changePage(
                        Frag_SettingPager_Set_Languages(),
                        "Frag_Register",true)
                }
            }
        }else if(getItem(index)!!.title =="jz.397".getFix()){
            Log.e("holder.itemView","click")
            holder.itemView.setOnClickListener {
                press1+=1
                if(press1>=10){
                    press1=0
                    JzActivity.getControlInstance().changePage(
                        Frag_Test_Source(),
                        "Frag_Test_Source",true)
                }
            }
        }else if(index==2){
            holder.itemView.setOnClickListener {
                press2+=1
                if(press2>=10){
                    press2=0
                    val intent= Intent(JzActivity.getControlInstance().getRootActivity(),Act_Show_Error::class.java)
                    JzActivity.getControlInstance().getRootActivity().startActivity(intent)
                }
            }
        }else if(index==8){
            holder.itemView.setOnClickListener {
                press3+=1
                if(press3>=10){
                    press3=0
                    JzActivity.getControlInstance().setPro("autoRead",!OgPublic.autoRead)
                    JzActivity.getControlInstance().toast(OgPublic.autoRead.toString())
                }
            }
        }else if(index==5){
            holder.itemView.setOnClickListener {
                press4+=1
                if(press4>=10){
                    press4=0
                    PublicBean.closeDisCharge=!PublicBean.closeDisCharge
                    JzActivity.getControlInstance().toast(PublicBean.closeDisCharge.toString())
                }
            }

        }else if(index==4){
            holder.itemView.setOnClickListener {
                press5+=1
                if(press5>=10){
                    JzActivity.getControlInstance().setPro("toBento2",!JzActivity.getControlInstance().getPro("toBento2",false))
                    JzActivity.getControlInstance().toast(JzActivity.getControlInstance().getPro("toBento2",false).toString())
                }
            }
        }else if(index==7){
            holder.itemView.setOnClickListener {
                press6+=1
                if(press6>=10){
                    val toggle=!JzActivity.getControlInstance().getPro("RFID",false)
                    JzActivity.getControlInstance().setPro("RFID",toggle)
                    JzActivity.getControlInstance().toast((toggle).toString())
                    press6=0
                }
            }
        }
        holder.tvInformation!!.text = getItem(index)!!.information
        holder.ivOk!!.visibility = View.INVISIBLE
    }

    /**
     * 点击回调接口
     */
    interface OnItemClickListener {
        fun onItemClick(pos: Int, content: InformationBean?)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.tv_title)
        var tvTitle: TextView? = null//Title
        @BindView(R.id.tv_information)
        var tvInformation: TextView? = null//Information
        @BindView(R.id.iv_ok)
        var ivOk: ImageView? = null//Ok

        init {
            Rocket.bindViewHolder(this, itemView)
            tvTitle=itemView.findViewById(R.id.tv_title)
            tvInformation=itemView.findViewById(R.id.tv_information)
            ivOk=itemView.findViewById(R.id.iv_ok)
        }
    }
}
