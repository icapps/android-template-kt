package be.icapps.template.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import be.icapps.template.R
import be.icapps.template.databinding.ActivityMainBinding

class MainActivity : Activity() {

    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

}