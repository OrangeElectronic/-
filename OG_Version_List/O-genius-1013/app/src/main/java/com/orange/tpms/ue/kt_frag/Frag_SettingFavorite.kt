package com.orange.tpms.ue.kt_frag


import androidx.recyclerview.widget.RecyclerView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.FavAdapter
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_my_favorite.view.*
import java.util.*


class Frag_SettingFavorite : RootFragement(R.layout.fragment_my_favorite) {
    lateinit var re: androidx.recyclerview.widget.RecyclerView
    lateinit var adapter: FavAdapter
    var data= ArrayList<String>()

    override fun viewInit() {
        rootview.fixLanguage()
        refresh=true
        rootview.add.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_AddFavorite_SetPro(),"Frag_AddFavorite_SetPro",true)
        }
        adapter=FavAdapter(PublicBean.getFavorite(),false)
        re=rootview.findViewById(R.id.adapter)
        re.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        re.adapter=adapter
        (re.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPosition(data.size)
    }

}
