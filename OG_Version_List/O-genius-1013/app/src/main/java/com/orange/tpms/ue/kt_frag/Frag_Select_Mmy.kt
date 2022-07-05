package com.orange.tpms.ue.kt_frag

import android.app.ActionBar
import android.media.Image
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jzpictureselector.PictureSelector
import com.example.jztaskhandler.runner
import com.jianzhi.customer_service.beans.getFix
import com.jianzhi.jzblehelper.callback.ConnectResult
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.jzutil.setImage
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.cell_big_item.view.*
import kotlinx.android.synthetic.main.cell_small_item.view.*
import kotlinx.android.synthetic.main.frag_select_mmy.view.*
import kotlinx.android.synthetic.main.root_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class Frag_Select_Mmy : RootFragement(R.layout.frag_select_mmy) {
    var focus = 0
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
        }
    var adapter = Adapter()

    data class item(var iconN: Int, var iconC: Int, var title: String, var runner: runner)

    var it: ArrayList<item> = ArrayList()


    override fun viewInit() {
        it.add(item(R.mipmap.mmy, R.mipmap.btn_vehicle_selection_p, "jz.121") {
            PublicBean.selectMmy.selectFun = PublicBean.SelectFun.Manual
            if (PublicBean.position == PublicBean.ID_COPY_OBD) {
                val blemanager =
                    (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager
                blemanager.scan {
                    if (it) {
                        JzActivity.getControlInstance().changePage(Frag_SelectMake(), "Frag_SelectMake", true)
                    }
                    JzActivity.getControlInstance().closeDiaLog()
                }
            } else {
                JzActivity.getControlInstance()
                    .changePage(Frag_SelectMake(), "Frag_SelectMake", true)
            }
        })
        if (JzActivity.getControlInstance().getLanguage() == Locale("de")) {
            it.add(item(R.mipmap.hsn_n, R.mipmap.hsn_p, "HSN/TSN") {
                PublicBean.selectMmy.selectFun = PublicBean.SelectFun.Hsn
                if (PublicBean.position == PublicBean.ID_COPY_OBD) {
                    val blemanager =
                        (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager
                    blemanager.scan {
                        if (it) {
                            JzActivity.getControlInstance()
                                .changePage(Frag_HSN_TSN(), "Frag_HSN_TSN", true)
                        }
                        JzActivity.getControlInstance().closeDiaLog()
                    }
                } else {
                    JzActivity.getControlInstance()
                        .changePage(Frag_HSN_TSN(), "Frag_HSN_TSN", true)
                }
            })
        }
        it.add(item(R.mipmap.scan_code, R.mipmap.btn_scan_code_p, "jz.120", runner {
            PublicBean.selectMmy.selectFun=PublicBean.SelectFun.Scan
            PublicBean.ScanType = PublicBean.掃描Mmy
            if (PublicBean.position == PublicBean.ID_COPY_OBD) {
                val blemanager =
                    (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager
                blemanager.scan(ConnectResult {
                    if (it) {
                        JzActivity.getControlInstance().changePage(Frag_Scan(), "Frag_Scan", true)
                    }
                    JzActivity.getControlInstance().closeDiaLog()
                })
            } else {
                JzActivity.getControlInstance().changePage(Frag_Scan(), "Frag_Scan", true)
            }
        }))
        it.add(item(R.mipmap.my_favorites, R.mipmap.btn_my_favourite_p, "jz.34") {
            PublicBean.selectMmy.selectFun = PublicBean.SelectFun.Favorite
            if (PublicBean.position == PublicBean.ID_COPY_OBD) {
                val blemanager =
                    (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager
                blemanager.scan(ConnectResult {
                    if (it) {
                        JzActivity.getControlInstance().changePage(Frag_Favorite(), "Frag_Favorite", true)
                    }
                    JzActivity.getControlInstance().closeDiaLog()
                })
            } else {
                JzActivity.getControlInstance().changePage(Frag_Favorite(), "Frag_Favorite", true)
            }
        })
        if(PublicBean.position==PublicBean.複製傳感器){
            it.add(item(R.mipmap.plate_b, R.mipmap.plate_g, "jz.516", runner {
                PublicBean.selectMmy.selectFun=PublicBean.SelectFun.Plate
                PublicBean.ScanType = PublicBean.車牌辨識
                if (PublicBean.position == PublicBean.ID_COPY_OBD) {
                    val blemanager = (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager
                    blemanager.scan(ConnectResult {
                        if (it) {
                            JzActivity.getControlInstance().changePage(Frag_Plate_Recognize{

                            }, "Frag_Plate_Recognize", true)
                        }
                        JzActivity.getControlInstance().closeDiaLog()
                    })
                } else {
                    JzActivity.getControlInstance().changePage(Frag_Plate_Recognize{}, "Frag_Plate_Recognize", true)
                }
            }))
        }

        rootview.re.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootview.re.adapter = adapter
        rootview.fixLanguage()
    }

    inner class Adapter : JzAdapter(R.layout.root_item) {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mView.root.removeAllViews()
            if (JzActivity.getControlInstance().getLanguage() == Locale("de")) {
                val view = if (position == 0 || position == 1) layoutInflater.inflate(
                    R.layout.cell_bid_left_item,
                    holder.mView.root,
                    false
                ) else layoutInflater.inflate(R.layout.cell_de_small_item, holder.mView.root, false)
                if (position == 0 || position == 1) {
                    view.imageView.setImageResource(if (focus == position) it[position].iconC else it[position].iconN)
                    view.textView5.text = it[position].title
                } else {
                    view.imageView.setImageResource(if (focus == position) it[position].iconC else it[position].iconN)
                    view.textView5.text = it[position].title
                }
                val divider = View(context)
                divider.setBackgroundColor(resources.getColor(R.color.gray))
                holder.mView.root.addView(divider)
                divider.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                divider.layoutParams.height = 1
                holder.mView.root.addView(view)
                holder.mView.root.fixLanguage()
                holder.mView.setOnClickListener {
                    this@Frag_Select_Mmy.it[position].runner.run()
                }
            } else {
                val view = if (position == 0) layoutInflater.inflate(
                    R.layout.cell_big_item,
                    holder.mView.root,
                    false
                ) else layoutInflater.inflate(R.layout.cell_small_item, holder.mView.root, false)
                if (position == 0) {
                    view.imageView2.setImageResource(if (focus == position) it[position].iconC else it[position].iconN)
                    view.tit.text = it[position].title
                } else {
                    view.imageView.setImageResource(if (focus == position) it[position].iconC else it[position].iconN)
                    view.textView5.text = it[position].title
                }
                val divider = View(context)
                divider.setBackgroundColor(resources.getColor(R.color.gray))
                holder.mView.root.addView(divider)
                divider.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                divider.layoutParams.height = 1
                holder.mView.root.addView(view)
                holder.mView.root.fixLanguage()
                holder.mView.setOnClickListener {
                    this@Frag_Select_Mmy.it[position].runner.run()
                }
            }
        }

        override fun sizeInit(): Int {
            return it.size
        }

    }

    override fun enter() {
        it[focus].runner.run()
    }

    override fun onTop() {
        if (focus - 1 >= 0) {
            focus -= 1
        }
        rootview.re.scrollToPosition(focus)
    }

    override fun onDown() {
        if (focus + 1 < it.size) {
            focus += 1
        }
        rootview.re.scrollToPosition(focus)
    }
}