package com.orange.tpms.ue.kt_frag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.orange.tpms.R

class test : AppCompatActivity() {
    lateinit var mBinding : ViewDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ViewDataBinding>(this,R.layout.fragment_frag__check__sensor__read)
    }
}
