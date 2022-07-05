package com.orange.tpms.ue.kt_frag


import androidx.fragment.app.Fragment
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.Util.fixLanguage


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Policy : RootFragement(R.layout.fragment_frag__policy) {
    override fun viewInit() {
        rootview.fixLanguage()
    }
}
